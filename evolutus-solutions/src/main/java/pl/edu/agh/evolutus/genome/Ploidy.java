package pl.edu.agh.evolutus.genome;

public enum Ploidy {
	HAPLOID, DIPLOID;

	public static Ploidy fromString(String string){
		string = string.toUpperCase().trim();
		return Ploidy.valueOf(string);
	}
}
