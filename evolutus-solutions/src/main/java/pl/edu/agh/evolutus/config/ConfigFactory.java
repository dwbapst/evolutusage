package pl.edu.agh.evolutus.config;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;

import javax.script.Invocable;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ConfigFactory {

	private static final Logger logger = LoggerFactory.getLogger(ConfigFactory.class);

	public static final String CONFIG_PROPERTY = "evolutus.config";
	public static final String CONFIG_CLASSPATH_LOCATION = "config.js";
	private final SystemConfig systemConfig;
	private final EnvironmentConfig environmentConfig;
	private final ForamConfig foramConfig;
	private final SimulationConfig simulationConfig;

	public ConfigFactory() throws ConfigServiceException {
		List<Reader> configReaders = new ArrayList<>();
		try {
			ScriptEngineManager scriptEngineManager = new ScriptEngineManager();
			ScriptEngine scriptEngine = scriptEngineManager.getEngineByName("JavaScript");

			configReaders = getConfigReaders();
			for (Reader configReader : configReaders) {
				scriptEngine.eval(configReader);
			}

			Invocable invocable = (Invocable) scriptEngine;
			IConfigJS configJS = invocable.getInterface(IConfigJS.class);
			systemConfig = new SystemConfig(configJS);
			environmentConfig = new EnvironmentConfig(configJS);
			foramConfig = new ForamConfig(configJS);
			simulationConfig = new SimulationConfig(configJS);
		} catch (IOException e) {
			throw new ConfigServiceException("Cannot read config files", e);
		} catch (ScriptException e) {
			throw new ConfigServiceException("Cannot evaluate config script", e);
		} finally {
			configReaders.forEach(IOUtils::closeQuietly);
		}
	}

	private List<Reader> getConfigReaders() throws IOException, ConfigServiceException {
		List<Reader> readers = new ArrayList<>();
		String configPathsString = System.getProperty(CONFIG_PROPERTY);

		if (configPathsString != null) {
			logger.info("Loading config files:");
			String[] configPaths = StringUtils.split(configPathsString, '\u0000');
			for (String configPath : configPaths) {
				readers.add(new FileReader(configPath));
				logger.info("\t" + new File(configPath).getAbsolutePath());
			}
		} else {
			InputStream configStream = getClass().getClassLoader().getResourceAsStream(CONFIG_CLASSPATH_LOCATION);
			if (configStream == null) {
				throw new ConfigServiceException("Cannot find config.js file in the classpath root directory.");
			}
			readers.add(new InputStreamReader(configStream));
		}
		return readers;
	}

	public SystemConfig getSystemConfig() {
		return systemConfig;
	}

	public EnvironmentConfig getEnvironmentConfig() {
		return this.environmentConfig;
	}

	public ForamConfig getForamConfig() {
		return this.foramConfig;
	}

	public SimulationConfig getSimulationConfig() {
		return simulationConfig;
	}

	public static class ConfigServiceException extends Exception {
		public ConfigServiceException(String message) {
			super(message);
		}

		public ConfigServiceException(String message, Throwable cause) {
			super(message, cause);
		}
	}
}
