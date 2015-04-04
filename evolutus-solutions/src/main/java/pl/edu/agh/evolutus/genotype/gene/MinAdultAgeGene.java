package pl.edu.agh.evolutus.genotype.gene;

import jdk.nashorn.internal.runtime.ScriptObject;

public class MinAdultAgeGene extends MutableGene<Integer> {

	public MinAdultAgeGene(ScriptObject genomeScriptObject) {
		super((ScriptObject) genomeScriptObject.get("minAdultAge"));
	}
}
