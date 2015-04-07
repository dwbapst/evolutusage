package pl.edu.agh.evolutus.environment;

import java.util.Collection;

import javax.inject.Inject;

import org.jage.address.agent.AgentAddress;
import org.jage.address.agent.AgentAddressSupplier;
import org.jage.agent.IAgent;
import org.jage.platform.component.exception.ComponentException;
import org.jage.query.AgentEnvironmentQuery;
import org.jage.workplace.ConnectedSimpleWorkplace;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import pl.edu.agh.evolutus.config.ConfigFactory;
import pl.edu.agh.evolutus.config.EnvironmentConfig;
import pl.edu.agh.evolutus.utils.VectorL;

public class OceanFragmentContainer extends ConnectedSimpleWorkplace implements IOceanFragmentContainer {

	private static final Logger logger = LoggerFactory.getLogger(OceanFragmentContainer.class);

	private EnvironmentConfig config;

	public OceanFragmentContainer(final AgentAddress address) {
		super(address);
	}

	@Inject
	public OceanFragmentContainer(final AgentAddressSupplier supplier, final ConfigFactory configFactory) {
		super(supplier);
		this.config = configFactory.getEnvironmentConfig();
	}

	@Override
	public <E extends IAgent, T> Collection<T> queryEnvironment(AgentEnvironmentQuery<E, T> query) {
		return super.queryEnvironment(query);
	}

	@Override
	public void init() throws ComponentException {
		super.init();

		logger.debug("Initialized ocean fragment container: {}", getAddress());

		VectorL oceanSize = config.oceanSize();
		for (long x = 0; x < oceanSize.x; x++) {
			for (long y = 0; y < oceanSize.y; y++) {
				for (long z = 0; z < oceanSize.z; z++) {
					IOceanFragment oceanFragment = instanceProvider.getInstance(IOceanFragment.class);
					VectorL position = new VectorL(x, y, z);
					oceanFragment.initialize(position);
					add(oceanFragment);
				}
			}
		}
		beg = System.currentTimeMillis();
	}

	@Override
	public boolean finish() throws ComponentException {
		logger.info("Simulation time: {}s", (System.currentTimeMillis() - beg) / 1000.0);
		return super.finish();
	}

	private long beg;

	private int foramsCount() {
		return getAgents().stream()
				.filter(agent -> agent instanceof IOceanFragment)
				.map(agent -> (IOceanFragment) agent)
				.mapToInt(oceanFragment -> oceanFragment.getForams().size())
				.sum();
	}

	@Override
	public void step() {
		logger.debug("Forams  count: {}", foramsCount());
		super.step();
	}
}
