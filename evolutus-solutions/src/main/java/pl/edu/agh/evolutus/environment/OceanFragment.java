package pl.edu.agh.evolutus.environment;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
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

import pl.edu.agh.evolutus.config.EnvironmentConfig;
import pl.edu.agh.evolutus.config.IConfigFactory;
import pl.edu.agh.evolutus.foram.IForam;
import pl.edu.agh.evolutus.genotype.DiploidGenotype;
import pl.edu.agh.evolutus.genotype.Genome;
import pl.edu.agh.evolutus.genotype.operator.CrossingOverOperator;
import pl.edu.agh.evolutus.genotype.operator.OnePointCrossingOverOperator;
import pl.edu.agh.evolutus.genotype.operator.TwoPointCrossingOverOperator;
import pl.edu.agh.evolutus.genotype.operator.UniformCrossingOverOperator;
import pl.edu.agh.evolutus.service.StatisticsService;
import pl.edu.agh.evolutus.utils.VectorL;

public class OceanFragment extends SimpleAggregate implements IOceanFragment {

	private static final Logger logger = LoggerFactory.getLogger(OceanFragment.class);
	private final Random random = new Random();

	@Inject
	private StatisticsService statisticsService;

	private EnvironmentConfig config;

	private IOceanFragmentProperties oceanFragmentProperties;

	private Map<Genome, Integer> gametes = new HashMap<>();

	@Inject
	public OceanFragment(AgentAddressSupplier supplier, IConfigFactory configFactory) {
		super(supplier);
		this.config = configFactory.getEnvironmentConfig();
	}

	@Override
	public IOceanFragmentProperties getOceanFragmentProperties() {
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
			this.gametes.put(gamete, 0);
		}
	}

	@Override
	public void initialize(VectorL position) {
		this.oceanFragmentProperties = new OceanFragmentProperties(position, this.config);

		long initialForamsCount = config.initialForamsCount(position);
		for (long i = 0; i < initialForamsCount; i++) {
			IForam foram = instanceProvider.getInstance(IForam.class);
			foram.setEnergy(config.initialEnergy());
			Genome initialGenome = config.initialGenome(position, foram.getAddress());
			foram.setGenotype(new DiploidGenotype(initialGenome, initialGenome, foram.getAddress(), getCrossingOverOperator()));
			add(foram);
		}
		logger.debug("Initialized ocean fragment: {} {}", getAddress(), oceanFragmentProperties.getPosition());
	}

	private long steps = 0;

	@Override
	public void step() {
		super.step();

		statisticsService.addStatistics(oceanFragmentProperties.getPosition(), steps++, foramsAlive());

		oceanFragmentProperties.regenerateAlgae();

		processGametes();
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

	private Map<OceanFragment, Double> migrationTargetsWithProbability = null;

	private Map<OceanFragment, Double> getMigrationTargetsWithProbability() {
		if (migrationTargetsWithProbability == null) {
			VectorL position = oceanFragmentProperties.getPosition();
			VectorL size = oceanFragmentProperties.getOceanSize();
			final Map<VectorL, Double> targetCoordinateProbabilities = oceanFragmentProperties.getCurrentDirection()
					.getTargetCoordinateProbabilities(position, size);

			AgentEnvironmentQuery<OceanFragment, OceanFragment> query = new AgentEnvironmentQuery<>(OceanFragment.class);
			Collection<OceanFragment> targetOceanFragments = queryParent(query.matching(
					oceanFragment -> targetCoordinateProbabilities.containsKey(oceanFragment.getPosition())));

			migrationTargetsWithProbability = new LinkedHashMap<>();
			for (OceanFragment oceanFragment : targetOceanFragments) {
				migrationTargetsWithProbability
						.put(oceanFragment, targetCoordinateProbabilities.get(oceanFragment.getPosition()));
			}
		}
		return migrationTargetsWithProbability;
	}

	@Override
	public AgentAddress getMigrationTarget() {
		Map<OceanFragment, Double> migrationTargetsWithProbability = getMigrationTargetsWithProbability();
		double rand = random.nextDouble();
		double probability = 0.0;
		for (OceanFragment migrationTarget : migrationTargetsWithProbability.keySet()) {
			probability += migrationTargetsWithProbability.get(migrationTarget);
			if (rand <= probability) {
				return migrationTarget.getAddress();
			}
		}
		return null;
	}

	private void processGametes() {
		List<IForam> foramsToAdd = new ArrayList<>();
		List<Genome> shuffledGametes = new ArrayList<>(gametes.keySet());
		Collections.shuffle(shuffledGametes);

		Genome prev = null;
		for (Genome curr : shuffledGametes) {
			if (gametes.get(curr) > 4) {
				gametes.remove(curr); // Remove gametes older than 4 steps
				continue;
			} else {
				gametes.put(curr, gametes.get(curr) + 1);
			}

			if (prev == null || prev.getForamIdentifier().equals(curr.getForamIdentifier())) {
				prev = curr;
			} else {
				gametes.remove(prev);
				gametes.remove(curr);
				foramsToAdd.add(createForam(prev, curr));
				prev = null;
			}
		}
		addAll(foramsToAdd);
	}

	private IForam createForam(Genome genomeA, Genome genomeB) {
		IForam foram = instanceProvider.getInstance(IForam.class);
		foram.setEnergy(config.initialEnergy());
		foram.setGenotype(new DiploidGenotype(genomeA, genomeB, foram.getAddress(), getCrossingOverOperator()));
		return foram;
	}

	private CrossingOverOperator getCrossingOverOperator() {
		String operatorName = config.crossingOverOperator();
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
}
