package pl.edu.agh.evolutus.service.config;

import jdk.nashorn.internal.runtime.ScriptObject;

public class SystemConfig extends Config {

	public String getDatabaseHost() {
		ScriptObject parameters = configJS.databaseParameters();
		return parameters.get("host").toString();
	}

	public int getDatabasePort() {
		ScriptObject parameters = configJS.databaseParameters();
		return (int) parameters.get("port");
	}

	public boolean isDatabaseInMemory() {
		ScriptObject parameters = configJS.databaseParameters();
		return (boolean) parameters.get("inMemory");
	}
    public String getDatabasePath() {
		ScriptObject parameters = configJS.databaseParameters();
		return parameters.get("dbpath").toString();
	}
}
