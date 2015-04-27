package pl.edu.agh.evolutus.statistics.model;

public class OceanFragmentInfo extends Statistics {

	private static final long serialVersionUID = 1L;

	private Long stepNo;
	private Long x;
	private Long y;
	private Long z;
	private Integer foramsCount;
	private Double algaeAvailability;
	private Double totalEnergy;
	private Double insolation;

	private OceanFragmentInfo() {
		// for morphia
	}

	public OceanFragmentInfo(
			Simulation simulation,
			Long stepNo,
			Long x,
			Long y,
			Long z,
			Integer foramsCount,
			Double algaeAvailability,
			Double totalEnergy,
			Double insolation
	) {
		super(simulation.getSimulationStart());
		this.stepNo = stepNo;
		this.x = x;
		this.y = y;
		this.z = z;
		this.foramsCount = foramsCount;
		this.algaeAvailability = algaeAvailability;
		this.totalEnergy = totalEnergy;
		this.insolation = insolation;
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
