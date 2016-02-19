package pl.edu.agh.evolutus.genotype;

import jdk.nashorn.internal.runtime.ScriptObject;

import java.util.Random;

public class Gene {

	private static final Random RAND = new Random();

	private final String name;
	private final Double value;
	private final Double minValue;
	private final Double maxValue;
	private final Double mutationRate;
	private final Double mutationProbability;

	private final boolean isDominant;

	public Gene(String name, Number value, Number minValue, Number maxValue, Number mutationRate, Number mutationProbability) {
		this.name = name;
		this.value = value.doubleValue();
		this.minValue = minValue.doubleValue();
		this.maxValue = maxValue.doubleValue();
		this.mutationRate = mutationRate.doubleValue();
		this.mutationProbability = mutationProbability.doubleValue();
		this.isDominant = RAND.nextBoolean();
	}

	public Gene(ScriptObject geneScriptObject) {
		this(
				getValue(geneScriptObject, "name", null, true),
				getValue(geneScriptObject, "value", null, true),
				getValue(geneScriptObject, "minValue", Double.NEGATIVE_INFINITY, false),
				getValue(geneScriptObject, "maxValue", Double.POSITIVE_INFINITY, false),
				getValue(geneScriptObject, "mutationRate", 0.0, false),
				getValue(geneScriptObject, "mutationProbability", Double.NaN, false)
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

	public Double getMutationRate() {
		return mutationRate;
	}

	public Double getMutationProbability() {
		return mutationProbability;
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

	public boolean isValid() {
		return value >= minValue && value <= maxValue;
	}

	public Gene mutate(double globalMutationProbability) {
		if (shouldMutate(globalMutationProbability)) {
			//double mutationFactor = RAND.nextBoolean() ? (1 + mutationRate) : (1 - mutationRate);
            double mutationFactor = RAND.nextGaussian() * mutationRate;
			double newValue = value + mutationFactor; //was *
			return new Gene(name, newValue, minValue, maxValue, mutationRate, mutationProbability);
		}
		return this;
	}

	private boolean shouldMutate(double globalMutationProbability) {
		double probability = mutationProbability.isNaN() ? globalMutationProbability : mutationProbability;
		return RAND.nextDouble() < probability;
	}

	public static class GeneValidationException extends Exception {
		public GeneValidationException(Object value, String shouldBe, Object bound) {
			super(String.format("Gene value is not valid. %s should be %s than %s.", value, shouldBe, bound));
		}
	}
}
