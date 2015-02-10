package pl.edu.agh.evolutus.environment;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.stream.Collectors;

import javax.inject.Inject;

import org.jage.address.agent.AgentAddress;
import org.jage.address.agent.AgentAddressSupplier;
import org.jage.agent.SimpleAggregate;
import org.jage.platform.component.exception.ComponentException;
import org.jage.query.AgentEnvironmentQuery;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import pl.edu.agh.evolutus.config.IEnvironmentConfigService;
import pl.edu.agh.evolutus.foram.IForam;
import pl.edu.agh.evolutus.service.CoordinatesService;
import pl.edu.agh.evolutus.service.StatisticsService;
import pl.edu.agh.evolutus.utils.Vector;

public class OceanFragment extends SimpleAggregate implements IOceanFragment {

	private static final Logger logger = LoggerFactory.getLogger(OceanFragment.class);
	private final Random random = new Random();

	@Inject
	private StatisticsService statisticsService;

	@Inject
	private IEnvironmentConfigService configService;

	private IOceanFragmentProperties oceanFragmentProperties;

	private OceanFragmentContainer oceanFragmentContainer;

	private List<IForam> foramsToAdd = new ArrayList<>();

	private List<IForam> foramsToRemove = new ArrayList<>();

	@Inject
	public OceanFragment(AgentAddressSupplier supplier, CoordinatesService coordinatesService,
			IEnvironmentConfigService configService) {
		super(supplier);
		Vector oceanSize = coordinatesService.getSize();
		Vector position = coordinatesService.createCoordinates();
		this.oceanFragmentProperties = new OceanFragmentProperties(oceanSize, position, configService);
	}

	@Override
	public IOceanFragmentProperties getOceanFragmentProperties() {
		return oceanFragmentProperties;
	}

	@Override
	public Vector getPosition() {
		return oceanFragmentProperties.getPosition();
	}

	@Override
	public void setOceanFragmentContainer(OceanFragmentContainer oceanFragmentContainer) {
		this.oceanFragmentContainer = oceanFragmentContainer;
	}

	@Override
	public Collection<IForam> getForams() {
		return getAgents().stream().filter(agent -> agent instanceof IForam).map(agent -> (IForam) agent)
				.collect(Collectors.toList());
	}

	@Override
	public void init() throws ComponentException {
		super.init();

		logger.debug("Initialized ocean fragment: {} {}", getAddress(), oceanFragmentProperties.getPosition());

		long initialForamsCount = configService.getInitialForamsCount(getPosition());
		for (long i = 0; i < initialForamsCount; i++) {
			IForam foram = instanceProvider.getInstance(IForam.class);
			add(foram);
		}
	}

	private long steps = 0;

	@Override
	public void step() {
		super.step();

		statisticsService.addStatistics(oceanFragmentProperties.getPosition(), steps++, foramsAlive());

		oceanFragmentProperties.regenerateAlgae();
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
			Vector position = oceanFragmentProperties.getPosition();
			Vector size = oceanFragmentProperties.getOceanSize();
			final Map<Vector, Double> targetCoordinateProbabilities = oceanFragmentProperties.getCurrentDirection()
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
}
