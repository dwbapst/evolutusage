package pl.edu.agh.evolutus.foram;

import java.util.Collection;
import java.util.List;
import java.util.Random;

import javax.inject.Inject;

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
import pl.edu.agh.evolutus.utils.VectorL;

public class Foram extends SimpleAgent implements IForam {

	private static final Logger logger = LoggerFactory.getLogger(Foram.class);

	private ForamType type;
	private boolean alive = true;
	private double energy;
	private double age = 0;
	private double stepDurationInHours;
	private boolean foramActiveMotion;

	private Shell shell;

	private Genotype genotype = null;

	@Inject
	private ForamConfig config;

	@Inject
	private UnitsConverter unitsConverter;

	@Inject
	private ShellFactory shellFactory;

	private Random random = new Random();

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
		this.stepDurationInHours = unitsConverter.stepDurationInHours();
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

	private Double consumptionPerStep;

	private void consumeStepEnergy() {
		if (consumptionPerStep == null) {
			double consumptionPerHour;
			if (config.isInHibernationState(envState, foramState, currentTime)) {
				consumptionPerHour = genotype.get(Genome.HIBERNATION_ENERGY_CONSUMPTION_PER_HOUR).getValue();
			} else {
				consumptionPerHour =
						genotype.get(Genome.ENERGY_DEMAND_PER_CHAMBER_PER_HOUR).getValue() * shell.getChambersCount();
			}

			consumptionPerStep = consumptionPerHour * config.stepDurationInHours();
		}
		energy -= consumptionPerStep;
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
		double maxEnergy = genotype.get(Genome.MAX_ENERGY_PER_CHAMBER).getValue() * shell.getChambersCount();
		double energyDemand = genotype.get(Genome.MAX_ENERGY_COLLECTING_PER_CHAMBER_PER_HOUR).getValue() *
				shell.getChambersCount() * config.stepDurationInHours();
		double radiusOfCollectingInMeters = config.raduisOfFoodCollecting(envState, foramState, currentTime);
		energy += getOceanFragment().takeAlgae(Math.min(energyDemand, maxEnergy), radiusOfCollectingInMeters);
	}

	private AgentAddress lastParentAddress = null;
	private OceanFragment lastParentReference = null;

	/**
	 * @return the distance in units that ocean current 'travels' within one step.
	 * It could be also interpreted as a probability that foram will migrate to another ocean fragment.
	 * If current 'travels' one unit per step (one ocean fragment per step) then the probability of migration is 100%.
	 */
	private double getCurrentStrength() {
		double distance = getOceanFragment().getEnvState().currentDirection.getStrength();
		return unitsConverter.metersToUnits(distance);
	}

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
		energy -= energy * genotype.get(Genome.CHAMBER_GROWTH_COST_FACTOR).getValue();
		shell = shellFactory.createShellWithNewChamber(this);
	}

	private void tryMigrate() {

		AgentAddress migrationTarget;
		foramActiveMotion = config.foramActiveMotion();
		if (foramActiveMotion) {
			migrationTarget = getOceanFragment().getActiveMigrationTarget();
		} else {
			migrationTarget = tryFlowWithCurrent();
		}

		if (migrationTarget != null && !migrationTarget.equals(lastParentAddress)) {
			doAction(AgentActions.migrate(this, migrationTarget));
		}
	}

	private double migrationProbability = 0.0;

	private AgentAddress tryFlowWithCurrent() {
		migrationProbability += 0.01;
		double currentStrength = getCurrentStrength();
		if (random.nextDouble() < migrationProbability + currentStrength) {
			migrationProbability = 0.0;
			return getOceanFragment().getPlanktonicMigrationTarget();
		}
		return null;
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
