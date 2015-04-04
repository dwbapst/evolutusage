package pl.edu.agh.evolutus.genotype.gene;

import jdk.nashorn.internal.runtime.ScriptObject;

public class MaxEnergyGene extends MutableGene<Double> {

	public MaxEnergyGene(ScriptObject genomeScriptObject) {
		super((ScriptObject) genomeScriptObject.get("maxEnergyGene"));
	}
}
