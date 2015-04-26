package pl.edu.agh.evolutus.genotype;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import com.google.common.collect.ImmutableSet;

import jdk.nashorn.internal.objects.NativeArray;
import jdk.nashorn.internal.runtime.ScriptObject;
import pl.edu.agh.evolutus.genotype.Gene.GeneValidationException;

public class Genome implements Iterable<Gene> {

	public static final String TRANSLATION_FACTOR_NAME = "translationFactor";
	public static final String GROWTH_FACTOR_NAME = "growthFactor";
	public static final String ROTATION_ANGLE_NAME = "rotationAngle";
	public static final String DEVIATION_ANGLE_NAME = "deviationAngle";

	public static final String HAPLOID_FIRST_CHAMBER_RADIUS_NAME = "haploidFirstChamberRadius";
	public static final String DIPLOID_FIRST_CHAMBER_RADIUS_NAME = "diploidFirstChamberRadius";
	public static final String WALL_THICKNESS_FACTOR_NAME = "wallThicknessFactor";
	public static final String MIN_ADULT_AGE_NAME = "minAdultAge";
	public static final String MIN_ADULT_VOLUME_NAME = "minAdultVolume";
	public static final String HAPLOID_JUVENILE_VOLUME_FACTOR_NAME = "haploidJuvenileVolumeFactor";
	public static final String DIPLOID_JUVENILE_VOLUME_FACTOR_NAME = "diploidJuvenileVolumeFactor";

	public static final String MAX_ENERGY_NAME = "maxEnergy";
	public static final String MIN_ENERGY_NAME = "minEnergy";
	public static final String METABOLIC_EFFECTIVENESS_NAME = "metabolicEffectiveness";
	public static final String MIN_METABOLIC_EFFECTIVENESS_NAME = "minMetabolicEffectiveness";

	public static Genome forGenome(Genome genome, String foramIdentifier) {
		return forGenes(genome.genes, foramIdentifier);
	}

	public static Genome forGenes(Map<String, Gene> genes, String foramIdentifier) {
		return new Genome(genes, foramIdentifier);
	}

	public static Genome fromScriptObject(NativeArray initialGenome) {
		Map<String, Gene> genes = new HashMap<>();
		for (Object geneScriptObject : initialGenome.asObjectArray()) {
			Gene gene = new Gene((ScriptObject) geneScriptObject);
			genes.put(gene.getName(), gene);
		}
		return new Genome(genes, null);
	}

	private Map<String, Gene> genes = new HashMap<>();

	private final String foramIdentifier;

	private Genome(String foramIdentifier) {
		this.foramIdentifier = foramIdentifier;
	}

	private Genome(Map<String, Gene> genes, String foramIdentifier) {
		this(foramIdentifier);
		this.genes.putAll(genes);
	}

	public Genome copy() {
		return new Genome(this.genes, this.foramIdentifier);
	}

	public String getForamIdentifier() {
		return foramIdentifier;
	}

	public Gene get(String name) {
		return genes.get(name);
	}

	public Gene getOrThrowException(String name) {
		Gene gene = get(name);
		if (gene == null) {
			throw new IllegalStateException("Cannot find gene: " + name);
		}
		return gene;
	}

	public Genome mutate(double globalMutationProbability) {
		Genome mutatedGenome = new Genome(foramIdentifier);
		for (Entry<String, Gene> entry : genes.entrySet()) {
			mutatedGenome.genes.put(entry.getKey(), entry.getValue().mutate(globalMutationProbability));
		}
		return mutatedGenome;
	}

	public void validate() throws GeneValidationException {
		for (Gene gene : this) {
			gene.validate();
		}
	}

	public boolean isValid() {
		for (Gene gene : this) {
			if (!gene.isValid()) {
				return false;
			}
		}
		return true;
	}

	public Set<String> geneNames() {
		return ImmutableSet.copyOf(genes.keySet());
	}

	@Override
	public Iterator<Gene> iterator() {
		return genes.values().iterator();
	}

	public Stream<Gene> stream() {
		return StreamSupport.stream(spliterator(), false);
	}

	public int size() {
		return genes.size();
	}
}