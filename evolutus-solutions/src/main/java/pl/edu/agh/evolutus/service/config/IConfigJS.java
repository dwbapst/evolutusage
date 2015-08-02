package pl.edu.agh.evolutus.service.config;

import jdk.nashorn.internal.objects.NativeArray;
import jdk.nashorn.internal.runtime.ScriptObject;
import pl.edu.agh.evolutus.service.config.utils.EnvState;
import pl.edu.agh.evolutus.service.config.utils.ForamState;
import pl.edu.agh.evolutus.utils.VectorD;

public interface IConfigJS {

	/*-********************
	 *    ENVIRONMENT     *
	 **********************/

	ScriptObject oceanSize();

	long initialForamsCount(double x, double y, double z);

	double initialAlgaeAvailability(double x, double y, double z);

	String boundaryConditions();

	double algaeEnergy(double time, EnvState[] envStates);

	double algaeGrowth(double time, EnvState[] envStates);

	double insolation(double time, EnvState[] envStates);

	ScriptObject currentDirection(double time, EnvState[] envStates);

	/*-********************
	 *       FORAM        *
	 **********************/

	String reproductionType();

	boolean foramActiveMotion();

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

	boolean shouldDie(EnvState envState, ForamState foramState, double time);

	boolean isInHibernationState(EnvState envState, ForamState foramState, double time);

	boolean canReproduce(EnvState envState, ForamState foramState, double time);

	boolean canCreateChamber(EnvState envState, ForamState foramState, double time);

	boolean canMigrate(EnvState envState, ForamState foramState, double time);

	NativeArray initialGenome(VectorD position);

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
