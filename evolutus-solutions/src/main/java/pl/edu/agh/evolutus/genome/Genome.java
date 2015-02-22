package pl.edu.agh.evolutus.genome;

import jdk.nashorn.api.scripting.ScriptObjectMirror;
import pl.edu.agh.evolutus.genome.gene.DeviationAngleGene;
import pl.edu.agh.evolutus.genome.gene.DiploidFirstChamberRadiusGene;
import pl.edu.agh.evolutus.genome.gene.DiploidJuvenileVolumeFactorGene;
import pl.edu.agh.evolutus.genome.gene.Gene;
import pl.edu.agh.evolutus.genome.gene.GrowthFactorGene;
import pl.edu.agh.evolutus.genome.gene.HaploidFirstChamberRadiusGene;
import pl.edu.agh.evolutus.genome.gene.HaploidJuvenileVolumeFactorGene;
import pl.edu.agh.evolutus.genome.gene.MaxEnergyGene;
import pl.edu.agh.evolutus.genome.gene.MetabolicEffectivenessGene;
import pl.edu.agh.evolutus.genome.gene.MinAdultVolumeGene;
import pl.edu.agh.evolutus.genome.gene.MinEnergyGene;
import pl.edu.agh.evolutus.genome.gene.MinMetabolicEffectivenessGene;
import pl.edu.agh.evolutus.genome.gene.PloidyGene;
import pl.edu.agh.evolutus.genome.gene.RotationAngleGene;
import pl.edu.agh.evolutus.genome.gene.TranslationFactorGene;
import pl.edu.agh.evolutus.genome.gene.WallThicknessFactorGene;

public class Genome {

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

	private Gene[] genes = new Gene[LENGTH];

	private Genome() {
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
}