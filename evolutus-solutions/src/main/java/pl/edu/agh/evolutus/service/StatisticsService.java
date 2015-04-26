package pl.edu.agh.evolutus.service;

import java.io.File;
import java.sql.Timestamp;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import org.jage.platform.component.IStatefulComponent;
import org.jage.platform.component.exception.ComponentException;
import org.jage.platform.component.provider.IComponentInstanceProvider;
import org.jage.platform.component.provider.IComponentInstanceProviderAware;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import pl.edu.agh.evolutus.service.output.CsvFileGenerator;
import pl.edu.agh.evolutus.service.output.HtmlFileGenerator;
import pl.edu.agh.evolutus.service.output.OutputFileGenerator;
import pl.edu.agh.evolutus.service.output.OutputFileGenerator.FileGeneratorException;
import pl.edu.agh.evolutus.statistics.dao.ForamFossilDao;
import pl.edu.agh.evolutus.statistics.dao.OceanFragmentInfoDao;
import pl.edu.agh.evolutus.statistics.model.ForamFossil;
import pl.edu.agh.evolutus.statistics.model.OceanFragmentInfo;
import pl.edu.agh.evolutus.utils.Utils;

public class StatisticsService implements IStatefulComponent, IComponentInstanceProviderAware {

	private static final Logger logger = LoggerFactory.getLogger(StatisticsService.class);

	@Inject
	private PsiFileGenerator psiFileGenerator;

	@Inject
	private CsvFileGenerator csvFileGenerator;

	@Inject
	private HtmlFileGenerator htmlFileGenerator;

	@Inject
	private OceanFragmentInfoDao oceanFragmentInfoDao;

	@Inject
	private ForamFossilDao foramFossilDao;

	private IComponentInstanceProvider instanceProvider;

	private final List<OceanFragmentInfo> oceanFragmentInfoToAdd = new LinkedList<>();
	private final List<ForamFossil> foramFossilsToAdd = new LinkedList<>();
	private Thread thread;
	private boolean isRunning = true;

	public final Timestamp simulationStart = new Timestamp(System.currentTimeMillis());

	@Override
	public void setInstanceProvider(IComponentInstanceProvider iComponentInstanceProvider) {
		this.instanceProvider = iComponentInstanceProvider;
	}

	@Override
	public void init() throws StatisticsServiceException {
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

		File outputDirectory;
		if (System.getProperty("evolutus.output.dir") != null) {
			outputDirectory = new File(System.getProperty("evolutus.output.dir"));
		} else {
			outputDirectory = new File(System.getProperty("java.io.tmpdir"));
		}
		outputDirectory = new File(outputDirectory, Utils.getTimestampAsString(simulationStart));

		try {
			Map<Long, List<OceanFragmentInfo>> infoMap = oceanFragmentInfoDao.getInfoGroupedByStepNo(simulationStart);
			for (OutputFileGenerator generator : OutputFileGenerator.getGenerators(instanceProvider)) {
				generator.generate(simulationStart, outputDirectory, infoMap);
			}
		} catch (FileGeneratorException e) {
			throw new StatisticsServiceException("Exception thrown while rendering results.", e);
		}

		logger.info("{} finished.", StatisticsService.class.getSimpleName());
		return false;
	}

	public Timestamp getSimulationStart() {
		return simulationStart;
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

		public StatisticsServiceException(Throwable cause) {
			super(cause);
		}

		public StatisticsServiceException(String message, Throwable cause) {
			super(message, cause);
		}
	}
}
