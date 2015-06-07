package pl.edu.agh.evolutus.statistics.model;

import java.util.Map;

import pl.edu.agh.evolutus.foram.ForamType;
import pl.edu.agh.evolutus.genotype.Genotype;

public class ForamFossil extends Statistics {

	private static final long serialVersionUID = 1L;

	private Long deathStepNo;
	private Double age;
	private boolean isDiploid;
	private ForamType foramType;
	private Map<String, Double[]> genotype;
	private Long x;
	private Long y;
	private Long z;

	private ForamFossil() {
		// for morphia
	}

	public ForamFossil(
			Simulation simulation,
			Long deathStepNo,
			Double age,
			ForamType foramType,
			Genotype genotype,
			Long x,
			Long y,
			Long z
	) {
		super(simulation.getSimulationStart());
		this.deathStepNo = deathStepNo;
		this.age = age;
		this.isDiploid = genotype.isDiploid();
		this.foramType = foramType;
		this.genotype = genotype.toFossilizationMap();
		this.x = x;
		this.y = y;
		this.z = z;
	}

	public Long getDeathStepNo() {
		return deathStepNo;
	}

	public Double getAge() {
		return age;
	}

	public boolean isDiploid() {
		return isDiploid;
	}

	public ForamType getForamType() {
		return foramType;
	}

	public Map<String, Double[]> getGenotype() {
		return genotype;
	}

	public Long getX() {
		return x;
	}

	public Long getY() {
		return y;
	}

	public Long getZ() {
		return z;
	}
}
