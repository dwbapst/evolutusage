package pl.edu.agh.evolutus.genome.gene;

import jdk.nashorn.api.scripting.ScriptObjectMirror;

public class MaxEnergyGene implements Gene<Double> {

	private Double value;

	public MaxEnergyGene(Double value) {
		this.value = value;
	}

	@Override
	public Double getValue() {
		return value;
	}

	public static MaxEnergyGene fromGenomeScriptObject(ScriptObjectMirror genomeScriptObject) {
		return new MaxEnergyGene((Double) genomeScriptObject.get("maxEnergy"));
	}
}
