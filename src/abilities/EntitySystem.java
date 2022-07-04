package abilities;

import abilities.types.SystemType;

/**
 * a system of an entity which reflects its state
 * 
 * @author borah
 *
 */
public abstract class EntitySystem {

	private ISystemHolder owner;
	private SystemType<?> type;

	/**
	 * TODO add body part related stuff
	 * 
	 * @param name
	 * @param owner
	 */
	protected EntitySystem(SystemType<?> type, ISystemHolder owner) {
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
		if (!this.isConstantUpdate())
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
	public boolean isConstantUpdate() {
		return false;
	}

	/**
	 * runs for each required system when this system updates; runs after the update
	 * method
	 * 
	 * @param other
	 */
	protected void update(SystemType<?> type, EntitySystem other) {

	}

	public abstract String report();

	@Override
	public String toString() {
		return this.type.toString();
	}

}
