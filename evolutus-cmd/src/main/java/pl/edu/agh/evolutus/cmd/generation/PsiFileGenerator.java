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

public class PsiFileGenerator extends OutputFileGenerator {

	@Inject
	private OceanFragmentInfoDao oceanFragmentInfoDao;

	@Override
	protected String outputDirectoryName() {
		return "psi";
	}

	public void generate(Simulation simulation, File baseOutputDirectory) throws IOException {

		File outputDirectory = getOutputDirectory(baseOutputDirectory);

		String simulationStartString = Utils.getTimestampAsString(simulation.getSimulationStart());

		Map<Long, List<OceanFragmentInfo>> infoMap = oceanFragmentInfoDao.getInfoGroupedByStepNo(simulation);

		for (Long stepNo : infoMap.keySet()) {
			List<OceanFragmentInfo> infoList = infoMap.get(stepNo);
			Map<String, Object> parameters = Utils.immutableMap(
					"rowsCount", infoList.size(),
					"stats", infoList
			);

			File psiFile = new File(outputDirectory, getPsiFileName(simulationStartString, stepNo));
			templateRenderer.render("templates/foramsPSI.vm", psiFile, parameters);
		}
	}

	private String getPsiFileName(String timePart, Long stepNo) {
		String stepNoString = String.format("%06d", stepNo);
		return "foramsPSI-" + timePart + stepNoString + ".psi";
	}

}

