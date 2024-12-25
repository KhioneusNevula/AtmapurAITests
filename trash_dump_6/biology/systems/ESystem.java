package biology.systems;

import java.util.stream.Stream;

import actor.construction.ISystemHolder;
import civilization_and_minds.mind.goals.INeed;

/**
 * a system of an entity which reflects its state
 * 
 * @author borah
 *
 */
public abstract class ESystem {

	private ISystemHolder owner;
	private SystemType<?> type;
	// protected Multimap<INeedType, INeed> needs;

	/**
	 * TODO add body part related stuff
	 * 
	 * @param name
	 * @param owner
	 */
	protected ESystem(SystemType<?> type, ISystemHolder owner) {
		this.owner = owner;
		this.type = type;
	}

	public SystemType<?> getType() {
		return type;
	}

	public String getName() {
		return type.getId();
	}

	public ISystemHolder getOwner() {
		return owner;
	}

	public final void _update(long ticks) {
		if (!this.canUpdate())
			return;
		this.update(ticks);
		for (SystemType<?> type : this.type.getRequiredSystems()) {
			if (!this.owner.hasSystem(type)) {
				throw new IllegalStateException("Owner does not have required system " + type);
			}
			this.update(type, this.owner.getSystem(type));
		}
	}

	/**
	 * Generate needs for this system
	 * 
	 * @param forBeing
	 * @return
	 */
	public abstract Stream<? extends INeed> generateNeeds();

	/**
	 * Whether this system has undergone a change in severity of needs since the
	 * last time {@link #generateNeeds()} was called.
	 * 
	 * @return
	 */
	public abstract boolean changedStateSinceLastUpdatedNeeds();

	/*
	 * protected void postNeed(INeed need) { (this.needs == null ? needs =
	 * MultimapBuilder.hashKeys().hashSetValues().build() :
	 * needs).put(need.getType(), need); }
	 * 
	 * protected void clearNeeds(INeedType type) { if (this.needs != null) {
	 * needs.get(type).clear(); if (needs.isEmpty()) needs = null; } }
	 * 
	 * public Multimap<INeedType, INeed> getNeeds() { return needs == null ?
	 * ImmutableMultimap.of() : needs; }
	 */

	/**
	 * updates every tick if this system constantly updates
	 */
	protected void update(long ticks) {
	}

	/**
	 * Whether this system periodically updates; can change real time due to
	 * circumstances
	 * 
	 * @return
	 */
	public boolean canUpdate() {
		return false;
	}

	/**
	 * runs for each required system when this system updates; runs after the update
	 * method
	 * 
	 * @param other
	 */
	protected void update(SystemType<?> type, ESystem other) {

	}

	public abstract String report();

	@Override
	public String toString() {
		return this.type.toString();
	}

}
