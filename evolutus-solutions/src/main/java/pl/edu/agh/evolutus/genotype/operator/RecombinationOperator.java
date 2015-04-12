package pl.edu.agh.evolutus.genotype.operator;

import java.util.Map;
import java.util.Random;
import java.util.stream.Collectors;

import pl.edu.agh.evolutus.genotype.Gene;
import pl.edu.agh.evolutus.genotype.Genome;

public class RecombinationOperator {

	private final Random rand = new Random();

	public Genome apply(final Genome genomeA, final Genome genomeB) {
		if (!genomeA.getForamIdentifier().equals(genomeB.getForamIdentifier())) {
			throw new IllegalArgumentException("Cannot recombine genomes with two different ownerIdentifiers.");
		}
		Map<String, Gene> genes = genomeA.stream()
				.map(geneA -> getGene(geneA, genomeB.getOrThrowException(geneA.getName())))
				.collect(Collectors.toMap(
						Gene::getName,
						gene -> gene
				));
		return Genome.forGenes(genes, genomeA.getForamIdentifier());
	}

	private Gene getGene(Gene geneA, Gene geneB) {
		if (geneA.isDominant() && geneB.isRecessive()) {
			return geneA;
		} else if (geneA.isRecessive() && geneB.isDominant()) {
			return geneB;
		} else {
			return rand.nextBoolean() ? geneA : geneB;
		}
	}
}
