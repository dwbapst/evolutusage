package pl.edu.agh.evolutus.service;

import static org.jooq.impl.DSL.*;
import static pl.edu.agh.evolutus.database.tables.Stats.*;

import java.io.File;
import java.sql.Connection;
import java.sql.Timestamp;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.inject.Inject;

import org.apache.commons.lang3.tuple.Triple;
import org.jage.platform.component.IStatefulComponent;
import org.jage.platform.component.exception.ComponentException;
import org.jooq.DSLContext;
import org.jooq.Record2;
import org.jooq.SQLDialect;
import org.jooq.impl.DSL;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import pl.edu.agh.evolutus.database.tables.pojos.Stats;
import pl.edu.agh.evolutus.utils.Utils;
import pl.edu.agh.evolutus.utils.VectorL;

public class VisualizationService implements IStatefulComponent {

	private static final Logger log = LoggerFactory.getLogger(VisualizationService.class);

	@Inject
	private ConnectionProvider connectionProvider;

	@Inject
	private TemplateRenderer templateRenderer;

	@Override
	public void init() throws ComponentException {
	}

	@Override
	public boolean finish() throws ComponentException {
		return false;
	}

	public void visualize(Timestamp timestamp, File outputDirectory) throws Exception {
		outputDirectory = new File(outputDirectory, "html");
		outputDirectory.mkdirs();

		try (Connection connection = connectionProvider.getConnection()) {
			DSLContext create = DSL.using(connection, SQLDialect.H2);

			Map<?, ?> parameters = Utils.immutableMap(
					"bubbleChartInitialStats", getBubbleChartInitialStats(create, timestamp),
					"bubbleChartUpdates", getBubbleChartUpdates(create, timestamp),
					"lineChartData", getLineChartData(create, timestamp),
					"size", getSize(create, timestamp),
					"maxCellForamsCount", getMaxCellForamsCount(create, timestamp),
					"maxTotalForamsCount", getMaxTotalForamsCount(create, timestamp)
			);

			File tmpFile = File.createTempFile("vis_", ".html", outputDirectory);
			templateRenderer.render("templates/visualization.vm", tmpFile, parameters);
			log.info("Created visualization: {}", tmpFile.getCanonicalPath());
		}
	}

	private List<Stats> getBubbleChartInitialStats(DSLContext create, Timestamp timestamp) {
		return create.selectFrom(STATS)
				.where(STATS.SIMULATION_START.equal(timestamp))
				.and(STATS.STEP_NO.equal(0L))
				.fetchInto(Stats.class);
	}

	private List<List<Number>> getBubbleChartUpdates(DSLContext create, Timestamp timestamp) {
		List<Stats> statsList = create.selectFrom(STATS)
				.where(STATS.SIMULATION_START.equal(timestamp))
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
				.where(STATS.SIMULATION_START.equal(timestamp))
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
				.where(STATS.SIMULATION_START.equal(timestamp))
				.fetchOne();
		return new VectorL(record.value1(), record.value2(), 0);
	}

	private Integer getMaxCellForamsCount(DSLContext create, Timestamp timestamp) {
		return create.select(max(STATS.FORAMS_COUNT))
				.from(STATS)
				.where(STATS.SIMULATION_START.equal(timestamp))
				.fetchOne(max(STATS.FORAMS_COUNT));
	}

	private Integer getMaxTotalForamsCount(DSLContext create, Timestamp timestamp) {
		return getLineChartData(create, timestamp)
				.stream()
				.mapToInt(Triple::getMiddle)
				.max().orElse(0);
	}
}
