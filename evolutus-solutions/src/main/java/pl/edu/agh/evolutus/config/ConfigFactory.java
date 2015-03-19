package pl.edu.agh.evolutus.config;

import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;

import javax.script.Invocable;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

import org.apache.commons.io.IOUtils;

public class ConfigFactory implements IConfigFactory {

	public static final String CONFIG_PROPERTY = "evolutus.config";
	public static final String CONFIG_CLASSPATH_LOCATION = "config.js";
	private final EnvironmentConfig environmentConfig;
	private final ForamConfig foramConfig;
	private final SimulationConfig simulationConfig;

	public ConfigFactory() throws ConfigServiceException {
		Reader configReader = null;
		try {
			configReader = getConfigReader();

			ScriptEngineManager scriptEngineManager = new ScriptEngineManager();
			ScriptEngine scriptEngine = scriptEngineManager.getEngineByName("JavaScript");
			scriptEngine.eval(configReader);

			Invocable invocable = (Invocable) scriptEngine;
			IConfigJS configJS = invocable.getInterface(IConfigJS.class);
			environmentConfig = new EnvironmentConfig(configJS);
			foramConfig = new ForamConfig(configJS);
			simulationConfig = new SimulationConfig(configJS);
		} catch (IOException e) {
			throw new ConfigServiceException("Cannot read config files", e);
		} catch (ScriptException e) {
			throw new ConfigServiceException("Cannot evaluate config script", e);
		} finally {
			IOUtils.closeQuietly(configReader);
		}
	}

	private Reader getConfigReader() throws IOException, ConfigServiceException {
		String configPath = System.getProperty(CONFIG_PROPERTY);
		if (configPath != null) {
			return new FileReader(configPath);
		} else {
			InputStream configStream = getClass().getClassLoader().getResourceAsStream(CONFIG_CLASSPATH_LOCATION);
			if (configStream == null) {
				throw new ConfigServiceException("Cannot find config.js file in the classpath root directory.");
			}
			return new InputStreamReader(configStream);
		}
	}

	@Override
	public EnvironmentConfig getEnvironmentConfig() {
		return this.environmentConfig;
	}

	@Override
	public ForamConfig getForamConfig() {
		return this.foramConfig;
	}

	@Override
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
