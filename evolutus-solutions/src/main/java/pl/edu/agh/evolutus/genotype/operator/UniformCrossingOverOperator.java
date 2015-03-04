package pl.edu.agh.evolutus.genotype.operator;

import java.util.HashSet;
import java.util.Set;

import org.apache.commons.lang3.tuple.Pair;

import pl.edu.agh.evolutus.genotype.Genome;

public class UniformCrossingOverOperator extends CrossingOverOperator {

	@Override
	protected Set<Integer> getSwapPoints() {
		Set<Integer> swapPoints = new HashSet<>();
		for (int i = 0; i < Genome.LENGTH; i++) {
			if (rand.nextBoolean()) {
				swapPoints.add(i);
			}
		}
		return swapPoints;
	}

	@Override
	public Pair<Genome, Genome> apply(Pair<Genome, Genome> genomeGenomePair) {
		return null;
	}
}
