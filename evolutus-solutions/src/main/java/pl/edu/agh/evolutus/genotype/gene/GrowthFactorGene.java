package pl.edu.agh.evolutus.genotype.gene;

import jdk.nashorn.internal.runtime.ScriptObject;
import pl.edu.agh.evolutus.utils.VectorD;

public class GrowthFactorGene extends Gene<VectorD> {

	public GrowthFactorGene(ScriptObject genomeScriptObject) {
		super((ScriptObject) genomeScriptObject.get("growthFactor"));
	}

	@Override
	public void validate() throws GeneValidationException {
	}
}
