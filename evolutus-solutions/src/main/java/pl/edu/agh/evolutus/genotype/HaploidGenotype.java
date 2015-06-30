package pl.edu.agh.evolutus.genotype;

import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import org.jage.address.agent.AgentAddress;

import pl.edu.agh.evolutus.genotype.operator.CrossingOverOperator;

public class HaploidGenotype extends Genotype {

	private final String parentId;
	private final Genome genome;

	public HaploidGenotype(Genome genome, AgentAddress agentAddress) {
		this.parentId = genome.getForamIdentifier();
		this.genome = Genome.forGenome(genome, agentAddress.toQualifiedString());
	}

	@Override
	public Genome getEffectiveGenome() {
		return genome;
	}

	@Override
	protected Stream<Genome> createGameteStream(int number, CrossingOverOperator crossingOverOperator) {
		return IntStream.range(0, number).mapToObj(i -> genome.copy());
	}

	public Map<String, Double[]> toFossilizationMap() {
		return genome.stream()
				.collect(Collectors.toMap(
						Gene::getName,
						gene -> new Double[] { gene.getValue(), null, null }
				));
	}

	@Override
	public String getFirstParentId() {
		return parentId;
	}

	@Override
	public String getSecondParentId() {
		return null;
	}

	@Override
	public boolean isDiploid() {
		return false;
	}
}
