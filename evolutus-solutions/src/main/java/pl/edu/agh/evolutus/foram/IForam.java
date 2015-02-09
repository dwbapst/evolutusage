package pl.edu.agh.evolutus.foram;

import org.jage.agent.ISimpleAgent;

public interface IForam extends ISimpleAgent {

	void setEnergy(double energy);

	boolean isAlive();
}
