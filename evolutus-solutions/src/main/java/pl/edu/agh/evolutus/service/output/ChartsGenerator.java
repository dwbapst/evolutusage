package pl.edu.agh.evolutus.service.output;

import org.apache.commons.lang3.tuple.Pair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pl.edu.agh.evolutus.service.config.SimulationConfig;
import pl.edu.agh.evolutus.statistics.dao.ForamFossilDao;
import pl.edu.agh.evolutus.statistics.model.OceanFragmentInfo;
import pl.edu.agh.evolutus.statistics.model.Simulation;
import pl.edu.agh.evolutus.utils.Utils;

import javax.inject.Inject;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class ChartsGenerator extends OutputFileGenerator {

	private static final Logger log = LoggerFactory.getLogger(ChartsGenerator.class);

	@Inject
	private SimulationConfig simulationConfig;

	@Inject
	private ForamFossilDao foramFossilDao;

	@Override
	protected String outputDirectoryName() {
		return "charts";
	}

	@Override
	protected void generateInner(Simulation simulation, File outputDirectory, Map<Long, List<OceanFragmentInfo>> infoMap)
			throws IOException, FileGeneratorException {

		String simulationStartString = Utils.getTimestampAsString(simulation.getSimulationStart());

		List<Stats> statsList = infoMapToStatsList(infoMap);
		File htmlFile_populationChart = new File(outputDirectory, getHtmlFileName("populationChart", simulationStartString));
		templateRenderer.render("templates/populationChart.vm", htmlFile_populationChart, Utils.immutableMap("stats", statsList));

		File htmlFile_borndeadChart = new File(outputDirectory, getHtmlFileName("borndeadChart", simulationStartString));
		templateRenderer.render("templates/borndeadChart.vm", htmlFile_borndeadChart, Utils.immutableMap("stats", statsList));

		File htmlFile_energyChart = new File(outputDirectory, getHtmlFileName("energyChart", simulationStartString));
		templateRenderer.render("templates/energyChart.vm", htmlFile_energyChart, Utils.immutableMap("stats", statsList));

		if (simulationConfig.virtualFossilizationEnabled()) {
			generateGeneEvolutionCharts(simulation, outputDirectory);
		}

		log.info("Saved charts in {}", outputDirectory.getAbsolutePath());
	}

	private String getHtmlFileName(String name, String timePart) {
		return name + "-" + timePart + ".html";
	}

	private void generateGeneEvolutionCharts(Simulation simulation, File outputDirectory) throws IOException {
		Set<String> geneNames = foramFossilDao.getGeneNames(simulation);
		String genesList = System.getProperty("evolutus.genes.list");
		if (genesList != null) {
			for (String geneName : genesList.split(",")) {
				if (geneNames.contains(geneName)) {
					generateGeneEvolutionChart(geneName, simulation, outputDirectory);
				}
			}
		}
	}

	private void generateGeneEvolutionChart(String geneName, Simulation simulation, File outputDirectory) throws IOException {
		List<Pair<Long, Double>> fossilStats = foramFossilDao
				.getAvgGeneValuesByStep(simulation, geneName)
				.entrySet()
				.stream()
				.map(entry -> Pair.of(entry.getKey(), entry.getValue()[0]))
				.collect(Collectors.toList());
		File htmlFile = new File(outputDirectory, geneName + "-evolution.html");
		templateRenderer.render("templates/geneEvolution.vm", htmlFile,
				Utils.immutableMap(
						"gene", geneName,
						"stats", fossilStats
				));
	}

}

