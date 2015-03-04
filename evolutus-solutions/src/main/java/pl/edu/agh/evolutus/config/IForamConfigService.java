package pl.edu.agh.evolutus.config;

import pl.edu.agh.evolutus.genotype.Genome;
import pl.edu.agh.evolutus.utils.VectorL;

public interface IForamConfigService {
	double getForamInitialEnergy();

	double getEnergyCapacity(int chambersCount);

	double getEnergyDemand(int chambersCount);

	double getChamberGrowthEnergyCost(int chambersCount);

	double getEnergyNeededForGrowth();

	double getGrowthProbability();

	int getChambersLimit();

	int getNewBornLimit();

	double getEnergyNeededToReproduce();

	double getReproductionProbability();

	Genome getInitialGenome(VectorL position);
}
