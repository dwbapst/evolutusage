package pl.edu.agh.evolutus.genotype;

import java.util.Iterator;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import org.apache.commons.lang3.tuple.Pair;
import org.jage.address.agent.AgentAddress;

import pl.edu.agh.evolutus.genotype.operator.CrossingOverOperator;
import pl.edu.agh.evolutus.genotype.operator.RecombinationOperator;

public class DiploidGenotype extends Genotype {

	private final String firstParentId;
	private final String secondParentId;

	private final Genome genomeA;
	private final Genome genomeB;
	private final Genome effectiveGenome;

	public DiploidGenotype(Genome genomeA, Genome genomeB, AgentAddress agentAddress) {
		this.firstParentId = genomeA.getForamIdentifier();
		this.secondParentId = genomeB.getForamIdentifier();
		this.genomeA = Genome.forGenome(genomeA, agentAddress.toQualifiedString());
		this.genomeB = Genome.forGenome(genomeB, agentAddress.toQualifiedString());
		this.effectiveGenome = new RecombinationOperator().apply(this.genomeA, this.genomeB);
	}

	@Override
	public Genome getEffectiveGenome() {
		return effectiveGenome;
	}

	@Override
	protected Stream<Genome> createGameteStream(int number, CrossingOverOperator crossingOverOperator) {
		return IntStream.range(0, number / 2)
				.mapToObj(i -> Pair.of(genomeA.copy(), genomeB.copy()))
				.map(crossingOverOperator::apply)
				.flatMap(pair -> Stream.of(pair.getLeft(), pair.getRight()));
	}

	public Map<String, Double[]> toFossilizationMap() {
		Iterator<Gene> genomeAIt = genomeA.iterator();
		Iterator<Gene> genomeBIt = genomeB.iterator();
		return effectiveGenome.stream()
				.collect(Collectors.toMap(
						Gene::getName,
						gene -> new Double[] { gene.getValue(), genomeAIt.next().getValue(), genomeBIt.next().getValue() }
				));
	}

	@Override
	public String getFirstParentId() {
		return firstParentId;
	}

	@Override
	public String getSecondParentId() {
		return secondParentId;
	}

	@Override
	public boolean isDiploid() {
		return true;
	}
}
