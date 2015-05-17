package pl.edu.agh.evolutus.environment;

import java.util.Collection;
import java.util.List;

import org.jage.address.agent.AgentAddress;
import org.jage.agent.ISimpleAggregate;

import pl.edu.agh.evolutus.foram.ForamType;
import pl.edu.agh.evolutus.foram.IForam;
import pl.edu.agh.evolutus.genotype.Genome;
import pl.edu.agh.evolutus.utils.VectorL;

public interface IOceanFragment extends ISimpleAggregate {

	OceanFragmentProperties getOceanFragmentProperties();

	VectorL getPosition();

	Collection<IForam> getForams();

	void addGametes(List<Genome> gametes, ForamType foramType);

	void initialize(VectorL position);

	double takeAlgae(double demand);

	int foramsAlive();

	double totalEnergy();

	AgentAddress getMigrationTarget();

	long currentStep();
}
