package pl.edu.agh.evolutus.genotype;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeMap;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import com.google.common.collect.ImmutableSet;

import jdk.nashorn.internal.objects.NativeArray;
import jdk.nashorn.internal.runtime.ScriptObject;
import pl.edu.agh.evolutus.genotype.Gene.GeneValidationException;

public class Genome implements Iterable<Gene> {

	public static final String TRANSLATION_FACTOR = "translationFactor";
	public static final String GROWTH_FACTOR = "growthFactor";
	public static final String ROTATION_ANGLE = "rotationAngle";
	public static final String DEVIATION_ANGLE = "deviationAngle";

	public static final String HAPLOID_FIRST_CHAMBER_RADIUS = "haploidFirstChamberRadius";
	public static final String DIPLOID_FIRST_CHAMBER_RADIUS = "diploidFirstChamberRadius";
	public static final String WALL_THICKNESS_FACTOR = "wallThicknessFactor";
	public static final String MIN_ADULT_AGE = "minAdultAge";
	public static final String MIN_ADULT_VOLUME = "minAdultVolume";
	public static final String HAPLOID_JUVENILE_VOLUME_FACTOR = "haploidJuvenileVolumeFactor";
	public static final String DIPLOID_JUVENILE_VOLUME_FACTOR = "diploidJuvenileVolumeFactor";

	public static final String MAX_ENERGY_PER_CHAMBER = "maxEnergyPerChamber";
	public static final String FOOD_COLLECTING_RATE = "foodCollectingRate";
	public static final String ENERGY_DEMAND_PER_CHAMBER_PER_HOUR = "energyDemandPerChamberPerHour";
	public static final String MIN_ENERGY = "minEnergy";
	public static final String CHAMBER_GROWTH_COST_FACTOR = "chamberGrowthCostFactor";
	public static final String METABOLIC_EFFECTIVENESS = "metabolicEffectiveness";
	public static final String MIN_METABOLIC_EFFECTIVENESS = "minMetabolicEffectiveness";
	public static final String HIBERNATION_ENERGY_LEVEL = "hibernationEnergyLevel";
	public static final String HIBERNATION_ENERGY_CONSUMPTION_PER_HOUR = "hibernationEnergyConsumptionPerHour";

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

	private Map<String, Gene> genes = new TreeMap<>();

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