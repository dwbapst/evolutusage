package pl.edu.agh.evolutus.service.config;

import jdk.nashorn.internal.objects.NativeArray;
import jdk.nashorn.internal.runtime.ScriptObject;
import pl.edu.agh.evolutus.service.config.utils.EnvState;
import pl.edu.agh.evolutus.service.config.utils.ForamState;

public interface IConfigJS {

	/*-********************
	 *    ENVIRONMENT     *
	 **********************/

	ScriptObject oceanSize();

	double algaeEnergy();

	long initialForamsCount(double x, double y, double z);

	double initialAlgaeAvailability(double x, double y, double z);

	double algaeGrowth(double insolation);

	double insolation(double x, double y, double z);

	ScriptObject currentDirection(double x, double y, double z);

	String boundaryConditions();

	/*-********************
	 *       FORAM        *
	 **********************/

	double initialEnergy();

	double energyNeededForGrowth(EnvState envState, ForamState foramState, double time);

	double growthProbability(EnvState envState, ForamState foramState, double time);

	int chambersLimit(EnvState envState, ForamState foramState, double time);

	double energyNeededToReproduce(EnvState envState, ForamState foramState, double time);

	double reproductionProbability(EnvState envState, ForamState foramState, double time);

	int gametesProduction(EnvState envState, ForamState foramState, double time);

	double gametesSievingCoefficient(EnvState envState, ForamState foramState, double time);

	String crossingOverOperator(EnvState envState, ForamState foramState, double time);

	double globalMutationProbability(EnvState envState, ForamState foramState, double time);

	NativeArray initialGenome(EnvState envState);

	/*-********************
	 *     SIMULATION     *
	 **********************/

	double unitLengthInMeters();

	double stepDurationInHours();

	double simulationDuration();

	String simulationName();

	String simulationDescription();

	boolean virtualFossilizationEnabled();

	boolean generatePSI();

	boolean generateCSV();

	boolean generateHTML();

	/*-********************
	 *       SYSTEM       *
	 **********************/

	ScriptObject databaseParameters();

}
