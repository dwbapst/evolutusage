package pl.edu.agh.evolutus.config;

public interface IConfigFactory {

	EnvironmentConfig getEnvironmentConfig();

	ForamConfig getForamConfig();

	SimulationConfig getSimulationConfig();
}
