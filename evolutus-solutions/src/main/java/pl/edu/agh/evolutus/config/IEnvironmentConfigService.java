package pl.edu.agh.evolutus.config;

import pl.edu.agh.evolutus.genome.Genome;
import pl.edu.agh.evolutus.utils.CurrentDirection;
import pl.edu.agh.evolutus.utils.VectorL;

public interface IEnvironmentConfigService {

	VectorL getOceanSize();

	double getAlgaeEnergy();

	long getInitialForamsCount(VectorL position);

	double getInitialAlgaeAvailability(VectorL position);

	double getAlgaeGrowth(double insolation);

	double getInsolation(VectorL position);

	CurrentDirection getCurrentDirection(VectorL position);

	double getCurrentStrength(VectorL position);

	Genome getInitialGenome(VectorL position);

}
