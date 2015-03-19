package pl.edu.agh.evolutus.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import pl.edu.agh.evolutus.environment.BoundaryConditions;

public class CurrentDirection extends VectorL {

	private static final Random rand = new Random();

	private final List<Axis> nonZeroAxes = new ArrayList<>();

	public CurrentDirection(VectorL vector) {
		this(vector.x, vector.y, vector.z);
	}

	public CurrentDirection(long x, long y, long z) {
		super(x, y, z);
		if (x != 0) {
			nonZeroAxes.add(Axis.X);
		}
		if (y != 0) {
			nonZeroAxes.add(Axis.Y);
		}
		if (z != 0) {
			nonZeroAxes.add(Axis.Z);
		}
	}

	public VectorL getTargetCoordinates(VectorL sourceCoordinates, VectorL oceanSize, BoundaryConditions boundaryConditions) {
		if (nonZeroAxes.isEmpty()) {
			return sourceCoordinates;
		}

		Axis randomAxis = nonZeroAxes.get(rand.nextInt(nonZeroAxes.size()));
		long component = get(randomAxis);
		long modifier = (component > 0) ? -1 : 1;
		while (component != 0 && component + modifier != 0 && rand.nextBoolean()) {
			component += modifier;
		}

		VectorL targetCoordinates = sourceCoordinates.add(new VectorL(component, randomAxis));
		return boundaryConditions.keepVectorWithinBounds(targetCoordinates, oceanSize);
	}
}
