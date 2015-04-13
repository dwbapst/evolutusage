package pl.edu.agh.evolutus.statistics.model;

import java.sql.Timestamp;
import java.util.Map;

import pl.edu.agh.evolutus.genotype.Genotype;

public class ForamFossil extends Statistics {

	private static final long serialVersionUID = 1L;

	private Long deathStepNo;
	private Integer age;
	private Map<String, Double> genotype;
	private Long x;
	private Long y;
	private Long z;

	private ForamFossil() {
		// for morphia
	}

	public ForamFossil(
			Timestamp simulationStart,
			Long deathStepNo,
			Integer age,
			Genotype genotype,
			Long x,
			Long y,
			Long z
	) {
		super(simulationStart);
		this.deathStepNo = deathStepNo;
		this.age = age;
		this.genotype = genotype.toMap();
		this.x = x;
		this.y = y;
		this.z = z;
	}

	public Long getDeathStepNo() {
		return deathStepNo;
	}

	public Integer getAge() {
		return age;
	}

	public Map<String, Double> getGenotype() {
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
