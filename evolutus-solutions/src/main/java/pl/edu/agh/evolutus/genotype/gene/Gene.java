package pl.edu.agh.evolutus.genotype.gene;

import java.util.Random;

import jdk.nashorn.internal.runtime.ScriptObject;

public abstract class Gene<V> {

	protected static final Random RAND = new Random();

	protected final V value;
	protected final boolean isDominant;

	protected Gene(V value) {
		this.value = value;
		this.isDominant = RAND.nextBoolean();
	}

	@SuppressWarnings("unchecked")
	protected Gene(ScriptObject geneScriptObject) {
		this(getValue(geneScriptObject, "value", (V) null));
	}

	@SuppressWarnings("unchecked")
	protected static <T> T getValue(ScriptObject scriptObject, String key, T defaultValue) {
		if (scriptObject.containsKey(key)) {
			return (T) scriptObject.get(key);
		} else {
			return defaultValue;
		}
	}

	public V getValue() {
		return value;
	}

	public boolean isDominant() {
		return isDominant;
	}

	public boolean isRecessive() {
		return !isDominant;
	}

	public abstract void validate() throws GeneValidationException;

	public static class GeneValidationException extends Exception {
		public GeneValidationException(String message) {
			super(message);
		}
	}
}
