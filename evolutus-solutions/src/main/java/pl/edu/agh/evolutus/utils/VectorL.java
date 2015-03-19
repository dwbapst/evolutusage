package pl.edu.agh.evolutus.utils;

import java.util.Arrays;
import java.util.List;

import jdk.nashorn.internal.runtime.ScriptObject;

public class VectorL {

	public static enum Axis {X, Y, Z}

	public final long x, y, z;

	public VectorL() {
		this(0, 0, 0);
	}

	public VectorL(long component, Axis axis) {
		this((axis == Axis.X) ? component : 0, (axis == Axis.Y) ? component : 0, (axis == Axis.Z) ? component : 0);
	}

	public VectorL(long x, long y, long z) {
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

	public long get(Axis axis) {
		switch (axis) {
		case X:
			return getX();
		case Y:
			return getY();
		case Z:
			return getZ();
		default:
			throw new IllegalArgumentException("Unknown axis: " + axis);
		}
	}

	public VectorL add(VectorL vector) {
		return new VectorL(x + vector.x, y + vector.y, z + vector.z);
	}

	public VectorL sub(VectorL vector) {
		return new VectorL(x - vector.x, y - vector.y, z - vector.z);
	}

	public VectorL mul(double factor) {
		return new VectorL(Math.round(x * factor), Math.round(y * factor), Math.round(z * factor));
	}

	public VectorL div(double divisor) {
		return mul(1.0 / divisor);
	}

	public List<VectorL> getComponents() {
		return Arrays.asList(new VectorL(x, 0L, 0L), new VectorL(0L, y, 0L), new VectorL(0L, 0L, z));
	}

	public boolean isZeroVector() {
		return x == 0 && y == 0 && z == 0;
	}

	@Override
	public String toString() {
		return String.format("(%d, %d, %d)", x, y, z);
	}

	public static VectorL fromScriptObject(ScriptObject scriptObject) {
		return new VectorL((int) scriptObject.get("x"), (int) scriptObject.get("y"), (int) scriptObject.get("z"));
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
