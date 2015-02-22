package pl.edu.agh.evolutus.utils;

import jdk.nashorn.api.scripting.ScriptObjectMirror;

public class VectorD {

	private double x, y, z;

	public VectorD() {
		this(0, 0, 0);
	}

	public VectorD(double x, double y, double z) {
		this.x = x;
		this.y = y;
		this.z = z;
	}

	public double x() {
		return x;
	}

	public double y() {
		return y;
	}

	public double z() {
		return z;
	}

	public VectorD add(VectorD vector) {
		return new VectorD(x + vector.x(), y + vector.y(), z + vector.z());
	}

	public VectorD sub(VectorD vector) {
		return new VectorD(x - vector.x(), y - vector.y(), z - vector.z());
	}

	public VectorD mul(double factor) {
		return new VectorD(x * factor, y * factor, z * factor);
	}

	public VectorD div(double divisor) {
		return mul(1.0 / divisor);
	}

	public static VectorD fromScriptObject(ScriptObjectMirror scriptObject) {
		return new VectorD((double) scriptObject.get("x"), (double) scriptObject.get("y"), (double) scriptObject.get("z"));
	}

	@Override
	public String toString() {
		return String.format("(%f, %f, %f)", x, y, z);
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (!(o instanceof VectorD)) {
			return false;
		}

		VectorD vectorD = (VectorD) o;

		if (Double.compare(vectorD.x, x) != 0) {
			return false;
		}
		if (Double.compare(vectorD.y, y) != 0) {
			return false;
		}
		if (Double.compare(vectorD.z, z) != 0) {
			return false;
		}

		return true;
	}

	@Override
	public int hashCode() {
		int result;
		long temp;
		temp = Double.doubleToLongBits(x);
		result = (int) (temp ^ (temp >>> 32));
		temp = Double.doubleToLongBits(y);
		result = 31 * result + (int) (temp ^ (temp >>> 32));
		temp = Double.doubleToLongBits(z);
		result = 31 * result + (int) (temp ^ (temp >>> 32));
		return result;
	}
}