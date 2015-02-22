package pl.edu.agh.evolutus.environment;

import pl.edu.agh.evolutus.utils.CurrentDirection;
import pl.edu.agh.evolutus.utils.VectorL;

public interface IOceanFragmentProperties {

	VectorL getOceanSize();

	VectorL getPosition();

	double getInsolation();

	double getAlgaeEnergy();

	double getAlgaeGrowth();

	double getAlgaeAvailability();

	void regenerateAlgae();

	void increaseAlgaeAvailability(double amount);

	void decreaseAlgaeAvailability(double amount);

	CurrentDirection getCurrentDirection();

	double getCurrentStrength();
}
