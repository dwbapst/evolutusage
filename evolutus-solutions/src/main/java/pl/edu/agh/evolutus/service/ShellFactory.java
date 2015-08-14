package pl.edu.agh.evolutus.service;

import pl.edu.agh.evolutus.foram.ForamType.Ploidy;
import pl.edu.agh.evolutus.foram.IForam;
import pl.edu.agh.evolutus.foram.Shell;
import pl.edu.agh.evolutus.genotype.Genome;

public class ShellFactory {

	public Shell createInitialShell(IForam foram) {
		double firstChamberRadius = (foram.getType().getPloidy() == Ploidy.DIPLOID) ?
				foram.getGenotype().get(Genome.DIPLOID_FIRST_CHAMBER_RADIUS).getValue() :
				foram.getGenotype().get(Genome.HAPLOID_FIRST_CHAMBER_RADIUS).getValue();
		double wallThicknessFactor = foram.getGenotype().get(Genome.WALL_THICKNESS_FACTOR).getValue();

		double translationFactor = foram.getGenotype().get(Genome.TRANSLATION_FACTOR).getValue();

		double deviationAngle = foram.getGenotype().get(Genome.DEVIATION_ANGLE).getValue();

		double rotationAngle = foram.getGenotype().get(Genome.ROTATION_ANGLE).getValue();

		double growthFactor = foram.getGenotype().get(Genome.GROWTH_FACTOR).getValue();


		return new Shell(firstChamberRadius, wallThicknessFactor, translationFactor, rotationAngle, deviationAngle, growthFactor, 1);
	}

	public Shell createShellWithNewChamber(IForam foram) {
		//double growthFactor = foram.getGenotype().get(Genome.GROWTH_FACTOR).getValue();

		Shell currentShell = foram.getShell();
		//double newRadius = currentShell.getLastChamberRadius() * growthFactor;
		//double newVolume = currentShell.getVolume() + Geometry.sphereVolume(newRadius);
		//return new Shell(currentShell.getFirstChamberRadius(), newRadius, currentShell.getChambersCount() + 1, newVolume);
		return new Shell(currentShell.getFirstChamberRadius(), currentShell.getTranslationFactor(),
				         currentShell.getRotationAngle(), currentShell.getDeviationAngle(),
				         currentShell.getDeviationAngle(), currentShell.getGrowthFactor(), currentShell.getChambersCount() + 1);
	}

}
