package pl.edu.agh.evolutus.environment;

import pl.edu.agh.evolutus.utils.CurrentDirection;
import pl.edu.agh.evolutus.utils.Vector;

public interface IOceanFragmentProperties {

	Vector getOceanSize();

	Vector getPosition();

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
