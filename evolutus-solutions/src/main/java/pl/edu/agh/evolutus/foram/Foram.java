package pl.edu.agh.evolutus.foram;

import org.apache.commons.lang3.tuple.Pair;
import org.jage.action.AgentActions;
import org.jage.address.agent.AgentAddress;
import org.jage.address.agent.AgentAddressSupplier;
import org.jage.agent.SimpleAgent;
import org.jage.platform.component.exception.ComponentException;
import org.jage.query.AgentEnvironmentQuery;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pl.edu.agh.evolutus.environment.IOceanFragment;
import pl.edu.agh.evolutus.environment.OceanFragment;
import pl.edu.agh.evolutus.genotype.Genome;
import pl.edu.agh.evolutus.genotype.Genotype;
import pl.edu.agh.evolutus.genotype.operator.CrossingOverOperator;
import pl.edu.agh.evolutus.genotype.operator.OnePointCrossingOverOperator;
import pl.edu.agh.evolutus.genotype.operator.TwoPointCrossingOverOperator;
import pl.edu.agh.evolutus.genotype.operator.UniformCrossingOverOperator;
import pl.edu.agh.evolutus.service.ShellFactory;
import pl.edu.agh.evolutus.service.StatisticsService;
import pl.edu.agh.evolutus.service.StatisticsService.StatisticsServiceException;
import pl.edu.agh.evolutus.service.config.ForamConfig;
import pl.edu.agh.evolutus.service.config.utils.EnvState;
import pl.edu.agh.evolutus.service.config.utils.ForamState;
import pl.edu.agh.evolutus.service.config.utils.UnitsConverter;
import pl.edu.agh.evolutus.statistics.model.ForamFossil;
import pl.edu.agh.evolutus.utils.MovementCostVector;
import pl.edu.agh.evolutus.utils.VectorD;
import pl.edu.agh.evolutus.utils.VectorL;
import pl.edu.agh.evolutus.utils.VelocityVector;

import javax.inject.Inject;
import java.util.Collection;
import java.util.List;

public class Foram extends SimpleAgent implements IForam {

	private static final Logger logger = LoggerFactory.getLogger(Foram.class);

	private ForamType type;
	private boolean alive = true;
	private double energy;
	private double age = 0;
	private double stepDurationInHours;
	private double stepDurationInSeconds;
	private boolean foramActiveMotion;

	private Shell shell;

	private Genotype genotype = null;

	@Inject
	private ForamConfig config;

	@Inject
	private UnitsConverter unitsConverter;

	@Inject
	private ShellFactory shellFactory;

	@Inject
	private StatisticsService statisticsService;

	@Inject
	public Foram(AgentAddressSupplier supplier) {
		super(supplier);
	}

	@Override
	public void setType(ForamType type) {
		this.type = type;
	}

	@Override
	public ForamType getType() {
		return type;
	}

	@Override
	public void setEnergy(double energy) {
		this.energy = energy;
	}

	@Override
	public double getEnergy() {
		return energy;
	}

	@Override
	public double getShellVolume() {
		return shell.getVolumeShell();
	}

    @Override
    public double getShapeFactor() { return  shell. getShapeFactor(); }

	@Override
	public void setGenotype(Genotype genotype) {
		if (this.genotype == null) {
			this.genotype = genotype;
		} else {
			throw new IllegalStateException("setGenotype() called multiple times on foram: " + getAddress());
		}
	}

	@Override
	public Genotype getGenotype() {
		return genotype;
	}

	@Override
	public void setShell(Shell shell) {
		this.shell = shell;
	}

	@Override
	public Shell getShell() {
		return shell;
	}

	@Override
	public void init() throws ComponentException {
		super.init();
		this.stepDurationInHours = config.stepDurationInHours();
		this.stepDurationInSeconds = stepDurationInHours * 3600;
		this.foramActiveMotion = config.foramActiveMotion();
	}

	@Override
	public boolean finish() throws ComponentException {
		logger.debug("\tFORAM finished: {}", getAddress());
		return super.finish();
	}

	private EnvState envState;
	private ForamState foramState;
	private double currentTime;

	private void updateEnvState() {
		envState = getOceanFragment().getEnvState();
	}

	private void updateForamState() {
		foramState = new ForamState(type, foramActiveMotion, genotype, energy, age, shell);
	}

	private void updateCurrentStep() {
		currentTime = unitsConverter.stepsToHours(getOceanFragment().currentStep());
	}

