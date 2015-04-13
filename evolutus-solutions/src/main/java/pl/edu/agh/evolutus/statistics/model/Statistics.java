package pl.edu.agh.evolutus.statistics.model;

import java.io.Serializable;
import java.sql.Timestamp;

public abstract class Statistics implements Serializable {

	private Long simulationStart;

	public Statistics() {
		// for morphia
	}

	public Statistics(Timestamp simulationStart) {
		this.simulationStart = simulationStart.getTime();
	}

	public Timestamp getSimulationStart() {
		return new Timestamp(simulationStart);
	}

}
