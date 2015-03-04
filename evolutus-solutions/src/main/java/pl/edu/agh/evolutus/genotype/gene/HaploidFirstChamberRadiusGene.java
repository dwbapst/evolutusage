package pl.edu.agh.evolutus.genotype.gene;

import jdk.nashorn.api.scripting.ScriptObjectMirror;

public class HaploidFirstChamberRadiusGene extends FirstChamberRadiusGene {

	private HaploidFirstChamberRadiusGene(Double value, boolean isDominant) {
		super(value, isDominant);
	}

	public static HaploidFirstChamberRadiusGene fromGenomeScriptObject(ScriptObjectMirror genomeScriptObject) {
		return new HaploidFirstChamberRadiusGene((Double) genomeScriptObject.get("haploidFirstChamberRadius"),
				RAND.nextDouble() < 0.5);
	}
}
