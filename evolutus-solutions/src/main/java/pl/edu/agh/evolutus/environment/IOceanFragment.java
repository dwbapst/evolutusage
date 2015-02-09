package pl.edu.agh.evolutus.environment;

import java.util.Collection;

import org.jage.address.agent.AgentAddress;

import pl.edu.agh.evolutus.foram.IForam;
import pl.edu.agh.evolutus.utils.Vector;

public interface IOceanFragment {

	IOceanFragmentProperties getOceanFragmentProperties();

	Vector getPosition();

	void setOceanFragmentContainer(OceanFragmentContainer oceanFragmentContainer);

	Collection<IForam> getForams();

	double takeAlgae(double demand);

	int foramsAlive();

	AgentAddress getMigrationTarget();
}
