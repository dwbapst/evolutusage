package pl.edu.agh.evolutus.genotype;

import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import pl.edu.agh.evolutus.genotype.gene.DeviationAngleGene;
import pl.edu.agh.evolutus.genotype.gene.FirstChamberRadiusGene;
import pl.edu.agh.evolutus.genotype.gene.GrowthFactorGene;
import pl.edu.agh.evolutus.genotype.gene.JuvenileVolumeFactorGene;
import pl.edu.agh.evolutus.genotype.gene.MaxEnergyGene;
import pl.edu.agh.evolutus.genotype.gene.MetabolicEffectivenessGene;
import pl.edu.agh.evolutus.genotype.gene.MinAdultVolumeGene;
import pl.edu.agh.evolutus.genotype.gene.MinEnergyGene;
import pl.edu.agh.evolutus.genotype.gene.MinMetabolicEffectivenessGene;
import pl.edu.agh.evolutus.genotype.gene.PloidyGene;
import pl.edu.agh.evolutus.genotype.gene.RotationAngleGene;
import pl.edu.agh.evolutus.genotype.gene.TranslationFactorGene;
import pl.edu.agh.evolutus.genotype.gene.WallThicknessFactorGene;

public abstract class Genotype {

	private final Random rand = new Random();

	public abstract Genome getEffectiveGenome();

	protected abstract Stream<Genome> createGameteStream(int number);

	public List<Genome> createGametes(int number, double sievingCoefficient) {
		if (number % 2 != 0) {
			throw new IllegalArgumentException("Number of gametes to create has to be even. Given: " + number);
		}
		return createGameteStream(number)
				.filter(gamete -> rand.nextDouble() > sievingCoefficient)
				.collect(Collectors.toList());
	}

	public TranslationFactorGene getTranslationFactorGene() {
		return getEffectiveGenome().getTranslationFactorGene();
	}

	public GrowthFactorGene getGrowthFactorGene() {
		return getEffectiveGenome().getGrowthFactorGene();
	}

	public RotationAngleGene getRotationAngleGene() {
		return getEffectiveGenome().getRotationAngleGene();
	}

	public DeviationAngleGene getDeviationAngleGene() {
		return getEffectiveGenome().getDeviationAngleGene();
	}

	public PloidyGene getPloidyGene() {
		return getEffectiveGenome().getPloidyGene();
	}

	public FirstChamberRadiusGene getFirstChamberRadiusGene() {
		return getEffectiveGenome().getHaploidFirstChamberRadiusGene();
	}

	public WallThicknessFactorGene getWallThicknessFactorGene() {
		return getEffectiveGenome().getWallThicknessFactorGene();
	}

	public MinAdultVolumeGene getMinAdultVolumeGene() {
		return getEffectiveGenome().getMinAdultVolumeGene();
	}

	public JuvenileVolumeFactorGene getJuvenileVolumeFactorGene() {
		return getEffectiveGenome().getHaploidJuvenileVolumeFactorGene();
	}

	public MaxEnergyGene getMaxEnergyGene() {
		return getEffectiveGenome().getMaxEnergyGene();
	}

	public MinEnergyGene getMinEnergyGene() {
		return getEffectiveGenome().getMinEnergyGene();
	}

	public MetabolicEffectivenessGene getMetabolicEffectivenessGene() {
		return getEffectiveGenome().getMetabolicEffectivenessGene();
	}

	public MinMetabolicEffectivenessGene getMinMetabolicEffectivenessGene() {
		return getEffectiveGenome().getMinMetabolicEffectivenessGene();
	}
}
