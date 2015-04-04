package pl.edu.agh.evolutus.utils;

import static java.lang.Math.*;

import java.util.LinkedHashMap;
import java.util.Map;

import pl.edu.agh.evolutus.environment.BoundaryConditions;

public class CurrentDirection extends VectorD {

	private final Map<VectorL, Double> vectorComponents = new LinkedHashMap<>();

	private final double strength; // vector length

	public CurrentDirection(VectorD vector) {
		this(vector.x, vector.y, vector.z);
	}

	public CurrentDirection(double x, double y, double z) {
		super(x, y, z);

		computeVectorComponents(x, y, z);
		strength = sqrt(pow(x, 2) + pow(y, 2) + pow(z, 2));
	}

	private void computeVectorComponents(double x, double y, double z) {
		double absX = abs(x), absY = abs(y), absZ = abs(z);
		double sum = absX + absY + absZ;
		if (sum > 0) {
			if (absX > 0) {
				vectorComponents.put(new VectorL(round(x / absX), 0, 0), absX / sum);
			}
			if (absY > 0) {
				vectorComponents.put(new VectorL(0, round(y / absY), 0), absY / sum);
			}
			if (absZ > 0) {
				vectorComponents.put(new VectorL(0, 0, round(z / absZ)), absZ / sum);
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

	public double getStrength() {
		return strength;
	}

	public Map<VectorL, Double> getVectorComponents() {
		return vectorComponents;
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
