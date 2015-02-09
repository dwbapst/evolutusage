package pl.edu.agh.evolutus.utils;

public class Vector {

	private long x, y, z;

	public Vector() {
		this(0, 0, 0);
	}

	public Vector(long x, long y, long z) {
		this.x = x;
		this.y = y;
		this.z = z;
	}

	public long x() {
		return x;
	}

	public long y() {
		return y;
	}

	public long z() {
		return z;
	}

	public Vector add(Vector vector) {
		return new Vector(x + vector.x(), y + vector.y(), z + vector.z());
	}

	public Vector sub(Vector vector) {
		return new Vector(x - vector.x(), y - vector.y(), z - vector.z());
	}

	public Vector mul(double factor) {
		return new Vector(Math.round(x * factor), Math.round(y * factor), Math.round(z * factor));
	}

	public Vector div(double divisor) {
		return mul(1.0 / divisor);
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
		if (!(o instanceof Vector)) {
			return false;
		}

		Vector that = (Vector) o;

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
