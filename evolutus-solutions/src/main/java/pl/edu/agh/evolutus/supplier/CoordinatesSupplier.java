package pl.edu.agh.evolutus.supplier;

import pl.edu.agh.evolutus.environment.Coordinates;

public class CoordinatesSupplier {

	private final long sizeX;
	private final long sizeY;
	private final long sizeZ;

	private long coordinatesCreated = 0;

	public CoordinatesSupplier(long sizeX, long sizeY, long sizeZ) {
		this.sizeX = sizeX;
		this.sizeY = sizeY;
		this.sizeZ = sizeZ;
	}

	private synchronized long newCoordinatesIndex() {
		return coordinatesCreated++;
	}

	public Coordinates createCoordinates() {
		return createCoordinates(newCoordinatesIndex());
	}

	public Coordinates createCoordinates(long index) {
		long x = index % sizeX;
		long y = (index / sizeX) % sizeY;
		long z = index / sizeX / sizeY;
		return new Coordinates(x, y, z);
	}

}
