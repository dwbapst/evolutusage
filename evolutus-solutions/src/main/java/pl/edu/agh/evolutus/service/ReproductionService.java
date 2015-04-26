package pl.edu.agh.evolutus.service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import javax.inject.Inject;

import org.apache.commons.lang3.tuple.Pair;
import org.jage.platform.component.IStatefulComponent;
import org.jage.platform.component.exception.ComponentException;
import org.jage.platform.component.provider.IComponentInstanceProvider;
import org.jage.platform.component.provider.IComponentInstanceProviderAware;

import pl.edu.agh.evolutus.foram.IForam;
import pl.edu.agh.evolutus.genotype.DiploidGenotype;
import pl.edu.agh.evolutus.genotype.Genome;
import pl.edu.agh.evolutus.genotype.operator.CrossingOverOperator;
import pl.edu.agh.evolutus.genotype.operator.OnePointCrossingOverOperator;
import pl.edu.agh.evolutus.genotype.operator.TwoPointCrossingOverOperator;
import pl.edu.agh.evolutus.genotype.operator.UniformCrossingOverOperator;
import pl.edu.agh.evolutus.service.config.EnvironmentConfig;
import pl.edu.agh.evolutus.service.config.UnitsConverter;

public class ReproductionService implements IStatefulComponent, IComponentInstanceProviderAware {

	private static final int MAX_GAMETE_AGE_IN_HOURS = 48;

	private IComponentInstanceProvider instanceProvider;

	@Inject
	private EnvironmentConfig config;

	@Inject
	private UnitsConverter unitsConverter;

	private CrossingOverOperator crossingOverOperator;
	private long maxGameteAgeInSteps;

	@Override
	public void setInstanceProvider(IComponentInstanceProvider iComponentInstanceProvider) {
		this.instanceProvider = iComponentInstanceProvider;
	}

	@Override
	public void init() throws ComponentException {
		this.crossingOverOperator = getCrossingOverOperator();
		this.maxGameteAgeInSteps = unitsConverter.hoursToSteps(MAX_GAMETE_AGE_IN_HOURS);
	}

	@Override
	public boolean finish() throws ComponentException {
		return true;
	}

	public Collection<IForam> processGametesAndReturnNewForams(Map<Genome, Integer> gametesWithAge) {

		updateAgeAndRemoveTooOld(gametesWithAge);
		List<IForam> foramsToAdd = new ArrayList<>();

		streamOfPairs(gametesWithAge)
				.filter(pair -> !pair.getLeft().getForamIdentifier().equals(pair.getRight().getForamIdentifier()))
				.filter(pair -> pair.getLeft().isValid() && pair.getRight().isValid())
				.forEach(pair -> {
					gametesWithAge.remove(pair.getLeft());
					gametesWithAge.remove(pair.getRight());
					foramsToAdd.add(createForam(pair.getLeft(), pair.getRight()));
				});

		return foramsToAdd;
	}

	private void updateAgeAndRemoveTooOld(Map<Genome, Integer> gametesWithAge) {
		for (Genome curr : new LinkedList<>(gametesWithAge.keySet())) {
			if (gametesWithAge.get(curr) > maxGameteAgeInSteps) {
				gametesWithAge.remove(curr); // Remove gametes older than 4 steps
			} else {
				gametesWithAge.put(curr, gametesWithAge.get(curr) + 1);
			}
		}
	}

	private Stream<Pair<Genome, Genome>> streamOfPairs(Map<Genome, Integer> gametesWithAge) {
		if (gametesWithAge.size() < 2) {
			return Stream.of();
		}

		List<Genome> gametes = new ArrayList<>(gametesWithAge.keySet());
		Collections.shuffle(gametes);
		Iterator<Genome> iterator = gametes.iterator();

		Iterator<Pair<Genome, Genome>> pairIterator = new Iterator<Pair<Genome, Genome>>() {
			Genome first = iterator.next();

			@Override
			public boolean hasNext() {
				return iterator.hasNext();
			}

			@Override
			public Pair<Genome, Genome> next() {
				Pair<Genome, Genome> pair = Pair.of(first, iterator.next());
				if (iterator.hasNext()) {
					first = iterator.next();
				}
				return pair;
			}
		};

		Iterable<Pair<Genome, Genome>> iterable = () -> pairIterator;
		return StreamSupport.stream(iterable.spliterator(), false);
	}

	public IForam createForam(Genome genomeA, Genome genomeB) {
		IForam foram = instanceProvider.getInstance(IForam.class);
		foram.setEnergy(config.initialEnergy());
		foram.setGenotype(new DiploidGenotype(genomeA, genomeB, foram.getAddress(), crossingOverOperator));
		return foram;
	}

	private CrossingOverOperator getCrossingOverOperator() {
		String operatorName = config.crossingOverOperator();
		switch (operatorName) {
		case "OnePointCrossingOverOperator":
			return instanceProvider.getInstance(OnePointCrossingOverOperator.class);
		case "TwoPointCrossingOverOperator":
			return instanceProvider.getInstance(TwoPointCrossingOverOperator.class);
		case "UniformCrossingOverOperator":
			return instanceProvider.getInstance(UniformCrossingOverOperator.class);
		default:
			throw new IllegalStateException("Unknown crossing-over operator: " + operatorName);
		}
	}
}
