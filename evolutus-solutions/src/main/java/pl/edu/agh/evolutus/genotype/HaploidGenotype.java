package pl.edu.agh.evolutus.genotype;

import java.util.stream.IntStream;
import java.util.stream.Stream;

import org.jage.address.agent.AgentAddress;

public class HaploidGenotype extends Genotype {

	private final Genome genome;

	public HaploidGenotype(Genome genome, AgentAddress agentAddress) {
		this.genome = Genome.forGenome(genome, agentAddress.toQualifiedString());
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