	@Override
	public void step() {
		updateEnvState();
		updateForamState();
		updateCurrentStep();

		if (!couldForamPerformStep()) {
			return;
		}

		consumeStepEnergy();

		try {
			if (config.shouldDie(envState, foramState, currentTime)) {
				die();
			}
			eat();
			if (config.canReproduce(envState, foramState, currentTime)) {
				reproduce();
			}
			if ((timeLeftToMigrationInSeconds == null) && config.canCreateChamber(envState, foramState, currentTime)) {
				//cannot create chamber during motion
				createChamber();
			}
			if ((timeToBuildNewChamber == null) && config.canMigrate(envState, foramState, currentTime)) {
				tryMigrate();
			}

			age += stepDurationInHours;
		} catch (AgentDiedException e) {
			logger.debug("Foram died: {}", getAddress());
		}
	}

	private void consumeStepEnergy() {
		energy -= config.consumptionPerHour(envState, foramState, currentTime) * stepDurationInHours;
		if (energy < 0.0)
			energy = 0;
		if(timeToBuildNewChamber !=null) {
			timeToBuildNewChamber -= stepDurationInHours;
			if (timeToBuildNewChamber <= 0.0)
				timeToBuildNewChamber = null;
		}
	}

	private boolean couldForamPerformStep() {
		if (genotype == null) {
			logger.warn("Called step() on foram with uninitialized genotype: {}", getAddress());
			return false;
		}
		if (!alive) {
			logger.warn("Called step() on dead foram: {}", getAddress());
			return false;
		}
		return true;
	}

	@Override
	public boolean isAlive() {
		return alive;
	}

	private void die() throws AgentDiedException {
		energy = 0.0;
		alive = false;
		addStats();
		getOceanFragment().onForamDied();
		doAction(AgentActions.death(this));
		throw new AgentDiedException();
	}

	private void addStats() {
		try {
			IOceanFragment oceanFragment = getOceanFragment();
			VectorL position = oceanFragment.getPosition();
			String id = (getAddress() == null) ? "" : getAddress().toQualifiedString();
			Double deathHour = unitsConverter.stepsToHours(oceanFragment.currentStep());
			ForamFossil fossil = new ForamFossil(
					id,
					statisticsService.getSimulation(),
					deathHour,
					age,
					shell.getChambersCount(),
					type,
					genotype,
					position.x, position.y, position.z
			);
			statisticsService.add(fossil);
		} catch (StatisticsServiceException e) {
			logger.debug(e.getMessage(), e);
		}
	}

	private void eat() {
		if(timeToBuildNewChamber == null) { //cannot eat during chamber formation
			double foodCollectingRate = genotype.get(Genome.FOOD_COLLECTING_RATE).getValue();
            double metabolicEffectivenes = genotype.get(Genome.METABOLIC_EFFECTIVENESS).getValue();
			double shellVolume = shell.getVolumeShell(); //in cubic micrometers
			double stepInHours = config.stepDurationInHours();
            //maximum energy that foram is able to collect
			double energyDemand = foodCollectingRate * shellVolume * metabolicEffectivenes * stepInHours;
			double currentEnergy = getEnergy();
            //max energy that can be stored in a given volume of cytoplasm
			double maxEnergy = (metabolicEffectivenes * shellVolume) - currentEnergy;
			double radiusOfCollectingInMeters = genotype.get(Genome.FOOD_COLLECTING_RANGE).getValue()+0.0001*shell.getLastChamberRadius();
			energy += getOceanFragment().takeAlgae(Math.min(energyDemand, maxEnergy), radiusOfCollectingInMeters);
		}
	}

	private AgentAddress lastParentAddress = null;
	private OceanFragment lastParentReference = null;

	private IOceanFragment getOceanFragment() {
		if (lastParentAddress == null || !lastParentAddress.equals(getParentAddress())) {
			AgentEnvironmentQuery<OceanFragment, OceanFragment> query = new AgentEnvironmentQuery<>(OceanFragment.class);
			Collection<OceanFragment> result = queryParentEnvironment(
					query.matching(oceanFragment -> getParentAddress() != null && getParentAddress()
							.equals(oceanFragment.getAddress())));
			lastParentReference = result.iterator().next();
			lastParentAddress = lastParentReference.getAddress();
		}
		return lastParentReference;
	}

