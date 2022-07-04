package psych_first.mind;

import java.util.function.Function;
import java.util.function.Predicate;

import abilities.types.SystemType;
import culture.CulturalContext;
import culture.Culture;
import psych_first.action.goal.Goal.Priority;
import psych_first.actionstates.checks.ICheckable;
import sim.ICanHaveMind;
import sim.IHasCulture;
import sim.World;
import sociology.Profile;

public enum Need implements ICheckable<Integer>, IHasCulture {
	SATIATION("satiation", Culture.ORGANIC, Priority.NECESSITY, (m) -> m.getOwner().hasSystem(SystemType.HUNGER),
			(m) -> (int) m.getOwner().getSystem(SystemType.HUNGER).getHunger());

	private String name;
	private Function<Mind, Integer> getNeed;
	private Predicate<Mind> hasNeed;
	private Priority priority;
	private String culture;

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
	private Need(String name, String culture, Priority priority, Predicate<Mind> hasNeed,
			Function<Mind, Integer> getNeed) {
		this.name = name;
		this.getNeed = getNeed;
		this.hasNeed = hasNeed;
		this.priority = priority;
		this.culture = culture;
	}

	public String getCultureString() {
		return culture;
	}

	public Culture getCulture(World world) {
		return world.getCulture(culture);
	}

	public Priority getPriority() {
		return priority;
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

	@Override
	public Integer getValue(Profile p, CulturalContext ctxt) {
		if (!ctxt.isUniversal()) {
			if (!this.getCulture(ctxt.getWorld()).isSuperiorToAny(ctxt)) {
				return null;
			}
		}
		return p.getOwner() instanceof ICanHaveMind ? (((ICanHaveMind) p.getOwner()).hasMind()
				? ((ICanHaveMind) p.getOwner()).getMind().getNeeds().getNeed(this)
				: null) : null;
	}

	@Override
	public boolean hasValue(Profile p, CulturalContext ctxt) {
		if (!ctxt.isUniversal()) {
			if (!this.getCulture(ctxt.getWorld()).isSuperiorToAny(ctxt)) {
				return false;
			}
		}
		return p.getOwner() instanceof ICanHaveMind ? (((ICanHaveMind) p.getOwner()).hasMind()
				? ((ICanHaveMind) p.getOwner()).getMind().getNeeds().hasNeed(this)
				: false) : false;
	}

	@Override
	public void setValue(Profile p, Integer value) {

	}

}
