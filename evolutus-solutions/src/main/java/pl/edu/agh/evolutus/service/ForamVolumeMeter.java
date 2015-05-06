package pl.edu.agh.evolutus.service;

import static java.lang.Math.*;

import pl.edu.agh.evolutus.genotype.Genome;
import pl.edu.agh.evolutus.genotype.Genotype;

public class ForamVolumeMeter {

	public double getForamVolume(int chambersCount, Genotype genotype, boolean isHaploid) {
		Double radius = isHaploid ?
				genotype.get(Genome.HAPLOID_FIRST_CHAMBER_RADIUS).getValue() :
				genotype.get(Genome.DIPLOID_FIRST_CHAMBER_RADIUS).getValue();
		Double growthFactor = genotype.get(Genome.GROWTH_FACTOR).getValue();

		double volume = 0.0;
		for (int i = 0; i < chambersCount; i++) {
			volume += sphereVolume(radius);
			radius *= growthFactor;
		}
		return volume;
	}

	private double sphereVolume(double radius) {
		return 4.0 / 3.0 * PI * pow(radius, 3.0);
	}

}
