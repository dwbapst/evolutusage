package pl.edu.agh.evolutus.service.config.utils;

import pl.edu.agh.evolutus.utils.VelocityVector;
import pl.edu.agh.evolutus.utils.VectorD;

public class EnvState {

	public final double oxygen;
	public final double temperature;
	public final double salinity;
	public final double algaeAvailability;
	public final double insolation;
	public final double ph;

	public final VectorD position;
	public final double algaeEnergy;
	public final double algaeGrowth;
	public final VelocityVector currentDirection;

	public EnvState(EnvState envState, double algaeAvailability) {
		this(envState.oxygen, envState.temperature, envState.salinity, (envState.algaeAvailability < 0) ? 0.0: algaeAvailability, envState.insolation,
				envState.ph, envState.position, envState.algaeEnergy, envState.algaeGrowth, envState.currentDirection);
	}

	public EnvState(double oxygen, double temperature, double salinity, double algaeAvailability, double insolation, double ph,
			VectorD position, double algaeEnergy, double algaeGrowth, VelocityVector currentDirection) {
		this.oxygen = oxygen;
		this.temperature = temperature;
		this.salinity = salinity;
		this.algaeAvailability = algaeAvailability;
		this.insolation = insolation;
		this.ph = ph;

		this.position = position; //real worls coordinates
		this.algaeEnergy = algaeEnergy;
		this.algaeGrowth = algaeGrowth;
		this.currentDirection = currentDirection;
	}
}
