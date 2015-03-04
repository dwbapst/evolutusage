package pl.edu.agh.evolutus.genotype.gene;

import jdk.nashorn.api.scripting.ScriptObjectMirror;

public class MinEnergyGene extends Gene<Double> {

	private MinEnergyGene(Double value, boolean isDominant) {
		super(value, isDominant);
	}

	public static MinEnergyGene fromGenomeScriptObject(ScriptObjectMirror genomeScriptObject) {
		return new MinEnergyGene((Double) genomeScriptObject.get("minEnergy"), RAND.nextDouble() < 0.5);
	}
}
