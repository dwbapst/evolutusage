package pl.edu.agh.evolutus.service.config.utils;

import java.util.Map;

import pl.edu.agh.evolutus.foram.ForamType;
import pl.edu.agh.evolutus.foram.Shell;
import pl.edu.agh.evolutus.genotype.Genotype;

public class ForamState {

	public final ForamType foramType;
	public final boolean foramActiveMotion;
	public final Map<String, Double[]> genotype;
	public final double energy;
	public final double age;
	public final Shell shell;

	public ForamState(ForamType foramType, boolean foramActiveMotion, Genotype genotype, double energy, double age, Shell shell) {
		this(foramType, foramActiveMotion, genotype.toFossilizationMap(), energy, age, shell);
	}

	public ForamState(ForamType foramType, boolean foramActiveMotion, Map<String, Double[]> genotype, double energy, double age,
			Shell shell) {
		this.foramType = foramType;
		this.foramActiveMotion = foramActiveMotion;
		this.genotype = genotype;
		this.energy = energy;
		this.age = age;
		this.shell = shell;
	}
}
