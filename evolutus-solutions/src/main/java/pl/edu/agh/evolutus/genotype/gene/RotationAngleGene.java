package pl.edu.agh.evolutus.genotype.gene;

import jdk.nashorn.api.scripting.ScriptObjectMirror;

public class RotationAngleGene extends Gene<Double> {

	private RotationAngleGene(Double value, boolean isDominant) {
		super(value, isDominant);
	}

	public static RotationAngleGene fromGenomeScriptObject(ScriptObjectMirror genomeScriptObject) {
		return new RotationAngleGene((Double) genomeScriptObject.get("rotationAngle"), RAND.nextDouble() < 0.5);
	}
}
