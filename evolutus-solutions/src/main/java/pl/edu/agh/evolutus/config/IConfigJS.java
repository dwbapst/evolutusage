package pl.edu.agh.evolutus.config;

import java.awt.geom.RoundRectangle2D;

import jdk.nashorn.internal.objects.NativeArray;
import jdk.nashorn.internal.runtime.ScriptObject;
import pl.edu.agh.evolutus.utils.VectorD;
import pl.edu.agh.evolutus.utils.VectorL;

public interface IConfigJS {

	double unitLengthInMeters();

	double stepDurationInHours();

	ScriptObject oceanSize();

	double algaeEnergy();

	long initialForamsCount(double x, double y, double z);

	double initialAlgaeAvailability(double x, double y, double z);

	double algaeGrowth(double insolation);

	double insolation(double x, double y, double z);

	ScriptObject currentDirection(double x, double y, double z);

	String boundaryConditions();

	double initialEnergy();

	double energyCapacity(int chambersCount);

	double energyDemand(int chambersCount);

	double chamberGrowthEnergyCost(int chambersCount);

	double energyNeededForGrowth();

	double growthProbability();

	int chambersLimit();

	double energyNeededToReproduce();

	double reproductionProbability();

	int gametesProduction(int chambersCount);

	double gametesSievingCoefficient();

	NativeArray initialGenome(double x, double y, double z);

	String crossingOverOperator();

	double simulationDuration();

	ScriptObject databaseParameters();
}
