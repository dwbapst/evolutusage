package pl.edu.agh.evolutus.genotype.operator;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.Set;

import org.apache.commons.lang3.tuple.Pair;

import pl.edu.agh.evolutus.genotype.Gene;
import pl.edu.agh.evolutus.genotype.Genome;

public abstract class CrossingOverOperator {

	protected final Random rand = new Random();

	protected abstract Set<Integer> getSwapPoints(int genomeLength);

	public Pair<Genome, Genome> apply(Genome genomeA, Genome genomeB) {
		Set<Integer> swapPoints = getSwapPoints(genomeA.size());

		Map<String, Gene> genesA = new HashMap<>();
		Map<String, Gene> genesB = new HashMap<>();

		int i = 0;
		boolean isSwapping = false;
		for (String name : genomeA.geneNames()) {
			if (swapPoints.contains(i++)) {
				isSwapping = !isSwapping;
			}
			genesA.put(name, isSwapping ? genomeB.get(name) : genomeA.get(name));
			genesB.put(name, isSwapping ? genomeA.get(name) : genomeB.get(name));
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
