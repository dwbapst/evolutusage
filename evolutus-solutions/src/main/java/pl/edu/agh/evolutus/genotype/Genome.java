package pl.edu.agh.evolutus.genotype;

import java.util.Iterator;

import jdk.nashorn.internal.runtime.ScriptObject;
import pl.edu.agh.evolutus.genotype.gene.DeviationAngleGene;
import pl.edu.agh.evolutus.genotype.gene.DiploidFirstChamberRadiusGene;
import pl.edu.agh.evolutus.genotype.gene.DiploidJuvenileVolumeFactorGene;
import pl.edu.agh.evolutus.genotype.gene.Gene;
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

public class Genome implements Iterable<Gene> {

	public static final int LENGTH = 16;

	public static final int TRANSLATION_FACTOR_INDEX = 0;
	public static final int GROWTH_FACTOR_INDEX = 1;
	public static final int ROTATION_ANGLE_INDEX = 2;
	public static final int DEVIATION_ANGLE_INDEX = 3;

	public static final int PLOIDY_INDEX = 4;

	public static final int HAPLOID_FIRST_CHAMBER_RADIUS_INDEX = 5;
	public static final int DIPLOID_FIRST_CHAMBER_RADIUS_INDEX = 6;
	public static final int WALL_THICKNESS_FACTOR_INDEX = 7;
	public static final int MIN_ADULT_AGE_INDEX = 8;
	public static final int MIN_ADULT_VOLUME_INDEX = 9;
	public static final int HAPLOID_JUVENILE_VOLUME_FACTOR_INDEX = 10;
	public static final int DIPLOID_JUVENILE_VOLUME_FACTOR_INDEX = 11;

	public static final int MAX_ENERGY_INDEX = 12;
	public static final int MIN_ENERGY_INDEX = 13;
	public static final int METABOLIC_EFFECTIVENESS_INDEX = 14;
	public static final int MIN_METABOLIC_EFFECTIVENESS_INDEX = 15;

	public static Genome forGenome(Genome genome, String foramIdentifier) {
		return forGenes(genome.genes, foramIdentifier);
	}

	public static Genome forGenes(Gene[] genes, String foramIdentifier) {
		if (genes.length != LENGTH) {
			throw new IllegalArgumentException(String.format("Genes array have to be %d elements long.", LENGTH));
		}
		return new Genome(genes, foramIdentifier);
	}

	public static Genome fromScriptObject(ScriptObject scriptObject) {
		Genome genome = new Genome(null);
		genome.genes[TRANSLATION_FACTOR_INDEX] = new TranslationFactorGene(scriptObject);
		genome.genes[GROWTH_FACTOR_INDEX] = new GrowthFactorGene(scriptObject);
		genome.genes[ROTATION_ANGLE_INDEX] = new RotationAngleGene(scriptObject);
		genome.genes[DEVIATION_ANGLE_INDEX] = new DeviationAngleGene(scriptObject);
		genome.genes[PLOIDY_INDEX] = new PloidyGene(scriptObject);
		genome.genes[HAPLOID_FIRST_CHAMBER_RADIUS_INDEX] = new HaploidFirstChamberRadiusGene(scriptObject);
		genome.genes[DIPLOID_FIRST_CHAMBER_RADIUS_INDEX] = new DiploidFirstChamberRadiusGene(scriptObject);
		genome.genes[WALL_THICKNESS_FACTOR_INDEX] = new WallThicknessFactorGene(scriptObject);
		genome.genes[MIN_ADULT_AGE_INDEX] = new MinAdultAgeGene(scriptObject);
		genome.genes[MIN_ADULT_VOLUME_INDEX] = new MinAdultVolumeGene(scriptObject);
		genome.genes[HAPLOID_JUVENILE_VOLUME_FACTOR_INDEX] = new HaploidJuvenileVolumeFactorGene(scriptObject);
		genome.genes[DIPLOID_JUVENILE_VOLUME_FACTOR_INDEX] = new DiploidJuvenileVolumeFactorGene(scriptObject);
		genome.genes[MAX_ENERGY_INDEX] = new MaxEnergyGene(scriptObject);
		genome.genes[MIN_ENERGY_INDEX] = new MinEnergyGene(scriptObject);
		genome.genes[METABOLIC_EFFECTIVENESS_INDEX] = new MetabolicEffectivenessGene(scriptObject);
		genome.genes[MIN_METABOLIC_EFFECTIVENESS_INDEX] = new MinMetabolicEffectivenessGene(scriptObject);
		return genome;
	}

	private Gene[] genes = new Gene[LENGTH];

	private final String foramIdentifier;

	protected Genome(String foramIdentifier) {
		this.foramIdentifier = foramIdentifier;
	}

	protected Genome(Gene[] genes, String foramIdentifier) {
		this(foramIdentifier);
		System.arraycopy(genes, 0, this.genes, 0, genes.length);
	}

	public Genome copy() {
		return new Genome(this.genes, this.foramIdentifier);
	}

	public String getForamIdentifier() {
		return foramIdentifier;
	}

	@SuppressWarnings("unchecked")
	public <T extends Gene> T get(int index) {
		if (index < 0 || index >= LENGTH) {
			throw new IndexOutOfBoundsException(String.format("Gene index outside of [0, %d] range.", LENGTH - 1));
		}
		return (T) genes[index];
	}

	public void validate() throws GeneValidationException {
		for(Gene gene : this){
			gene.validate();
		}
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