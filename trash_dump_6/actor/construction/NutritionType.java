package actor.construction;

import java.util.HashSet;
import java.util.Set;

import utilities.MathHelp;

public enum NutritionType implements INutritionType {
	/**
	 * this provides nutrition of every kind. don't use to check if nutrition can be
	 * accepted
	 */
	ANYTHING_CAN_EAT(0),
	/**
	 * this cannot be eaten; alternatively, it can only be eaten by things which can
	 * eat literally anything.
	 */
	EATS_UNEATABLE(1),
	/**
	 * is a fruit; provides fruit nutrition
	 */
	FRUIT,
	/**
	 * is a vegetable; provides vegetable nutrition
	 */
	VEGETABLE,
	/**
	 * is an animal; provides animal nutrition
	 */
	MEAT,
	/**
	 * is seafood; provides seafood nutrition
	 */
	SEAFOOD,
	/**
	 * is an insect; provides insect nutrition
	 */
	INSECT,
	/**
	 * is wood; provides wood nutrition
	 */
	WOOD,
	/**
	 * is crystal; provides crystal nutrition
	 */
	CRYSTAL,
	/**
	 * for metaphysical foods like emotions or something
	 */
	METAPHYSICAL,
	/**
	 * for divine foods that gods eat or whatever
	 */
	AMBROSIA;

	private int pr;

	private NutritionType() {
		pr = nextPrime();
		System.out.println(this.name() + pr);
	}

	private NutritionType(int factor) {
		this.pr = factor;
	}

	@Override
	public String getName() {
		return toString();
	}

	@Override
	public int primeFactor() {
		return pr;
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

	/**
	 * return all nutrition types this value represents; do not include "all types"
	 * unless "for display" is true; do not include "uneatable" unless no nbutrition
	 * are present
	 * 
	 * @param physi
	 * @param forDisplay
	 * @return
	 */
	public static Set<NutritionType> decomposeCombinedValue(int physi, boolean forDisplay) {
		Set<NutritionType> set = new HashSet<>();
		if (physi == 1) {
			set.add(EATS_UNEATABLE);
			return set;
		}
		boolean excludedAny = false;
		for (NutritionType ex : NutritionType.values()) {
			if (ex.primeFactor() == 0 || ex.primeFactor() == 1)
				continue;
			if (physi % ex.primeFactor() == 0)
				set.add(ex);
			else
				excludedAny = true;
		}
		if (!excludedAny && forDisplay) {
			set.clear();
			set.add(ANYTHING_CAN_EAT);
		}
		return set;
	}

}
