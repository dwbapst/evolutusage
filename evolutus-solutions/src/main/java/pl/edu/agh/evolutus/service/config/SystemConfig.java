package pl.edu.agh.evolutus.service.config;

import jdk.nashorn.internal.runtime.ScriptObject;

public class SystemConfig extends Config {

	public String getDatabaseHost() {
		ScriptObject parameters = configJS.databaseParameters();
		return parameters.get("host").toString();
	}

	public int getDatabasePort() {
		ScriptObject parameters = configJS.databaseParameters();
		return parameters.getInt("port");
	}

	public boolean isDatabaseInMemory() {
		ScriptObject parameters = configJS.databaseParameters();
		return (boolean) parameters.get("inMemory");
	}

}
