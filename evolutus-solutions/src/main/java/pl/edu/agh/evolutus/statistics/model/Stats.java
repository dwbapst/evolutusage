package pl.edu.agh.evolutus.statistics.model;

import java.sql.Timestamp;

public class Stats implements java.io.Serializable {

	private static final long serialVersionUID = 1L;

	private Long simulationStart;
	private Long stepNo;
	private Long x;
	private Long y;
	private Long z;
	private Integer foramsCount;
	private Double algaeAvailability;
	private Double totalEnergy;
	private Double insolation;

	private Stats() {
		// for morphia
	}

	public Stats(
			java.sql.Timestamp simulationStart,
			Long stepNo,
			Long x,
			Long y,
			Long z,
			Integer foramsCount,
			Double algaeAvailability,
			Double totalEnergy,
			Double insolation
	) {
		this.simulationStart = simulationStart.getTime();
		this.stepNo = stepNo;
		this.x = x;
		this.y = y;
		this.z = z;
		this.foramsCount = foramsCount;
		this.algaeAvailability = algaeAvailability;
		this.totalEnergy = totalEnergy;
		this.insolation = insolation;
	}

	public java.sql.Timestamp getSimulationStart() {
		return new Timestamp(this.simulationStart);
	}

	public Long getStepNo() {
		return this.stepNo;
	}

	public Long getX() {
		return this.x;
	}

	public Long getY() {
		return this.y;
	}

	public Long getZ() {
		return this.z;
	}

	public Integer getForamsCount() {
		return this.foramsCount;
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
}
