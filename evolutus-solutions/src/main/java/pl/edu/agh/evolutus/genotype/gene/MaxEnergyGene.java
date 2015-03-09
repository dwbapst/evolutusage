package pl.edu.agh.evolutus.genotype.gene;

import jdk.nashorn.internal.runtime.ScriptObject;

public class MaxEnergyGene extends Gene<Double> {

	private MaxEnergyGene(Double value, boolean isDominant) {
		super(value, isDominant);
	}

	public static MaxEnergyGene fromGenomeScriptObject(ScriptObject genomeScriptObject) {
		return new MaxEnergyGene((Double) genomeScriptObject.get("maxEnergy"), RAND.nextDouble() < 0.5);
	}
}
