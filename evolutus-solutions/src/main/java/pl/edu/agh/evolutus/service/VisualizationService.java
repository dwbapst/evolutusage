package pl.edu.agh.evolutus.service;

import static org.jooq.impl.DSL.*;
import static pl.edu.agh.evolutus.database.tables.Stats.*;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Connection;
import java.sql.Timestamp;
import java.util.LinkedList;
import java.util.List;
import java.util.Properties;
import java.util.stream.Collectors;

import javax.inject.Inject;

import org.apache.commons.lang3.tuple.Triple;
import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;
import org.jage.platform.component.IStatefulComponent;
import org.jage.platform.component.exception.ComponentException;
import org.jooq.DSLContext;
import org.jooq.Record2;
import org.jooq.SQLDialect;
import org.jooq.impl.DSL;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import pl.edu.agh.evolutus.database.tables.pojos.Stats;
import pl.edu.agh.evolutus.utils.VectorL;

public class VisualizationService implements IStatefulComponent {

	private static final Logger log = LoggerFactory.getLogger(VisualizationService.class);

	@Inject
	private ConnectionProvider connectionProvider;

	@Override
	public void init() throws ComponentException {
	}

	@Override
	public boolean finish() throws ComponentException {
		return false;
	}

	public static void main(String[] args) throws Exception {
		VisualizationService visualizationService = new VisualizationService();
		visualizationService.connectionProvider = new ConnectionProvider();
		visualizationService.connectionProvider.init();
		visualizationService.visualize(Timestamp.valueOf("2015-03-16 20:06:07.614"));
	}

	public void visualize(Timestamp timestamp) throws Exception {
		try (Connection connection = connectionProvider.getConnection()) {
			DSLContext create = DSL.using(connection, SQLDialect.H2);

			VelocityContext context = new VelocityContext();
			context.put("bubbleChartInitialStats", getBubbleChartInitialStats(create, timestamp));
			context.put("bubbleChartUpdates", getBubbleChartUpdates(create, timestamp));
			context.put("lineChartData", getLineChartData(create, timestamp));
			context.put("size", getSize(create, timestamp));
			context.put("maxCellForamsCount", getMaxCellForamsCount(create, timestamp));
			context.put("maxTotalForamsCount", getMaxTotalForamsCount(create, timestamp));
			render(context);
		}
	}

	private void render(VelocityContext context) throws IOException, InterruptedException {
		Properties properties = new Properties();
		properties.put("file.resource.loader.class", "org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader");
		properties.put("runtime.log.logsystem.class", "org.apache.velocity.runtime.log.NullLogSystem");

		VelocityEngine ve = new VelocityEngine();
		ve.init(properties);
		Template template = ve.getTemplate("templates/visualization.vm");

		File outputDir = null;
		if (System.getProperty("evolutus.output.dir") != null) {
			outputDir = new File(System.getProperty("evolutus.output.dir"));
		}
		File tempFile = File.createTempFile("vis_", ".html", outputDir);
		//		tempFile.deleteOnExit();
		FileWriter writer = new FileWriter(tempFile);
		template.merge(context, writer);
		writer.close();

		log.info("Created visualization: {}", tempFile.getCanonicalPath());

		//		ProcessBuilder pb = new ProcessBuilder("google-chrome", tempFile.getAbsolutePath());
		//		Process process = pb.start();
		//		process.waitFor();
		//		Thread.sleep(2000);
	}

	private List<Stats> getBubbleChartInitialStats(DSLContext create, Timestamp timestamp) {
		return create.selectFrom(STATS)
				.where(STATS.SIMULATION_TIME.equal(timestamp))
				.and(STATS.STEP_NO.equal(0L))
				.fetchInto(Stats.class);
	}

	private List<List<Number>> getBubbleChartUpdates(DSLContext create, Timestamp timestamp) {
		List<Stats> statsList = create.selectFrom(STATS)
				.where(STATS.SIMULATION_TIME.equal(timestamp))
				.and(STATS.STEP_NO.greaterThan(0L))
				.fetchInto(Stats.class);

		List<List<Number>> updates = new LinkedList<>();
		List<Number> update = new LinkedList<>();
		long stepNo = -1;
		boolean anyForam = false;
		for (Stats stats : statsList) {
			if (stats.getStepNo() != stepNo && update.size() > 0) {
				updates.add(update);
				update = new LinkedList<>();
				if (!anyForam) {
					break;
				}
				anyForam = false;
			}
			stepNo = stats.getStepNo();
			update.add(stats.getForamsCount());
			update.add(stats.getAlgaeAvailability());
			if (stats.getForamsCount() > 0) {
				anyForam = true;
			}
		}
		return updates;
	}

	private List<Triple<Long, Integer, Double>> getLineChartData(DSLContext create, Timestamp timestamp) {
		return create.select(STATS.STEP_NO, sum(STATS.FORAMS_COUNT), sum(STATS.ALGAE_AVAILABILITY))
				.from(STATS)
				.where(STATS.SIMULATION_TIME.equal(timestamp))
				.groupBy(STATS.STEP_NO)
				.orderBy(STATS.STEP_NO)
				.fetch().stream()
				.map(record -> Triple.of(record.value1(), record.value2().intValue(), record.value3().doubleValue()))
				.filter(pair -> pair.getMiddle() > 0)
				.collect(Collectors.toList());
	}

	private VectorL getSize(DSLContext create, Timestamp timestamp) {
		Record2<Long, Long> record = create.select(max(STATS.X), max(STATS.Y))
				.from(STATS)
				.where(STATS.SIMULATION_TIME.equal(timestamp))
				.fetchOne();
		return new VectorL(record.value1(), record.value2(), 0);
	}

	private Integer getMaxCellForamsCount(DSLContext create, Timestamp timestamp) {
		return create.select(max(STATS.FORAMS_COUNT))
				.from(STATS)
				.where(STATS.SIMULATION_TIME.equal(timestamp))
				.fetchOne(max(STATS.FORAMS_COUNT));
	}

	private Integer getMaxTotalForamsCount(DSLContext create, Timestamp timestamp) {
		return getLineChartData(create, timestamp)
				.stream()
				.mapToInt(Triple::getMiddle)
				.max().orElse(0);
	}
}
