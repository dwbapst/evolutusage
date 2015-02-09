package pl.edu.agh.evolutus.environment;

import javax.inject.Inject;

import org.jage.address.agent.AgentAddress;
import org.jage.address.agent.AgentAddressSupplier;
import org.jage.workplace.ConnectedSimpleWorkplace;

public class OceanFragmentContainer extends ConnectedSimpleWorkplace {

	public OceanFragmentContainer(final AgentAddress address) {
		super(address);
	}

	@Inject
	public OceanFragmentContainer(final AgentAddressSupplier supplier) {
		super(supplier);
	}
}
