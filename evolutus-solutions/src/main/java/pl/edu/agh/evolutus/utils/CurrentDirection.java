package pl.edu.agh.evolutus.utils;

import static java.lang.Math.*;

import java.util.LinkedHashMap;
import java.util.Map;

public class CurrentDirection extends VectorL {

	private final Map<VectorL, Double> vectorComponents = new LinkedHashMap<>();

	public CurrentDirection(VectorL vector) {
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
				vectorComponents.put(new VectorL(x / absX, 0, 0), absX / sum);
			}
			if (absY > 0) {
				vectorComponents.put(new VectorL(0, y / absY, 0), absY / sum);
			}
			if (absZ > 0) {
				vectorComponents.put(new VectorL(0, 0, z / absZ), absZ / sum);
			}
		}
	}

	public Map<VectorL, Double> getVectorComponents() {
		return vectorComponents;
	}

	public Map<VectorL, Double> getTargetCoordinateProbabilities(VectorL cellCoordinates, VectorL poolSize) {
		Map<VectorL, Double> targetCoordinates = new LinkedHashMap<>();

		for (VectorL vectorComponent : vectorComponents.keySet()) {
			VectorL target = cellCoordinates.add(vectorComponent);

			if (target.x() >= poolSize.x()) {
				target = new VectorL(0, target.y(), target.z());
			}
			if (target.y() >= poolSize.y()) {
				target = new VectorL(target.x(), 0, target.z());
			}
			if (target.z() >= poolSize.z()) {
				target = new VectorL(target.x(), target.y(), 0);
			}

			if (target.x() < 0) {
				target = new VectorL(poolSize.x() - 1, target.y(), target.z());
			}
			if (target.y() < 0) {
				target = new VectorL(target.x(), poolSize.y() - 1, target.z());
			}
			if (target.z() < 0) {
				target = new VectorL(target.x(), target.y(), poolSize.z() - 1);
			}

			targetCoordinates.put(target, vectorComponents.get(vectorComponent));
		}
		return targetCoordinates;
	}
}
