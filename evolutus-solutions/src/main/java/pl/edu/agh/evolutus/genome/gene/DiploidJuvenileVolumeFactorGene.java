package pl.edu.agh.evolutus.genome.gene;

import jdk.nashorn.api.scripting.ScriptObjectMirror;

public class DiploidJuvenileVolumeFactorGene implements Gene<Double> {

	private Double volume;

	public DiploidJuvenileVolumeFactorGene(Double volume) {
		this.volume = volume;
	}

	@Override
	public Double getValue() {
		return volume;
	}

	public static DiploidJuvenileVolumeFactorGene fromGenomeScriptObject(ScriptObjectMirror genomeScriptObject) {
		return new DiploidJuvenileVolumeFactorGene((Double) genomeScriptObject.get("diploidJuvenileVolumeFactor"));
	}
}
