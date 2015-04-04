package pl.edu.agh.evolutus.genotype.gene;

import jdk.nashorn.internal.runtime.ScriptObject;

public class MinMetabolicEffectivenessGene extends MutableGene<Double> {

	public MinMetabolicEffectivenessGene(ScriptObject genomeScriptObject) {
		super((ScriptObject) genomeScriptObject.get("minMetabolicEffectiveness"));
	}
}
