package pl.edu.agh.evolutus.service.config;

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
import org.jage.platform.component.IStatefulComponent;
import org.jage.platform.component.exception.ComponentException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ConfigFactory implements IStatefulComponent {

	private static final Logger logger = LoggerFactory.getLogger(ConfigFactory.class);

	public static final String CONFIG_PROPERTY = "evolutus.config";
	public static final String[] CLASSPATH_CONFIG_FILES = {
			"conf_js/system_config.js",
			"conf_js/simulation_config.js",
			"conf_js/environment_config.js",
			"conf_js/foram_config.js"
	};
	private IConfigJS configJS;

	@Override
	public void init() throws ComponentException {
		List<Reader> configReaders = new ArrayList<>();
		try {
			ScriptEngineManager scriptEngineManager = new ScriptEngineManager();
			ScriptEngine scriptEngine = scriptEngineManager.getEngineByName("JavaScript");

			configReaders = getConfigReaders();
			for (Reader configReader : configReaders) {
				scriptEngine.eval(configReader);
			}

			Invocable invocable = (Invocable) scriptEngine;
			this.configJS = invocable.getInterface(IConfigJS.class);
		} catch (ScriptException e) {
			throw new ConfigServiceException("Cannot evaluate config script", e);
		} finally {
			configReaders.forEach(IOUtils::closeQuietly);
		}
	}

	@Override
	public boolean finish() throws ComponentException {
		return true;
	}

	public String getConfigAsString() throws ConfigServiceException {
		try {
			StringBuilder config = new StringBuilder();
			for (Reader reader : getConfigReaders()) {
				config.append(IOUtils.toString(reader));
			}
			return config.toString();
		} catch (IOException e) {
			throw new ConfigServiceException("Cannot read config.", e);
		}
	}

	public List<Reader> getConfigReaders() throws ConfigServiceException {
		List<Reader> readers = new ArrayList<>();
		String configPathsString = System.getProperty(CONFIG_PROPERTY);

		if (configPathsString != null) {
			logger.info("Loading config files:");
			String[] configPaths = StringUtils.split(configPathsString, '\u0000');
			try {
				for (String configPath : configPaths) {
					readers.add(new FileReader(configPath));
					logger.info("\t" + new File(configPath).getAbsolutePath());
				}
			} catch (IOException e) {
				throw new ConfigServiceException("Cannot read config files", e);
			}
		} else {
			for (String configFile : CLASSPATH_CONFIG_FILES) {
				InputStream configStream = getClass().getClassLoader().getResourceAsStream(configFile);
				if (configStream == null) {
					throw new ConfigServiceException("Cannot find " + configFile + " file in the classpath root directory.");
				}
				readers.add(new InputStreamReader(configStream));
			}
		}
		return readers;
	}

	public IConfigJS getConfigJS() {
		return configJS;
	}

	public static class ConfigServiceException extends ComponentException {
		public ConfigServiceException(String message) {
			super(message);
		}

		public ConfigServiceException(String message, Throwable cause) {
			super(message, cause);
		}
	}
}
