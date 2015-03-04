package pl.edu.agh.evolutus.genotype;

import java.util.Iterator;

import jdk.nashorn.api.scripting.ScriptObjectMirror;
import pl.edu.agh.evolutus.genotype.gene.DeviationAngleGene;
import pl.edu.agh.evolutus.genotype.gene.DiploidFirstChamberRadiusGene;
import pl.edu.agh.evolutus.genotype.gene.DiploidJuvenileVolumeFactorGene;
import pl.edu.agh.evolutus.genotype.gene.Gene;
import pl.edu.agh.evolutus.genotype.gene.GrowthFactorGene;
import pl.edu.agh.evolutus.genotype.gene.HaploidFirstChamberRadiusGene;
import pl.edu.agh.evolutus.genotype.gene.HaploidJuvenileVolumeFactorGene;
import pl.edu.agh.evolutus.genotype.gene.MaxEnergyGene;
import pl.edu.agh.evolutus.genotype.gene.MetabolicEffectivenessGene;
import pl.edu.agh.evolutus.genotype.gene.MinAdultVolumeGene;
import pl.edu.agh.evolutus.genotype.gene.MinEnergyGene;
import pl.edu.agh.evolutus.genotype.gene.MinMetabolicEffectivenessGene;
import pl.edu.agh.evolutus.genotype.gene.PloidyGene;
import pl.edu.agh.evolutus.genotype.gene.RotationAngleGene;
import pl.edu.agh.evolutus.genotype.gene.TranslationFactorGene;
import pl.edu.agh.evolutus.genotype.gene.WallThicknessFactorGene;

public class Genome implements Iterable<Gene> {

	public static final int LENGTH = 15;

	private static final int TRANSLATION_FACTOR_INDEX = 0;
	private static final int GROWTH_FACTOR_INDEX = 1;
	private static final int ROTATION_ANGLE_INDEX = 2;
	private static final int DEVIATION_ANGLE_INDEX = 3;

	private static final int PLOIDY_INDEX = 4;

	private static final int HAPLOID_FIRST_CHAMBER_RADIUS_INDEX = 5;
	private static final int DIPLOID_FIRST_CHAMBER_RADIUS_INDEX = 6;
	private static final int WALL_THICKNESS_FACTOR_INDEX = 7;
	private static final int MIN_ADULT_VOLUME_INDEX = 8;
	private static final int HAPLOID_JUVENILE_VOLUME_FACTOR_INDEX = 9;
	private static final int DIPLOID_JUVENILE_VOLUME_FACTOR_INDEX = 10;

	private static final int MAX_ENERGY_INDEX = 11;
	private static final int MIN_ENERGY_INDEX = 12;
	private static final int METABOLIC_EFFECTIVENESS_INDEX = 13;
	private static final int MIN_METABOLIC_EFFECTIVENESS_INDEX = 14;

	public static Genome forGenes(Gene[] genes) {
		if (genes.length != LENGTH) {
			throw new IllegalArgumentException(String.format("Genes array have to be %d elements long.", LENGTH));
		}
		return new Genome(genes);
	}

	public static Genome fromScriptObject(ScriptObjectMirror scriptObject) {
		Genome genome = new Genome();
		genome.genes[TRANSLATION_FACTOR_INDEX] = TranslationFactorGene.fromGenomeScriptObject(scriptObject);
		genome.genes[GROWTH_FACTOR_INDEX] = GrowthFactorGene.fromGenomeScriptObject(scriptObject);
		genome.genes[ROTATION_ANGLE_INDEX] = RotationAngleGene.fromGenomeScriptObject(scriptObject);
		genome.genes[DEVIATION_ANGLE_INDEX] = DeviationAngleGene.fromGenomeScriptObject(scriptObject);
		genome.genes[PLOIDY_INDEX] = PloidyGene.fromGenomeScriptObject(scriptObject);
		genome.genes[HAPLOID_FIRST_CHAMBER_RADIUS_INDEX] = HaploidFirstChamberRadiusGene.fromGenomeScriptObject(scriptObject);
		genome.genes[DIPLOID_FIRST_CHAMBER_RADIUS_INDEX] = DiploidFirstChamberRadiusGene.fromGenomeScriptObject(scriptObject);
		genome.genes[WALL_THICKNESS_FACTOR_INDEX] = WallThicknessFactorGene.fromGenomeScriptObject(scriptObject);
		genome.genes[MIN_ADULT_VOLUME_INDEX] = MinAdultVolumeGene.fromGenomeScriptObject(scriptObject);
		genome.genes[HAPLOID_JUVENILE_VOLUME_FACTOR_INDEX] = HaploidJuvenileVolumeFactorGene.fromGenomeScriptObject(scriptObject);
		genome.genes[DIPLOID_JUVENILE_VOLUME_FACTOR_INDEX] = DiploidJuvenileVolumeFactorGene.fromGenomeScriptObject(scriptObject);
		genome.genes[MAX_ENERGY_INDEX] = TranslationFactorGene.fromGenomeScriptObject(scriptObject);
		genome.genes[MIN_ENERGY_INDEX] = TranslationFactorGene.fromGenomeScriptObject(scriptObject);
		genome.genes[METABOLIC_EFFECTIVENESS_INDEX] = MetabolicEffectivenessGene.fromGenomeScriptObject(scriptObject);
		genome.genes[MIN_METABOLIC_EFFECTIVENESS_INDEX] = MinMetabolicEffectivenessGene.fromGenomeScriptObject(scriptObject);
		return genome;
	}

