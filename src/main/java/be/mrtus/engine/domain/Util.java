package be.mrtus.engine.domain;

public class Util {

	public static double RADIAN_FACTOR = 180 / Math.PI;

	public static float map(float value, float min1, float max1, float min2, float max2) {
		return min2 + ((max2 - min2) * ((value - min1) / (max1 - min1)));
	}

	private Util() {
	}
}
