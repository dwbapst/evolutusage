package pl.edu.agh.evolutus.genotype.gene;

import jdk.nashorn.internal.runtime.ScriptObject;

public class HaploidJuvenileVolumeFactorGene extends JuvenileVolumeFactorGene {

	private HaploidJuvenileVolumeFactorGene(Double value, boolean isDominant) {
		super(value, isDominant);
	}

	public static HaploidJuvenileVolumeFactorGene fromGenomeScriptObject(ScriptObject genomeScriptObject) {
		return new HaploidJuvenileVolumeFactorGene((Double) genomeScriptObject.get("haploidJuvenileVolumeFactor"),
				RAND.nextDouble() < 0.5);
	}
}
