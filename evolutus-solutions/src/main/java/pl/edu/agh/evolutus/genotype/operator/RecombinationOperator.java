package pl.edu.agh.evolutus.genotype.operator;

import java.util.Random;
import java.util.function.Function;

import org.apache.commons.lang3.tuple.Pair;

import pl.edu.agh.evolutus.genotype.Genome;
import pl.edu.agh.evolutus.genotype.gene.Gene;

public class RecombinationOperator {

	private final Random rand = new Random();

	public Genome apply(Genome genomeA, Genome genomeB) {
		if(!genomeA.getForamIdentifier().equals(genomeB.getForamIdentifier())){
			throw new IllegalArgumentException("Cannot recombine genomes with two different ownerIdentifiers.");
		}
		Gene[] genes = new Gene[Genome.LENGTH];
		for (int i = 0; i < Genome.LENGTH; i++) {
			genes[0] = getGene(genomeA.get(i), genomeB.get(i));
		}
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
