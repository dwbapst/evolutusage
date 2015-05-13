package pl.edu.agh.evolutus.service.config;

public class SimulationConfig extends Config {

	public String simulationName() {
		return configJS.simulationName();
	}

	public String simulationDescription() {
		return configJS.simulationDescription();
	}

	public boolean virtualFossilizationEnabled() {
		return configJS.virtualFossilizationEnabled();
	}

	public boolean generatePSI() {
		return configJS.generatePSI();
	}

	public boolean generateCSV() {
		return configJS.generateCSV();
	}

	public boolean generateHTML() {
		return configJS.generateHTML();
	}

	public long simulationDuration() {
		return unitsConverter.hoursToSteps(configJS.simulationDuration());
	}
}
