package pl.edu.agh.evolutus.genotype.gene;

import jdk.nashorn.api.scripting.ScriptObjectMirror;

public class MaxEnergyGene extends Gene<Double> {

	private MaxEnergyGene(Double value, boolean isDominant) {
		super(value, isDominant);
	}

	public static MaxEnergyGene fromGenomeScriptObject(ScriptObjectMirror genomeScriptObject) {
		return new MaxEnergyGene((Double) genomeScriptObject.get("maxEnergy"), RAND.nextDouble() < 0.5);
	}
}
