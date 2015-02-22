package pl.edu.agh.evolutus.foram;

import org.jage.agent.ISimpleAgent;

import pl.edu.agh.evolutus.genome.Genome;

public interface IForam extends ISimpleAgent {

	void setEnergy(double energy);

	void setGenome(Genome genome);

	boolean isAlive();
}
