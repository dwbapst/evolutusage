package pl.edu.agh.evolutus.utils;

import java.util.LinkedHashMap;
import java.util.Map;

import pl.edu.agh.evolutus.environment.BoundaryConditions;

public class VelocityVector extends VectorD {

	private final Map<VectorL, Double> vectorComponents = new LinkedHashMap<>();

	public VelocityVector(VectorD vector) {
		this(vector.x, vector.y, vector.z);
	}

	public VelocityVector(double x, double y, double z) {
		super(x, y, z);

		computeVectorComponents(x, y, z);
	}

	private void computeVectorComponents(double x, double y, double z) {
		double absX = Math.abs(x), absY = Math.abs(y), absZ = Math.abs(z);
		double sum = absX + absY + absZ;
		if (sum > 0) {
			if (absX > 0) {
				vectorComponents.put(new VectorL(Math.round(x / absX), 0, 0), absX / sum);
			}
			if (absY > 0) {
				vectorComponents.put(new VectorL(0, Math.round(y / absY), 0), absY / sum);
			}
			if (absZ > 0) {
				vectorComponents.put(new VectorL(0, 0, Math.round(z / absZ)), absZ / sum);
			}
		} else {
			vectorComponents.put(new VectorL(1, 0, 0), 1.0 / 6.0);
			vectorComponents.put(new VectorL(0, 1, 0), 1.0 / 6.0);
			vectorComponents.put(new VectorL(0, 0, 1), 1.0 / 6.0);
			vectorComponents.put(new VectorL(-1, 0, 0), 1.0 / 6.0);
			vectorComponents.put(new VectorL(0, -1, 0), 1.0 / 6.0);
			vectorComponents.put(new VectorL(0, 0, -1), 1.0 / 6.0);
		}
	}

	public Map<VectorL, Double> getTargetCoordinateProbabilities(VectorL sourceCoordinates, VectorL oceanSize,
			BoundaryConditions boundaryConditions) {
		Map<VectorL, Double> targetCoordinates = new LinkedHashMap<>();

		for (VectorL vectorComponent : vectorComponents.keySet()) {
			VectorL target = sourceCoordinates.add(vectorComponent);
			target = boundaryConditions.keepVectorWithinBounds(target, oceanSize);
			targetCoordinates.put(target, vectorComponents.get(vectorComponent));
		}
		return targetCoordinates;
	}
}
