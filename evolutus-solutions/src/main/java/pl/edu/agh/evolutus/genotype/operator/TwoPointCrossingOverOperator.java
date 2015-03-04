package pl.edu.agh.evolutus.genotype.operator;

import java.util.Set;

import com.google.common.collect.Sets;

import pl.edu.agh.evolutus.genotype.Genome;

public class TwoPointCrossingOverOperator extends CrossingOverOperator {

	@Override
	protected Set<Integer> getSwapPoints() {
		int firstSwapPoint = rand.nextInt(Genome.LENGTH);
		int secondSwapPoint = rand.nextInt(Genome.LENGTH);
		while (secondSwapPoint == firstSwapPoint) {
			secondSwapPoint = rand.nextInt(Genome.LENGTH);
		}
		return Sets.newHashSet(firstSwapPoint, secondSwapPoint);
	}
}
