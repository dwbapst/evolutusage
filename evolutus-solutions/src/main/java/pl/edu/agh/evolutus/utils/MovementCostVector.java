package pl.edu.agh.evolutus.utils;

import jdk.nashorn.internal.runtime.ScriptObject;

public class MovementCostVector extends VectorD {

	public MovementCostVector(VectorD vector) {
		this(vector.x, vector.y, vector.z);
	}

	public MovementCostVector(Number x, Number y, Number z) {
		super(x, y, z);
	}

	public static MovementCostVector fromScriptObject(ScriptObject scriptObject) {
		return new MovementCostVector((Number) scriptObject.get("up"), (Number) scriptObject.get("down"),
				(Number) scriptObject.get("horizontally"));
	}

	public double getUpCost() {
		return x;
	}

	public double getDownCost() {
		return y;
	}

	public double getHorizontalCost() {
		return z;
	}

	public double getCostByMovementDirection(VectorD direction) {
		if (direction.z > 0) {
			return getDownCost();
		} else if (direction.z < 0) {
			return getUpCost();
		} else if (Math.abs(direction.x) > 0 || Math.abs(direction.y) > 0) {
			return getHorizontalCost();
		} else {
			return 0.0;
		}
	}
}
