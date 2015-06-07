package pl.edu.agh.evolutus.foram;

public class Shell {

	private final double firstChamberRadius;
	private final double lastChamberRadius;
	private final int chambersCount;
	private final double volume;

	public Shell(double firstChamberRadius, double lastChamberRadius, int chambersCount, double volume) {
		this.firstChamberRadius = firstChamberRadius;
		this.lastChamberRadius = lastChamberRadius;
		this.chambersCount = chambersCount;
		this.volume = volume;
	}

	public double getFirstChamberRadius() {
		return firstChamberRadius;
	}

	public double getLastChamberRadius() {
		return lastChamberRadius;
	}

	public int getChambersCount() {
		return chambersCount;
	}

	public double getVolume() {
		return volume;
	}
}
