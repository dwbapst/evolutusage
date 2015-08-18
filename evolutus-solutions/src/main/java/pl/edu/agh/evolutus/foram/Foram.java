package pl.edu.agh.evolutus.foram;

import java.util.Collection;
import java.util.List;

import javax.inject.Inject;

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
import pl.edu.agh.evolutus.utils.Geometry;
import pl.edu.agh.evolutus.utils.MovementCostVector;
import pl.edu.agh.evolutus.utils.VectorD;
import pl.edu.agh.evolutus.utils.VectorL;
import pl.edu.agh.evolutus.utils.VelocityVector;

import javax.inject.Inject;
import java.util.Collection;
import java.util.List;
import java.util.Random;

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
		this.energy = config.initialEnergy();
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
			if (config.canCreateChamber(envState, foramState, currentTime)) {
				createChamber();
			}
			if (config.canMigrate(envState, foramState, currentTime)) {
				tryMigrate();
			}

			age += stepDurationInHours;
		} catch (AgentDiedException e) {
			logger.debug("Foram died: {}", getAddress());
		}
	}

	private void consumeStepEnergy() {
		double consumptionPerHour;
		if (config.isInHibernationState(envState, foramState, currentTime)) {
			consumptionPerHour = genotype.get(Genome.HIBERNATION_ENERGY_CONSUMPTION_PER_HOUR).getValue();
		} else {
			//energy consumption is related to volume of cytoplams
			double volumeCytoplasm = getEnergy()/genotype.get(Genome.METABOLIC_EFFECTIVENESS).getValue();
			consumptionPerHour =
					genotype.get(Genome.ENERGY_DEMAND_PER_CHAMBER_PER_HOUR).getValue() * volumeCytoplasm;
		}

		energy -= consumptionPerHour * stepDurationInHours;
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
			ForamFossil fossil = new ForamFossil(
					id,
					statisticsService.getSimulation(),
					oceanFragment.currentStep(),
					age,
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
		//cannot consume more then shell can contain.
		double maxEnergy = (genotype.get(Genome.METABOLIC_EFFECTIVENESS).getValue() * shell.getVolumeShell()) - getEnergy();
		//max energy that can be collected at this moment by the creature
		//it is influenced by size of foram as well as their effectivness in food collecting.
		double energyDemand = genotype.get(Genome.FOOD_COLLECTING_RATE).getValue() * (0.1*shell.getVolumeShell())* config.stepDurationInHours();

		double radiusOfCollectingInMeters = config.raduisOfFoodCollecting(envState, foramState, currentTime);
		energy += getOceanFragment().takeAlgae(Math.min(energyDemand, maxEnergy), radiusOfCollectingInMeters);
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

	private void createChamber() {
		//energy needed for creating new chamber should be related to size of the new chamber.
		//size of new chamber
		double newChamberRadius = shell.getLastChamberRadius()*shell.getGrowthFactor();

		energy -= genotype.get(Genome.CHAMBER_GROWTH_COST_FACTOR).getValue() *
				genotype.get(Genome.METABOLIC_EFFECTIVENESS).getValue()* Geometry.sphereVolume(newChamberRadius);
		//energy -= energy * genotype.get(Genome.CHAMBER_GROWTH_COST_FACTOR).getValue();
		shell = shellFactory.createShellWithNewChamber(this);
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
			} else {
				migrationVelocityVector = getOceanFragment().getEnvState().currentDirection;
				migrationTargetToMigrationDirection = getOceanFragment().getPassiveMigrationTarget();
			}

			migrationTarget = migrationTargetToMigrationDirection.getLeft();
			VectorD migrationDirection = migrationTargetToMigrationDirection.getRight().toDouble().abs();
			double velocity = migrationVelocityVector.dotProduct(migrationDirection);
			double oceanFragmentSizeInMeters = unitsConverter.unitLengthInMeters().dotProduct(migrationDirection);

			if (VectorD.ZERO_VECTOR.equals(migrationDirection) || velocity == 0.0) {
				return;
			}

			timeLeftToMigrationInSeconds = oceanFragmentSizeInMeters / velocity;

			double distancePerStep = velocity * stepDurationInSeconds;

			MovementCostVector movementCost = config.activeMotionEnergyCostPerChamberPerMeter(envState, foramState, currentTime);
			double movementCostPerChamberPerMeter = movementCost.getCostByMovementDirection(migrationDirection);
			movementCostPerStep = movementCostPerChamberPerMeter * foramState.shell.getChambersCount() * distancePerStep;
		}

		timeLeftToMigrationInSeconds -= stepDurationInSeconds;
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
