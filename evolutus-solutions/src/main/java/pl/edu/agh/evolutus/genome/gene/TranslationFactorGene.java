package pl.edu.agh.evolutus.genome.gene;

import jdk.nashorn.api.scripting.ScriptObjectMirror;

public class TranslationFactorGene implements Gene<Double> {

	private Double value;

	public TranslationFactorGene(Double value) {
		this.value = value;
	}

	@Override
	public Double getValue() {
		return value;
	}

	public static TranslationFactorGene fromGenomeScriptObject(ScriptObjectMirror genomeScriptObject) {
		return new TranslationFactorGene((Double) genomeScriptObject.get("translationFactor"));
	}
}
