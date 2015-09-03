package pl.edu.agh.evolutus.statistics.model;

import java.util.Map;

import pl.edu.agh.evolutus.foram.ForamType;
import pl.edu.agh.evolutus.genotype.Genotype;

public class ForamFossil extends Statistics {

	private static final long serialVersionUID = 1L;

	private String foramId;
	private String firstParentId;
	private String secondParentId;

	private Double deathHour;
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
			String foramId,
			Simulation simulation,
			Double deathHour,
			Double age,
			ForamType foramType,
			Genotype genotype,
			Long x,
			Long y,
			Long z
	) {
		super(simulation.getSimulationStart());
		this.foramId = foramId;
		this.firstParentId = genotype.getFirstParentId();
		this.secondParentId = genotype.getSecondParentId();

		this.deathHour = deathHour;
		this.age = age;
		this.isDiploid = genotype.isDiploid();
		this.foramType = foramType;
		this.genotype = genotype.toFossilizationMap();
		this.x = x;
		this.y = y;
		this.z = z;
	}

	public String getForamId() {
		return foramId;
	}

	public String getFirstParentId() {
		return firstParentId;
	}

	public String getSecondParentId() {
		return secondParentId;
	}

	public Double getDeathHour() {
		return deathHour;
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
