package pl.edu.agh.evolutus.genome.gene;

import jdk.nashorn.api.scripting.ScriptObjectMirror;

public class HaploidJuvenileVolumeFactorGene implements Gene<Double> {

	private Double volume;

	public HaploidJuvenileVolumeFactorGene(Double volume) {
		this.volume = volume;
	}

	@Override
	public Double getValue() {
		return volume;
	}

	public static HaploidJuvenileVolumeFactorGene fromGenomeScriptObject(ScriptObjectMirror genomeScriptObject) {
		return new HaploidJuvenileVolumeFactorGene((Double) genomeScriptObject.get("haploidJuvenileVolumeFactor"));
	}
}
