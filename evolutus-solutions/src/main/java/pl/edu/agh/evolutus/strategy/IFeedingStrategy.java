package pl.edu.agh.evolutus.strategy;

import org.jage.strategy.IStrategy;
import pl.edu.agh.evolutus.environment.IEnvironmentInfo;

public interface IFeedingStrategy extends IStrategy {

	double feed(double foramEnergy, int chambersCount, IEnvironmentInfo environmentInfo);

}
