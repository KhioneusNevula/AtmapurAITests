package sim.interfaces;

public interface IDynamicsObject extends IPhysicalEntity {

	/**
	 * accelerate by given factor in x and y
	 * 
	 * @param x
	 * @param y
	 */
	public void accelerate(float xVec, float yVec);

	public float getXVelocity();

	public float getYVelocity();

	/**
	 * Whether this object is not moving
	 * 
	 * @return
	 */
	public default boolean isStationary() {
		return getXVelocity() == 0 && getYVelocity() == 0;
	}

	/**
	 * if this objectt experiences friction
	 * 
	 * @return
	 */
	public boolean hasFriction();

	/**
	 * If this object experiences drag force
	 * 
	 * @return
	 */
	public boolean experiencesDrag();

	/**
	 * Get mass of this object
	 * 
	 * @return
	 */
	public float getMass();

	/**
	 * Apply a force to this object
	 * 
	 * @param xVec
	 * @param yVec
	 */
	public void applyForce(Force force, boolean ignoreFriction);

	/**
	 * Applies force, factoring in friction
	 * 
	 * @param force
	 */
	public default void applyForce(Force force) {
		this.applyForce(force, false);
	}

	/**
	 * Forces (measured in newtons)
	 * 
	 * @author borah
	 *
	 */
	public static class Force {
		private float xForce;
		private float yForce;
		private float mag;

		public static Force fromAngleInRadians(double radians, float magnitude) {
			return new Force((float) (magnitude * Math.cos(radians)), (float) (magnitude * Math.sin(radians)),
					magnitude);
		}

		public static Force fromAngleInDegrees(int angleDegrees, float magnitude) {
			return new Force((float) (magnitude * Math.cos(Math.toRadians(angleDegrees))),
					(float) (magnitude * Math.sin(Math.toRadians(angleDegrees))), magnitude);
		}

		/**
		 * xVec and yVec must define a directional vector, which will be normalized
		 * 
		 * @param xVec
		 * @param yVec
		 * @param magnitude
		 * @return
		 */
		public static Force fromVectorAndMagnitude(float xVec, float yVec, float magnitude) {
			float dirmag = (float) Math.sqrt(xVec * xVec + yVec * yVec);
			float x = xVec * magnitude / dirmag;
			float y = yVec * magnitude / dirmag;
			return new Force(x, y, magnitude);
		}

		public static Force fromComponents(float xForce, float yForce) {
			return new Force(xForce, yForce);
		}

		/**
		 * x and y acceleration alongside mass
		 * 
		 * @param xvec
		 * @param yvec
		 * @param mass
		 * @return
		 */
		public static Force fromAcceleration(float xvec, float yvec, int mass) {
			return new Force(xvec * mass, yvec * mass);
		}

		private Force(float xVec, float yVec, float mag) {
			this.xForce = xVec;
			this.yForce = yVec;
			this.mag = mag;
		}

		private Force(float xf, float yf) {
			this.xForce = xf;
			this.yForce = yf;
			this.mag = (float) Math.sqrt(xForce * xForce + yForce * yForce);
		}

		public float getXForce() {
			return xForce;
		}

		public float getYForce() {
			return yForce;
		}

		public float getMagnitude() {
			return mag;
		}

		@Override
		public String toString() {
			return String.format("%.2f", this.mag) + "N";
		}

	}
}
