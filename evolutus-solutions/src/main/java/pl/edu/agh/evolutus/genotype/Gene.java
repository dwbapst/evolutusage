package pl.edu.agh.evolutus.genotype;

import java.util.Random;

import jdk.nashorn.internal.runtime.ScriptObject;

public class Gene {

	private static final Random RAND = new Random();

	private final String name;
	private final Double value;
	private final Double minValue;
	private final Double maxValue;
	private final Double mutationFactor; // FIXME: mutationProbability?

	private final boolean isDominant;

	public Gene(String name, Number value, Number minValue, Number maxValue, Number mutationFactor) {
		this.name = name;
		this.value = value.doubleValue();
		this.minValue = minValue.doubleValue();
		this.maxValue = maxValue.doubleValue();
		this.mutationFactor = mutationFactor.doubleValue();
		this.isDominant = RAND.nextBoolean();
	}

	public Gene(ScriptObject geneScriptObject) {
		this(
				getValue(geneScriptObject, "name", null, true),
				getValue(geneScriptObject, "value", null, true),
				getValue(geneScriptObject, "minValue", Double.MIN_VALUE, false),
				getValue(geneScriptObject, "maxValue", Double.MAX_VALUE, false),
				getValue(geneScriptObject, "mutationFactor", 0.0, false)
		);
	}

	@SuppressWarnings("unchecked")
	private static <T> T getValue(ScriptObject scriptObject, String key, T defaultValue, boolean required) {
		if (scriptObject.containsKey(key)) {
			return (T) scriptObject.get(key);
		} else if (required) {
			throw new IllegalStateException(String.format("Gene '%s' property is required.", key));
		} else {
			return defaultValue;
		}
	}

	public String getName() {
		return name;
	}

	public Double getValue() {
		return value;
	}

	public Double getMinValue() {
		return minValue;
	}

	public Double getMaxValue() {
		return maxValue;
	}

	public Double getMutationFactor() {
		return mutationFactor;
	}

	public boolean isDominant() {
		return isDominant;
	}

	public boolean isRecessive() {
		return !isDominant;
	}

	public void validate() throws GeneValidationException {
		if (minValue > value) {
			throw new GeneValidationException(value, "less", minValue);
		}
		if (maxValue < value) {
			throw new GeneValidationException(value, "greater", maxValue);
		}
	}

	public static class GeneValidationException extends Exception {
		public GeneValidationException(Object value, String shouldBe, Object bound) {
			super(String.format("Gene value is not valid. %s should be %s than %s.", value, shouldBe, bound));
		}
	}
}
