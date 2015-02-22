package pl.edu.agh.evolutus.genome.gene;

import jdk.nashorn.api.scripting.ScriptObjectMirror;

public class MinEnergyGene implements Gene<Double> {

	private Double value;

	public MinEnergyGene(Double value) {
		this.value = value;
	}

	@Override
	public Double getValue() {
		return value;
	}

	public static MinEnergyGene fromGenomeScriptObject(ScriptObjectMirror genomeScriptObject) {
		return new MinEnergyGene((Double) genomeScriptObject.get("minEnergy"));
	}
}
