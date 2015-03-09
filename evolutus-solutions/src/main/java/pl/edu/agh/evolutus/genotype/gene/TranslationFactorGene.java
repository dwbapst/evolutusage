package pl.edu.agh.evolutus.genotype.gene;

import jdk.nashorn.internal.runtime.ScriptObject;

public class TranslationFactorGene extends Gene<Double> {

	private TranslationFactorGene(Double value, boolean isDominant) {
		super(value, isDominant);
	}

	public static TranslationFactorGene fromGenomeScriptObject(ScriptObject genomeScriptObject) {
		return new TranslationFactorGene((Double) genomeScriptObject.get("translationFactor"), RAND.nextDouble() < 0.5);
	}
}
