package pl.edu.agh.evolutus.genotype.gene;

import jdk.nashorn.internal.runtime.ScriptObject;

public class TranslationFactorGene extends MutableGene<Double> {

	public TranslationFactorGene(ScriptObject genomeScriptObject) {
		super((ScriptObject) genomeScriptObject.get("translationFactorGene"));
	}
}
