package pl.edu.agh.evolutus.environment;

import pl.edu.agh.evolutus.config.IEnvironmentConfigService;
import pl.edu.agh.evolutus.utils.CurrentDirection;
import pl.edu.agh.evolutus.utils.Vector;

public class OceanFragmentProperties implements IOceanFragmentProperties {

	private final Vector oceanSize;
	private final Vector position;
	private final double insolation;
	private final double algaeEnergy;
	private final double algaeGrowth;
	private double algaeAvailability;
	private final CurrentDirection currentDirection;
	private final double currentStrength;

	public OceanFragmentProperties(Vector oceanSize, Vector position, IEnvironmentConfigService configService) {
		this.oceanSize = oceanSize;
		this.position = position;
		this.insolation = configService.getInsolation(position);
		this.algaeEnergy = configService.getAlgaeEnergy();
		this.algaeGrowth = configService.getAlgaeGrowth(insolation);
		this.algaeAvailability = configService.getInitialAlgaeAvailability(position);
		this.currentDirection = configService.getCurrentDirection(position);
		this.currentStrength = configService.getCurrentStrength(position);
	}

	@Override
	public Vector getOceanSize() {
		return oceanSize;
	}

	@Override
	public Vector getPosition() {
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
