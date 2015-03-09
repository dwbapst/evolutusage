package pl.edu.agh.evolutus.genotype.gene;

import jdk.nashorn.internal.runtime.ScriptObject;

public class MinMetabolicEffectivenessGene extends Gene<Double> {

	private MinMetabolicEffectivenessGene(Double value, boolean isDominant) {
		super(value, isDominant);
	}

	public static MinMetabolicEffectivenessGene fromGenomeScriptObject(ScriptObject genomeScriptObject) {
		return new MinMetabolicEffectivenessGene((Double) genomeScriptObject.get("minMetabolicEffectiveness"),
				RAND.nextDouble() < 0.5);
	}
}
