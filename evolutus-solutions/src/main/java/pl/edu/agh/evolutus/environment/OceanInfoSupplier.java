package pl.edu.agh.evolutus.environment;

public class OceanInfoSupplier {

	private Coordinates oceanSize;

	public OceanInfoSupplier(int x, int y, int z) {
		this.oceanSize = new Coordinates(x, y, z);
	}

	public Coordinates getOceanSize() {
		return oceanSize;
	}
}
