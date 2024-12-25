package sim.physicality;

/**
 * Interactibility modes are used to both indicate what class of things
 * something is interactable with/visible to, and also for seers or interactors
 * to indicate whether they can interact with something. Basically, if the
 * Interactability number of a visage can be divided by any of the
 * interactability numbers of a seer, it is visible to it.
 * 
 * @author borah
 *
 */
public interface IInteractability {

	/**
	 * The prime number used to index the visibility/interactibility of this mode,
	 * so it can be multiplied with others
	 * 
	 * @return
	 */
	int primeFactor();

	String getName();

	/**
	 * Return a number combining these physicalities. literally just multiplication.
	 * 
	 * @param ins
	 * @return
	 */
	public static int combine(IInteractability... ins) {
		int prod = 1;
		for (IInteractability in : ins)
			prod *= in.primeFactor();
		return prod;
	}

	/**
	 * If the interactor's given interactabilities can interact with the int factor
	 * given
	 * 
	 * @param factor
	 * @param interactabilities
	 * @return
	 */
	public static boolean interactibleWith(int interactedTo, IInteractability... interactabilities) {
		for (IInteractability in : interactabilities) {
			if (interactedTo % in.primeFactor() == 0) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Non-varargs implementation of
	 * {@link #interactibleWith(int, IInteractability...)}
	 * 
	 * @param factor
	 * @param interactabilities
	 * @return
	 */
	public static boolean interactibleWith(int interactedTo, Iterable<IInteractability> interactibilities) {
		for (IInteractability in : interactibilities) {
			if (interactedTo % in.primeFactor() == 0) {
				return true;
			}
		}
		return false;
	}

	/**
	 * For when two objects collide: what kind of collision occurred
	 * 
	 * @author borah
	 *
	 */
	public static enum CollisionType {
		/** for when no collision occurs */
		NO_INTERSECTION,
		/** for when a collision occurs in the same plane */
		COLLISION,
		/** for when an intersection occurs but not in the same plane */
		CROSS_PLANE_INTERSECTION;

		/**
		 * if two objects are intersecting across planes
		 * 
		 * @return
		 */
		public boolean intersectingAcrossPlanes() {
			return this == CROSS_PLANE_INTERSECTION;
		}

		/**
		 * if objects neither collide nor intersect across planes
		 * 
		 * @return
		 */
		public boolean notIntersecting() {
			return this == NO_INTERSECTION;
		}

		/**
		 * if things are not touching, or in different planes
		 * 
		 * @return
		 */
		public boolean notColliding() {
			return this == CROSS_PLANE_INTERSECTION || this == NO_INTERSECTION;
		}

		/**
		 * if things are colliding in the same plane
		 * 
		 * @return
		 */
		public boolean colliding() {
			return this == COLLISION;
		}

		/**
		 * if things are intersecting (but may or may not be colliding)
		 * 
		 * @return
		 */
		public boolean intersecting() {
			return this == COLLISION || this == CROSS_PLANE_INTERSECTION;
		}
	}

}