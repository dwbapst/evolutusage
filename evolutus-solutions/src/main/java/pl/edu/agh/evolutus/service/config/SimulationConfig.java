package pl.edu.agh.evolutus.service.config;

public class SimulationConfig extends Config {

	public long simulationDuration() {
		return unitsConverter.hoursToSteps(configJS.simulationDuration());
	}
}
