package pl.edu.agh.evolutus.genome.gene;

import jdk.nashorn.api.scripting.ScriptObjectMirror;

public class DeviationAngleGene implements Gene<Double> {

	private Double value;

	public DeviationAngleGene(Double value) {
		this.value = value;
	}

	@Override
	public Double getValue() {
		return value;
	}

	public static DeviationAngleGene fromGenomeScriptObject(ScriptObjectMirror genomeScriptObject) {
		return new DeviationAngleGene((Double) genomeScriptObject.get("deviationAngle"));
	}
}