	private Gene[] genes = new Gene[LENGTH];

	protected Genome() {
	}

	protected Genome(Gene[] genes) {
		this.genes = genes;
	}

	public Genome copy() {
		return new Genome(this.genes);
	}

	public TranslationFactorGene getTranslationFactorGene() {
		return (TranslationFactorGene) genes[TRANSLATION_FACTOR_INDEX];
	}

	public GrowthFactorGene getGrowthFactorGene() {
		return (GrowthFactorGene) genes[GROWTH_FACTOR_INDEX];
	}

	public RotationAngleGene getRotationAngleGene() {
		return (RotationAngleGene) genes[ROTATION_ANGLE_INDEX];
	}

	public DeviationAngleGene getDeviationAngleGene() {
		return (DeviationAngleGene) genes[DEVIATION_ANGLE_INDEX];
	}

	public PloidyGene getPloidyGene() {
		return (PloidyGene) genes[PLOIDY_INDEX];
	}

	public HaploidFirstChamberRadiusGene getHaploidFirstChamberRadiusGene() {
		return (HaploidFirstChamberRadiusGene) genes[HAPLOID_FIRST_CHAMBER_RADIUS_INDEX];
	}

	public DiploidFirstChamberRadiusGene getDiploidFirstChamberRadiusGene() {
		return (DiploidFirstChamberRadiusGene) genes[DIPLOID_FIRST_CHAMBER_RADIUS_INDEX];
	}

	public WallThicknessFactorGene getWallThicknessFactorGene() {
		return (WallThicknessFactorGene) genes[WALL_THICKNESS_FACTOR_INDEX];
	}

	public MinAdultVolumeGene getMinAdultVolumeGene() {
		return (MinAdultVolumeGene) genes[MIN_ADULT_VOLUME_INDEX];
	}

	public HaploidJuvenileVolumeFactorGene getHaploidJuvenileVolumeFactorGene() {
		return (HaploidJuvenileVolumeFactorGene) genes[HAPLOID_JUVENILE_VOLUME_FACTOR_INDEX];
	}

	public DiploidJuvenileVolumeFactorGene getDiploidJuvenileVolumeFactorGene() {
		return (DiploidJuvenileVolumeFactorGene) genes[DIPLOID_JUVENILE_VOLUME_FACTOR_INDEX];
	}

	public MaxEnergyGene getMaxEnergyGene() {
		return (MaxEnergyGene) genes[MAX_ENERGY_INDEX];
	}

	public MinEnergyGene getMinEnergyGene() {
		return (MinEnergyGene) genes[MIN_ENERGY_INDEX];
	}

	public MetabolicEffectivenessGene getMetabolicEffectivenessGene() {
		return (MetabolicEffectivenessGene) genes[METABOLIC_EFFECTIVENESS_INDEX];
	}

	public MinMetabolicEffectivenessGene getMinMetabolicEffectivenessGene() {
		return (MinMetabolicEffectivenessGene) genes[MIN_METABOLIC_EFFECTIVENESS_INDEX];
	}

	public Gene get(int index) {
		if (index < 0 || index >= LENGTH) {
			throw new IndexOutOfBoundsException(String.format("Gene index outside of [0, %d] range.", LENGTH - 1));
		}
		return genes[index];
	}

	@Override
	public Iterator<Gene> iterator() {
		return new GenomeIterator(this);
	}

	private static class GenomeIterator implements Iterator<Gene> {

		private final Genome genome;
		private int index = 0;

		public GenomeIterator(Genome genome) {
			this.genome = genome;
		}

		@Override
		public boolean hasNext() {
			return index < Genome.LENGTH;
		}

		@Override
		public Gene next() {
			return genome.genes[index++];
		}
	}
}