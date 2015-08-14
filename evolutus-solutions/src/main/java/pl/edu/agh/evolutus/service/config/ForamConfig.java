package pl.edu.agh.evolutus.service.config;

import jdk.nashorn.internal.runtime.ScriptObject;
import pl.edu.agh.evolutus.foram.ForamType.ReproductionType;
import pl.edu.agh.evolutus.service.config.utils.EnvState;
import pl.edu.agh.evolutus.service.config.utils.ForamState;
import pl.edu.agh.evolutus.utils.MovementCostVector;
import pl.edu.agh.evolutus.utils.VectorD;
import pl.edu.agh.evolutus.utils.VelocityVector;

public class ForamConfig extends Config {

	public ReproductionType reproductionType() {
		return ReproductionType.fromString(configJS.reproductionType());
	}

	public boolean foramActiveMotion() {
		return configJS.foramActiveMotion();
	}

	public double initialEnergy() {
		return configJS.initialEnergy();
	}

	public double energyNeededForGrowth(EnvState envState, ForamState foramState, double time) {
		return configJS.energyNeededForGrowth(envState, foramState, time);
	}

	public double growthProbability(EnvState envState, ForamState foramState, double time) {
		return configJS.growthProbability(envState, foramState, time);
	}

	public int chambersLimit(EnvState envState, ForamState foramState, double time) {
		return configJS.chambersLimit(envState, foramState, time);
	}

	public double energyNeededToReproduce(EnvState envState, ForamState foramState, double time) {
		return configJS.energyNeededToReproduce(envState, foramState, time);
	}

	public double reproductionProbability(EnvState envState, ForamState foramState, double time) {
		return configJS.reproductionProbability(envState, foramState, time);
	}

	public int gametesProduction(EnvState envState, ForamState foramState, double time) {
		return configJS.gametesProduction(envState, foramState, time);
	}

	public double gametesSievingCoefficient(EnvState envState, ForamState foramState, double time) {
		return configJS.gametesSievingCoefficient(envState, foramState, time);
	}

	public double raduisOfFoodCollecting(EnvState envState, ForamState foramState, double time) {
		return configJS.raduisOfFoodCollecting(envState, foramState, time);
	}

	public String crossingOverOperator(EnvState envState, ForamState foramState, double time) {
		return configJS.crossingOverOperator(envState, foramState, time);
	}

	public double globalMutationProbability(EnvState envState, ForamState foramState, double time) {
		return configJS.globalMutationProbability(envState, foramState, time);
	}

	public VelocityVector foramActiveSpeed(EnvState envState, ForamState foramState, double time) {
		ScriptObject currentDirection = configJS.foramActiveSpeed(envState, foramState, time);
		return new VelocityVector(VectorD.fromScriptObject(currentDirection));
	}

	public MovementCostVector activeMotionEnergyCostPerChamberPerMeter(EnvState envState, ForamState foramState, double time) {
		ScriptObject energyCost = configJS.activeMotionEnergyCostPerChamberPerMeter(envState, foramState, time);
		return MovementCostVector.fromScriptObject(energyCost);
	}

	public boolean shouldDie(EnvState envState, ForamState foramState, double time) {
		return configJS.shouldDie(envState, foramState, time);
	}

	public boolean isInHibernationState(EnvState envState, ForamState foramState, double time) {
		return configJS.isInHibernationState(envState, foramState, time);
	}

	public boolean canReproduce(EnvState envState, ForamState foramState, double time) {
		return configJS.canReproduce(envState, foramState, time);
	}

	public boolean canCreateChamber(EnvState envState, ForamState foramState, double time) {
		return configJS.canCreateChamber(envState, foramState, time);
	}

	public boolean canMigrate(EnvState envState, ForamState foramState, double time) {
		return configJS.canMigrate(envState, foramState, time);
	}

	public double stepDurationInHours() {
		return configJS.stepDurationInHours();
	}
}
