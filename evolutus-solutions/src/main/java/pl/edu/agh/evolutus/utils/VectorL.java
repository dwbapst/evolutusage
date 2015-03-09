package pl.edu.agh.evolutus.utils;

import jdk.nashorn.api.scripting.ScriptObjectMirror;
import jdk.nashorn.internal.runtime.ScriptObject;

public class VectorL {

	public final long x, y, z;

	public VectorL() {
		this(0, 0, 0);
	}

	public VectorL(long x, long y, long z) {
		this.x = x;
		this.y = y;
		this.z = z;
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
