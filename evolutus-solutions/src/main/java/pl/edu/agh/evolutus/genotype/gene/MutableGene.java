package pl.edu.agh.evolutus.genotype.gene;

import jdk.nashorn.internal.runtime.ScriptObject;

public abstract class MutableGene<V extends Comparable<V>> extends Gene<V> {

	protected final V minValue;
	protected final V maxValue;
	protected final double mutationRate;

	public MutableGene(V value, V minValue, V maxValue, double mutationRate) {
		super(value);
		this.minValue = minValue;
		this.maxValue = maxValue;
		this.mutationRate = mutationRate;
	}

	@SuppressWarnings("unchecked")
	public MutableGene(ScriptObject geneScriptObject) {
		super(geneScriptObject);
		this.minValue = getValue(geneScriptObject, "minValue", null);
		this.maxValue = getValue(geneScriptObject, "maxValue", null);
		this.mutationRate = getValue(geneScriptObject, "mutationRate", 0.0);
	}

	public double getMutationRate() {
		return mutationRate;
	}

	public void validate() throws GeneValidationException {
		if (minValue != null && greater(minValue, value)) {
			throw new GeneValidationException(validationMessage(value, "less", minValue));
		}
		if (maxValue != null && less(maxValue, value)) {
			throw new GeneValidationException(validationMessage(value, "greater", maxValue));
		}

		validateSpecific();
	}

	protected void validateSpecific() throws GeneValidationException {
	}

	private boolean less(Comparable<V> c1, V c2) {
		return c1.compareTo(c2) < 0;
	}

	private boolean greater(Comparable<V> c1, V c2) {
		return c1.compareTo(c2) > 0;
	}

	public String validationMessage(Object value, String shouldBe, Object bound) {
		return String.format("Gene value is not valid. %s should be %s than %s.", value, shouldBe, bound);
	}
}
