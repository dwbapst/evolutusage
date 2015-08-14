package pl.edu.agh.evolutus.utils;

import static java.lang.Math.*;

import jdk.nashorn.internal.runtime.ScriptObject;

public class VectorD {

	public final double x, y, z;

	public VectorD() {
		this(0, 0, 0);
	}

	public VectorD(Number x, Number y, Number z) {
		this.x = x.doubleValue();
		this.y = y.doubleValue();
		this.z = z.doubleValue();
	}

	public VectorD add(VectorD vector) {
		return new VectorD(x + vector.x, y + vector.y, z + vector.z);
	}

	public VectorD sub(VectorD vector) {
		return new VectorD(x - vector.x, y - vector.y, z - vector.z);
	}

	public VectorD mul(double factor) {
		return new VectorD(x * factor, y * factor, z * factor);
	}

	public VectorD div(double divisor) {
		return mul(1.0 / divisor);
	}

	public double dotProduct(VectorD vector) {
		return x * vector.x + y * vector.y + z * vector.z;
	}

	public double length() {
		return sqrt(pow(x, 2) + pow(y, 2) + pow(z, 2));
	}

	public VectorL toLong() {
		return new VectorL(Math.round(x), Math.round(y), Math.round(z));
	}

	public static VectorD fromScriptObject(ScriptObject scriptObject) {
		return new VectorD((Number) scriptObject.get("x"), (Number) scriptObject.get("y"), (Number) scriptObject.get("z"));
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