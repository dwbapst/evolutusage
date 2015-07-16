package pl.edu.agh.evolutus.service;

import pl.edu.agh.evolutus.foram.IForam;
import pl.edu.agh.evolutus.foram.Shell;
import pl.edu.agh.evolutus.genotype.Genome;
import pl.edu.agh.evolutus.utils.Geometry;

public class ShellFactory {

	public Shell createInitialShell(IForam foram) {
		double radius = foram.getType().isDiploid() ?
				foram.getGenotype().get(Genome.DIPLOID_FIRST_CHAMBER_RADIUS).getValue() :
				foram.getGenotype().get(Genome.HAPLOID_FIRST_CHAMBER_RADIUS).getValue();

		return new Shell(radius, radius, 1, Geometry.sphereVolume(radius));
	}

	public Shell createShellWithNewChamber(IForam foram) {
		double growthFactor = foram.getGenotype().get(Genome.GROWTH_FACTOR).getValue();

		Shell currentShell = foram.getShell();
		double newRadius = currentShell.getLastChamberRadius() * growthFactor;
		double newVolume = currentShell.getVolume() + Geometry.sphereVolume(newRadius);
		return new Shell(currentShell.getFirstChamberRadius(), newRadius, currentShell.getChambersCount() + 1, newVolume);
	}

}
