package pl.edu.agh.evolutus.service.config;

import pl.edu.agh.evolutus.service.config.utils.EnvState;
import pl.edu.agh.evolutus.service.config.utils.ForamState;

public class ForamConfig extends Config {

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

	public String crossingOverOperator(EnvState envState, ForamState foramState, double time) {
		return configJS.crossingOverOperator(envState, foramState, time);
	}

	public double globalMutationProbability(EnvState envState, ForamState foramState, double time) {
		return configJS.globalMutationProbability(envState, foramState, time);
	}

	public boolean shouldDie(EnvState envState, ForamState foramState, double time) {
		return configJS.shouldDie(envState, foramState, time);
	}

	public 	boolean isInHibernationState(EnvState envState, ForamState foramState, double time) {
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
}
