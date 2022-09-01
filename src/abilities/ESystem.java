package abilities;

import java.util.Collections;
import java.util.Map;

import psychology.perception.info.BruteTrait;

/**
 * a system of an entity which reflects its state
 * 
 * @author borah
 *
 */
public abstract class ESystem {

	private ISystemHolder owner;
	private SystemType<?> type;

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
	 * updates every tick if this system constantly updates
	 */
	protected void update(long ticks) {
	}

	/**
	 * initializes a trait or traits that this system can create
	 * 
	 * @return
	 */
	public Map<BruteTrait<?>, Object> initTraits() {

		return Collections.emptyMap();
	}

	/**
	 * called after every update-systems iteration
	 * 
	 * @param trait
	 * @return
	 */
	public <T> T updateTrait(BruteTrait<T> trait, Object oldval) {

		return (T) oldval;
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
