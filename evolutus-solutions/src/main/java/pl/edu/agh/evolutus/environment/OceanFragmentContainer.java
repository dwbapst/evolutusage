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

public class OceanFragmentContainer extends ConnectedSimpleWorkplace implements IOceanFragmentContainer {

	private static final Logger logger = LoggerFactory.getLogger(OceanFragmentContainer.class);

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

		temporaryAgentsList.stream().filter(agent -> agent instanceof IOceanFragment).forEach(agent -> {
			IOceanFragment oceanFragment = (IOceanFragment) agent;
			oceanFragment.setOceanFragmentContainer(this);
		});
	}

}
