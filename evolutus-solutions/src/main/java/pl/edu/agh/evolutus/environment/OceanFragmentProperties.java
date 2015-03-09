package pl.edu.agh.evolutus.environment;

import pl.edu.agh.evolutus.config.EnvironmentConfig;
import pl.edu.agh.evolutus.utils.CurrentDirection;
import pl.edu.agh.evolutus.utils.VectorL;

public class OceanFragmentProperties implements IOceanFragmentProperties {

	private final VectorL oceanSize;
	private final VectorL position;
	private final double insolation;
	private final double algaeEnergy;
	private final double algaeGrowth;
	private double algaeAvailability;
	private final CurrentDirection currentDirection;
	private final double currentStrength;

	public OceanFragmentProperties(VectorL position, EnvironmentConfig configService) {
		this.oceanSize = configService.oceanSize();
		this.position = position;
		this.insolation = configService.insolation(position);
		this.algaeEnergy = configService.algaeEnergy();
		this.algaeGrowth = configService.algaeGrowth(insolation);
		this.algaeAvailability = configService.initialAlgaeAvailability(position);
		this.currentDirection = configService.currentDirection(position);
		this.currentStrength = configService.currentStrength(position);
	}

	@Override
	public VectorL getOceanSize() {
		return oceanSize;
	}

	@Override
	public VectorL getPosition() {
		return position;
	}

	@Override
	public double getInsolation() {
		return insolation;
	}

	@Override
	public double getAlgaeEnergy() {
		return algaeEnergy;
	}

	@Override
	public double getAlgaeGrowth() {
		return algaeGrowth;
	}

	@Override
	public double getAlgaeAvailability() {
		return algaeAvailability;
	}

	@Override
	public void regenerateAlgae() {
		increaseAlgaeAvailability(algaeGrowth);
	}

	@Override
	public void increaseAlgaeAvailability(double amount) {
		this.algaeAvailability += amount;
	}

	@Override
	public void decreaseAlgaeAvailability(double amount) {
		this.algaeAvailability -= amount;
	}

	@Override
	public CurrentDirection getCurrentDirection() {
		return currentDirection;
	}

	@Override
	public double getCurrentStrength() {
		return currentStrength;
	}

}
