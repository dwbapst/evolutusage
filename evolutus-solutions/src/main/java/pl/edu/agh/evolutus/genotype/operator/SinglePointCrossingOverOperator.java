package pl.edu.agh.evolutus.genotype.operator;

import java.util.Set;

import com.google.common.collect.Sets;

import pl.edu.agh.evolutus.genotype.Genome;

public class SinglePointCrossingOverOperator extends CrossingOverOperator {

	@Override
	protected Set<Integer> getSwapPoints() {
		int swapPoint = rand.nextInt(Genome.LENGTH);
		return Sets.newHashSet(swapPoint);
	}
}
