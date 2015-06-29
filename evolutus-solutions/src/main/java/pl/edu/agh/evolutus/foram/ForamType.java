package pl.edu.agh.evolutus.foram;

public enum ForamType {

	PLANKTONIC(false, true),
	HAPLOID_BENTHIC(true, false),
	DIPLOID_BENTHIC(true, true);

	private final boolean isBenthic;
	private final boolean isDiploid;

	ForamType(boolean isBenthic, boolean isDiploid) {
		this.isBenthic = isBenthic; //remove
		//this.isActiveMoving = false/true;
		//this.reproductionType = sexual | asexual/sexual
		this.isDiploid = isDiploid;
	}

	public boolean isBenthic() {
		return isBenthic;
	}

	public boolean isDiploid() {
		return isDiploid;
	}
}
