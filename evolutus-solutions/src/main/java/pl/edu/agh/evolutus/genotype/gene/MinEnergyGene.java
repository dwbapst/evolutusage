package pl.edu.agh.evolutus.genotype.gene;

import jdk.nashorn.internal.runtime.ScriptObject;

public class MinEnergyGene extends Gene<Double> {

	private MinEnergyGene(Double value, boolean isDominant) {
		super(value, isDominant);
	}

	public static MinEnergyGene fromGenomeScriptObject(ScriptObject genomeScriptObject) {
		return new MinEnergyGene((Double) genomeScriptObject.get("minEnergy"), RAND.nextDouble() < 0.5);
	}
}
