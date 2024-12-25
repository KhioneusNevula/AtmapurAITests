package sim.physicality;

import java.util.HashSet;
import java.util.Set;

import utilities.MathHelp;

/**
 * Standard interaction modes, corresponding to the "planes of existence," so to
 * speak
 * 
 * @author borah
 *
 */
public enum ExistencePlane implements IInteractability {
	/**
	 * Indicates something is always interactable in every plane. should not be used
	 * as a seer mode
	 */
	ALL_PLANES(0),
	/**
	 * depending on usage, this is either interactable by nothing or able to
	 * interact with anything
	 */
	ONLY_KNOWN_TO_GOD(1),
	/** standard mode for things that are physical */
	PHYSICAL,
	/** standard mode for things that are spiritual */
	SPIRITUAL,
	/** standard mode for things in the mind(?) */
	MENTAL,
	/** standard mode for divine entities */
	DIVINE;

	private int prime;

	private ExistencePlane(int prime) {
		this.prime = prime;
		System.out.println(this.name() + prime);
	}

	private ExistencePlane() {
		this.prime = nextPrime();
	}

	@Override
	public String getName() {
		return toString();
	}

	@Override
	public int primeFactor() {
		return prime;
	}

	/**
	 * index of the next prime number to generate
	 */
	private static int primeIndex = 0;

	/**
	 * return next prime in sequence
	 * 
	 * @return
	 */
	public static int nextPrime() {
		return MathHelp.getNthPrime(primeIndex++);
	}

	public static int currentPrimeIndex() {
		return primeIndex;
	}

	public static Set<ExistencePlane> decomposeCombinedValue(int physi) {
		return decomposeCombinedValue(physi, false);
	}

	/**
	 * return all planes this value represents; do not include "all planes" unless
	 * "for display" is true; do not include "only known to god" unless no planes
	 * are present
	 * 
	 * @param physi
	 * @param forDisplay
	 * @return
	 */
	public static Set<ExistencePlane> decomposeCombinedValue(int physi, boolean forDisplay) {
		Set<ExistencePlane> set = new HashSet<>();
		if (physi == 1) {
			set.add(ONLY_KNOWN_TO_GOD);
			return set;
		}
		boolean excludedAny = false;
		for (ExistencePlane ex : ExistencePlane.values()) {
			if (ex.primeFactor() == 0 || ex.primeFactor() == 1)
				continue;
			if (physi % ex.primeFactor() == 0)
				set.add(ex);
			else
				excludedAny = true;
		}
		if (!excludedAny && forDisplay) {
			set.clear();
			set.add(ALL_PLANES);
		}
		return set;
	}
}