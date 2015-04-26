package pl.edu.agh.evolutus.simulation;

import javax.inject.Inject;

import org.jage.workplace.FixedStepCountStopCondition;

import pl.edu.agh.evolutus.service.config.SimulationConfig;

public class StopCondition extends FixedStepCountStopCondition {

	@Inject
	public StopCondition(SimulationConfig simulationConfig) {
		super(simulationConfig.simulationDuration());
	}
}
