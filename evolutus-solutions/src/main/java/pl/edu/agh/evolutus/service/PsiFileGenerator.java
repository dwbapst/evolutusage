package pl.edu.agh.evolutus.service;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import pl.edu.agh.evolutus.service.output.OutputFileGenerator;
import pl.edu.agh.evolutus.statistics.model.OceanFragmentInfo;
import pl.edu.agh.evolutus.statistics.model.Simulation;
import pl.edu.agh.evolutus.utils.Utils;

public class PsiFileGenerator extends OutputFileGenerator {

	private static final Logger log = LoggerFactory.getLogger(PsiFileGenerator.class);

	@Inject
	private TemplateRenderer templateRenderer;

	@Override
	protected String outputDirectoryName() {
		return "psi";
	}

	@Override
	protected void generateInner(Simulation simulation, File outputDirectory, Map<Long, List<OceanFragmentInfo>> infoMap)
			throws IOException {

		String simulationStartString = Utils.getTimestampAsString(simulation.getSimulationStart());

		for (Long stepNo : infoMap.keySet()) {
			List<OceanFragmentInfo> infoList = infoMap.get(stepNo);
			Map<String, Object> parameters = Utils.immutableMap(
					"rowsCount", infoList.size(),
					"stats", infoList
			);

			File psiFile = new File(outputDirectory, getPsiFileName(simulationStartString, stepNo));
			templateRenderer.render("templates/foramsPSI.vm", psiFile, parameters);
		}
		log.info("Saved {} Amira PSI files in {}", infoMap.size(), outputDirectory.getAbsolutePath());
	}

	private String getPsiFileName(String timePart, Long stepNo) {
		String stepNoString = String.format("%06d", stepNo);
		return "foramsPSI-" + timePart + stepNoString + ".psi";
	}

}

