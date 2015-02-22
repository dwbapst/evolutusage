package pl.edu.agh.evolutus.genome.gene;

import jdk.nashorn.api.scripting.ScriptObjectMirror;
import pl.edu.agh.evolutus.utils.VectorD;

public class GrowthFactorGene implements Gene<VectorD> {

	private VectorD value;

	public GrowthFactorGene(double x, double y, double z) {
		this(new VectorD(x, y, z));
	}

	public GrowthFactorGene(VectorD value) {
		this.value = value;
	}

	@Override
	public VectorD getValue() {
		return value;
	}

	public static GrowthFactorGene fromGenomeScriptObject(ScriptObjectMirror genomeScriptObject) {
		VectorD vector = VectorD.fromScriptObject((ScriptObjectMirror) genomeScriptObject.get("growthFactor"));
		return new GrowthFactorGene(vector);
	}
}
