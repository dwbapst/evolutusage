package pl.edu.agh.evolutus.genotype.gene;

import jdk.nashorn.internal.runtime.ScriptObject;

public class HaploidJuvenileVolumeFactorGene extends JuvenileVolumeFactorGene {

	public HaploidJuvenileVolumeFactorGene(ScriptObject genomeScriptObject) {
		super((ScriptObject) genomeScriptObject.get("haploidJuvenileVolumeFactor"));
	}
}
