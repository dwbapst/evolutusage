package pl.edu.agh.evolutus.genotype.operator;

import java.util.Set;

import com.google.common.collect.Sets;

public class OnePointCrossingOverOperator extends CrossingOverOperator {

	@Override
	protected Set<Integer> getSwapPoints(int genomeLength) {
		int swapPoint = rand.nextInt(genomeLength);
		return Sets.newHashSet(swapPoint);
	}
}
