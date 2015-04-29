package pl.edu.agh.evolutus.statistics.model;

import java.sql.Timestamp;

public class Simulation extends Statistics {

	private static final long serialVersionUID = 1L;

	private String name;

	private String description;

	private String config;

	private String simulationStartString;

	private Simulation() {
		// for morphia
	}

	public Simulation(long simulationStart, String name, String description, String config) {
		super(new Timestamp(simulationStart));
		this.name = name;
		this.description = description;
		this.config = config;
		this.simulationStartString = getSimulationStart().toString();
	}

	public String getName() {
		return name;
	}

	public String getDescription() {
		return description;
	}

	public String getConfig() {
		return config;
	}

	public String getSimulationStartString() {
		return simulationStartString;
	}
}
