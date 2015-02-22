package pl.edu.agh.evolutus.genome.gene;

import jdk.nashorn.api.scripting.ScriptObjectMirror;

public class MinMetabolicEffectivenessGene implements Gene<Double> {

	private Double value;

	public MinMetabolicEffectivenessGene(Double value) {
		this.value = value;
	}

	@Override
	public Double getValue() {
		return value;
	}

	public static MinMetabolicEffectivenessGene fromGenomeScriptObject(ScriptObjectMirror genomeScriptObject) {
		return new MinMetabolicEffectivenessGene((Double) genomeScriptObject.get("minMetabolicEffectiveness"));
	}
}
