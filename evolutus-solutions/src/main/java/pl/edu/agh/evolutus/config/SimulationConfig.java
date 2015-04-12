package pl.edu.agh.evolutus.config;

public class SimulationConfig {

	private final IConfigJS configJS;

	public SimulationConfig(IConfigJS configJS) {
		this.configJS = configJS;
	}

	public long simulationDuration() {
		return hoursToSteps(configJS.simulationDuration());
	}

	long hoursToSteps(double hours) {
		return Math.round(hours / configJS.stepDurationInHours());
	}

}
