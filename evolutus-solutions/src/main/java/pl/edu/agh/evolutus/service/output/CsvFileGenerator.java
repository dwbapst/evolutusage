package pl.edu.agh.evolutus.service.output;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import pl.edu.agh.evolutus.service.TemplateRenderer;
import pl.edu.agh.evolutus.statistics.model.OceanFragmentInfo;
import pl.edu.agh.evolutus.statistics.model.Simulation;
import pl.edu.agh.evolutus.utils.Utils;

public class CsvFileGenerator extends OutputFileGenerator {

	private static final Logger log = LoggerFactory.getLogger(CsvFileGenerator.class);

	@Inject
	private TemplateRenderer templateRenderer;

	@Override
	protected String outputDirectoryName() {
		return "csv";
	}

	@Override
	protected void generateInner(Simulation simulation, File outputDirectory, Map<Long, List<OceanFragmentInfo>> infoMap)
			throws IOException {

		String simulationStartString = Utils.getTimestampAsString(simulation.getSimulationStart());

		List<Stats> statsList = infoMapToStatsList(infoMap);
		File csvFile = new File(outputDirectory, getCsvFileName(simulationStartString));
		templateRenderer.render("templates/foramsCSV.vm", csvFile, Utils.immutableMap("stats", statsList));
		log.info("Saved CSV file in {}", outputDirectory.getAbsolutePath());
	}

	private String getCsvFileName(String timePart) {
		return "foramsCSV-" + timePart + ".csv";
	}

}

