package pl.edu.agh.evolutus.statistics.model;

import pl.edu.agh.evolutus.utils.VectorD;

public class OceanFragmentInfo extends Statistics {

	private static final long serialVersionUID = 1L;

	private Long stepNo;
	private Double x;
	private Double y;
	private Double z;
	private Integer foramsCount;
	private Integer foramsHaploCount;
	private Integer foramsDiploCount;
	private Double algaeAvailability;
	private Double totalEnergy;
	private Double insolation;
	private Integer deathCount;
	private Integer birthCount;
	private Double averageShellVolume;

	private OceanFragmentInfo() {
		// for morphia
	}

	public OceanFragmentInfo(
			Simulation simulation,
			Long stepNo,
			VectorD position,
			Integer foramsCount,
			Integer foramsHaploidCount,
			Integer foramsDiploidCount,
			Double algaeAvailability,
			Double totalEnergy,
			Double insolation,
			Integer deathCount,
			Integer birthCount,
			Double averageShellVolume
	) {
		super(simulation.getSimulationStart());
		this.stepNo = stepNo;
		this.x = position.x;
		this.y = position.y;
		this.z = position.z;
		this.foramsCount = foramsCount;
		this.foramsHaploCount = foramsHaploidCount;
		this.foramsDiploCount = foramsDiploidCount;
		this.algaeAvailability = algaeAvailability;
		this.totalEnergy = totalEnergy;
		this.insolation = insolation;
		this.deathCount = deathCount;
		this.birthCount = birthCount;
		this.averageShellVolume = averageShellVolume;
	}

	public Long getStepNo() {
		return this.stepNo;
	}

	public Double getX() {
		return this.x;
	}

	public Double getY() {
		return this.y;
	}

	public Double getZ() {
		return this.z;
	}

	public Integer getForamsCount() {
		return this.foramsCount;
	}

	public Integer getHaploidForamsCount() {
		return this.foramsHaploCount;
	}

	public Integer getDiploidForamsCount() {
		return this.foramsDiploCount;
	}

	public Double getAlgaeAvailability() {
		return this.algaeAvailability;
	}

	public Double getTotalEnergy() {
		return this.totalEnergy;
	}

	public Double getInsolation() {
		return this.insolation;
	}

	public Integer getDeathCount() { return this.deathCount; }

	public Integer getBirthCount() { return this.birthCount; }

	public Double getAverageShellVolume() { return this.averageShellVolume; }
}
