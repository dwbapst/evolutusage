package pl.edu.agh.evolutus.statistics.model;

import java.sql.Timestamp;

public class Simulation extends Statistics {

	private static final long serialVersionUID = 1L;

	private String simulationStartString;

	private Simulation() {
		// for morphia
	}

	public Simulation(long simulationStart) {
		this(new Timestamp(simulationStart));
	}

	public Simulation(Timestamp simulationStart) {
		super(simulationStart);
		simulationStartString = simulationStart.toString();
	}

	public String getSimulationStartString() {
		return simulationStartString;
	}
}
