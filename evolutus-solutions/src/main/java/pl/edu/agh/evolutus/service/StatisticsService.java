package pl.edu.agh.evolutus.service;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

import org.jage.platform.component.IStatefulComponent;
import org.jage.platform.component.exception.ComponentException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import pl.edu.agh.evolutus.utils.VectorL;

public class StatisticsService implements IStatefulComponent {

	private static final Logger logger = LoggerFactory.getLogger(StatisticsService.class);

	private Map<VectorL, Map<Long, Integer>> oceanFragmentStatistics = new HashMap<>();

	@Override
	public void init() throws ComponentException {
		logger.info("{} initialized", StatisticsService.class.getSimpleName());
	}

	public synchronized void addStatistics(VectorL position, long step, int foramsAlive) {
		if (!oceanFragmentStatistics.containsKey(position)) {
			oceanFragmentStatistics.put(position, new HashMap<>());
		}
		oceanFragmentStatistics.get(position).put(step, foramsAlive);
	}

	@Override
	public boolean finish() throws ComponentException {
		try {
			PrintWriter out = new PrintWriter("/home/maciek/tmp/stats.csv");
			for (VectorL pos : oceanFragmentStatistics.keySet()) {
				for (Long step : oceanFragmentStatistics.get(pos).keySet()) {
					out.println(
							String.format("%d,%d,%d,%d,%d", pos.x, pos.y, pos.z, step,
									oceanFragmentStatistics.get(pos).get(step)));
				}
			}
			out.close();
		} catch (FileNotFoundException e) {
			logger.error("Cannot open output file for statistics.", e);
		}
		return true;
	}
}
