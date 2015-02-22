package pl.edu.agh.evolutus.genome.gene;

import jdk.nashorn.api.scripting.ScriptObjectMirror;

public class HaploidFirstChamberRadiusGene implements Gene<Double> {

	private Double value;

	public HaploidFirstChamberRadiusGene(Double value) {
		this.value = value;
	}

	@Override
	public Double getValue() {
		return value;
	}

	public static HaploidFirstChamberRadiusGene fromGenomeScriptObject(ScriptObjectMirror genomeScriptObject) {
		return new HaploidFirstChamberRadiusGene((Double) genomeScriptObject.get("haploidFirstChamberRadius"));
	}
}
