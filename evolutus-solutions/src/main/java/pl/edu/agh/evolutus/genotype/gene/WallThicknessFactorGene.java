package pl.edu.agh.evolutus.genotype.gene;

import jdk.nashorn.internal.runtime.ScriptObject;

public class WallThicknessFactorGene extends MutableGene<Double> {

	public WallThicknessFactorGene(ScriptObject genomeScriptObject) {
		super((ScriptObject) genomeScriptObject.get("wallThicknessFactorGene"));
	}
}
