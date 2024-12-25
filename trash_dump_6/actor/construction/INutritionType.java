package actor.construction;

import java.util.Set;

/**
 * Interface to represent types of nutrition that material things can provide
 * when eaten
 * 
 * @author borah
 *
 */
public interface INutritionType {

	public static final Set<INutritionType> VEGETARIAN = Set.of(NutritionType.VEGETABLE, NutritionType.FRUIT);
	public static final Set<INutritionType> STANDARD_OMNIVORE = Set.of(NutritionType.VEGETABLE, NutritionType.FRUIT,
			NutritionType.MEAT, NutritionType.SEAFOOD, NutritionType.INSECT);
	public static final Set<INutritionType> CARNIVORE = Set.of(NutritionType.MEAT, NutritionType.SEAFOOD);

	/**
	 * The prime number used to index the edibility of this item, so it can be
	 * multiplied with others
	 * 
	 * @return
	 */
	int primeFactor();

	public String getName();

	/**
	 * Return a number combining these nutrition types. literally just
	 * multiplication.
	 * 
	 * @param ins
	 * @return
	 */
	public static int combine(INutritionType... ins) {
		int prod = 1;
		for (INutritionType in : ins)
			prod *= in.primeFactor();
		return prod;
	}

	/**
	 * If the eater can eat the food with the nutrition type given
	 * 
	 * @return
	 */
	public static boolean canEat(int food, INutritionType... eatAbilitieis) {
		for (INutritionType in : eatAbilitieis) {
			if (food % in.primeFactor() == 0) {
				return true;
			}
		}
		return false;
	}
}
