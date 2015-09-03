package pl.edu.agh.evolutus.cmd.generation;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;

import pl.edu.agh.evolutus.statistics.model.Simulation;
import pl.edu.agh.evolutus.utils.Utils;

public class ConfFileGenerator extends OutputFileGenerator {

	@Override
	protected String outputDirectoryName() {
		return "conf";
	}

	public void generate(Simulation simulation, File baseOutputDirectory) throws IOException, FileGeneratorException {

		File outputDirectory = getOutputDirectory(baseOutputDirectory);

		String simulationStartString = Utils.getTimestampAsString(simulation.getSimulationStart());
		File file = new File(outputDirectory, getFileName(simulationStartString));

		FileUtils.write(file, simulation.getConfig());
	}

	private String getFileName(String timePart) {
		return "config-" + timePart + ".js";
	}

}

