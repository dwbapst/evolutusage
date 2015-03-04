package pl.edu.agh.evolutus.genotype.gene;

import java.util.Random;

public abstract class Gene<V> {

	protected static final Random RAND = new Random();

	protected final V value;

	protected final boolean isDominant;

	protected Gene(V value, boolean isDominant) {
		this.value = value;
		this.isDominant = isDominant;
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
}
