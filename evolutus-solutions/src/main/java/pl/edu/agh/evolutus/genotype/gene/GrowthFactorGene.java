package pl.edu.agh.evolutus.genotype.gene;

import jdk.nashorn.api.scripting.ScriptObjectMirror;
import pl.edu.agh.evolutus.utils.VectorD;

public class GrowthFactorGene extends Gene<VectorD> {

	private GrowthFactorGene(double x, double y, double z, boolean isDominant) {
		this(new VectorD(x, y, z), isDominant);
	}

	private GrowthFactorGene(VectorD value, boolean isDominant) {
		super(value, isDominant);
	}

	public static GrowthFactorGene fromGenomeScriptObject(ScriptObjectMirror genomeScriptObject) {
		VectorD vector = VectorD.fromScriptObject((ScriptObjectMirror) genomeScriptObject.get("growthFactor"));
		return new GrowthFactorGene(vector, RAND.nextDouble() < 0.5);
	}
}
