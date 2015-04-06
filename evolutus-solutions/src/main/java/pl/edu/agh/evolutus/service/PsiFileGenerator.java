package pl.edu.agh.evolutus.service;

import java.io.File;
import java.io.IOException;
import java.sql.Timestamp;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import pl.edu.agh.evolutus.statistics.dao.StatsDao;
import pl.edu.agh.evolutus.statistics.model.Stats;
import pl.edu.agh.evolutus.utils.Utils;

public class PsiFileGenerator {

	private static final Logger log = LoggerFactory.getLogger(PsiFileGenerator.class);

	@Inject
	private TemplateRenderer templateRenderer;

	@Inject
	private StatsDao statsDao;

	public void generate(Timestamp simulationStart, File outputDirectory) throws PsiFileGeneratorException {
		outputDirectory = new File(outputDirectory, "psi");
		outputDirectory.mkdirs();

		try {
			String simulationStartString = Utils.getTimestampAsString(simulationStart);

			Map<Long, List<Stats>> statsMap = statsDao.getStatsGroupedByStepNo(simulationStart);
			for (Long stepNo : statsMap.keySet()) {
				List<Stats> statsList = statsMap.get(stepNo);
				Map<String, Object> parameters = Utils.immutableMap(
						"rowsCount", statsList.size(),
						"stats", statsList
				);

				File psiFile = new File(outputDirectory, getPsiFileName(simulationStartString, stepNo));
				templateRenderer.render("templates/foramsPSI.vm", psiFile, parameters);
			}
			log.info("Saved {} Amira PSI files in {}", statsMap.size(), outputDirectory.getAbsolutePath());

		} catch (IOException e) {
			throw new PsiFileGeneratorException(e);
		}
	}

	private String getPsiFileName(String timePart, Long stepNo) {
		String stepNoString = String.format("%06d", stepNo);
		return "foramsPSI-" + timePart + stepNoString + ".psi";
	}

	public static class PsiFileGeneratorException extends Exception {
		public PsiFileGeneratorException(Throwable cause) {
			super(cause.getMessage(), cause);
		}
	}

}

