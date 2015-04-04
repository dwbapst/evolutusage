package pl.edu.agh.evolutus.genotype.gene;

import jdk.nashorn.internal.runtime.ScriptObject;

public class MinEnergyGene extends MutableGene<Double> {

	public MinEnergyGene(ScriptObject genomeScriptObject) {
		super((ScriptObject) genomeScriptObject.get("minEnergy"));
	}
}
