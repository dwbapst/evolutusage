package pl.edu.agh.evolutus.cmd.parser;

import static java.util.Arrays.*;

import java.io.File;
import java.util.Optional;
import java.util.stream.Collectors;

import pl.edu.agh.evolutus.cmd.ApplicationException;

public class ArgsParserResult {

	private ApplicationMode applicationMode;
	private GenerationMode generationMode;
	private File[] configFiles;
	private File outputDir;
	private File dbPropertiesFile;
	private Long simulationStart;
	private String[] genesList;

	public ArgsParserResult(ApplicationMode applicationMode, String... args) throws ApplicationException {
		this(applicationMode, null, args);
	}

	public ArgsParserResult(ApplicationMode applicationMode, GenerationMode generationMode, String... args)
			throws ApplicationException {
		this.applicationMode = applicationMode;
		this.generationMode = generationMode;

		switch (applicationMode) {
		case SIMULATION:
			dbPropertiesFile = new File(args[0]);
			configFiles = asList(args)
					.subList(1, args.length)
					.stream()
					.map(File::new)
					.collect(Collectors.toList())
					.toArray(new File[args.length - 1]);
			break;
		case LIST:
			dbPropertiesFile = new File(args[0]);
			break;
		case GENERATION:
			dbPropertiesFile = new File(args[0]);
			outputDir = new File(args[1]);
			args = ArgsParser.subArray(args, 2);
			if (generationMode == GenerationMode.GENES_EVOLUTION) {
				processGenesEvolutionParams(args);
			} else {
				simulationStart = (args.length > 0) ? parseSimulationStart(args[0]) : null;
			}
		}
	}

	private void processGenesEvolutionParams(String[] args) throws ApplicationException {
		if (args.length == 0) {
			throw new ApplicationException("Too few parameters for generation mode: "
					+ GenerationMode.GENES_EVOLUTION.getDisplayName() + ". Required at least one gene name.");
		}

		simulationStart = tryParseSimulationStart(args[0]).orElse(null);
		if (simulationStart != null) {
			args = ArgsParser.subArray(args, 1);
		}
		genesList = args;
	}

	private long parseSimulationStart(String simulationStart) throws ApplicationException {
		Optional<Long> optional = tryParseSimulationStart(simulationStart);
		if (optional.isPresent()) {
			return optional.get();
		} else {
			throw new ApplicationException("Wrong format of <simulation> parameter. Expected integer number. Value: " +
					simulationStart + ".");
		}
	}

	private Optional<Long> tryParseSimulationStart(String simulationStart) {
		try {
			return Optional.ofNullable(
					Long.parseLong(simulationStart)
			);
		} catch (NumberFormatException e) {
			return Optional.<Long>empty();
		}
	}

	public ApplicationMode getApplicationMode() {
		return applicationMode;
	}

	public GenerationMode getGenerationMode() {
		return generationMode;
	}

	public File[] getConfigFiles() {
		return configFiles;
	}

	public File getOutputDir() {
		return outputDir;
	}

	public File getDbPropertiesFile() {
		return dbPropertiesFile;
	}

	public Long getSimulationStart() {
		return simulationStart;
	}

	public String[] getGenesList() {
		return genesList;
	}
}
