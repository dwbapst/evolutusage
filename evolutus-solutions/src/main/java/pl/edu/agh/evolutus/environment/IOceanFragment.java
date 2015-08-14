package pl.edu.agh.evolutus.environment;

import java.util.Collection;
import java.util.List;

import org.apache.commons.lang3.tuple.Pair;
import org.jage.address.agent.AgentAddress;
import org.jage.agent.ISimpleAggregate;

import pl.edu.agh.evolutus.foram.ForamType;
import pl.edu.agh.evolutus.foram.IForam;
import pl.edu.agh.evolutus.genotype.Genome;
import pl.edu.agh.evolutus.service.config.utils.EnvState;
import pl.edu.agh.evolutus.utils.VectorL;

public interface IOceanFragment extends ISimpleAggregate {

	EnvState getEnvState();

	EnvState getPrevEnvState();

	VectorL getPosition();

	double getAlgaeAvailability();

	Collection<IForam> getForams();

	void addGametes(List<Genome> gametes, ForamType foramType);

	void initialize(VectorL position);

	double takeAlgae(double demand, double radiusOfCollectingInMeters);

	int foramsAlive();

	double totalEnergy();

	Pair<AgentAddress, VectorL> getPassiveMigrationTarget();

	Pair<AgentAddress, VectorL> getActiveMigrationTarget();

	long currentStep();
}
