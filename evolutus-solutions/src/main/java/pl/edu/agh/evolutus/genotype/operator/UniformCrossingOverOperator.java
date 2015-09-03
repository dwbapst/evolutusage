package pl.edu.agh.evolutus.genotype.operator;

import java.util.HashSet;
import java.util.Set;

public class UniformCrossingOverOperator extends CrossingOverOperator {

	@Override
	protected Set<Integer> getSwapPoints(int genomeLength) {
		Set<Integer> swapPoints = new HashSet<>();
		for (int i = 0; i < genomeLength; i++) {
			if (rand.nextBoolean()) {
				swapPoints.add(i);
			}
		}
		return swapPoints;
	}
}
