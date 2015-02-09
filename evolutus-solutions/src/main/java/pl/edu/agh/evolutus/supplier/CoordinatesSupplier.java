package pl.edu.agh.evolutus.supplier;

import pl.edu.agh.evolutus.environment.Coordinates;

public class CoordinatesSupplier {

	private final Coordinates size;

	private long coordinatesCreated = 0;

	public CoordinatesSupplier(long sizeX, long sizeY, long sizeZ) {
		this.size = new Coordinates(sizeX, sizeY, sizeZ);
	}

	private synchronized long newCoordinatesIndex() {
		return coordinatesCreated++;
	}

	public Coordinates createCoordinates() {
		return createCoordinates(newCoordinatesIndex());
	}

	public Coordinates createCoordinates(long index) {
		long x = index % size.getX();
		long y = (index / size.getX()) % size.getY();
		long z = index / size.getX() / size.getY();
		return new Coordinates(x, y, z);
	}

	public Coordinates getSize() {
		return size;
	}

}
