package pl.edu.agh.evolutus.environment;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.stream.Collectors;

import javax.inject.Inject;

import org.jage.address.agent.AgentAddress;
import org.jage.address.agent.AgentAddressSupplier;
import org.jage.agent.SimpleAggregate;
import org.jage.query.AgentEnvironmentQuery;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import pl.edu.agh.evolutus.foram.IForam;
import pl.edu.agh.evolutus.genotype.Genome;
import pl.edu.agh.evolutus.service.ReproductionService;
import pl.edu.agh.evolutus.service.StatisticsService;
import pl.edu.agh.evolutus.service.StatisticsService.StatisticsServiceException;
import pl.edu.agh.evolutus.service.config.EnvironmentConfig;
import pl.edu.agh.evolutus.statistics.model.OceanFragmentInfo;
import pl.edu.agh.evolutus.utils.VectorL;

public class OceanFragment extends SimpleAggregate implements IOceanFragment {

	private static final Logger logger = LoggerFactory.getLogger(OceanFragment.class);
	private final Random random = new Random();

	@Inject
	private StatisticsService statisticsService;

	@Inject
	private ReproductionService reproductionService;

	@Inject
	private EnvironmentConfig config;

	private OceanFragmentProperties oceanFragmentProperties;

	// gamete -> age in steps
	private Map<Genome, Integer> gametesWithAge = new HashMap<>();

	@Inject
	public OceanFragment(AgentAddressSupplier supplier) {
		super(supplier);
	}

	@Override
	public OceanFragmentProperties getOceanFragmentProperties() {
		return oceanFragmentProperties;
	}

	@Override
	public VectorL getPosition() {
		return oceanFragmentProperties.getPosition();
	}

	@Override
	public Collection<IForam> getForams() {
		return getAgents().stream().filter(agent -> agent instanceof IForam).map(agent -> (IForam) agent)
				.collect(Collectors.toList());
	}

	@Override
	public void addGametes(List<Genome> gametes) {
		for (Genome gamete : gametes) {
			this.gametesWithAge.put(gamete, 0);
		}
	}

	@Override
	public void initialize(VectorL position) {
		this.oceanFragmentProperties = new OceanFragmentProperties(position, this.config);

		long initialForamsCount = config.initialForamsCount(position);
		for (long i = 0; i < initialForamsCount; i++) {
			Genome initialGenome = config.initialGenome(position);
			add(reproductionService.createForam(initialGenome, initialGenome));
		}
		logger.debug("Initialized ocean fragment: {} {}", getAddress(), oceanFragmentProperties.getPosition());
	}

	private long steps = 0;

	@Override
	public void step() {
		super.step();

		addStats();
		oceanFragmentProperties.regenerateAlgae();

		Collection<IForam> foramsToAdd = reproductionService.processGametesAndReturnNewForams(gametesWithAge);
		addAll(foramsToAdd);
	}

	private void addStats() {
		try {
			long x = oceanFragmentProperties.getPosition().x;
			long y = oceanFragmentProperties.getPosition().y;
			long z = oceanFragmentProperties.getPosition().z;
			double algaeAvailability = oceanFragmentProperties.getAlgaeAvailability();
			double insolation = oceanFragmentProperties.getInsolation();
			OceanFragmentInfo info = new OceanFragmentInfo(statisticsService.getSimulation(), steps++, x, y, z, foramsAlive(),
					algaeAvailability,
					totalEnergy(), insolation);
			statisticsService.add(info);
		} catch (StatisticsServiceException e) {
			logger.debug(e.getMessage(), e);
		}
	}

	@Override
	public double takeAlgae(double energyDemand) {
		double algaeNeeded = energyDemand / oceanFragmentProperties.getAlgaeEnergy();
		double availableAlgae = oceanFragmentProperties.getAlgaeAvailability();
		double takenAlgae = Math.min(availableAlgae, algaeNeeded);
		oceanFragmentProperties.decreaseAlgaeAvailability(takenAlgae);
		return takenAlgae * oceanFragmentProperties.getAlgaeEnergy();
	}

	@Override
	public int foramsAlive() {
		return (int) getAgents().stream().filter(agent -> ((IForam) agent).isAlive()).count();
	}

	private Map<AgentAddress, Double> migrationTargetsWithProbability = null;

	private Map<AgentAddress, Double> getMigrationTargetsWithProbability() {
		if (migrationTargetsWithProbability == null) {
			VectorL position = oceanFragmentProperties.getPosition();
			VectorL size = oceanFragmentProperties.getOceanSize();
			BoundaryConditions boundaryConditions = oceanFragmentProperties.getBoundaryConditions();
			final Map<VectorL, Double> targetCoordinateProbabilities = oceanFragmentProperties.getCurrentDirection()
					.getTargetCoordinateProbabilities(position, size, boundaryConditions);

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
	public AgentAddress getMigrationTarget() {
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
	public long currentStep() {
		return steps;
	}

}
