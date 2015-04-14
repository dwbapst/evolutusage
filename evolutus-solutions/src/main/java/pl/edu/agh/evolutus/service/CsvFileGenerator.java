package pl.edu.agh.evolutus.service;

import java.io.File;
import java.io.IOException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import pl.edu.agh.evolutus.statistics.model.OceanFragmentInfo;
import pl.edu.agh.evolutus.utils.Utils;

public class CsvFileGenerator {

	private static final Logger log = LoggerFactory.getLogger(CsvFileGenerator.class);

	@Inject
	private TemplateRenderer templateRenderer;

	public void generate(Timestamp simulationStart, File outputDirectory, Map<Long, List<OceanFragmentInfo>> infoMap)
			throws CsvFileGeneratorException {
		outputDirectory = new File(outputDirectory, "csv");
		outputDirectory.mkdirs();

		try {
			List<Stats> statsList = new ArrayList<>();
			for (Long stepNo : infoMap.keySet()) {
				long foramsCount = 0;
				double algaeAvailability = 0.0;
				for (OceanFragmentInfo info : infoMap.get(stepNo)) {
					foramsCount += info.getForamsCount();
					algaeAvailability += info.getAlgaeAvailability();
				}
				statsList.add(new Stats(stepNo, foramsCount, algaeAvailability));
			}

			String simulationStartString = Utils.getTimestampAsString(simulationStart);
			File csvFile = new File(outputDirectory, getCsvFileName(simulationStartString));
			templateRenderer.render("templates/foramsCSV.vm", csvFile, Utils.immutableMap("stats", statsList));
			log.info("Saved CSV file in {}", outputDirectory.getAbsolutePath());

		} catch (IOException e) {
			throw new CsvFileGeneratorException(e);
		}
	}

	private String getCsvFileName(String timePart) {
		return "foramsCSV-" + timePart + ".csv";
	}

	public static class CsvFileGeneratorException extends Exception {
		public CsvFileGeneratorException(Throwable cause) {
			super(cause.getMessage(), cause);
		}
	}

	public static class Stats {
		public final long stepNo;
		public final long foramsCount;
		public final double algaeAvailability;

		private Stats(Long stepNo, Long foramsCount, Double algaeAvailability) {
			this.stepNo = stepNo;
			this.foramsCount = foramsCount;
			this.algaeAvailability = algaeAvailability;
		}

		public long getStepNo() {
			return stepNo;
		}

		public long getForamsCount() {
			return foramsCount;
		}

		public double getAlgaeAvailability() {
			return algaeAvailability;
		}
	}

}

