package pl.edu.agh.evolutus.foram;

import java.util.Random;

public class ForamType {

	public static final ForamType SEXUAL_DIPLOID = new ForamType(ReproductionType.SEXUAL, Ploidy.DIPLOID);
	public static final ForamType SEXUAL_HAPLOID = new ForamType(ReproductionType.SEXUAL, Ploidy.HAPLOID);
	public static final ForamType SEXUAL_ASEXUAL_DIPLOID = new ForamType(ReproductionType.SEXUAL_ASEXUAL, Ploidy.DIPLOID);
	public static final ForamType SEXUAL_ASEXUAL_HAPLOID = new ForamType(ReproductionType.SEXUAL_ASEXUAL, Ploidy.HAPLOID);

	private ReproductionType reproductionType;
	private Ploidy ploidy;

	private ForamType() {
		// for morphia
	}

	public ForamType(ReproductionType reproductionType, Ploidy ploidy) {
		this.reproductionType = reproductionType;
		this.ploidy = ploidy;
	}

	public ReproductionType getReproductionType() {
		return reproductionType;
	}

	public Ploidy getPloidy() {
		return ploidy;
	}

	@Override
	public String toString() {
		return reproductionType + "_" + ploidy;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || getClass() != o.getClass()) {
			return false;
		}

		ForamType foramType = (ForamType) o;

		return reproductionType == foramType.reproductionType && ploidy == foramType.ploidy;

	}

	@Override
	public int hashCode() {
		int result = reproductionType != null ? reproductionType.hashCode() : 0;
		result = 31 * result + (ploidy != null ? ploidy.hashCode() : 0);
		return result;
	}

	/*-***********
	 *   ENUMS   *
	 *************/

	public enum ReproductionType {
		SEXUAL, SEXUAL_ASEXUAL;

		public static ReproductionType fromString(String string) {
			string = string.toUpperCase().trim();
			return ReproductionType.valueOf(string);
		}
	}

	public enum Ploidy {
		HAPLOID, DIPLOID;

		private static Random random = new Random();

		public static Ploidy random() {
			return random.nextBoolean() ? HAPLOID : DIPLOID;
		}
	}
}
