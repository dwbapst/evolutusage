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

	public long simulationDuration() {
		return unitsConverter.hoursToSteps(configJS.simulationDuration());
	}
}
