package pl.edu.agh.evolutus.environment;

import java.util.Collection;

import org.jage.address.agent.AgentAddress;

import pl.edu.agh.evolutus.foram.IForam;
import pl.edu.agh.evolutus.utils.VectorL;

public interface IOceanFragment {

	IOceanFragmentProperties getOceanFragmentProperties();

	VectorL getPosition();

	void setOceanFragmentContainer(OceanFragmentContainer oceanFragmentContainer);

	Collection<IForam> getForams();

	double takeAlgae(double demand);

	int foramsAlive();

	AgentAddress getMigrationTarget();
}
