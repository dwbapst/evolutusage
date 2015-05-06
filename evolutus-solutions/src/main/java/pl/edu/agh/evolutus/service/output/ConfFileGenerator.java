package pl.edu.agh.evolutus.service.output;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import pl.edu.agh.evolutus.service.config.ConfigFactory;
import pl.edu.agh.evolutus.statistics.model.OceanFragmentInfo;
import pl.edu.agh.evolutus.statistics.model.Simulation;
import pl.edu.agh.evolutus.utils.Utils;

public class ConfFileGenerator extends OutputFileGenerator {

	private static final Logger log = LoggerFactory.getLogger(ConfFileGenerator.class);

	@Inject
	private ConfigFactory configFactory;

	@Override
	protected String outputDirectoryName() {
		return "conf";
	}

	@Override
	protected void generateInner(Simulation simulation, File outputDirectory, Map<Long, List<OceanFragmentInfo>> infoMap)
			throws IOException, FileGeneratorException {

		String simulationStartString = Utils.getTimestampAsString(simulation.getSimulationStart());
		File file = new File(outputDirectory, getFileName(simulationStartString));

		String configAsString = configFactory.getConfigAsString();
		FileUtils.write(file, configAsString);
		log.info("Saved config file in {}", outputDirectory.getAbsolutePath());
	}

	private String getFileName(String timePart) {
		return "config-" + timePart + ".js";
	}

}

