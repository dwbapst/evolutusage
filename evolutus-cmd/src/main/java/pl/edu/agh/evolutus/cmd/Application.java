package pl.edu.agh.evolutus.cmd;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

import org.apache.commons.lang3.StringUtils;
import org.jage.platform.cli.CliNodeBootstrapper;
import org.jage.platform.component.pico.IPicoComponentInstanceProvider;
import org.jage.platform.component.pico.PicoComponentInstanceProviderFactory;
import org.mongodb.morphia.logging.MorphiaLoggerFactory;

import pl.edu.agh.evolutus.cmd.generation.ChartsGenerator;
import pl.edu.agh.evolutus.cmd.generation.ConfFileGenerator;
import pl.edu.agh.evolutus.cmd.generation.CsvFileGenerator;
import pl.edu.agh.evolutus.cmd.generation.GenerationConfig;
import pl.edu.agh.evolutus.cmd.generation.OutputFileGeneratingService;
import pl.edu.agh.evolutus.cmd.generation.OutputFileGeneratingService.OutputFileGeneratingServiceException;
import pl.edu.agh.evolutus.cmd.generation.PsiFileGenerator;
import pl.edu.agh.evolutus.cmd.generation.TemplateRenderer;
import pl.edu.agh.evolutus.cmd.parser.ArgsParser;
import pl.edu.agh.evolutus.cmd.parser.ArgsParserResult;
import pl.edu.agh.evolutus.cmd.util.MorphiaSilentLoggerFactory;
import pl.edu.agh.evolutus.service.MongoProvider;
import pl.edu.agh.evolutus.statistics.dao.ForamFossilDao;
import pl.edu.agh.evolutus.statistics.dao.OceanFragmentInfoDao;
import pl.edu.agh.evolutus.statistics.dao.SimulationDao;

public class Application {

	public static void main(String[] args) {

		MorphiaLoggerFactory.registerLogger(MorphiaSilentLoggerFactory.class);

		try {
			ArgsParserResult parserResult = ArgsParser.parse(args);
			initMongoParameters(parserResult.getDbPropertiesFile());

			switch (parserResult.getApplicationMode()) {
			case SIMULATION:
				runSimulation("evolutus_simulation.xml", parserResult.getConfigFiles());
				break;
			case LIST:
				runListSimulations();
				break;
			case GENERATION:
				runGeneration(parserResult);
				break;
			}

		} catch (ApplicationException e) {
			error(e);
		}
	}

	private static IPicoComponentInstanceProvider createInstanceProvider() {
		IPicoComponentInstanceProvider instanceProvider = PicoComponentInstanceProviderFactory.createInstanceProvider();
		instanceProvider.addComponent(OutputFileGeneratingService.class);
		instanceProvider.addComponent(TemplateRenderer.class);
		instanceProvider.addComponent(PsiFileGenerator.class);
		instanceProvider.addComponent(MongoProvider.class);
		instanceProvider.addComponent(CsvFileGenerator.class);
		instanceProvider.addComponent(ChartsGenerator.class);
		instanceProvider.addComponent(ConfFileGenerator.class);
		instanceProvider.addComponent(OceanFragmentInfoDao.class);
		instanceProvider.addComponent(ForamFossilDao.class);
		instanceProvider.addComponent(SimulationDao.class);
		return instanceProvider;
	}

	private static void runSimulation(String ageConfResource, File[] configFiles) {
		if (configFiles != null && configFiles.length > 0) {
			System.setProperty("evolutus.config", StringUtils.join(configFiles, '\u0000'));
		}

		CliNodeBootstrapper bootstrapper = new CliNodeBootstrapper(
				new String[] { "-Dage.node.conf=classpath:" + ageConfResource });
		bootstrapper.start();
	}

	private static void runListSimulations() throws ApplicationException {
		IPicoComponentInstanceProvider instanceProvider = createInstanceProvider();
		OutputFileGeneratingService generatingService = instanceProvider.getComponent(OutputFileGeneratingService.class);
		generatingService.listAllSimulations();
	}

	private static void runGeneration(ArgsParserResult result) throws ApplicationException {
		IPicoComponentInstanceProvider instanceProvider = createInstanceProvider();
		OutputFileGeneratingService generatingService = instanceProvider.getComponent(OutputFileGeneratingService.class);

		try {
			generatingService.generate(
					new GenerationConfig(result.getGenerationMode(), result.getSimulationStart(), result.getOutputDir(),
							result.getGenesList())
			);
		} catch (OutputFileGeneratingServiceException e) {
			throw new ApplicationException("Error occurred during output files generation: " + e.getMessage(), e);
		}
	}

	private static void initMongoParameters(File dbPropertiesFile) throws ApplicationException {
		Properties properties = new Properties();
		try {
			properties.load(new FileInputStream(dbPropertiesFile));
			String host = properties.getProperty("host");
			String port = properties.getProperty("port");
			MongoProvider.initializeParametersStatically(host, port);
		} catch (IOException e) {
			throw new ApplicationException("Cannot read database properties from: " + dbPropertiesFile);
		}
	}

	private static void error(ApplicationException e) {
		System.out.println("Error:");
		System.out.println("\t" + e.getMessage());
		System.out.println();
		System.out.println("Usage:");

		// @formatter:off
		System.out.println("\tevolutus.sh (simulation|sim)  <db_properties_file>  [config_files]                                          - start simulation");
		System.out.println("\tevolutus.sh  list             <db_properties_file>                                                          - list simulations");
		System.out.println("\tevolutus.sh (generation|gen)    population-chart      <db_properties_file> <out_dir> [simulation]             - generate population chart");
		System.out.println("\tevolutus.sh (generation|gen)    energy-chart          <db_properties_file> <out_dir> [simulation]             - enerate energy chart");
		System.out.println("\tevolutus.sh (generation|gen)    born-dead-chart       <db_properties_file> <out_dir> [simulation]             - generate born-dead chart");
		System.out.println("\tevolutus.sh (generation|gen)    psi                   <db_properties_file> <out_dir> [simulation]             - generate Amira PSI files");
		System.out.println("\tevolutus.sh (generation|gen)    csv                   <db_properties_file> <out_dir> [simulation]             - generate CSV files");
		System.out.println("\tevolutus.sh (generation|gen)    config                <db_properties_file> <out_dir> [simulation]             - dump simulation config");
		System.out.println("\tevolutus.sh (generation|gen)    genes-evolution       <db_properties_file> <out_dir> [simulation]  [gene]...  - generate evolution charts for given genes");
		System.exit(127);
		// @formatter:on
	}
}
