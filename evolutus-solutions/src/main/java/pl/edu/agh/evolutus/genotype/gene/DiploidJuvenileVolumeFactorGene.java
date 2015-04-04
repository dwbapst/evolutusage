package pl.edu.agh.evolutus.genotype.gene;

import jdk.nashorn.internal.runtime.ScriptObject;

public class DiploidJuvenileVolumeFactorGene extends JuvenileVolumeFactorGene {

	public DiploidJuvenileVolumeFactorGene(ScriptObject genomeScriptObject) {
		super((ScriptObject) genomeScriptObject.get("diploidJuvenileVolumeFactor"));
	}
}
