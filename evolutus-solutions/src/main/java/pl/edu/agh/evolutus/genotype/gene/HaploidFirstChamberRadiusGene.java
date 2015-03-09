package pl.edu.agh.evolutus.genotype.gene;

import jdk.nashorn.internal.runtime.ScriptObject;

public class HaploidFirstChamberRadiusGene extends FirstChamberRadiusGene {

	private HaploidFirstChamberRadiusGene(Double value, boolean isDominant) {
		super(value, isDominant);
	}

	public static HaploidFirstChamberRadiusGene fromGenomeScriptObject(ScriptObject genomeScriptObject) {
		return new HaploidFirstChamberRadiusGene((Double) genomeScriptObject.get("haploidFirstChamberRadius"),
				RAND.nextDouble() < 0.5);
	}
}
