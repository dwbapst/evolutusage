package pl.edu.agh.evolutus.strategy;

import pl.edu.agh.evolutus.environment.IEnvironmentInfo;

public class NoOpEnergyConsumptionStrategy implements IEnergyConsumptionStrategy {

	@Override
	public double consume(double foramEnergy, int chambersCount, IEnvironmentInfo environmentInfo) {
		return foramEnergy;
	}

}