	private void reproduce() throws AgentDiedException {
		int gametesProduction = config.gametesProduction(envState, foramState, currentTime);
		List<Genome> gametes = genotype
				.createGametes(gametesProduction,
						config.globalMutationProbability(envState, foramState, currentTime),
						config.gametesSievingCoefficient(envState, foramState, currentTime),
						getCrossingOverOperator());
		getOceanFragment().addGametes(gametes, type);
		die();
	}
	private Double timeToBuildNewChamber = null;

	private void createChamber() {
		if (timeToBuildNewChamber == null) {
			energy -= config.energyNeededForGrowth(envState, foramState, currentTime);
			shell = shellFactory.createShellWithNewChamber(this);
			timeToBuildNewChamber = 24.0; //24 hours
		}
	}

	private AgentAddress migrationTarget = null;
	private Double movementCostPerStep = null;
	private Double timeLeftToMigrationInSeconds = null;

	private void tryMigrate() {

		if (timeLeftToMigrationInSeconds == null) {
			VelocityVector migrationVelocityVector;
			Pair<AgentAddress, VectorL> migrationTargetToMigrationDirection;
			if (foramActiveMotion) {
				migrationVelocityVector = config.foramActiveSpeed(envState, foramState, currentTime);
				migrationTargetToMigrationDirection = getOceanFragment().getActiveMigrationTarget();
			} else { //planktic - migration vector is a sum of ocean currents, vertical movement and random walk.
				VectorD VectorOceanCurrent =  getOceanFragment().getEnvState().currentDirection;
                //VectorActiveSpeed should be zero on any directions except Z.
				VectorD VectorActiveSpeed = config.foramActiveSpeed(envState, foramState, currentTime);
				migrationVelocityVector = new VelocityVector(VectorActiveSpeed.add(VectorOceanCurrent));
				migrationTargetToMigrationDirection = getOceanFragment().getPassiveMigrationTarget();
			}
			//TODO passive migration should use activeMotionEnergyCostPerChamberPerMeter(envState, foramState, currentTime) function!!!
			//passive migration + costs set to "-1" = absolute passive movement
			//passive migration + cost >= 0 - is able to move in that direction!
			//-> remove passive and active migration - only check costs!!!
            if(migrationTargetToMigrationDirection == null)
                return;
			migrationTarget = migrationTargetToMigrationDirection.getLeft();
			VectorD migrationDirection = migrationTargetToMigrationDirection.getRight().toDouble().abs();

			//TODO jakie są jednostki prędkości - powinny być metry na sekunde
			double velocity = migrationVelocityVector.dotProduct(migrationDirection);
			double oceanFragmentSizeInMeters = unitsConverter.unitLengthInMeters().dotProduct(migrationDirection);

			if (VectorD.ZERO_VECTOR.equals(migrationDirection) || velocity == 0.0) {
				return;
			}

			timeLeftToMigrationInSeconds = oceanFragmentSizeInMeters / velocity;
			if(foramActiveMotion) {
				double distancePerStep = velocity * stepDurationInSeconds;
				MovementCostVector movementCost = config.activeMotionEnergyCostPerMeter(envState, foramState, currentTime);
				double movementCostPerMeter = movementCost.getCostByMovementDirection(migrationDirection);
				movementCostPerStep = movementCostPerMeter * distancePerStep;
			}
		}

		timeLeftToMigrationInSeconds -= stepDurationInSeconds;
		//TODO passive motion means no costs!!!
		if(foramActiveMotion)
		    energy -= movementCostPerStep;

		if (timeLeftToMigrationInSeconds <= 0.0 && migrationTarget != null && !migrationTarget.equals(lastParentAddress)) {
			doAction(AgentActions.migrate(this, migrationTarget));
			migrationTarget = null;
			movementCostPerStep = null;
			timeLeftToMigrationInSeconds = null;
		}
	}

	private CrossingOverOperator getCrossingOverOperator() {
		String operatorName = config.crossingOverOperator(envState, foramState, currentTime);
		switch (operatorName) {
		case "OnePointCrossingOverOperator":
			return instanceProvider.getInstance(OnePointCrossingOverOperator.class);
		case "TwoPointCrossingOverOperator":
			return instanceProvider.getInstance(TwoPointCrossingOverOperator.class);
		case "UniformCrossingOverOperator":
			return instanceProvider.getInstance(UniformCrossingOverOperator.class);
		default:
			throw new IllegalStateException("Unknown crossing-over operator: " + operatorName);
		}
	}

	private static class AgentDiedException extends Exception {

	}
}
