package pl.edu.agh.evolutus.strategy;

import pl.edu.agh.evolutus.environment.IEnvironmentInfo;

public class NoOpFeedingStrategy implements IFeedingStrategy {

	@Override
	public double feed(double foramEnergy, int chambersCount, IEnvironmentInfo environmentInfo) {
		return foramEnergy;
	}

}
