package pl.edu.agh.evolutus.genotype;

import java.util.stream.IntStream;
import java.util.stream.Stream;

public class HaploidGenotype extends Genotype {

	private final Genome genome;

	public HaploidGenotype(Genome genome) {
		this.genome = genome;
	}

	@Override
	public Genome getEffectiveGenome() {
		return genome;
	}

	@Override
	protected Stream<Genome> createGameteStream(int number) {
		return IntStream.range(0, number).mapToObj(i -> genome.copy());
	}
}
