package pl.edu.agh.evolutus.genotype;

import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import pl.edu.agh.evolutus.genotype.Gene.GeneValidationException;

public abstract class Genotype {

	private final Random rand = new Random();

	public abstract Genome getEffectiveGenome();

	protected abstract Stream<Genome> createGameteStream(int number);

	public List<Genome> createGametes(int number, double gametesSievingCoefficient) {
		if (number % 2 != 0) {
			throw new IllegalArgumentException("Number of gametes to create has to be even. Given: " + number);
		}
		return createGameteStream(number)
				.filter(gamete -> rand.nextDouble() > gametesSievingCoefficient)
				.collect(Collectors.toList());
	}

	public void validate() throws GeneValidationException {
		getEffectiveGenome().validate();
	}

	public Gene get(String name) {
		return getEffectiveGenome().get(name);
	}

	public Iterator<Gene> iterator() {
		return getEffectiveGenome().iterator();
	}

}
