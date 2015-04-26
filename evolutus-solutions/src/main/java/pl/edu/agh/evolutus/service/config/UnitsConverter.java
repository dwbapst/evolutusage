package pl.edu.agh.evolutus.service.config;

import javax.inject.Inject;

import pl.edu.agh.evolutus.utils.VectorD;
import pl.edu.agh.evolutus.utils.VectorL;

public class UnitsConverter {

	private final IConfigJS configJS;

	@Inject
	public UnitsConverter(ConfigFactory configFactory) {
		this.configJS = configFactory.getConfigJS();
	}

	public double unitsToMeters(long units) {
		return units * configJS.unitLengthInMeters();
	}

	public VectorD unitsToMeters(VectorL units) {
		return units.mul(configJS.unitLengthInMeters());
	}

	public long metersToUnits(double meters) {
		return Math.round(meters / configJS.unitLengthInMeters());
	}

	public VectorL metersToUnits(VectorD meters) {
		return meters.div(configJS.unitLengthInMeters()).toLong();
	}

	public double stepsToHours(long steps) {
		return steps * configJS.stepDurationInHours();
	}

	public long hoursToSteps(double hours) {
		return Math.round(hours / configJS.stepDurationInHours());
	}
}
