package pl.edu.agh.evolutus.genotype.gene;

import jdk.nashorn.internal.runtime.ScriptObject;

public class MetabolicEffectivenessGene extends Gene<Double> {

	private MetabolicEffectivenessGene(Double value, boolean isDominant) {
		super(value, isDominant);
	}

	public static MetabolicEffectivenessGene fromGenomeScriptObject(ScriptObject genomeScriptObject) {
		return new MetabolicEffectivenessGene((Double) genomeScriptObject.get("metabolicEffectiveness"), RAND.nextDouble() < 0.5);
	}
}
