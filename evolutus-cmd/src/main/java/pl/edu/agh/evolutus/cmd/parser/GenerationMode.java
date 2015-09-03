package pl.edu.agh.evolutus.cmd.parser;

import java.util.Arrays;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;

import pl.edu.agh.evolutus.cmd.ApplicationException;

public enum GenerationMode {
	POPULATION_CHART("population-chart"), //
	ENERGY_CHART("energy-chart"), //
	BORN_DEAD_CHART("born-dead-chart"), //
	PSI("psi"), //
	CSV("csv"), //
	CONFIG("config"), //
	GENES_EVOLUTION("genes-evolution"); //

	private final String displayName;

	GenerationMode(String displayName) {
		this.displayName = displayName;
	}

	public String getDisplayName() {
		return displayName;
	}

	public static GenerationMode fromString(String mode) throws ApplicationException {
		mode = mode.toLowerCase();
		for (GenerationMode generationMode : values()) {
			if (generationMode.displayName.equals(mode)) {
				return generationMode;
			}
		}

		throw new ApplicationException("Wrong generation mode: " + mode + ". Supported modes: " +
				StringUtils.join(displayNames(), ", "));
	}

	public static String[] displayNames() {
		return Arrays.asList(values())
				.stream()
				.map(generationMode -> generationMode.displayName)
				.collect(Collectors.toList())
				.toArray(new String[values().length]);
	}
}
