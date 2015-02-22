package pl.edu.agh.evolutus.genome.gene;

import jdk.nashorn.api.scripting.ScriptObjectMirror;

public class MinAdultVolumeGene implements Gene<Double> {

	private Double value;

	public MinAdultVolumeGene(Double value) {
		this.value = value;
	}

	@Override
	public Double getValue() {
		return value;
	}

	public static MinAdultVolumeGene fromGenomeScriptObject(ScriptObjectMirror genomeScriptObject) {
		return new MinAdultVolumeGene((Double) genomeScriptObject.get("minAdultVolume"));
	}
}
