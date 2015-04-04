package pl.edu.agh.evolutus.genotype.gene;

import jdk.nashorn.internal.runtime.ScriptObject;

public class MetabolicEffectivenessGene extends MutableGene<Double> {

	public MetabolicEffectivenessGene(ScriptObject genomeScriptObject) {
		super((ScriptObject) genomeScriptObject.get("metabolicEffectiveness"));
	}
}
