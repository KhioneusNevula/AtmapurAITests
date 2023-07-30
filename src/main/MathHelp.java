package main;

public class MathHelp {

	public static int clamp(int a, int b, int value) {
		if (a >= b)
			throw new IllegalArgumentException(a + ", " + b);
		return Math.min(Math.max(value, a), b);
	}

	public static double clamp(double a, double b, double value) {
		if (a >= b)
			throw new IllegalArgumentException(a + ", " + b);
		return Math.min(Math.max(value, a), b);
	}

	public static float clamp(float a, float b, float value) {
		if (a >= b)
			throw new IllegalArgumentException(a + ", " + b);
		return Math.min(Math.max(value, a), b);
	}
}
