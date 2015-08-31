package pl.edu.agh.evolutus.cmd.util;

import org.mongodb.morphia.logging.Logger;
import org.mongodb.morphia.logging.LoggerFactory;
import org.mongodb.morphia.logging.SilentLogger;

public class MorphiaSilentLoggerFactory implements LoggerFactory {
	@Override
	public Logger get(Class<?> c) {
		return new SilentLogger();
	}
}
