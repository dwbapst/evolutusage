package pl.edu.agh.evolutus.service.config;

public class SimulationConfig extends Config {

	public String simulationName() {
		return configJS.simulationName();
	}

	public String simulationDescription() {
		return configJS.simulationDescription();
	}

	public long simulationDuration() {
		return unitsConverter.hoursToSteps(configJS.simulationDuration());
	}

    public int statsStride() { return  configJS.statsStride(); }
}
