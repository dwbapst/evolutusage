package pl.edu.agh.evolutus.genome.gene;

import jdk.nashorn.api.scripting.ScriptObjectMirror;

public class MetabolicEffectivenessGene implements Gene<Double> {

	private Double value;

	public MetabolicEffectivenessGene(Double value) {
		this.value = value;
	}

	@Override
	public Double getValue() {
		return value;
	}

	public static MetabolicEffectivenessGene fromGenomeScriptObject(ScriptObjectMirror genomeScriptObject) {
		return new MetabolicEffectivenessGene((Double) genomeScriptObject.get("metabolicEffectiveness"));
	}
}
