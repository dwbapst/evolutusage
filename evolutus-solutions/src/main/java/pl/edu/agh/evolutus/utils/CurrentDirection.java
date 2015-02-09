package pl.edu.agh.evolutus.utils;

import static java.lang.Math.*;

import java.util.LinkedHashMap;
import java.util.Map;

public class CurrentDirection extends Vector {

	private final Map<Vector, Double> vectorComponents = new LinkedHashMap<>();

	public CurrentDirection(Vector vector) {
		this(vector.x(), vector.y(), vector.z());
	}

	public CurrentDirection(long x, long y, long z) {
		super(x, y, z);

		computeVectorComponents(x, y, z);
	}

	private void computeVectorComponents(long x, long y, long z) {
		long absX = abs(x), absY = abs(y), absZ = abs(z);
		double sum = 0.0 + absX + absY + absZ;
		if (sum > 0) {
			if (absX > 0) {
				vectorComponents.put(new Vector(x / absX, 0, 0), absX / sum);
			}
			if (absY > 0) {
				vectorComponents.put(new Vector(0, y / absY, 0), absY / sum);
			}
			if (absZ > 0) {
				vectorComponents.put(new Vector(0, 0, z / absZ), absZ / sum);
			}
		}
	}

	public Map<Vector, Double> getVectorComponents() {
		return vectorComponents;
	}

	public Map<Vector, Double> getTargetCoordinateProbabilities(Vector cellCoordinates, Vector poolSize) {
		Map<Vector, Double> targetCoordinates = new LinkedHashMap<>();

		for (Vector vectorComponent : vectorComponents.keySet()) {
			Vector target = cellCoordinates.add(vectorComponent);

			if (target.x() >= poolSize.x()) {
				target = new Vector(0, target.y(), target.z());
			}
			if (target.y() >= poolSize.y()) {
				target = new Vector(target.x(), 0, target.z());
			}
			if (target.z() >= poolSize.z()) {
				target = new Vector(target.x(), target.y(), 0);
			}

			if (target.x() < 0) {
				target = new Vector(poolSize.x() - 1, target.y(), target.z());
			}
			if (target.y() < 0) {
				target = new Vector(target.x(), poolSize.y() - 1, target.z());
			}
			if (target.z() < 0) {
				target = new Vector(target.x(), target.y(), poolSize.z() - 1);
			}

			targetCoordinates.put(target, vectorComponents.get(vectorComponent));
		}
		return targetCoordinates;
	}
}
