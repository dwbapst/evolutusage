package pl.edu.agh.evolutus.genotype.gene;

import jdk.nashorn.internal.runtime.ScriptObject;

public class WallThicknessFactorGene extends Gene<Double> {

	private WallThicknessFactorGene(Double value, boolean isDominant) {
		super(value, isDominant);
	}

	public static WallThicknessFactorGene fromGenomeScriptObject(ScriptObject genomeScriptObject) {
		return new WallThicknessFactorGene((Double) genomeScriptObject.get("wallThicknessFactor"), RAND.nextDouble() < 0.5);
	}
}
