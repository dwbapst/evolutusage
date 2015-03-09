package pl.edu.agh.evolutus.config;

import org.jage.address.agent.AgentAddress;

import pl.edu.agh.evolutus.genotype.Genome;
import pl.edu.agh.evolutus.genotype.operator.CrossingOverOperator;
import pl.edu.agh.evolutus.utils.CurrentDirection;
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
		return new CurrentDirection(VectorL.fromScriptObject(configJS.currentDirection(position.x, position.y, position.z)));
	}

	public double currentStrength(VectorL position) {
		return configJS.currentStrength(position.x, position.y, position.z);
	}

	public Genome initialGenome(VectorL position, AgentAddress foramIdentifier) {
		return Genome.fromScriptObject(configJS.initialGenome(position.x, position.y, position.z),
				foramIdentifier.toQualifiedString());
	}

	public String crossingOverOperator(){
		return configJS.crossingOverOperator();
	}

}
