package pl.edu.agh.evolutus.service;

import static pl.edu.agh.evolutus.database.tables.Stats.*;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import org.jooq.DSLContext;
import org.jooq.SQLDialect;
import org.jooq.impl.DSL;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import pl.edu.agh.evolutus.database.tables.pojos.Stats;
import pl.edu.agh.evolutus.utils.Utils;

public class PsiFileGenerator {

	private static final Logger log = LoggerFactory.getLogger(PsiFileGenerator.class);

	@Inject
	private ConnectionProvider connectionProvider;

	@Inject
	private TemplateRenderer templateRenderer;

	public void generate(Timestamp simulationStart, File outputDirectory) throws PsiFileGeneratorException {
		outputDirectory = new File(outputDirectory, "psi");
		outputDirectory.mkdirs();

		try (Connection connection = connectionProvider.getConnection()) {
			DSLContext create = DSL.using(connection, SQLDialect.H2);
			String simulationStartString = Utils.getTimestampAsString(simulationStart);

			Map<Long, List<Stats>> statsMap = getStats(create, simulationStart);
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

		} catch (IOException | SQLException e) {
			throw new PsiFileGeneratorException(e);
		}
	}

	private String getPsiFileName(String timePart, Long stepNo) {
		String stepNoString = String.format("%06d", stepNo);
		return "foramsPSI-" + timePart + stepNoString + ".psi";
	}

	private Map<Long, List<Stats>> getStats(DSLContext create, Timestamp simulationStart) {
		List<Stats> statsList = create.selectFrom(STATS)
				.where(STATS.SIMULATION_START.eq(simulationStart))
				.orderBy(STATS.STEP_NO, STATS.X, STATS.Y, STATS.Z)
				.fetchInto(Stats.class);

		if (statsList.size() == 0) {
			return new HashMap<>();
		}

		Map<Long, List<Stats>> result = new LinkedHashMap<>();
		List<Stats> tmpList = new ArrayList<>();
		long stepNo = statsList.get(0).getStepNo();

		for (Stats stats : statsList) {
			if (stepNo != stats.getStepNo()) {
				result.put(stepNo, tmpList);
				tmpList = new ArrayList<>();
			}
			stepNo = stats.getStepNo();
			tmpList.add(stats);
		}
		result.put(stepNo, tmpList);

		return result;
	}

	public static class PsiFileGeneratorException extends Exception {
		public PsiFileGeneratorException(Throwable cause) {
			super(cause.getMessage(), cause);
		}
	}

}

