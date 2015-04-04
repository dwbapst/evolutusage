package pl.edu.agh.evolutus.genotype.gene;

import jdk.nashorn.internal.runtime.ScriptObject;
import pl.edu.agh.evolutus.genotype.Ploidy;

public class PloidyGene extends Gene<Ploidy> {

	public PloidyGene(ScriptObject genomeScriptObject) {
		super(Ploidy.fromString(getValue(genomeScriptObject, "ploidy", "diploid")));
	}

	@Override
	public void validate() throws GeneValidationException {
	}

	public boolean isHaploid() {
		return value == Ploidy.HAPLOID;
	}

	public boolean isDiploid() {
		return value == Ploidy.DIPLOID;
	}
}
