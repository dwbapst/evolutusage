package pl.edu.agh.evolutus.service.output;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import pl.edu.agh.evolutus.statistics.model.OceanFragmentInfo;
import pl.edu.agh.evolutus.utils.Utils;

public class HtmlFileGenerator extends OutputFileGenerator {

	private static final Logger log = LoggerFactory.getLogger(HtmlFileGenerator.class);

	@Override
	protected String outputDirectoryName() {
		return "html";
	}

	@Override
	protected void generateInner(String simulationStartString, File outputDirectory, Map<Long, List<OceanFragmentInfo>> infoMap)
			throws IOException, FileGeneratorException {

		List<Stats> statsList = infoMapToStatsList(infoMap);
		File csvFile = new File(outputDirectory, getHtmlFileName(simulationStartString));
		templateRenderer.render("templates/populationChart.vm", csvFile, Utils.immutableMap("stats", statsList));
		log.info("Saved HTML file in {}", outputDirectory.getAbsolutePath());
	}

	private String getHtmlFileName(String timePart) {
		return "populationChart-" + timePart + ".html";
	}

}

