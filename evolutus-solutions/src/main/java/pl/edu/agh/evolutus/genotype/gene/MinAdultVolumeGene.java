package pl.edu.agh.evolutus.genotype.gene;

import jdk.nashorn.api.scripting.ScriptObjectMirror;

public class MinAdultVolumeGene extends Gene<Double> {

	private MinAdultVolumeGene(Double value, boolean isDominant) {
		super(value, isDominant);
	}

	public static MinAdultVolumeGene fromGenomeScriptObject(ScriptObjectMirror genomeScriptObject) {
		return new MinAdultVolumeGene((Double) genomeScriptObject.get("minAdultVolume"), RAND.nextDouble() < 0.5);
	}
}
