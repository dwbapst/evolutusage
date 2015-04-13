package pl.edu.agh.evolutus.utils;

import jdk.nashorn.internal.runtime.ScriptObject;

public class VectorL {

	public final long x, y, z;

	public VectorL() {
		this(0, 0, 0);
	}

	public VectorL(Number x, Number y, Number z) {
		this.x = x.longValue();
		this.y = y.longValue();
		this.z = z.longValue();
	}

	public VectorL add(VectorL vector) {
		return new VectorL(x + vector.x, y + vector.y, z + vector.z);
	}

	public VectorL sub(VectorL vector) {
		return new VectorL(x - vector.x, y - vector.y, z - vector.z);
	}

	public VectorD mul(double factor) {
		return new VectorD(x * factor, y * factor, z * factor);
	}

	public VectorD div(double divisor) {
		return mul(1.0 / divisor);
	}

	public VectorD toDouble() {
		return mul(1.0);
	}

	@Override
	public String toString() {
		return String.format("(%d, %d, %d)", x, y, z);
	}

	public static VectorL fromScriptObject(ScriptObject scriptObject) {
		return new VectorL((Number) scriptObject.get("x"), (Number) scriptObject.get("y"), (Number) scriptObject.get("z"));
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (!(o instanceof VectorL)) {
			return false;
		}

		VectorL that = (VectorL) o;

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
