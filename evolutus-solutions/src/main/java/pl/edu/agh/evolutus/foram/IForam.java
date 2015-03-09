package pl.edu.agh.evolutus.foram;

import org.jage.agent.ISimpleAgent;

import pl.edu.agh.evolutus.genotype.Genome;
import pl.edu.agh.evolutus.genotype.Genotype;

public interface IForam extends ISimpleAgent {

	void setEnergy(double energy);

	void setGenotype(Genotype genotype);

	boolean isAlive();
}
