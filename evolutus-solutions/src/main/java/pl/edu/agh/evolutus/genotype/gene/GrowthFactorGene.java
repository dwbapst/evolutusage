package pl.edu.agh.evolutus.genotype.gene;

import jdk.nashorn.internal.runtime.ScriptObject;
import pl.edu.agh.evolutus.utils.VectorD;

public class GrowthFactorGene extends Gene<VectorD> {

	private GrowthFactorGene(double x, double y, double z, boolean isDominant) {
		this(new VectorD(x, y, z), isDominant);
	}

	private GrowthFactorGene(VectorD value, boolean isDominant) {
		super(value, isDominant);
	}

	public static GrowthFactorGene fromGenomeScriptObject(ScriptObject genomeScriptObject) {
		VectorD vector = VectorD.fromScriptObject((ScriptObject) genomeScriptObject.get("growthFactor"));
		return new GrowthFactorGene(vector, RAND.nextDouble() < 0.5);
	}
}
