package pl.edu.agh.evolutus.service;

import javax.inject.Inject;

import pl.edu.agh.evolutus.config.IEnvironmentConfigService;
import pl.edu.agh.evolutus.utils.Vector;

public class CoordinatesService {

	private final Vector size;

	private long coordinatesCreated = 0;

	/* package */ CoordinatesService(long x, long y, long z) {
		this.size = new Vector(x, y, z);
	}

	@Inject
	public CoordinatesService(IEnvironmentConfigService configService) {
		this.size = configService.getOceanSize();
	}

	private synchronized long newCoordinatesIndex() {
		return coordinatesCreated++;
	}

	public Vector createCoordinates() {
		return createCoordinates(newCoordinatesIndex());
	}

	public Vector createCoordinates(long index) {
		long x = index % size.x();
		long y = (index / size.x()) % size.y();
		long z = index / size.x() / size.y();
		return new Vector(x, y, z);
	}

	public Vector getSize() {
		return size;
	}

}
