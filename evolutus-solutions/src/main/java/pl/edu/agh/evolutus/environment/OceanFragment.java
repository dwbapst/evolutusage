package pl.edu.agh.evolutus.environment;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.stream.Collectors;

import javax.inject.Inject;

import org.apache.commons.lang3.tuple.Pair;
import org.jage.address.agent.AgentAddress;
import org.jage.address.agent.AgentAddressSupplier;
import org.jage.agent.SimpleAggregate;
import org.jage.query.AgentEnvironmentQuery;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import pl.edu.agh.evolutus.foram.ForamType;
import pl.edu.agh.evolutus.foram.IForam;
import pl.edu.agh.evolutus.genotype.Genome;
import pl.edu.agh.evolutus.service.ForamFactory;
import pl.edu.agh.evolutus.service.ReproductionService;
import pl.edu.agh.evolutus.service.StatisticsService;
import pl.edu.agh.evolutus.service.StatisticsService.StatisticsServiceException;
import pl.edu.agh.evolutus.service.config.EnvironmentConfig;
import pl.edu.agh.evolutus.service.config.utils.EnvState;
import pl.edu.agh.evolutus.service.config.utils.UnitsConverter;
import pl.edu.agh.evolutus.statistics.model.OceanFragmentInfo;
import pl.edu.agh.evolutus.utils.Position;
import pl.edu.agh.evolutus.utils.VectorL;

public class OceanFragment extends SimpleAggregate implements IOceanFragment {

	private static final Logger logger = LoggerFactory.getLogger(OceanFragment.class);
	private final Random random = new Random();

	@Inject
	private StatisticsService statisticsService;

	@Inject
	private ReproductionService reproductionService;

	@Inject
	private ForamFactory foramFactory;

	@Inject
	private EnvironmentConfig config;

	@Inject
	private UnitsConverter unitsConverter;

	@Inject
	public OceanFragment(AgentAddressSupplier supplier) {
		super(supplier);
	}

	private long steps = 0;
	private EnvState envState;
	private Position position;

	// gamete -> type, age in steps
	private Map<Genome, Pair<ForamType, Integer>> gametesMap = new HashMap<>();

	@Override
	public EnvState getEnvState() {
		return envState;
	}

	@Override
	public Position getPosition() {
		return position;
	}

	@Override
	public double getAlgaeAvailability() {
		return envState.algaeAvailability;
	}

	@Override
	public Collection<IForam> getForams() {
		return getAgents().stream().filter(agent -> agent instanceof IForam).map(agent -> (IForam) agent)
				.collect(Collectors.toList());
	}

	@Override
	public void addGametes(List<Genome> gametes, ForamType foramType) {
		for (Genome gamete : gametes) {
			this.gametesMap.put(gamete, Pair.of(foramType, 0));
		}
	}

	@Override
	public void initialize(Position position) {
		this.position = position;
		this.envState = new EnvState(unitsConverter.unitsToMeters(position), 0.0, 0.0, 0.0,
				config.initialAlgaeAvailability(position), null);

		long initialForamsCount = config.initialForamsCount(position);
		for (long i = 0; i < initialForamsCount; i++) {
			add(createInitialForam());
		}
		logger.debug("Initialized ocean fragment: {} {}", getAddress(), position);
	}

	private IForam createInitialForam() {
		Genome initialGenome = config.initialGenome(position);

		if (config.oceanSize().z - 1 == position.z) {
			// benthos
			if (random.nextBoolean()) {
				// haploid
				return foramFactory.createForam(ForamType.HAPLOID_BENTHIC, initialGenome);
			} else {
				// diploid
				return foramFactory.createForam(ForamType.DIPLOID_BENTHIC, initialGenome, initialGenome);
			}
		} else {
			// plankton
			return foramFactory.createForam(ForamType.PLANKTONIC, initialGenome, initialGenome);
		}
	}

	private synchronized void updateEnvState() {
		envState = new EnvState(
				unitsConverter.unitsToMeters(position),
				config.insolation(steps, envState),
				config.algaeEnergy(steps, envState),
				config.algaeGrowth(steps, envState),
				envState.algaeAvailability,
				config.currentDirection(steps, envState)
		);
	}

