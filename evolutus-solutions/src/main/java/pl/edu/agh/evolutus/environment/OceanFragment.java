package pl.edu.agh.evolutus.environment;

import com.google.common.collect.Lists;
import org.apache.commons.lang3.tuple.Pair;
import org.jage.address.agent.AgentAddress;
import org.jage.address.agent.AgentAddressSupplier;
import org.jage.agent.SimpleAggregate;
import org.jage.query.AgentEnvironmentQuery;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pl.edu.agh.evolutus.foram.ForamType;
import pl.edu.agh.evolutus.foram.ForamType.Ploidy;
import pl.edu.agh.evolutus.foram.ForamType.ReproductionType;
import pl.edu.agh.evolutus.foram.IForam;
import pl.edu.agh.evolutus.genotype.Genome;
import pl.edu.agh.evolutus.service.ForamFactory;
import pl.edu.agh.evolutus.service.ReproductionService;
import pl.edu.agh.evolutus.service.StatisticsService;
import pl.edu.agh.evolutus.service.StatisticsService.StatisticsServiceException;
import pl.edu.agh.evolutus.service.config.EnvironmentConfig;
import pl.edu.agh.evolutus.service.config.ForamConfig;
import pl.edu.agh.evolutus.service.config.SimulationConfig;
import pl.edu.agh.evolutus.service.config.utils.EnvState;
import pl.edu.agh.evolutus.service.config.utils.UnitsConverter;
import pl.edu.agh.evolutus.statistics.model.OceanFragmentInfo;
import pl.edu.agh.evolutus.utils.*;

import javax.inject.Inject;
import java.util.*;
import java.util.stream.Collectors;

public class OceanFragment extends SimpleAggregate implements IOceanFragment {

	private int deadForamsCounter=0;
	private int bornForamsCounter=0;
	private static final Logger logger = LoggerFactory.getLogger(OceanFragment.class);
	private final Random random = new Random();
    private int statStride=1;

	@Inject
	private StatisticsService statisticsService;

	@Inject
	private ReproductionService reproductionService;

	@Inject
	private ForamFactory foramFactory;

	@Inject
	private EnvironmentConfig config;

	@Inject
	private ForamConfig foramConfig;

    @Inject
    private SimulationConfig simConfig;

	@Inject
	private UnitsConverter unitsConverter;

	@Inject
	public OceanFragment(AgentAddressSupplier supplier) {
		super(supplier);
	}

	private long steps = 0;
	private VectorL position;
	private List<OceanFragment> neighbors;

	private QueuedMap<Long, EnvState> envStatesCache = new QueuedMap<>(3);

	// gamete -> type, age in steps
	private Map<Genome, Pair<ForamType, Integer>> gametesMap = new HashMap<>();

	@Override
	public EnvState getEnvState() {
		return envStatesCache.getItem(steps);
	}

	@Override
	public EnvState getPrevEnvState() {
		return envStatesCache.getItem(steps - 1);
	}

	private void setEnvState(EnvState envState) {
		envStatesCache.addItem(steps, envState);
	}

	private void setPrevEnvState(EnvState envState) {
		envStatesCache.addItem(steps - 1, envState);
	}

	@Override
	public VectorL getPosition() {
		return position;
	}

