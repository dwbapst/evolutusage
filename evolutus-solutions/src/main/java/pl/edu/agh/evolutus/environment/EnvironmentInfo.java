package pl.edu.agh.evolutus.environment;

public class EnvironmentInfo implements IEnvironmentInfo {

	private Coordinates oceanSize;
	private Coordinates position;
	private double nutrientAvailability;

	public EnvironmentInfo(Coordinates oceanSize, Coordinates position, double nutrientAvailability) {
		this.oceanSize = oceanSize;
		this.position = position;
		this.nutrientAvailability = nutrientAvailability;
	}

	@Override
	public Coordinates getOceanSize() {
		return oceanSize;
	}

	@Override
	public Coordinates getPosition() {
		return position;
	}

	@Override
	public double getNutrientAvailability() {
		return nutrientAvailability;
	}

}
