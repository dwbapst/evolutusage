package pl.edu.agh.evolutus.environment;

import java.util.Collection;
import java.util.LinkedList;
import java.util.Queue;

import javax.inject.Inject;

import org.jage.address.agent.AgentAddress;
import org.jage.address.agent.AgentAddressSupplier;
import org.jage.agent.IAgent;
import org.jage.platform.component.exception.ComponentException;
import org.jage.query.AgentEnvironmentQuery;
import org.jage.workplace.ConnectedSimpleWorkplace;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import pl.edu.agh.evolutus.service.config.EnvironmentConfig;
import pl.edu.agh.evolutus.service.config.SimulationConfig;
import pl.edu.agh.evolutus.utils.Position;
import pl.edu.agh.evolutus.utils.VectorL;

public class OceanFragmentContainer extends ConnectedSimpleWorkplace implements IOceanFragmentContainer {

	private static final Logger logger = LoggerFactory.getLogger(OceanFragmentContainer.class);

	@Inject
	private SimulationConfig simulationConfig;

	@Inject
	private EnvironmentConfig environmentConfig;

	public OceanFragmentContainer(final AgentAddress address) {
		super(address);
	}

	@Inject
	public OceanFragmentContainer(final AgentAddressSupplier supplier) {
		super(supplier);
	}

	@Override
	public <E extends IAgent, T> Collection<T> queryEnvironment(AgentEnvironmentQuery<E, T> query) {
		return super.queryEnvironment(query);
	}

	@Override
	public void init() throws ComponentException {
		super.init();

		logger.debug("Initialized ocean fragment container: {}", getAddress());

		VectorL oceanSize = environmentConfig.oceanSize();
		for (long x = 0; x < oceanSize.x; x++) {
			for (long y = 0; y < oceanSize.y; y++) {
				for (long z = 0; z < oceanSize.z; z++) {
					IOceanFragment oceanFragment = instanceProvider.getInstance(IOceanFragment.class);
					Position position = new Position(x, y, z);
					oceanFragment.initialize(position);
					add(oceanFragment);
				}
			}
		}
		beg = System.currentTimeMillis();
		simulationDuration = simulationConfig.simulationDuration();
	}

	@Override
	public boolean finish() throws ComponentException {
		logger.info("Simulation time: {}s", (System.currentTimeMillis() - beg) / 1000.0);
		return super.finish();
	}

	private long beg;

	private long simulationDuration;

	private long steps = 0;

	@Override
	public void step() {
		logProgress();
		super.step();
		steps++;
	}

	private double lastProgress = -1.0;
	private Queue<Long> lastProgressTimes = new LinkedList<>();

	private void logProgress() {
		long progress = Math.round(100.0 * steps / simulationDuration);
		if (progress > lastProgress) {
			String message = String.format("Progress: %s%%.", progress);

			if (lastProgressTimes.size() >= 10) {
				double timeSinceLastProgress = (System.currentTimeMillis() - lastProgressTimes.poll()) / 1000.0;
				long timeRemaining = Math.round((100.0 - progress) * timeSinceLastProgress / 10.0);
				message += String.format(" Time remaining: %sm %ss", timeRemaining / 60, timeRemaining %
						60);
			}
			logger.info(message);

			lastProgress = progress;
			lastProgressTimes.offer(System.currentTimeMillis());
		}
	}

}