	@Override
	public double getAlgaeAvailability() {
		return getEnvState().algaeAvailability;
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
	public void initialize(VectorL position) {
		this.position = position;
        statStride = simConfig.statsStride();
		EnvState initialEnvState = new EnvState(0.0, 0.0, 0.0, config.initialAlgaeAvailability(position), 0.0, 0.0,
				unitsConverter.unitsToMeters(position), 0.0, 0.0, null);
		setEnvState(initialEnvState);
		setPrevEnvState(initialEnvState);

		long initialForamsCount = config.initialForamsCount(position);
		for (long i = 0; i < initialForamsCount; i++) {
			add(createInitialForam());
		}
		logger.debug("Initialized ocean fragment: {} {}", getAddress(), position);
	}

	private void initNeighbors() {
		neighbors = Lists.newArrayList();
		neighbors.add(this);
		neighbors.addAll(getNeighbors());
	}

	private IForam createInitialForam() {
		Genome initialGenome = config.initialGenome(position);

		ReproductionType reproductionType = foramConfig.reproductionType();
		Ploidy ploidy = (reproductionType == ReproductionType.SEXUAL) ? Ploidy.DIPLOID : Ploidy.random();
		ForamType foramType = new ForamType(reproductionType, ploidy);

		if (ploidy == Ploidy.HAPLOID) {
			// haploid
			return foramFactory.createForam(foramType, initialGenome);
		} else {
			// diploid
			return foramFactory.createForam(foramType, initialGenome, initialGenome);
		}
	}

	private synchronized void updateEnvState() {
		EnvState[] envStates = neighbors.stream()
				.map(neighbor -> (neighbor == null) ? null : neighbor.getPrevEnvState())
				.collect(Collectors.toList())
				.toArray(new EnvState[neighbors.size()]);

		EnvState prevEnvState = getPrevEnvState();
		EnvState newEnvState = new EnvState(
				config.oxygen(steps, envStates),
				config.temperature(steps, envStates),
				config.salinity(steps, envStates),
				prevEnvState.algaeAvailability,
				config.insolation(steps, envStates),
				config.ph(steps, envStates),
				unitsConverter.unitsToMeters(position),
				config.algaeEnergy(steps, envStates),
				config.algaeGrowth(steps, envStates),
				config.currentDirection(steps, envStates)
		);
		setEnvState(newEnvState);
	}


	private synchronized void changeAlgaeAvailability(double amount) {
		setEnvState(new EnvState(getEnvState(), getEnvState().algaeAvailability + amount));
	}

	@Override
	public void step() {
		if (steps == 0) {
			initNeighbors();
			updateEnvState();
		}
		changeAlgaeAvailability(getEnvState().algaeGrowth); // regenerate algae

		Collection<IForam> foramsToAdd = reproductionService.processGametesAndReturnNewForams(gametesMap);
		bornForamsCounter += foramsToAdd.size();
		addAll(foramsToAdd);

		super.step(); // call forams' step() methods
		if((steps+statStride) % statStride == 0)
            addStats();
		steps++;
		updateEnvState();
	}

	@Override
	public void onForamDied() {
		deadForamsCounter++;
	}

	private void addStats() {
		try {
			OceanFragmentInfo info = new OceanFragmentInfo(statisticsService.getSimulation(),
					steps, getEnvState().position.mul(unitsConverter.scaleGrid()),
					foramsAlive(), foramsHaploid(),  foramsDiploid(),
					getEnvState().algaeAvailability, averageEnergy(), getEnvState().insolation,
					deadForamsCounter, bornForamsCounter,
                    averageShellVolume(), averageShapeFactor());

			statisticsService.add(info);
			deadForamsCounter=0;
			bornForamsCounter=0;
		} catch (StatisticsServiceException e) {
			logger.debug(e.getMessage(), e);
		}
	}


	@Override
	public double takeAlgae(double energyDemand, double radiusOfCollectingInMeters) {
		double algaeNeeded = energyDemand / getEnvState().algaeEnergy; //in grams
		double availableAlgae = getEnvState().algaeAvailability *
				Geometry.sphereVolume(radiusOfCollectingInMeters) / oceanFragmentVolumeInMeters(); //in grams
		double takenAlgae = Math.min(availableAlgae, algaeNeeded);
		changeAlgaeAvailability(-takenAlgae);  //algae removed from the system in grams
		return takenAlgae * getEnvState().algaeEnergy; //energy of portion (in grams) of algae
	}

	private Double oceanFragmentVolumeInMeters;

	private Double oceanFragmentVolumeInMeters() {
		if (oceanFragmentVolumeInMeters == null) {
			VectorD unitLength = unitsConverter.unitLengthInMeters();
			oceanFragmentVolumeInMeters = Geometry.cuboidVolume(unitLength.x, unitLength.y, unitLength.z);
		}
		return oceanFragmentVolumeInMeters;
	}

	@Override
	public int foramsAlive() {
		return (int) getAgents().stream().filter(agent -> ((IForam) agent).isAlive()).count();
	}

	@Override
	public int foramsHaploid() {
		return (int) getAgents().stream().filter(agent -> ((IForam) agent).getType().getPloidy() == Ploidy.HAPLOID).count();
	}

	@Override
	public int foramsDiploid() {
		return (int) getAgents().stream().filter(agent -> ((IForam) agent).getType().getPloidy() == Ploidy.DIPLOID).count();
	}
	private Pair<VelocityVector, Map<OceanFragment, Double>> migrationTargetsWithProbabilityCache = null;

	private Map<OceanFragment, Double> getPassiveMigrationTargetsWithProbability() {
		if (migrationTargetsWithProbabilityCache == null
				|| !migrationTargetsWithProbabilityCache.getLeft().equals(getEnvState().currentDirection)) {

			final Map<VectorL, Double> targetCoordinateProbabilities = getEnvState().currentDirection
					.getTargetCoordinateProbabilities(position, config.oceanSize(), config.boundaryConditions());

			AgentEnvironmentQuery<OceanFragment, OceanFragment> query = new AgentEnvironmentQuery<>(OceanFragment.class);
			migrationTargetsWithProbabilityCache = Pair.of(
					getEnvState().currentDirection,
					queryParent(
							query.matching(
									oceanFragment -> targetCoordinateProbabilities.containsKey(oceanFragment.getPosition())
							))
							.stream()
							.collect(Collectors.toMap(
											oceanFragment -> oceanFragment,
											oceanFragment -> targetCoordinateProbabilities.get(oceanFragment.getPosition()))
							)
			);
		}
		return migrationTargetsWithProbabilityCache.getRight();
	}

    //summarized energy of all agents inside the cell.
	@Override
	public double totalEnergy() {
		return getAgents()
				.stream()
				.map(agent -> (IForam) agent)
				.filter(IForam::isAlive)
				.mapToDouble(IForam::getEnergy)
				.sum();
	}
    //average energy of all agents inside the cell
	@Override
	public double averageEnergy() {
		return getAgents()
				.stream()
				.map(agent -> (IForam) agent)
				.filter(IForam::isAlive)
				.mapToDouble(IForam::getEnergy)
				.average().orElse(0.0);
	}

    //summarized/average volume of all agents inside the cell
	@Override
	public double averageShellVolume() {
		return getAgents()
				.stream()
				.map(agent -> (IForam) agent)
				.filter(IForam::isAlive)
				.mapToDouble(IForam::getShellVolume)
				.average().orElse(0.0);
	}

    //average shape factor inside the cell.
    @Override
    public double averageShapeFactor() {
        return getAgents()
				.stream()
				.map(agent -> (IForam) agent)
				.filter(IForam::isAlive)
				.mapToDouble(IForam::getShapeFactor)
				.average().orElse(0.0);
    }


	public Pair<AgentAddress, VectorL> getPassiveMigrationTarget() {
		Map<OceanFragment, Double> migrationTargetsWithProbability = getPassiveMigrationTargetsWithProbability();
		double rand = random.nextDouble();
		double probability = 0.0;
		for (OceanFragment target : migrationTargetsWithProbability.keySet()) {
			probability += migrationTargetsWithProbability.get(target);
			if (rand <= probability) {
				return Pair.of(target.getAddress(), target.getPosition().sub(this.getPosition()));
			}
		}
		return null;
	}

	@Override
	public Pair<AgentAddress, VectorL> getActiveMigrationTarget() {
		//move to higher value of food!!!
		OceanFragment target = this;
		for (OceanFragment neighbor : neighbors) {
			if (neighbor != null && neighbor.getAlgaeAvailability() > target.getAlgaeAvailability()) {
				target = neighbor;
			}
		}
		return Pair.of(target.getAddress(), target.getPosition().sub(this.getPosition()));
	}

	@Override
	public long currentStep() {
		return steps;
	}

	private List<OceanFragment> getNeighbors() {
		ArrayList<VectorL> neighborPositions = getNeighborPositions();
		AgentEnvironmentQuery<OceanFragment, OceanFragment> query = new AgentEnvironmentQuery<>(OceanFragment.class);

		Map<VectorL, OceanFragment> neighbors = queryParent(
				query.matching(oceanFragment -> neighborPositions.contains(oceanFragment.getPosition())))
				.stream()
				.collect(Collectors.toMap(
						OceanFragment::getPosition,
						oceanFragment -> oceanFragment
				));

		return neighborPositions
				.stream()
				.map(neighbors::get)
				.collect(Collectors.toList());
	}

	private ArrayList<VectorL> getNeighborPositions() {
		return Lists.newArrayList(
				new VectorL(position.x + 1, position.y, position.z),
				new VectorL(position.x, position.y + 1, position.z),
				new VectorL(position.x, position.y, position.z + 1),
				new VectorL(position.x - 1, position.y, position.z),
				new VectorL(position.x, position.y - 1, position.z),
				new VectorL(position.x, position.y, position.z - 1)
		);
	}

}
