package pl.edu.agh.evolutus.cmd.generation;

import java.io.File;
import java.io.IOException;
import java.util.Optional;

import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import pl.edu.agh.evolutus.cmd.generation.OutputFileGenerator.FileGeneratorException;
import pl.edu.agh.evolutus.cmd.parser.GenerationMode;
import pl.edu.agh.evolutus.statistics.dao.OceanFragmentInfoDao;
import pl.edu.agh.evolutus.statistics.dao.SimulationDao;
import pl.edu.agh.evolutus.statistics.model.Simulation;
import pl.edu.agh.evolutus.utils.Utils;

public class OutputFileGeneratingService {

	private static final Logger logger = LoggerFactory.getLogger(OutputFileGeneratingService.class);

	@Inject
	private SimulationDao simulationDao;

	@Inject
	private OceanFragmentInfoDao oceanFragmentInfoDao;

	@Inject
	private ChartsGenerator chartsGenerator;

	@Inject
	private ConfFileGenerator confFileGenerator;

	@Inject
	private CsvFileGenerator csvFileGenerator;

	@Inject
	private PsiFileGenerator psiFileGenerator;

	public void listAllSimulations() {
		simulationDao.get().stream()
				.map(sim -> sim.getSimulationStart().getTime() + "\t" + sim.getName() + "\t" + sim.getSimulationStartString())
				.forEach(System.out::println);
	}

	public void generate(GenerationConfig generationConfig) throws OutputFileGeneratingServiceException {

		GenerationMode generationMode = generationConfig.getGenerationMode();
		logger.info("Starting output files generation. Generation mode: " + generationMode);

		Optional<Simulation> optSimulation;
		if (generationConfig.isSimulationStartDefined()) {
			long time = generationConfig.getSimulationStart();
			optSimulation = simulationDao.get(time);
		} else {
			optSimulation = simulationDao.getLatest();
		}
		Simulation simulation = optSimulation.orElseThrow(
				() -> new OutputFileGeneratingServiceException("Cannot find desired simulation in database.")
		);

		File outputDirectory = generationConfig.getOutputDir();
		outputDirectory = new File(outputDirectory, Utils.getTimestampAsString(simulation.getSimulationStart()));
		try {
			switch (generationMode) {
			case POPULATION_CHART:
			case ENERGY_CHART:
			case BORN_DEAD_CHART:
				chartsGenerator.generate(generationMode, simulation, outputDirectory);
				break;
			case PSI:
				psiFileGenerator.generate(simulation, outputDirectory);
				break;
			case CSV:
				csvFileGenerator.generate(simulation, outputDirectory);
				break;
			case CONFIG:
				confFileGenerator.generate(simulation, outputDirectory);
				break;
			case GENES_EVOLUTION:
				chartsGenerator.generateGeneEvolutionCharts(simulation, outputDirectory, generationConfig.getGenesList());
				break;
			default:
				throw new OutputFileGeneratingServiceException("Unsupported generation mode: " + generationMode);
			}
		} catch (FileGeneratorException e) {
			throw new OutputFileGeneratingServiceException(e);
		} catch (IOException e) {
			throw new OutputFileGeneratingServiceException("Error occurred while generating output files: " + e.getMessage(), e);
		}

		logger.info("Output files generation finished. Generation mode: " + generationMode + ", directory: " +
				outputDirectory.getAbsolutePath());
	}

	public static class OutputFileGeneratingServiceException extends Exception {

		public OutputFileGeneratingServiceException(String message) {
			super(message);
		}

		public OutputFileGeneratingServiceException(Throwable cause) {
			super(cause.getMessage(), cause);
		}

		public OutputFileGeneratingServiceException(String message, Throwable cause) {
			super(message, cause);
		}
	}
}
