package pl.edu.agh.evolutus.environment;

public class Coordinates {

	private long x, y, z;

	public Coordinates() {
		this(0, 0, 0);
	}

	public Coordinates(long x, long y, long z) {
		this.x = x;
		this.y = y;
		this.z = z;
	}

	public long getX() {
		return x;
	}

	public long getY() {
		return y;
	}

	public long getZ() {
		return z;
	}

	public void setX(long x) {
		this.x = x;
	}

	public void setY(long y) {
		this.y = y;
	}

	public void setZ(long z) {
		this.z = z;
	}

	@Override
	public String toString() {
		return String.format("(%d, %d, %d)", x, y, z);
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (!(o instanceof Coordinates)) {
			return false;
		}

		Coordinates that = (Coordinates) o;

		if (x != that.x) {
			return false;
		}
		if (y != that.y) {
			return false;
		}
		if (z != that.z) {
			return false;
		}

		return true;
	}

	@Override
	public int hashCode() {
		int result = (int) (x ^ (x >>> 32));
		result = 31 * result + (int) (y ^ (y >>> 32));
		result = 31 * result + (int) (z ^ (z >>> 32));
		return result;
	}
}
