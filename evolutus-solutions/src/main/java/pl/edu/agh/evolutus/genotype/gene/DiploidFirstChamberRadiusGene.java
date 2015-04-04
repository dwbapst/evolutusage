package pl.edu.agh.evolutus.genotype.gene;

import jdk.nashorn.internal.runtime.ScriptObject;

public class DiploidFirstChamberRadiusGene extends FirstChamberRadiusGene {

	public DiploidFirstChamberRadiusGene(ScriptObject genomeScriptObject) {
		super((ScriptObject) genomeScriptObject.get("diploidFirstChamberRadiusGene"));
	}
}
