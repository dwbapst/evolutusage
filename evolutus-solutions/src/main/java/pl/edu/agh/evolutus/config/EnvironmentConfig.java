package pl.edu.agh.evolutus.config;

import org.jage.address.agent.AgentAddress;

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

	public VectorL oceanSize() {
		return VectorL.fromScriptObject(configJS.oceanSize());
	}

	public double algaeEnergy() {
		return configJS.algaeEnergy();
	}

	public long initialForamsCount(VectorL position) {
		return configJS.initialForamsCount(position.x, position.y, position.z);
	}

	public double initialEnergy() {
		return configJS.initialEnergy();
	}

	public double initialAlgaeAvailability(VectorL position) {
		return configJS.initialAlgaeAvailability(position.x, position.y, position.z);
	}

	public double algaeGrowth(double insolation) {
		return configJS.algaeGrowth(insolation);
	}

	public double insolation(VectorL position) {
		return configJS.insolation(position.x, position.y, position.z);
	}

	public CurrentDirection currentDirection(VectorL position) {
		return new CurrentDirection(VectorD.fromScriptObject(configJS.currentDirection(position.x, position.y, position.z)));
	}

	public BoundaryConditions boundaryConditions() {
		return BoundaryConditions.fromString(configJS.boundaryConditions());
	}

	public Genome initialGenome(VectorL position) {
		return Genome.fromScriptObject(configJS.initialGenome(position.x, position.y, position.z));
	}

	public String crossingOverOperator() {
		return configJS.crossingOverOperator();
	}

}
