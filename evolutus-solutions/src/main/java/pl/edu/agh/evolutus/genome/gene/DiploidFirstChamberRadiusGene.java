package pl.edu.agh.evolutus.genome.gene;

import jdk.nashorn.api.scripting.ScriptObjectMirror;

public class DiploidFirstChamberRadiusGene implements Gene<Double> {

	private Double value;

	public DiploidFirstChamberRadiusGene(Double value) {
		this.value = value;
	}

	@Override
	public Double getValue() {
		return value;
	}

	public static DiploidFirstChamberRadiusGene fromGenomeScriptObject(ScriptObjectMirror genomeScriptObject) {
		return new DiploidFirstChamberRadiusGene((Double) genomeScriptObject.get("diploidFirstChamberRadius"));
	}
}
