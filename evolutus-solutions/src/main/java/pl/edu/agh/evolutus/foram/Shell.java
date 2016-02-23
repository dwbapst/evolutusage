package pl.edu.agh.evolutus.foram;

import pl.edu.agh.evolutus.utils.Geometry;
import static java.lang.Math.*;

public class Shell {

	//this parameters are set according to value taken from genotype - they does not change during ontogenesis
	private final double firstChamberRadius;
	private final double wallThicknessFactor;
	private final double translationFactor;
	private final double rotationAngle;
	private final double deviationAngle;
	private final double growthFactor;


	private final double lastChamberRadius;
	private final int chambersCount;
	private final double volumeShell; //volume of space inside the shell

	public Shell(double firstChamberRadius, double lastChamberRadius, int chambersCount, double volume) {
		this.firstChamberRadius = firstChamberRadius;
		this.wallThicknessFactor = 0.1;
		this.rotationAngle = 0.0;
		this.deviationAngle = 0.0;
		this.growthFactor = 1.0;
		this.translationFactor = 1.0;
		this.lastChamberRadius = lastChamberRadius;
		this.chambersCount = chambersCount;
		this.volumeShell = volume;
	}

	public Shell(double firstChamberRadius,
                 double wallThicknessFactor,
                 double translationFactor,
				 double rotationAngle,
                 double deviationAngle,
                 double growthFactor,
				 int chambersCount) {
		this.firstChamberRadius = firstChamberRadius;
		this.wallThicknessFactor = wallThicknessFactor;
		this.rotationAngle = rotationAngle;
		this.deviationAngle = deviationAngle;
		this.growthFactor = growthFactor;
		this.translationFactor = translationFactor;

		this.chambersCount = chambersCount;
		this.lastChamberRadius = pow(growthFactor, (double)chambersCount-1.0)*firstChamberRadius;
		this.volumeShell = calculateShellVolume();
	}

	public double getFirstChamberRadius() {
		return firstChamberRadius;
	}

	public double getLastChamberRadius() {
		return lastChamberRadius;
	}

	public int getChambersCount() {
		return chambersCount;
	}

	public double getTranslationFactor() { return translationFactor; }

	public double getWallThicknessFactor() { return wallThicknessFactor; }

	public double getRotationAngle () { return rotationAngle; }

	public double getDeviationAngle() { return  deviationAngle; }

	public double getGrowthFactor() { return  growthFactor; }

	public double getVolumeShell() {
		return volumeShell;
	}

	public double getTortuosityFactor(){
		//this value indicated whether shell is mole elongated or coiled.
		//at this moment it is only a dummy
		return Math.abs(deviationAngle/180.0);
	}

	private double calculateShellVolume()
	{
		//this function calculates shell volume in a very simplified way!!!
		double volume = 0.0;
		double radius = firstChamberRadius;
		for(int i=0; i < chambersCount; i++)
		{
			volume += Geometry.sphereVolume(radius*(1-wallThicknessFactor));
			radius *= growthFactor;
		}
		return volume;
	}

}
