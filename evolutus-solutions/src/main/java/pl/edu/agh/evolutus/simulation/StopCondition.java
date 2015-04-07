package pl.edu.agh.evolutus.simulation;

import javax.inject.Inject;

import org.jage.workplace.FixedStepCountStopCondition;

import pl.edu.agh.evolutus.config.ConfigFactory;

public class StopCondition extends FixedStepCountStopCondition {

	@Inject
	public StopCondition(ConfigFactory configFactory) {
		super(configFactory.getSimulationConfig().simulationDuration());
	}
}
