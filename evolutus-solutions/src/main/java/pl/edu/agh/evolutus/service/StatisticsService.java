package pl.edu.agh.evolutus.service;

import java.io.File;
import java.sql.Timestamp;
import java.util.LinkedList;
import java.util.List;

import javax.inject.Inject;

import org.jage.platform.component.IStatefulComponent;
import org.jage.platform.component.exception.ComponentException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import pl.edu.agh.evolutus.statistics.dao.StatsDao;
import pl.edu.agh.evolutus.statistics.model.Stats;
import pl.edu.agh.evolutus.utils.Utils;

public class StatisticsService implements IStatefulComponent {

	private static final Logger logger = LoggerFactory.getLogger(StatisticsService.class);

	@Inject
	private PsiFileGenerator psiFileGenerator;

	@Inject
	private StatsDao statsDao;

	private final List<Stats> statsToAdd = new LinkedList<>();
	private Thread thread;
	private boolean isRunning = true;

	public final Timestamp simulationStart = new Timestamp(System.currentTimeMillis());

	@Override
	public void init() throws StatisticsServiceException {
		thread = createThread();
		thread.start();
		logger.info("{} initialized. Thread started.", StatisticsService.class.getSimpleName());
	}

	public void add(Stats stats) {
		if (!isRunning) {
			throw new StatisticsServiceException("Cannot add statistics. Thread is not running.");
		}
		synchronized (statsToAdd) {
			statsToAdd.add(stats);
		}
	}

	@Override
	public boolean finish() throws StatisticsServiceException {
		isRunning = false;
		try {
			thread.join();
		} catch (InterruptedException e) {
			throw new StatisticsServiceException("Exception thrown while waiting for statistics thread to finish.", e);
		}

		File outputDirectory;
		if (System.getProperty("evolutus.output.dir") != null) {
			outputDirectory = new File(System.getProperty("evolutus.output.dir"));
		} else {
			outputDirectory = new File(System.getProperty("java.io.tmpdir"));
		}
		outputDirectory = new File(outputDirectory, Utils.getTimestampAsString(simulationStart));

		try {
			psiFileGenerator.generate(simulationStart, outputDirectory);
		} catch (Exception e) {
			throw new StatisticsServiceException("Exception thrown while rendering results.", e);
		}

		logger.info("{} finished.", StatisticsService.class.getSimpleName());
		return false;
	}

	private Thread createThread() {
		return new Thread() {

			@Override
			public void run() {
				while (isRunning || !statsToAdd.isEmpty()) {
					try {
						if (statsToAdd.isEmpty()) {
							Thread.sleep(1000);
						}
					} catch (InterruptedException e) {
						logger.error("Statistics thread interrupted.", e);
						isRunning = false;
					}

					List<Stats> stats;
					synchronized (statsToAdd) {
						stats = new LinkedList<>(statsToAdd);
						statsToAdd.clear();
					}
					statsDao.insert(stats.toArray(new Stats[stats.size()]));
				}
			}
		};
	}

	public static class StatisticsServiceException extends ComponentException {
		public StatisticsServiceException(String message) {
			super(message);
		}

		public StatisticsServiceException(Throwable cause) {
			super(cause);
		}

		public StatisticsServiceException(String message, Throwable cause) {
			super(message, cause);
		}
	}
}
