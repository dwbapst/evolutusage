package pl.edu.agh.evolutus.strategy;

import org.jage.strategy.IStrategy;
import pl.edu.agh.evolutus.environment.IEnvironmentInfo;

public interface IEnergyConsumptionStrategy extends IStrategy {

	double consume(double foramEnergy, int chambersCount, IEnvironmentInfo environmentInfo);

}
