package pl.edu.agh.evolutus.genotype.gene;

import jdk.nashorn.api.scripting.ScriptObjectMirror;
import pl.edu.agh.evolutus.genotype.Ploidy;

public class PloidyGene extends Gene<Ploidy> {

	private PloidyGene(Ploidy value, boolean isDominant) {
		super(value, isDominant);
	}

	public boolean isHaploid() {
		return value == Ploidy.HAPLOID;
	}

	public boolean isDiploid() {
		return value == Ploidy.DIPLOID;
	}

	public static PloidyGene fromGenomeScriptObject(ScriptObjectMirror genomeScriptObject) {
		String ploidy = genomeScriptObject.get("ploidy").toString();
		return new PloidyGene(Ploidy.fromString(ploidy), RAND.nextDouble() < 0.5);
	}
}
