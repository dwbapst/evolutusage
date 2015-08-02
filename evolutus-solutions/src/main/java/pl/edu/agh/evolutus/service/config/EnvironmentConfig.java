package pl.edu.agh.evolutus.service.config;

import jdk.nashorn.internal.objects.NativeArray;
import jdk.nashorn.internal.runtime.ScriptObject;
import pl.edu.agh.evolutus.environment.BoundaryConditions;
import pl.edu.agh.evolutus.genotype.Genome;
import pl.edu.agh.evolutus.service.config.utils.EnvState;
import pl.edu.agh.evolutus.utils.CurrentDirection;
import pl.edu.agh.evolutus.utils.Position;
import pl.edu.agh.evolutus.utils.VectorD;
import pl.edu.agh.evolutus.utils.VectorL;

public class EnvironmentConfig extends Config {

	double unitLengthInMeters() {
		return configJS.unitLengthInMeters();
	}

	double stepDurationInHours() {
		return configJS.stepDurationInHours();
	}

	private VectorL oceanSize = null;
	private BoundaryConditions boundaryConditions = null;

	public VectorL oceanSize() {
		if (oceanSize == null) {
			VectorD sizeMeters = VectorD.fromScriptObject(configJS.oceanSize());
			oceanSize = unitsConverter.metersToUnits(sizeMeters);
		}
		return oceanSize;
	}

	public long initialForamsCount(VectorL positionUnits) {
		VectorD positionMeters = unitsConverter.unitsToMeters(positionUnits);
		return configJS.initialForamsCount(positionMeters.x, positionMeters.y, positionMeters.z);
	}

	public double initialAlgaeAvailability(VectorL positionUnits) {
		VectorD positionMeters = unitsConverter.unitsToMeters(positionUnits);
		return configJS.initialAlgaeAvailability(positionMeters.x, positionMeters.y, positionMeters.z);
	}

	public BoundaryConditions boundaryConditions() {
		if (boundaryConditions == null) {
			boundaryConditions = BoundaryConditions.fromString(configJS.boundaryConditions());
		}
		return boundaryConditions;
	}

	public double algaeEnergy(long steps, EnvState[] envStates) {
		return configJS.algaeEnergy(unitsConverter.stepsToHours(steps), envStates);
	}

	public double algaeGrowth(long steps, EnvState[] envStates) {
		return configJS.algaeGrowth(unitsConverter.stepsToHours(steps), envStates);
	}

	public double oxygen(long steps, EnvState[] envStates) {
		return configJS.oxygen(unitsConverter.stepsToHours(steps), envStates);
	}

	public double temperature(long steps, EnvState[] envStates) {
		return configJS.temperature(unitsConverter.stepsToHours(steps), envStates);
	}

	public double salinity(long steps, EnvState[] envStates) {
		return configJS.salinity(unitsConverter.stepsToHours(steps), envStates);
	}

	public double insolation(long steps, EnvState[] envStates) {
		return configJS.insolation(unitsConverter.stepsToHours(steps), envStates);
	}

	public double ph(long steps, EnvState[] envStates) {
		return configJS.ph(unitsConverter.stepsToHours(steps), envStates);
	}

	public CurrentDirection currentDirection(long steps, EnvState[] envStates) {
		ScriptObject currentDirection = configJS.currentDirection(unitsConverter.stepsToHours(steps), envStates);
		return new CurrentDirection(VectorD.fromScriptObject(currentDirection));
	}

	public Genome initialGenome(Position position) {
		NativeArray initialGenome = configJS.initialGenome(unitsConverter.unitsToMeters(position));
		return Genome.fromScriptObject(initialGenome);
	}
}
