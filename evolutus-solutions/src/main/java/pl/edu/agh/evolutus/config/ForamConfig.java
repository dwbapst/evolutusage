package pl.edu.agh.evolutus.config;

public class ForamConfig {

	private final IConfigJS configJS;

	public ForamConfig(IConfigJS configJS) {
		this.configJS = configJS;
	}

	public double initialEnergy() {
		return configJS.initialEnergy();
	}

	public double energyCapacity(int chambersCount) {
		return configJS.energyCapacity(chambersCount);
	}

	public double energyDemand(int chambersCount) {
		return configJS.energyDemand(chambersCount);
	}

	public double chamberGrowthEnergyCost(int chambersCount) {
		return configJS.chamberGrowthEnergyCost(chambersCount);
	}

	public double energyNeededForGrowth() {
		return configJS.energyNeededForGrowth();
	}

	public double growthProbability() {
		return configJS.growthProbability();
	}

	public int chambersLimit() {
		return configJS.chambersLimit();
	}

	public double energyNeededToReproduce() {
		return configJS.energyNeededToReproduce();
	}

	public double reproductionProbability() {
		return configJS.reproductionProbability();
	}

	public int gametesProduction(int chambersCount) {
		return configJS.gametesProduction(chambersCount);
	}

	public double gametesSievingCoefficient() {
		return configJS.gametesSievingCoefficient();
	}
}
