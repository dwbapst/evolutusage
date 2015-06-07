package pl.edu.agh.evolutus.service;

import java.io.File;
import java.util.HashMap;
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

import com.google.common.collect.Lists;

import pl.edu.agh.evolutus.service.config.ConfigFactory;
import pl.edu.agh.evolutus.service.config.SimulationConfig;
import pl.edu.agh.evolutus.service.output.ChartsGenerator;
import pl.edu.agh.evolutus.service.output.ConfFileGenerator;
import pl.edu.agh.evolutus.service.output.CsvFileGenerator;
import pl.edu.agh.evolutus.service.output.OutputFileGenerator;
import pl.edu.agh.evolutus.service.output.OutputFileGenerator.FileGeneratorException;
import pl.edu.agh.evolutus.statistics.dao.ForamFossilDao;
import pl.edu.agh.evolutus.statistics.dao.OceanFragmentInfoDao;
import pl.edu.agh.evolutus.statistics.dao.SimulationDao;
import pl.edu.agh.evolutus.statistics.model.ForamFossil;
import pl.edu.agh.evolutus.statistics.model.OceanFragmentInfo;
import pl.edu.agh.evolutus.statistics.model.Simulation;
import pl.edu.agh.evolutus.utils.Utils;

public class StatisticsService implements IStatefulComponent, IComponentInstanceProviderAware {

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

	private IComponentInstanceProvider instanceProvider;

	private final List<OceanFragmentInfo> oceanFragmentInfoToAdd = new LinkedList<>();
	private final List<ForamFossil> foramFossilsToAdd = new LinkedList<>();
	private Thread thread;
	private boolean isRunning = true;

	private Simulation simulation;

	@Override
	public void setInstanceProvider(IComponentInstanceProvider iComponentInstanceProvider) {
		this.instanceProvider = iComponentInstanceProvider;
	}

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

		File outputDirectory;
		if (System.getProperty("evolutus.output.dir") != null) {
			outputDirectory = new File(System.getProperty("evolutus.output.dir"));
		} else {
			outputDirectory = new File(System.getProperty("java.io.tmpdir"));
		}
		outputDirectory = new File(outputDirectory, Utils.getTimestampAsString(simulation.getSimulationStart()));

		try {
			Map<Long, List<OceanFragmentInfo>> infoMap = new HashMap<>();
			if (getGenerators().size() > 1) {
				logger.info("Generating output files");
				infoMap = oceanFragmentInfoDao.getInfoGroupedByStepNo(simulation);
			}
			for (OutputFileGenerator generator : getGenerators()) {
				generator.generate(simulation, outputDirectory, infoMap);
			}
		} catch (FileGeneratorException e) {
			throw new StatisticsServiceException("Exception thrown while rendering results.", e);
		}

		logger.info("{} finished.", StatisticsService.class.getSimpleName());
		return false;
	}

	private List<OutputFileGenerator> getGenerators() {
		List<OutputFileGenerator> generators = Lists.newArrayList(instanceProvider.getInstance(ConfFileGenerator.class));
		if (simulationConfig.generatePSI()) {
			generators.add(instanceProvider.getInstance(PsiFileGenerator.class));
		}
		if (simulationConfig.generateCSV()) {
			generators.add(instanceProvider.getInstance(CsvFileGenerator.class));
		}
		if (simulationConfig.generateHTML()) {
			generators.add(instanceProvider.getInstance(ChartsGenerator.class));
		}
		return generators;
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
		if (simulationConfig.virtualFossilizationEnabled()) {
			assertRunning();
			synchronized (foramFossilsToAdd) {
				foramFossilsToAdd.add(foramFossil);
			}
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
