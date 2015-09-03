package pl.edu.agh.evolutus.cmd.generation;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import pl.edu.agh.evolutus.statistics.dao.OceanFragmentInfoDao;
import pl.edu.agh.evolutus.statistics.model.OceanFragmentInfo;
import pl.edu.agh.evolutus.statistics.model.Simulation;
import pl.edu.agh.evolutus.utils.Utils;

public class CsvFileGenerator extends OutputFileGenerator {

	@Inject
	private OceanFragmentInfoDao oceanFragmentInfoDao;

	@Override
	protected String outputDirectoryName() {
		return "csv";
	}

	public void generate(Simulation simulation, File baseOutputDirectory) throws IOException {

		File outputDirectory = getOutputDirectory(baseOutputDirectory);

		Map<Long, List<OceanFragmentInfo>> infoMap = oceanFragmentInfoDao.getInfoGroupedByStepNo(simulation);

		List<Stats> statsList = infoMapToStatsList(infoMap);
		File csvFile = new File(outputDirectory, getCsvFileName(simulation.getSimulationStartString()));
		templateRenderer.render("templates/foramsCSV.vm", csvFile, Utils.immutableMap("stats", statsList));
	}

	private String getCsvFileName(String timePart) {
		return "foramsCSV-" + timePart + ".csv";
	}

}

