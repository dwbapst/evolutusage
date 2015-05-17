package pl.edu.agh.evolutus.environment;

import pl.edu.agh.evolutus.service.config.EnvironmentConfig;
import pl.edu.agh.evolutus.utils.CurrentDirection;
import pl.edu.agh.evolutus.utils.Position;
import pl.edu.agh.evolutus.utils.VectorL;

public class OceanFragmentProperties {

	private final VectorL oceanSize;
	private final Position position;
	private final double insolation;
	private final double algaeEnergy;
	private final double algaeGrowth;
	private double algaeAvailability;
	private final CurrentDirection currentDirection;
	private final BoundaryConditions boundaryConditions;

	public OceanFragmentProperties(Position position, EnvironmentConfig config) {
		this.oceanSize = config.oceanSize();
		this.position = position;
		this.insolation = config.insolation(position);
		this.algaeEnergy = config.algaeEnergy();
		this.algaeGrowth = config.algaeGrowth(insolation);
		this.algaeAvailability = config.initialAlgaeAvailability(position);
		this.currentDirection = config.currentDirection(position);
		this.boundaryConditions = config.boundaryConditions();
	}

	public VectorL getOceanSize() {
		return oceanSize;
	}

	public Position getPosition() {
		return position;
	}

	public double getInsolation() {
		return insolation;
	}

	public double getAlgaeEnergy() {
		return algaeEnergy;
	}

	public double getAlgaeGrowth() {
		return algaeGrowth;
	}

	public double getAlgaeAvailability() {
		return algaeAvailability;
	}

	public void regenerateAlgae() {
		increaseAlgaeAvailability(algaeGrowth);
	}

	public void increaseAlgaeAvailability(double amount) {
		this.algaeAvailability += amount;
	}

	public void decreaseAlgaeAvailability(double amount) {
		this.algaeAvailability -= amount;
	}

	public CurrentDirection getCurrentDirection() {
		return currentDirection;
	}

	public BoundaryConditions getBoundaryConditions() {
		return boundaryConditions;
	}
}
