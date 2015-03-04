package pl.edu.agh.evolutus.genotype;

import java.util.stream.IntStream;
import java.util.stream.Stream;

import org.apache.commons.lang3.tuple.Pair;

import pl.edu.agh.evolutus.genotype.operator.CrossingOverOperator;
import pl.edu.agh.evolutus.genotype.operator.RecombinationOperator;
import pl.edu.agh.evolutus.genotype.operator.UniformCrossingOverOperator;

public class DiploidGenotype extends Genotype {

	private final Genome genomeA;
	private final Genome genomeB;
	private final Genome effectiveGenome;

	private final CrossingOverOperator crossingOverOperator;

	public DiploidGenotype(Genome genomeA, Genome genomeB) {
		this.genomeA = genomeA;
		this.genomeB = genomeB;
		this.effectiveGenome = new RecombinationOperator().apply(genomeA, genomeB);
		this.crossingOverOperator = new UniformCrossingOverOperator();
	}

	@Override
	public Genome getEffectiveGenome() {
		return effectiveGenome;
	}

	@Override
	protected Stream<Genome> createGameteStream(int number) {
		return IntStream.range(0, number / 2)
				.mapToObj(i -> Pair.of(genomeA.copy(), genomeB.copy()))
				.map(crossingOverOperator)
				.flatMap(pair -> Stream.of(pair.getLeft(), pair.getRight()));
	}
}
