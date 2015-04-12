package pl.edu.agh.evolutus.genotype.operator;

import java.util.Set;

import com.google.common.collect.Sets;

public class TwoPointCrossingOverOperator extends CrossingOverOperator {

	@Override
	protected Set<Integer> getSwapPoints(int genomeLength) {
		int firstSwapPoint = rand.nextInt(genomeLength);
		int secondSwapPoint = rand.nextInt(genomeLength);
		while (secondSwapPoint == firstSwapPoint) {
			secondSwapPoint = rand.nextInt(genomeLength);
		}
		return Sets.newHashSet(firstSwapPoint, secondSwapPoint);
	}
}
