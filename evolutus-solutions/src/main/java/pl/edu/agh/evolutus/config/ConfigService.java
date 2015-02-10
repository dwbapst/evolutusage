package pl.edu.agh.evolutus.config;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import javax.script.Invocable;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

import org.apache.commons.io.IOUtils;

import jdk.nashorn.api.scripting.ScriptObjectMirror;
import pl.edu.agh.evolutus.utils.CurrentDirection;
import pl.edu.agh.evolutus.utils.Vector;

public class ConfigService implements IConfigService {

	public static final String CONFIG_LOCATION = "config.js";
	private final Invocable config;

	public ConfigService() throws ConfigServiceException {
		InputStream configStream = getClass().getClassLoader().getResourceAsStream(CONFIG_LOCATION);
		if (configStream == null) {
			throw new ConfigServiceException("Cannot find config.js file in the classpath root directory.");
		}
		try {
			String configScriptString = IOUtils.toString(configStream);
			ScriptEngineManager scriptEngineManager = new ScriptEngineManager();
			ScriptEngine scriptEngine = scriptEngineManager.getEngineByName("JavaScript");
			scriptEngine.eval(configScriptString);
			this.config = (Invocable) scriptEngine;
		} catch (IOException e) {
			throw new ConfigServiceException("Cannot read config files", e);
		} catch (ScriptException e) {
			throw new ConfigServiceException("Cannot evaluate config script", e);
		}
	}

	private Map<String, Object> constantsMap = new HashMap<>();

	private <T> T call(String function, Class<T> returnClass, Object... args) {
		try {
			if (args.length == 0 && constantsMap.containsKey(function)) {
				return (T) constantsMap.get(function);
			}

			T result = (T) config.invokeFunction(function, args);

			if (args.length == 0) {
				constantsMap.put(function, result);
			}
			return result;
		} catch (ScriptException | NoSuchMethodException e) {
			throw new ConfigServiceRuntimeException(e.getMessage(), e);
		}
	}


	/* *********************** *
	 *        SIMULATION       *
	 * *********************** */

	@Override
	public int getSimulationDuration() {
		return call("simulationDuration", Integer.class);
	}

	/* *********************** *
	 *       ENVIRONMENT       *
	 * *********************** */

	@Override
	public Vector getOceanSize() {
		ScriptObjectMirror result = call("oceanSize", ScriptObjectMirror.class);
		return scriptObjectToVector(result);
	}

	@Override
	public double getAlgaeEnergy() {
		return call("algaeEnergy", Double.class);
	}

	@Override
	public long getInitialForamsCount(Vector position) {
		Double result = call("initialForamsCount", Double.class, position.x(), position.y(), position.z());
		return result.longValue();
	}

	@Override
	public double getInitialAlgaeAvailability(Vector position) {
		return call("initialAlgaeAvailability", Double.class, position.x(), position.y(), position.z());
	}

	@Override
	public double getAlgaeGrowth(double insolation) {
		return call("algaeGrowth", Double.class, insolation);
	}

	@Override
	public double getInsolation(Vector position) {
		return call("insolation", Double.class, position.x(), position.y(), position.z());
	}

	@Override
	public CurrentDirection getCurrentDirection(Vector position) {
		ScriptObjectMirror result = call("currentDirection", ScriptObjectMirror.class, position.x(), position.y(), position.z());
		return new CurrentDirection(scriptObjectToVector(result));
	}

	@Override
	public double getCurrentStrength(Vector position) {
		return call("currentStrength", Double.class, position.x(), position.y(), position.z());
	}

	/* ************************* *
	 *           FORAM           *
	 * ************************* */

	@Override
	public double getForamInitialEnergy() {
		return call("initialEnergy", Double.class);
	}

	@Override
	public double getEnergyCapacity(int chambersCount) {
		return call("energyCapacity", Double.class, chambersCount);
	}

	@Override
	public double getEnergyDemand(int chambersCount) {
		return call("energyDemand", Double.class, chambersCount);
	}

	@Override
	public double getChamberGrowthEnergyCost(int chambersCount) {
		return call("chamberGrowthEnergyCost", Double.class, chambersCount);
	}

	@Override
	public double getEnergyNeededForGrowth() {
		return call("energyNeededForGrowth", Double.class);
	}

	@Override
	public double getGrowthProbability() {
		return call("growthProbability", Double.class);
	}

	@Override
	public int getChambersLimit() {
		return call("chambersLimit", Integer.class);
	}

	@Override
	public int getNewBornLimit() {
		return call("newBornLimit", Integer.class);
	}

	@Override
	public double getEnergyNeededToReproduce() {
		return call("energyNeededToReproduce", Double.class);
	}

	@Override
	public double getReproductionProbability() {
		return call("reproductionProbability", Double.class);
	}

	private Vector scriptObjectToVector(ScriptObjectMirror scriptObject) {
		return new Vector((int) scriptObject.get("x"), (int) scriptObject.get("y"), (int) scriptObject.get("z"));
	}

	public static class ConfigServiceException extends Exception {
		public ConfigServiceException(String message) {
			super(message);
		}

		public ConfigServiceException(String message, Throwable cause) {
			super(message, cause);
		}
	}

	public static class ConfigServiceRuntimeException extends RuntimeException {
		public ConfigServiceRuntimeException(String message) {
			super(message);
		}

		public ConfigServiceRuntimeException(String message, Throwable cause) {
			super(message, cause);
		}
	}
}
