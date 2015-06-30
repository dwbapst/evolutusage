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

	public abstract String getFirstParentId();

	public abstract String getSecondParentId();

	public abstract boolean isDiploid();

	public List<Genome> createGametes(int number, double globalMutationProbability, double gametesSievingCoefficient,
			CrossingOverOperator crossingOverOperator) {

		number = Double.valueOf(number * (1.0 - gametesSievingCoefficient)).intValue();
		if (number % 2 != 0) {
			number++;
		}
		return createGameteStream(number, crossingOverOperator)
				.map(gamete -> gamete.mutate(globalMutationProbability))
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