	private synchronized void changeAlgaeAvailability(double amount) {
		envState = new EnvState(envState, envState.algaeAvailability + amount);
	}

	@Override
	public void step() {
		updateEnvState();
		changeAlgaeAvailability(envState.algaeGrowth); // regenerate algae

		Collection<IForam> foramsToAdd = reproductionService.processGametesAndReturnNewForams(gametesMap);
		addAll(foramsToAdd);

		super.step(); // call forams' step() methods
		addStats();
		steps++;
	}

	private void addStats() {
		try {
			OceanFragmentInfo info = new OceanFragmentInfo(statisticsService.getSimulation(), steps, position, foramsAlive(),
					envState.algaeAvailability, totalEnergy(), envState.insolation);
			statisticsService.add(info);
		} catch (StatisticsServiceException e) {
			logger.debug(e.getMessage(), e);
		}
	}

	@Override
	public double takeAlgae(double energyDemand) {
		double algaeNeeded = energyDemand / envState.algaeEnergy;
		double availableAlgae = envState.algaeAvailability;
		double takenAlgae = Math.min(availableAlgae, algaeNeeded);
		changeAlgaeAvailability(-takenAlgae);
		return takenAlgae * envState.algaeEnergy;
	}

	@Override
	public int foramsAlive() {
		return (int) getAgents().stream().filter(agent -> ((IForam) agent).isAlive()).count();
	}

	private Map<AgentAddress, Double> migrationTargetsWithProbability = null;

	private Map<AgentAddress, Double> getMigrationTargetsWithProbability() {
		if (migrationTargetsWithProbability == null) {
			final Map<VectorL, Double> targetCoordinateProbabilities = envState.currentDirection
					.getTargetCoordinateProbabilities(position, config.oceanSize(), config.boundaryConditions());

			AgentEnvironmentQuery<OceanFragment, OceanFragment> query = new AgentEnvironmentQuery<>(OceanFragment.class);
			migrationTargetsWithProbability = queryParent(
					query.matching(
							oceanFragment -> targetCoordinateProbabilities.containsKey(oceanFragment.getPosition())
					))
					.stream()
					.collect(Collectors.toMap(
									OceanFragment::getAddress,
									oceanFragment -> targetCoordinateProbabilities.get(oceanFragment.getPosition()))
					);
		}
		return migrationTargetsWithProbability;
	}

	@Override
	public double totalEnergy() {
		return getAgents()
				.stream()
				.map(agent -> (IForam) agent)
				.filter(IForam::isAlive)
				.mapToDouble(IForam::getEnergy)
				.sum();
	}

	@Override
	public AgentAddress getPlanktonicMigrationTarget() {
		Map<AgentAddress, Double> migrationTargetsWithProbability = getMigrationTargetsWithProbability();
		double rand = random.nextDouble();
		double probability = 0.0;
		for (AgentAddress migrationTarget : migrationTargetsWithProbability.keySet()) {
			probability += migrationTargetsWithProbability.get(migrationTarget);
			if (rand <= probability) {
				return migrationTarget;
			}
		}
		return null;
	}

	@Override
	public AgentAddress getBenthicMigrationTarget() {
		List<Position> neighborhood = position.getTheSameLevelNeighborhood(config.oceanSize(), config.boundaryConditions());

		AgentEnvironmentQuery<OceanFragment, OceanFragment> query = new AgentEnvironmentQuery<>(OceanFragment.class);
		Collection<OceanFragment> neighbors = queryParent(
				query.matching(
						oceanFragment -> neighborhood.contains(oceanFragment.getPosition())
				));

		OceanFragment target = this;
		for (OceanFragment neighbor : neighbors) {
			if (neighbor.getAlgaeAvailability() > target.getAlgaeAvailability()) {
				target = neighbor;
			}
		}
		return target.getAddress();
	}

	@Override
	public long currentStep() {
		return steps;
	}

}
