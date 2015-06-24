package pl.edu.agh.evolutus.service.config.utils;

import java.util.Map;

import pl.edu.agh.evolutus.foram.Shell;
import pl.edu.agh.evolutus.genotype.Genotype;

public class ForamState {

	public final boolean isBenthic;
	public final Map<String, Double[]> genotype;
	public final double energy;
	public final double age;
	public final Shell shell;

	public ForamState(boolean isBenthic, Genotype genotype, double energy, double age, Shell shell) {
		this(isBenthic, genotype.toFossilizationMap(), energy, age, shell);
	}

	public ForamState(boolean isBenthic, Map<String, Double[]> genotype, double energy, double age, Shell shell) {
		this.isBenthic = isBenthic;
		this.genotype = genotype;
		this.energy = energy;
		this.age = age;
		this.shell = shell;
	}
}
