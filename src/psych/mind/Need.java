package psych.mind;

import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.function.Predicate;

import entity.Eatable;
import entity.ICanHaveMind;
import entity.Thinker;
import psych.actionstates.traits.ICheckable;
import sociology.Profile;

public enum Need implements ICheckable<Integer> {
	SATIATION("satiation", (a) -> a.getOwner() instanceof Thinker,
			(a) -> a.getOwner() instanceof Thinker ? ((Thinker) a.getOwner()).getHunger() : null,
			(a, i) -> ((Thinker) a.getOwner()).setHunger(i), (a) -> a.getOwner() instanceof Eatable,
			(a) -> a.getOwner() instanceof Eatable ? ((Eatable) a.getOwner()).getNourishment() : null);

	private String name;
	private Function<Mind, Integer> getNeed;
	private Function<Profile, Integer> getFulfillmentValue;
	private Predicate<Mind> hasNeed;
	private Predicate<Profile> canFulfill;
	private BiConsumer<Mind, Integer> setNeed;

	/**
	 * function returns null if actor is invalid ; getNeed gets the official value
	 * of the need from the being ; getFulfillmentValue gets the official value of
	 * fulfillment for the need from this actor ; setNeed is the counterpart to
	 * getNeed ; hasNeed checks if the given mind belongs to an entity with the
	 * appropriate need ; canFulfill checks if the given entity has an interaction
	 * with the need ; getImpact is for actions unrelated to the need, i.e. the
	 * impact of running on one's hunger
	 * 
	 * @param name
	 * @param getNeed
	 */
	private Need(String name, Predicate<Mind> hasNeed, Function<Mind, Integer> getNeed,
			BiConsumer<Mind, Integer> setNeed, Predicate<Profile> canFulfill,
			Function<Profile, Integer> getFulfillmentValue) {
		this.name = name;
		this.getNeed = getNeed;
		this.setNeed = setNeed;
		this.hasNeed = hasNeed;
		this.canFulfill = canFulfill;
		this.getFulfillmentValue = getFulfillmentValue;
	}

	public String getName() {
		return name;
	}

	/**
	 * Function returns null if invalid
	 * 
	 * @param arg
	 * @return
	 */
	public Integer getNeedValue(Mind arg) {
		return hasNeed.test(arg) ? getNeed.apply(arg) : null;
	}

	public void changeNeedValue(Mind arg, int by) {
		this.setNeedValue(arg, this.getNeedValue(arg) + by);
	}

	public void setNeedValue(Mind arg, int arg2) {
		if (hasNeed.test(arg))
			setNeed.accept(arg, arg2);
	}

	public Integer getFulfillmentValue(Profile arg) {
		return canFulfill.test(arg) ? getFulfillmentValue.apply(arg) : null;
	}

	@Override
	public Integer getValue(Profile p) {
		return p.getOwner() instanceof ICanHaveMind
				? (((ICanHaveMind) p.getOwner()).hasMind() ? ((ICanHaveMind) p.getOwner()).getMind().getNeed(this)
						: null)
				: null;
	}

	@Override
	public void setValue(Profile p, Integer value) {

	}

}
