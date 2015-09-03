package pl.edu.agh.evolutus.cmd.generation;

import java.io.File;

import pl.edu.agh.evolutus.cmd.parser.GenerationMode;

public class GenerationConfig {

	private final GenerationMode generationMode;
	private final Long simulationStart;
	private final File outputDir;
	private final String[] genesList;

	public GenerationConfig(GenerationMode generationMode, Long simulationStart, File outputDir, String[] genesList) {
		this.generationMode = generationMode;
		this.simulationStart = simulationStart;
		this.outputDir = outputDir;
		this.genesList = genesList;
	}

	public GenerationMode getGenerationMode() {
		return generationMode;
	}

	public Long getSimulationStart() {
		return simulationStart;
	}

	public boolean isSimulationStartDefined() {
		return simulationStart != null;
	}

	public File getOutputDir() {
		return outputDir;
	}

	public String[] getGenesList() {
		return genesList;
	}
}
