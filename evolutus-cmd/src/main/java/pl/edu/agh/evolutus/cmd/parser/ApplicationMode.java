package pl.edu.agh.evolutus.cmd.parser;

import org.apache.commons.lang3.StringUtils;

import pl.edu.agh.evolutus.cmd.ApplicationException;

public enum ApplicationMode {
	SIMULATION, LIST, GENERATION;

	public static ApplicationMode fromString(String mode) throws ApplicationException {
		switch (mode.toLowerCase()) {
		case "simulation":
			return SIMULATION;
		case "sim":
			return SIMULATION;
		case "list":
			return LIST;
		case "generation":
			return GENERATION;
		case "gen":
			return GENERATION;
		default:
			throw new ApplicationException("Wrong application mode: " + mode + ". Supported modes: " +
					StringUtils.join(values(), ", "));
		}
	}
}
