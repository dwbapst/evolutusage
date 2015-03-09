package pl.edu.agh.evolutus.genotype.operator;

import java.util.Random;
import java.util.Set;

import org.apache.commons.lang3.tuple.Pair;

import pl.edu.agh.evolutus.genotype.Genome;
import pl.edu.agh.evolutus.genotype.gene.Gene;

public abstract class CrossingOverOperator {

	protected final Random rand = new Random();

	protected abstract Set<Integer> getSwapPoints();

	public Pair<Genome, Genome> apply(Genome genomeA, Genome genomeB) {
		Set<Integer> swapPoints = getSwapPoints();

		Gene[] genesA = new Gene[Genome.LENGTH];
		Gene[] genesB = new Gene[Genome.LENGTH];

		boolean isSwapping = false;
		for (int i = 0; i < Genome.LENGTH; i++) {
			if (swapPoints.contains(i)) {
				isSwapping = !isSwapping;
			}
			genesA[i] = isSwapping ? genomeB.get(i) : genomeA.get(i);
			genesB[i] = isSwapping ? genomeA.get(i) : genomeB.get(i);
		}

		return Pair.of(
				Genome.forGenes(genesA, genomeA.getForamIdentifier()),
				Genome.forGenes(genesB, genomeB.getForamIdentifier())
		);
	}

	public Pair<Genome, Genome> apply(Pair<Genome, Genome> genomeGenomePair) {
		return apply(genomeGenomePair.getLeft(), genomeGenomePair.getRight());
	}
}
