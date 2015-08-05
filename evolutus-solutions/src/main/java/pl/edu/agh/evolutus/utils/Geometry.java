package pl.edu.agh.evolutus.utils;

import static java.lang.Math.*;

public class Geometry {

	public static double cuboidVolume(double x, double y, double z) {
		return x * y * z;
	}

	public static double sphereVolume(double radius) {
		return 4.0 / 3.0 * PI * pow(radius, 3.0);
	}
}
