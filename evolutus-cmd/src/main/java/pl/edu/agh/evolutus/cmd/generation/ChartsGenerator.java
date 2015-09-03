package pl.edu.agh.evolutus.cmd.generation;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import javax.inject.Inject;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.tuple.Pair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import pl.edu.agh.evolutus.cmd.parser.GenerationMode;
import pl.edu.agh.evolutus.statistics.dao.ForamFossilDao;
import pl.edu.agh.evolutus.statistics.dao.OceanFragmentInfoDao;
import pl.edu.agh.evolutus.statistics.model.OceanFragmentInfo;
import pl.edu.agh.evolutus.statistics.model.Simulation;
import pl.edu.agh.evolutus.utils.Utils;

public class ChartsGenerator extends OutputFileGenerator {

	private static final Logger logger = LoggerFactory.getLogger(ChartsGenerator.class);

	@Inject
	private OceanFragmentInfoDao oceanFragmentInfoDao;

	@Inject
	private ForamFossilDao foramFossilDao;

	@Override
	protected String outputDirectoryName() {
		return "charts";
	}

	public void renderChart(String chartName, File outputDirectory, String simulationStartString, List<Stats> statsList)
			throws IOException {
		File htmlFile_populationChart = new File(outputDirectory, getHtmlFileName(chartName, simulationStartString));
		templateRenderer.render("templates/" + chartName + ".vm", htmlFile_populationChart,
				Utils.immutableMap("stats", statsList));
	}

	public void generate(GenerationMode generationMode, Simulation simulation, File baseOutputDirectory)
			throws IOException, FileGeneratorException {

		File outputDirectory = getOutputDirectory(baseOutputDirectory);

		copyLibrariesIfNotCopiedYet(outputDirectory);

		String simulationStartString = Utils.getTimestampAsString(simulation.getSimulationStart());

		Map<Long, List<OceanFragmentInfo>> infoGroupedByStepNo = oceanFragmentInfoDao.getInfoGroupedByStepNo(simulation);
		List<Stats> statsList = infoMapToStatsList(infoGroupedByStepNo);

		switch (generationMode) {
		case POPULATION_CHART:
			renderChart("populationChart", outputDirectory, simulationStartString, statsList);
			break;
		case ENERGY_CHART:
			renderChart("energyChart", outputDirectory, simulationStartString, statsList);
			break;
		case BORN_DEAD_CHART:
			renderChart("borndeadChart", outputDirectory, simulationStartString, statsList);
			break;
		default:
			throw new FileGeneratorException("Not supported generation mode for ChartsGenerator: " + generationMode);
		}
	}

	private String getHtmlFileName(String name, String timePart) {
		return name + "-" + timePart + ".html";
	}

	private void copyLibrariesIfNotCopiedYet(File outputDirectory) throws IOException {
		File libraryDir = new File(outputDirectory, "js");
		if (!libraryDir.exists()) {
			for (String lib : Arrays.asList("jquery-1.11.2.min.js", "jsapi-visualization-1.1.min.js")) {
				FileUtils.copyInputStreamToFile(Utils.getResourceAsStream("js/" + lib), new File(libraryDir, lib));
			}
		}
	}

	public void generateGeneEvolutionCharts(Simulation simulation, File baseOutputDirectory, String[] geneList)
			throws FileGeneratorException {
		if (geneList.length == 0) {
			throw new FileGeneratorException("You have to provide at least one gene to generate evolution chart.");
		}

		try {
			File outputDirectory = getOutputDirectory(baseOutputDirectory);
			copyLibrariesIfNotCopiedYet(outputDirectory);

			Set<String> geneNames = foramFossilDao.getGeneNames(simulation);
			for (String gene : geneList) {
				if (geneNames.contains(gene)) {
					generateGeneEvolutionChart(gene, simulation, outputDirectory);
				} else {
					logger.warn("Unknown gene: " + gene);
				}
			}

		} catch (IllegalStateException | IOException e) {
			throw new FileGeneratorException("Exception thrown while generating gene evolution charts. : " + e.getMessage(), e);
		}
	}

	private void generateGeneEvolutionChart(String geneName, Simulation simulation, File outputDirectory) {
		List<Pair<Double, Double>> fossilStats = foramFossilDao
				.getAvgGeneValuesByDeathHour(simulation, geneName)
				.entrySet()
				.stream()
				.map(entry -> Pair.of(entry.getKey(), entry.getValue()[0]))
				.collect(Collectors.toList());
		File htmlFile = new File(outputDirectory, geneName + "-evolution.html");

		try {
			templateRenderer.render("templates/geneEvolution.vm", htmlFile,
					Utils.immutableMap(
							"gene", geneName,
							"stats", fossilStats
					));
		} catch (IOException e) {
			throw new IllegalStateException(e);
		}
	}

}

