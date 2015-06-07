package pl.edu.agh.evolutus.service.config;

import jdk.nashorn.internal.objects.NativeArray;
import jdk.nashorn.internal.runtime.ScriptObject;
import pl.edu.agh.evolutus.environment.BoundaryConditions;
import pl.edu.agh.evolutus.genotype.Genome;
import pl.edu.agh.evolutus.service.config.utils.EnvState;
import pl.edu.agh.evolutus.utils.CurrentDirection;
import pl.edu.agh.evolutus.utils.VectorD;
import pl.edu.agh.evolutus.utils.VectorL;

public class EnvironmentConfig extends Config {

	double unitLengthInMeters() {
		return configJS.unitLengthInMeters();
	}

	double stepDurationInHours() {
		return configJS.stepDurationInHours();
	}

	public VectorL oceanSize() {
		VectorD sizeMeters = VectorD.fromScriptObject(configJS.oceanSize());
		return unitsConverter.metersToUnits(sizeMeters);
	}

	public double algaeEnergy() {
		return configJS.algaeEnergy();
	}

	public long initialForamsCount(VectorL positionUnits) {
		VectorD positionMeters = unitsConverter.unitsToMeters(positionUnits);
		return configJS.initialForamsCount(positionMeters.x, positionMeters.y, positionMeters.z);
	}

	public double initialAlgaeAvailability(VectorL positionUnits) {
		VectorD positionMeters = unitsConverter.unitsToMeters(positionUnits);
		return configJS.initialAlgaeAvailability(positionMeters.x, positionMeters.y, positionMeters.z);
	}

	public double algaeGrowth(double insolation) {
		return configJS.algaeGrowth(insolation);
	}

	public double insolation(VectorL positionUnits) {
		VectorD positionMeters = unitsConverter.unitsToMeters(positionUnits);
		return configJS.insolation(positionMeters.x, positionMeters.y, positionMeters.z);
	}

	public CurrentDirection currentDirection(VectorL positionUnits) {
		VectorD positionMeters = unitsConverter.unitsToMeters(positionUnits);
		ScriptObject currentDirection = configJS.currentDirection(positionMeters.x, positionMeters.y, positionMeters.z);
		return new CurrentDirection(VectorD.fromScriptObject(currentDirection));
	}

	public BoundaryConditions boundaryConditions() {
		return BoundaryConditions.fromString(configJS.boundaryConditions());
	}

	public Genome initialGenome(EnvState envState) {
		NativeArray initialGenome = configJS.initialGenome(envState);
		return Genome.fromScriptObject(initialGenome);
	}
}
