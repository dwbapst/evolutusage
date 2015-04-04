package pl.edu.agh.evolutus.genotype.gene;

import jdk.nashorn.internal.runtime.ScriptObject;

public class RotationAngleGene extends MutableGene<Double> {

	public RotationAngleGene(ScriptObject genomeScriptObject) {
		super((ScriptObject) genomeScriptObject.get("rotationAngle"));
	}
}
