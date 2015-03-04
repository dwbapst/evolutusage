package pl.edu.agh.evolutus.genotype.gene;

import jdk.nashorn.api.scripting.ScriptObjectMirror;

public class WallThicknessFactorGene extends Gene<Double> {

	private WallThicknessFactorGene(Double value, boolean isDominant) {
		super(value, isDominant);
	}

	public static WallThicknessFactorGene fromGenomeScriptObject(ScriptObjectMirror genomeScriptObject) {
		return new WallThicknessFactorGene((Double) genomeScriptObject.get("wallThicknessFactor"), RAND.nextDouble() < 0.5);
	}
}
