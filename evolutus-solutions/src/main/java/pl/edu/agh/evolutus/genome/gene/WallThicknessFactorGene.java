package pl.edu.agh.evolutus.genome.gene;

import jdk.nashorn.api.scripting.ScriptObjectMirror;

public class WallThicknessFactorGene implements Gene<Double> {

	private Double value;

	public WallThicknessFactorGene(Double value) {
		this.value = value;
	}

	@Override
	public Double getValue() {
		return value;
	}

	public static WallThicknessFactorGene fromGenomeScriptObject(ScriptObjectMirror genomeScriptObject) {
		return new WallThicknessFactorGene((Double) genomeScriptObject.get("wallThicknessFactor"));
	}
}
