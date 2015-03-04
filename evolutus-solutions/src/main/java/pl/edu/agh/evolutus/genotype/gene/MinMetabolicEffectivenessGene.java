package pl.edu.agh.evolutus.genotype.gene;

import jdk.nashorn.api.scripting.ScriptObjectMirror;

public class MinMetabolicEffectivenessGene extends Gene<Double> {

	private MinMetabolicEffectivenessGene(Double value, boolean isDominant) {
		super(value, isDominant);
	}

	public static MinMetabolicEffectivenessGene fromGenomeScriptObject(ScriptObjectMirror genomeScriptObject) {
		return new MinMetabolicEffectivenessGene((Double) genomeScriptObject.get("minMetabolicEffectiveness"),
				RAND.nextDouble() < 0.5);
	}
}
