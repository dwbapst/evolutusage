package pl.edu.agh.evolutus.config;

import jdk.nashorn.internal.runtime.ScriptObject;
import pl.edu.agh.evolutus.environment.BoundaryConditions;
import pl.edu.agh.evolutus.genotype.Genome;
import pl.edu.agh.evolutus.utils.CurrentDirection;
import pl.edu.agh.evolutus.utils.VectorD;
import pl.edu.agh.evolutus.utils.VectorL;

public class SystemConfig {

	private final IConfigJS configJS;

	public SystemConfig(IConfigJS configJS) {
		this.configJS = configJS;
	}

	public String getDatabaseHost(){
		ScriptObject parameters = configJS.databaseParameters();
		return parameters.get("host").toString();
	}

	public int getDatabasePort(){
		ScriptObject parameters = configJS.databaseParameters();
		return parameters.getInt("port");
	}

	public boolean isDatabaseInMemory(){
		ScriptObject parameters = configJS.databaseParameters();
		return (boolean) parameters.get("inMemory");
	}

}
