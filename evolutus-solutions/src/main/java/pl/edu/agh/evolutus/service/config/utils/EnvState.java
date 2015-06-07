package pl.edu.agh.evolutus.service.config.utils;

import pl.edu.agh.evolutus.environment.OceanFragmentProperties;
import pl.edu.agh.evolutus.utils.CurrentDirection;
import pl.edu.agh.evolutus.utils.VectorD;

public class EnvState {

	public final VectorD position;
	public final double insolation;
	public final double algaeEnergy;
	public final double algaeGrowth;
	public final double algaeAvailability;
	public final CurrentDirection currentDirection;

	public EnvState(OceanFragmentProperties properties, UnitsConverter unitsConverter) {
		this(
				unitsConverter.unitsToMeters(properties.getPosition()),
				properties.getInsolation(),
				properties.getAlgaeEnergy(),
				properties.getAlgaeGrowth(),
				properties.getAlgaeAvailability(),
				properties.getCurrentDirection()
		);
	}

	public EnvState(VectorD position, double insolation, double algaeEnergy, double algaeGrowth, double algaeAvailability,
			CurrentDirection currentDirection) {
		this.position = position;
		this.insolation = insolation;
		this.algaeEnergy = algaeEnergy;
		this.algaeGrowth = algaeGrowth;
		this.algaeAvailability = algaeAvailability;
		this.currentDirection = currentDirection;
	}
}
