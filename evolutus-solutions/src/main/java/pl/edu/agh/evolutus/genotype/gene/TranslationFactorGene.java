package pl.edu.agh.evolutus.genotype.gene;

import jdk.nashorn.api.scripting.ScriptObjectMirror;

public class TranslationFactorGene extends Gene<Double> {

	private TranslationFactorGene(Double value, boolean isDominant) {
		super(value, isDominant);
	}

	public static TranslationFactorGene fromGenomeScriptObject(ScriptObjectMirror genomeScriptObject) {
		return new TranslationFactorGene((Double) genomeScriptObject.get("translationFactor"), RAND.nextDouble() < 0.5);
	}
}
