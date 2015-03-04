package pl.edu.agh.evolutus.genotype.gene;

import jdk.nashorn.api.scripting.ScriptObjectMirror;

public class DeviationAngleGene extends Gene<Double> {

	private DeviationAngleGene(Double value, boolean isDominant) {
		super(value, isDominant);
	}

	public static DeviationAngleGene fromGenomeScriptObject(ScriptObjectMirror genomeScriptObject) {
		return new DeviationAngleGene((Double) genomeScriptObject.get("deviationAngle"), RAND.nextDouble() < 0.5);
	}
}
