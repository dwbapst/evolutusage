package pl.edu.agh.evolutus.environment;

import org.apache.commons.lang3.tuple.Pair;
import org.jage.address.agent.AgentAddress;
import org.jage.agent.ISimpleAggregate;
import pl.edu.agh.evolutus.foram.ForamType;
import pl.edu.agh.evolutus.foram.IForam;
import pl.edu.agh.evolutus.genotype.Genome;
import pl.edu.agh.evolutus.service.config.utils.EnvState;
import pl.edu.agh.evolutus.utils.VectorL;

import java.util.Collection;
import java.util.List;

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

	int foramsHaploid();

	int foramsDiploid();

	void onForamDied();

	double totalEnergy();

	double averageEnergy();

	double averageShellVolume();

	Pair<AgentAddress, VectorL> getPassiveMigrationTarget();

	Pair<AgentAddress, VectorL> getActiveMigrationTarget();

	long currentStep();
}
