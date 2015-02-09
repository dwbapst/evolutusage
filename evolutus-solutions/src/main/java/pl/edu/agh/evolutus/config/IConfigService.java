package pl.edu.agh.evolutus.config;

import pl.edu.agh.evolutus.utils.CurrentDirection;
import pl.edu.agh.evolutus.utils.Vector;

public interface IConfigService {

	Vector getOceanSize();

	double getAlgaeEnergy();

	long getInitialForamsCount(Vector position);

	double getInitialAlgaeAvailability(Vector position);

	double getAlgaeGrowth(double insolation);

	double getInsolation(Vector position);

	CurrentDirection getCurrentDirection(Vector position);

	double getCurrentStrength(Vector position);
}
