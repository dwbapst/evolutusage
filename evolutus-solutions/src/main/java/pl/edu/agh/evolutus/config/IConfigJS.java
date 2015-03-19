package pl.edu.agh.evolutus.config;

import jdk.nashorn.internal.runtime.ScriptObject;

public interface IConfigJS {

	ScriptObject oceanSize();

	double algaeEnergy();

	long initialForamsCount(long x, long y, long z);

	double initialAlgaeAvailability(long x, long y, long z);

	double algaeGrowth(double insolation);

	double insolation(long x, long y, long z);

	ScriptObject currentDirection(long x, long y, long z);

	String boundaryConditions();

	double initialEnergy();

	double energyCapacity(int chambersCount);

	double energyDemand(int chambersCount);

	double chamberGrowthEnergyCost(int chambersCount);

	double energyNeededForGrowth();

	double growthProbability();

	int chambersLimit();

	int newBornLimit();

	double energyNeededToReproduce();

	double reproductionProbability();

	int gametesProduction(int chambersCount);

	double gametesSievingCoefficient();

	ScriptObject initialGenome(long x, long y, long z);

	String crossingOverOperator();

	long simulationDuration();
}
