package pl.edu.agh.evolutus.statistics.dao;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import org.jage.platform.component.IStatefulComponent;
import org.jage.platform.component.exception.ComponentException;
import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.Morphia;

import pl.edu.agh.evolutus.service.MongoProvider;
import pl.edu.agh.evolutus.statistics.model.Stats;

public class StatsDao implements IStatefulComponent {

	private static final String COLLECTION_NAME = "stats";

	@Inject
	private MongoProvider mongoProvider;

	private Datastore ds;

	@Override
	public void init() throws ComponentException {
		ds = new Morphia().createDatastore(mongoProvider.getMongoClient(), COLLECTION_NAME);
	}

	@Override
	public boolean finish() throws ComponentException {
		return true;
	}

	public void insert(Stats... stats) {
		ds.save(stats);
	}

	public List<Stats> getStats(Timestamp simulationStart) {
		return ds.createQuery(Stats.class)
				.field("simulationStart").equal(simulationStart.getTime())
				.field("stepNo").greaterThan(0L)
				.asList();
	}

	public Map<Long, List<Stats>> getStatsGroupedByStepNo(Timestamp simulationStart) {
		List<Stats> statsList = getStats(simulationStart);
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
}

