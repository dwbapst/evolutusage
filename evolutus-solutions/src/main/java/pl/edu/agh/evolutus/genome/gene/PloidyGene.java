package pl.edu.agh.evolutus.genome.gene;

import jdk.nashorn.api.scripting.ScriptObjectMirror;
import pl.edu.agh.evolutus.genome.Ploidy;

public class PloidyGene implements Gene<Ploidy> {

	private Ploidy value;

	public PloidyGene(Ploidy value) {
		this.value = value;
	}

	@Override
	public Ploidy getValue() {
		return value;
	}

	public boolean isHaploid() {
		return value == Ploidy.HAPLOID;
	}

	public boolean isDiploid() {
		return value == Ploidy.DIPLOID;
	}

	public static PloidyGene fromGenomeScriptObject(ScriptObjectMirror genomeScriptObject) {
		String ploidy = genomeScriptObject.get("ploidy").toString();
		return new PloidyGene(Ploidy.fromString(ploidy));
	}
}
