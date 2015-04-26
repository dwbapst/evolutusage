package pl.edu.agh.evolutus.service.output;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.Reader;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import pl.edu.agh.evolutus.service.config.ConfigFactory;
import pl.edu.agh.evolutus.service.config.ConfigFactory.ConfigServiceException;
import pl.edu.agh.evolutus.statistics.model.OceanFragmentInfo;

public class ConfFileGenerator extends OutputFileGenerator {

	private static final Logger log = LoggerFactory.getLogger(ConfFileGenerator.class);

	@Inject
	private ConfigFactory configFactory;

	@Override
	protected String outputDirectoryName() {
		return "conf";
	}

	@Override
	protected void generateInner(String simulationStartString, File outputDirectory, Map<Long, List<OceanFragmentInfo>> infoMap)
			throws IOException, FileGeneratorException {

		File file = new File(outputDirectory, getFileName(simulationStartString));
		PrintWriter writer = new PrintWriter(file);

		try {
			for (Reader reader : configFactory.getConfigReaders()) {
				writer.println(IOUtils.toString(reader));
				reader.close();
			}
			writer.close();
		} catch (ConfigServiceException e) {
			throw new FileGeneratorException(e);
		}
		log.info("Saved config file in {}", outputDirectory.getAbsolutePath());
	}

	private String getFileName(String timePart) {
		return "config-" + timePart + ".js";
	}

}

