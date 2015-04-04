package pl.edu.agh.evolutus.genotype.gene;

import jdk.nashorn.internal.runtime.ScriptObject;

public class MinAdultVolumeGene extends MutableGene<Double> {

	public MinAdultVolumeGene(ScriptObject genomeScriptObject) {
		super((ScriptObject) genomeScriptObject.get("minAdultVolume"));
	}
}
