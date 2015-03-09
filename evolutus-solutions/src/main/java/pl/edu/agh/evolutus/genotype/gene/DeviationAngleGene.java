package pl.edu.agh.evolutus.genotype.gene;

import jdk.nashorn.internal.runtime.ScriptObject;

public class DeviationAngleGene extends Gene<Double> {

	private DeviationAngleGene(Double value, boolean isDominant) {
		super(value, isDominant);
	}

	public static DeviationAngleGene fromGenomeScriptObject(ScriptObject genomeScriptObject) {
		return new DeviationAngleGene((Double) genomeScriptObject.get("deviationAngle"), RAND.nextDouble() < 0.5);
	}
}
