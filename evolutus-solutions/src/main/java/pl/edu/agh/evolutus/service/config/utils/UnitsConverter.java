package pl.edu.agh.evolutus.service.config.utils;

import javax.inject.Inject;

import pl.edu.agh.evolutus.service.config.ConfigFactory;
import pl.edu.agh.evolutus.service.config.IConfigJS;
import pl.edu.agh.evolutus.utils.VectorD;
import pl.edu.agh.evolutus.utils.VectorL;

public class UnitsConverter {

	private final IConfigJS configJS;

	@Inject
	public UnitsConverter(ConfigFactory configFactory) {
		this.configJS = configFactory.getConfigJS();
	}

	public VectorD unitsToMeters(VectorL units) {
		VectorD unitLength = unitLengthInMeters();
		return new VectorD(units.x * unitLength.x, units.y * unitLength.y, units.z * unitLength.z);
	}

	public VectorL metersToUnits(VectorD meters) {
		VectorD unitLength = unitLengthInMeters();
		return new VectorD(meters.x / unitLength.x, meters.y / unitLength.y, meters.z / unitLength.z).toLong();
	}

	public double stepsToHours(long steps) {
		return steps * configJS.stepDurationInHours();
	}

	public long hoursToSteps(double hours) {
		return Math.round(hours / configJS.stepDurationInHours());
	}

	public VectorD unitLengthInMeters() {
		return VectorD.fromScriptObject(configJS.unitLengthInMeters());
	}

	public double stepDurationInHours() {
		return configJS.stepDurationInHours();
	}

}
