package pl.edu.agh.evolutus.genotype.gene;

import jdk.nashorn.internal.runtime.ScriptObject;

public class DeviationAngleGene extends MutableGene<Double> {

	public DeviationAngleGene(ScriptObject genomeScriptObject) {
		super((ScriptObject) genomeScriptObject.get("deviationAngleGene"));
	}
}
