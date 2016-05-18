package pl.edu.agh.evolutus.foram;

import org.jage.agent.ISimpleAgent;
import pl.edu.agh.evolutus.genotype.Genotype;

public interface IForam extends ISimpleAgent {

	void setType(ForamType type);

	ForamType getType();

	void setEnergy(double energy);

	double getEnergy();

	double getShellVolume();

    double getShapeFactor();

	void setGenotype(Genotype genotype);

	Genotype getGenotype();

	void setShell(Shell shell);

	Shell getShell();

	boolean isAlive();
}
