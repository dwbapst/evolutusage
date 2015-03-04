package pl.edu.agh.evolutus.genotype.gene;

import jdk.nashorn.api.scripting.ScriptObjectMirror;

public class DiploidJuvenileVolumeFactorGene extends JuvenileVolumeFactorGene {

	private DiploidJuvenileVolumeFactorGene(Double value, boolean isDominant) {
		super(value, isDominant);
	}

	public static DiploidJuvenileVolumeFactorGene fromGenomeScriptObject(ScriptObjectMirror genomeScriptObject) {
		return new DiploidJuvenileVolumeFactorGene((Double) genomeScriptObject.get("diploidJuvenileVolumeFactor"),
				RAND.nextDouble() < 0.5);
	}
}
