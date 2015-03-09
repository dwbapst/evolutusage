package pl.edu.agh.evolutus.genotype.gene;

import jdk.nashorn.internal.runtime.ScriptObject;

public class DiploidFirstChamberRadiusGene extends FirstChamberRadiusGene {

	private DiploidFirstChamberRadiusGene(Double value, boolean isDominant) {
		super(value, isDominant);
	}

	public static DiploidFirstChamberRadiusGene fromGenomeScriptObject(ScriptObject genomeScriptObject) {
		return new DiploidFirstChamberRadiusGene((Double) genomeScriptObject.get("diploidFirstChamberRadius"),
				RAND.nextDouble() < 0.5);
	}
}
