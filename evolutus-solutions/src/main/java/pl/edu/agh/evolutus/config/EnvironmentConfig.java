package pl.edu.agh.evolutus.config;

import jdk.nashorn.internal.objects.NativeArray;
import jdk.nashorn.internal.runtime.ScriptObject;
import pl.edu.agh.evolutus.environment.BoundaryConditions;
import pl.edu.agh.evolutus.genotype.Genome;
import pl.edu.agh.evolutus.utils.CurrentDirection;
import pl.edu.agh.evolutus.utils.VectorD;
import pl.edu.agh.evolutus.utils.VectorL;

public class EnvironmentConfig {

	private final IConfigJS configJS;

	public EnvironmentConfig(IConfigJS configJS) {
		this.configJS = configJS;
	}

	double unitLengthInMeters() {
		return configJS.unitLengthInMeters();
	}

	double stepDurationInHours() {
		return configJS.stepDurationInHours();
	}

	public VectorL oceanSize() {
		VectorD sizeMeters = VectorD.fromScriptObject(configJS.oceanSize());
		return metersToUnits(sizeMeters);
	}

	public double algaeEnergy() {
		return configJS.algaeEnergy();
	}

	public long initialForamsCount(VectorL positionUnits) {
		VectorD positionMeters = unitsToMeters(positionUnits);
		return configJS.initialForamsCount(positionMeters.x, positionMeters.y, positionMeters.z);
	}

	public double initialEnergy() {
		return configJS.initialEnergy();
	}

	public double initialAlgaeAvailability(VectorL positionUnits) {
		VectorD positionMeters = unitsToMeters(positionUnits);
		return configJS.initialAlgaeAvailability(positionMeters.x, positionMeters.y, positionMeters.z);
	}

	public double algaeGrowth(double insolation) {
		return configJS.algaeGrowth(insolation);
	}

	public double insolation(VectorL positionUnits) {
		VectorD positionMeters = unitsToMeters(positionUnits);
		return configJS.insolation(positionMeters.x, positionMeters.y, positionMeters.z);
	}

	public CurrentDirection currentDirection(VectorL positionUnits) {
		VectorD positionMeters = unitsToMeters(positionUnits);
		ScriptObject currentDirection = configJS.currentDirection(positionMeters.x, positionMeters.y, positionMeters.z);
		return new CurrentDirection(VectorD.fromScriptObject(currentDirection));
	}

	public BoundaryConditions boundaryConditions() {
		return BoundaryConditions.fromString(configJS.boundaryConditions());
	}

	public Genome initialGenome(VectorL positionUnits) {
		VectorD positionMeters = unitsToMeters(positionUnits);
		NativeArray initialGenome = configJS.initialGenome(positionMeters.x, positionMeters.y, positionMeters.z);
		return Genome.fromScriptObject(initialGenome);
	}

	public String crossingOverOperator() {
		return configJS.crossingOverOperator();
	}

	double unitsToMeters(long units) {
		return units * unitLengthInMeters();
	}

	VectorD unitsToMeters(VectorL units) {
		return units.mul(unitLengthInMeters());
	}

	long metersToUnits(double meters) {
		return Math.round(meters / unitLengthInMeters());
	}

	VectorL metersToUnits(VectorD meters) {
		return meters.div(unitLengthInMeters()).toLong();
	}

	double stepsToHours(long steps) {
		return steps * stepDurationInHours();
	}

	long hoursToSteps(double hours) {
		return Math.round(hours / stepDurationInHours());
	}
}
