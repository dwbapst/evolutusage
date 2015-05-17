package pl.edu.agh.evolutus.service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import javax.inject.Inject;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.Pair;
import org.jage.platform.component.IStatefulComponent;
import org.jage.platform.component.exception.ComponentException;
import org.jage.platform.component.provider.IComponentInstanceProvider;
import org.jage.platform.component.provider.IComponentInstanceProviderAware;

import pl.edu.agh.evolutus.foram.ForamType;
import pl.edu.agh.evolutus.foram.IForam;
import pl.edu.agh.evolutus.genotype.DiploidGenotype;
import pl.edu.agh.evolutus.genotype.Genome;
import pl.edu.agh.evolutus.genotype.HaploidGenotype;
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

	public Collection<IForam> processGametesAndReturnNewForams(Map<Genome, Pair<ForamType, Integer>> gametesMap) {

		updateAgeAndRemoveTooOld(gametesMap);
		List<IForam> foramsToAdd = new ArrayList<>();

		list(gametesMap, ForamType.DIPLOID_BENTHIC)
				.stream()
				.filter(Genome::isValid)
				.forEach(gamete -> {
					gametesMap.remove(gamete);
					foramsToAdd.add(createForam(ForamType.HAPLOID_BENTHIC, gamete));
				});

		processGametes(gametesMap, ForamType.HAPLOID_BENTHIC, ForamType.DIPLOID_BENTHIC, foramsToAdd);

		processGametes(gametesMap, ForamType.PLANKTONIC, ForamType.PLANKTONIC, foramsToAdd);

		return foramsToAdd;
	}

	private void updateAgeAndRemoveTooOld(Map<Genome, Pair<ForamType, Integer>> gametesMap) {
		for (Genome curr : new LinkedList<>(gametesMap.keySet())) {
			ForamType type = gametesMap.get(curr).getLeft();
			Integer age = gametesMap.get(curr).getRight();
			if (age > maxGameteAgeInSteps) {
				gametesMap.remove(curr); // Remove gametes older than 4 steps
			} else {
				gametesMap.put(curr, Pair.of(type, age + 1));
			}
		}
	}

	private void processGametes(Map<Genome, Pair<ForamType, Integer>> gametesMap, ForamType parentType, ForamType childrenType,
			List<IForam> foramsToAdd) {

		List<Genome> list = list(gametesMap, parentType);

		streamOfPairs(list)
				.filter(pair -> !pair.getLeft().getForamIdentifier().equals(pair.getRight().getForamIdentifier()))
				.filter(pair -> pair.getLeft().isValid() && pair.getRight().isValid())
				.forEach(pair -> {
					gametesMap.remove(pair.getLeft());
					gametesMap.remove(pair.getRight());
					foramsToAdd.add(createForam(childrenType, pair.getLeft(), pair.getRight()));
				});
	}

	private List<Genome> list(Map<Genome, Pair<ForamType, Integer>> gametesMap, ForamType foramType) {
		return gametesMap.entrySet()
				.stream()
				.filter(entry -> entry.getValue().getLeft() == foramType)
				.map(Entry::getKey)
				.collect(Collectors.toList());
	}

	private Stream<Pair<Genome, Genome>> streamOfPairs(Collection<Genome> gametes) {
		if (gametes.size() < 2) {
			return Stream.of();
		}

		List<Genome> gametesList = new ArrayList<>(gametes);
		Collections.shuffle(gametesList);
		Iterator<Genome> iterator = gametesList.iterator();

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

	private IForam createForam(ForamType foramType) {
		IForam foram = instanceProvider.getInstance(IForam.class);
		foram.setType(foramType);
		foram.setEnergy(config.initialEnergy());
		return foram;
	}

	public IForam createForam(ForamType foramType, Genome genome) {
		assertType(foramType, ForamType.HAPLOID_BENTHIC);
		IForam foram = createForam(foramType);
		foram.setGenotype(new HaploidGenotype(genome, foram.getAddress()));
		return foram;
	}

	public IForam createForam(ForamType foramType, Genome genomeA, Genome genomeB) {
		assertType(foramType, ForamType.PLANKTONIC, ForamType.DIPLOID_BENTHIC);
		IForam foram = createForam(foramType);
		foram.setGenotype(new DiploidGenotype(genomeA, genomeB, foram.getAddress(), crossingOverOperator));
		return foram;
	}

	private void assertType(ForamType actual, ForamType... expecteds) {
		for (ForamType expected : expecteds) {
			if (expected == actual) {
				return;
			}
		}
		String msg = String.format("Unexpected foram type: %s. Allowed: %s", actual, StringUtils.join(expecteds, ", "));
		throw new IllegalArgumentException(msg);
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
