package pl.edu.agh.evolutus.genotype.gene;

import jdk.nashorn.internal.runtime.ScriptObject;

public class HaploidFirstChamberRadiusGene extends FirstChamberRadiusGene {

	public HaploidFirstChamberRadiusGene(ScriptObject genomeScriptObject) {
		super((ScriptObject) genomeScriptObject.get("haploidFirstChamberRadiusGene"));
	}
}
