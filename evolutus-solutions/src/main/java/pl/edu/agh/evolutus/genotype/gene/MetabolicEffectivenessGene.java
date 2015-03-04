package pl.edu.agh.evolutus.genotype.gene;

import jdk.nashorn.api.scripting.ScriptObjectMirror;

public class MetabolicEffectivenessGene extends Gene<Double> {

	private MetabolicEffectivenessGene(Double value, boolean isDominant) {
		super(value, isDominant);
	}

	public static MetabolicEffectivenessGene fromGenomeScriptObject(ScriptObjectMirror genomeScriptObject) {
		return new MetabolicEffectivenessGene((Double) genomeScriptObject.get("metabolicEffectiveness"), RAND.nextDouble() < 0.5);
	}
}
