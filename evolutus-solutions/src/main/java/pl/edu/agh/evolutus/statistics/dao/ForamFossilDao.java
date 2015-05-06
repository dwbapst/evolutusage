package pl.edu.agh.evolutus.statistics.dao;

import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.commons.lang3.tuple.Pair;

import pl.edu.agh.evolutus.statistics.model.ForamFossil;
import pl.edu.agh.evolutus.statistics.model.Simulation;

public class ForamFossilDao extends Dao<ForamFossil> {

	@Override
	protected Class<ForamFossil> getReturnType() {
		return ForamFossil.class;
	}

	public Map<Long, double[]> getAvgGeneValuesByStep(Simulation simulation, String... genes) {
		return createQuery(simulation)
				.asList()
				.stream()
				.map(fossil -> {
					double[] values = new double[genes.length];
					for (int i = 0; i < genes.length; i++) {
						values[i] = fossil.getGenotype().get(genes[i])[0];
					}
					return Pair.of(fossil.getDeathStepNo(), values);
				})
				.collect(Collectors.groupingBy(Pair::getLeft))
				.entrySet()
				.stream()
				.collect(Collectors.toMap(
						Entry::getKey,
						entry -> {
							double[] avgValues = new double[genes.length];
							for (Pair<Long, double[]> pair : entry.getValue()) {
								for (int i = 0; i < avgValues.length; i++) {
									avgValues[i] += pair.getRight()[i];
								}
							}
							for (int i = 0; i < avgValues.length; i++) {
								avgValues[i] /= entry.getValue().size();
							}
							return avgValues;
						}
				));
	}

	public Set<String> getGeneNames(Simulation simulation) {
		return createQuery(simulation)
				.get()
				.getGenotype()
				.keySet();
	}
}

