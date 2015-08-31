package pl.edu.agh.evolutus.service;

import java.util.LinkedList;
import java.util.List;

import javax.inject.Inject;

import org.jage.platform.component.IStatefulComponent;
import org.jage.platform.component.exception.ComponentException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import pl.edu.agh.evolutus.service.config.ConfigFactory;
import pl.edu.agh.evolutus.statistics.dao.ForamFossilDao;
import pl.edu.agh.evolutus.statistics.dao.OceanFragmentInfoDao;
import pl.edu.agh.evolutus.statistics.dao.SimulationDao;
import pl.edu.agh.evolutus.statistics.model.OceanFragmentInfo;
import pl.edu.agh.evolutus.statistics.model.Simulation;
import pl.edu.agh.evolutus.service.config.SimulationConfig;
import pl.edu.agh.evolutus.statistics.model.ForamFossil;

public class StatisticsService implements IStatefulComponent {

	private static final Logger logger = LoggerFactory.getLogger(StatisticsService.class);

	@Inject
	private OceanFragmentInfoDao oceanFragmentInfoDao;

	@Inject
	private ForamFossilDao foramFossilDao;

	@Inject
	private SimulationDao simulationDao;

	@Inject
	private SimulationConfig simulationConfig;

	@Inject
	private ConfigFactory configFactory;

	private final List<OceanFragmentInfo> oceanFragmentInfoToAdd = new LinkedList<>();
	private final List<ForamFossil> foramFossilsToAdd = new LinkedList<>();
	private Thread thread;
	private boolean isRunning = true;

	private Simulation simulation;

	@Override
	public void init() throws StatisticsServiceException {
		simulation = new Simulation(System.currentTimeMillis(),
				simulationConfig.simulationName(),
				simulationConfig.simulationDescription(),
				configFactory.getConfigAsString());
		simulationDao.insert(simulation);

		thread = createThread();
		thread.start();
		logger.info("{} initialized. Thread started.", StatisticsService.class.getSimpleName());
	}

	@Override
	public boolean finish() throws StatisticsServiceException {
		isRunning = false;
		try {
			thread.join();
		} catch (InterruptedException e) {
			throw new StatisticsServiceException("Exception thrown while waiting for statistics thread to finish.", e);
		}
		return false;
	}

	public Simulation getSimulation() {
		return simulation;
	}

	private void assertRunning() {
		if (!isRunning) {
			throw new StatisticsServiceException("Cannot add statistics. Thread is not running.");
		}
	}

	public void add(OceanFragmentInfo info) {
		assertRunning();
		synchronized (oceanFragmentInfoToAdd) {
			oceanFragmentInfoToAdd.add(info);
		}
	}

	public void add(ForamFossil foramFossil) {
		assertRunning();
		synchronized (foramFossilsToAdd) {
			foramFossilsToAdd.add(foramFossil);
		}
	}

	private boolean areStatsQueuesEmpty() {
		return oceanFragmentInfoToAdd.isEmpty() && foramFossilsToAdd.isEmpty();
	}

	private boolean shouldThreadContinue() {
		return isRunning || !areStatsQueuesEmpty();
	}

	@SuppressWarnings("SynchronizationOnLocalVariableOrMethodParameter")
	private <T> List<T> copyAndClearListAtomically(List<T> list) {
		List<T> copy;
		synchronized (list) {
			copy = new LinkedList<>(list);
			list.clear();
		}
		return copy;
	}

	private Thread createThread() {
		return new Thread() {

			@Override
			public void run() {
				while (shouldThreadContinue()) {
					try {
						if (areStatsQueuesEmpty()) {
							Thread.sleep(1000);
						}
					} catch (InterruptedException e) {
						logger.error("Statistics thread interrupted.", e);
						isRunning = false;
					}

					oceanFragmentInfoDao.insert(copyAndClearListAtomically(oceanFragmentInfoToAdd));
					foramFossilDao.insert(copyAndClearListAtomically(foramFossilsToAdd));
				}
			}
		};
	}

	public static class StatisticsServiceException extends ComponentException {
		public StatisticsServiceException(String message) {
			super(message);
		}

		public StatisticsServiceException(String message, Throwable cause) {
			super(message, cause);
		}
	}
}
