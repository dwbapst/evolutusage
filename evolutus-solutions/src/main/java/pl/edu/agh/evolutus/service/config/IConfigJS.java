package pl.edu.agh.evolutus.service.config;

import jdk.nashorn.internal.objects.NativeArray;
import jdk.nashorn.internal.runtime.ScriptObject;

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

	double energyNeededForGrowth();

	double growthProbability();

	int chambersLimit();

	double energyNeededToReproduce();

	double reproductionProbability();

	int gametesProduction(int chambersCount);

	double gametesSievingCoefficient();

	double globalMutationProbability();

	NativeArray initialGenome(double x, double y, double z);

	String crossingOverOperator();

	double simulationDuration();

	String simulationName();

	String simulationDescription();

	ScriptObject databaseParameters();

	boolean virtualFossilizationEnabled();

	boolean generatePSI();

	boolean generateCSV();

	boolean generateHTML();
}
