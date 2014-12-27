package pl.edu.agh.evolutus.foram;

import org.jage.agent.ISimpleAgent;
import pl.edu.agh.evolutus.environment.IOceanFragment;
import pl.edu.agh.evolutus.strategy.IEnergyConsumptionStrategy;
import pl.edu.agh.evolutus.strategy.IFeedingStrategy;

public interface IForam extends ISimpleAgent {

	void setEnergy(double energy);

	void setFeedingStrategy(IFeedingStrategy feedingStrategy);

	void setEnergyConsumptionStrategy(IEnergyConsumptionStrategy energyConsumptionStrategy);

	void setOceanFragment(IOceanFragment oceanFragment);

}
