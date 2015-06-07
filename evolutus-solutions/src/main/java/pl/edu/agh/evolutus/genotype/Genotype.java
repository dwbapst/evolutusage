package pl.edu.agh.evolutus.genotype;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import pl.edu.agh.evolutus.genotype.Gene.GeneValidationException;
import pl.edu.agh.evolutus.genotype.operator.CrossingOverOperator;

public abstract class Genotype {

	private final Random rand = new Random();

	public abstract Genome getEffectiveGenome();

	protected abstract Stream<Genome> createGameteStream(int number, CrossingOverOperator crossingOverOperator);

	public abstract Map<String, Double[]> toFossilizationMap();

	public abstract boolean isDiploid();

	public List<Genome> createGametes(int number, double globalMutationProbability, double gametesSievingCoefficient,
			CrossingOverOperator crossingOverOperator) {
		if (number % 2 != 0) {
			throw new IllegalArgumentException("Number of gametes to create has to be even. Given: " + number);
		}
		return createGameteStream(number, crossingOverOperator)
				.map(gamete -> gamete.mutate(globalMutationProbability))
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
