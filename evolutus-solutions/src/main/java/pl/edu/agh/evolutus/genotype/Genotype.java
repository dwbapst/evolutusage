package pl.edu.agh.evolutus.genotype;

import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import pl.edu.agh.evolutus.genotype.gene.DeviationAngleGene;
import pl.edu.agh.evolutus.genotype.gene.DiploidFirstChamberRadiusGene;
import pl.edu.agh.evolutus.genotype.gene.DiploidJuvenileVolumeFactorGene;
import pl.edu.agh.evolutus.genotype.gene.Gene.GeneValidationException;
import pl.edu.agh.evolutus.genotype.gene.GrowthFactorGene;
import pl.edu.agh.evolutus.genotype.gene.HaploidFirstChamberRadiusGene;
import pl.edu.agh.evolutus.genotype.gene.HaploidJuvenileVolumeFactorGene;
import pl.edu.agh.evolutus.genotype.gene.MaxEnergyGene;
import pl.edu.agh.evolutus.genotype.gene.MetabolicEffectivenessGene;
import pl.edu.agh.evolutus.genotype.gene.MinAdultAgeGene;
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

	public List<Genome> createGametes(int number, double gametesSievingCoefficient) {
		if (number % 2 != 0) {
			throw new IllegalArgumentException("Number of gametes to create has to be even. Given: " + number);
		}
		return createGameteStream(number)
				.filter(gamete -> rand.nextDouble() > gametesSievingCoefficient)
				.collect(Collectors.toList());
	}

	public void validate() throws GeneValidationException {
		getEffectiveGenome().validate();
	}

	public TranslationFactorGene getTranslationFactorGene() {
		return getEffectiveGenome().get(Genome.TRANSLATION_FACTOR_INDEX);
	}

	public GrowthFactorGene getGrowthFactorGene() {
		return getEffectiveGenome().get(Genome.GROWTH_FACTOR_INDEX);
	}

	public RotationAngleGene getRotationAngleGene() {
		return getEffectiveGenome().get(Genome.ROTATION_ANGLE_INDEX);
	}

	public DeviationAngleGene getDeviationAngleGene() {
		return getEffectiveGenome().get(Genome.DEVIATION_ANGLE_INDEX);
	}

	public PloidyGene getPloidyGene() {
		return getEffectiveGenome().get(Genome.PLOIDY_INDEX);
	}

	public HaploidFirstChamberRadiusGene getHaploidFirstChamberRadiusGene() {
		return getEffectiveGenome().get(Genome.HAPLOID_FIRST_CHAMBER_RADIUS_INDEX);
	}

	public DiploidFirstChamberRadiusGene getDiploidFirstChamberRadiusGene() {
		return getEffectiveGenome().get(Genome.DIPLOID_FIRST_CHAMBER_RADIUS_INDEX);
	}

	public WallThicknessFactorGene getWallThicknessFactorGene() {
		return getEffectiveGenome().get(Genome.WALL_THICKNESS_FACTOR_INDEX);
	}

	public MinAdultAgeGene getMinAdultAgeGene() {
		return getEffectiveGenome().get(Genome.MIN_ADULT_AGE_INDEX);
	}

	public MinAdultVolumeGene getMinAdultVolumeGene() {
		return getEffectiveGenome().get(Genome.MIN_ADULT_VOLUME_INDEX);
	}

	public HaploidJuvenileVolumeFactorGene getHaploidJuvenileVolumeFactorGene() {
		return getEffectiveGenome().get(Genome.HAPLOID_JUVENILE_VOLUME_FACTOR_INDEX);
	}

	public DiploidJuvenileVolumeFactorGene getDiploidJuvenileVolumeFactorGene() {
		return getEffectiveGenome().get(Genome.DIPLOID_JUVENILE_VOLUME_FACTOR_INDEX);
	}

	public MaxEnergyGene getMaxEnergyGene() {
		return getEffectiveGenome().get(Genome.MAX_ENERGY_INDEX);
	}

	public MinEnergyGene getMinEnergyGene() {
		return getEffectiveGenome().get(Genome.MIN_ENERGY_INDEX);
	}

	public MetabolicEffectivenessGene getMetabolicEffectivenessGene() {
		return getEffectiveGenome().get(Genome.METABOLIC_EFFECTIVENESS_INDEX);
	}

	public MinMetabolicEffectivenessGene getMinMetabolicEffectivenessGene() {
		return getEffectiveGenome().get(Genome.MIN_METABOLIC_EFFECTIVENESS_INDEX);
	}

}
