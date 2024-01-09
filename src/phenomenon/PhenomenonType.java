package phenomenon;

import java.util.Collection;
import java.util.EnumSet;
import java.util.Set;

import biology.systems.types.ISensor;
import mind.concepts.type.Property;
import mind.memory.IPropertyData;

public enum PhenomenonType implements IPhenomenonType {
	/** damage to a creature */
	DAMAGE(true, Properties.AFFECTS_OBJECT, Properties.HAS_SOURCE, Properties.TRANSFORMS_OBJECT),
	/** death of an animal or plant */
	DEATH(true, Properties.AFFECTS_OBJECT, Properties.TRANSFORMS_OBJECT),
	/** existence of fire on an object */
	FLAME(Properties.AFFECTS_OBJECT),
	/** explosion occurrence */
	EXPLOSION(true, Properties.REMOVES_OBJECT),
	/** the event where something transforms by burning, e.g. wood charring */
	BURNING(true, Properties.AFFECTS_OBJECT, Properties.TRANSFORMS_OBJECT),
	/**
	 * the event where something is removed by burning, e.g. grass burning up
	 */
	BURN_UP(Properties.AFFECTS_OBJECT, Properties.REMOVES_OBJECT),
	/** existence of rain somewhere */
	RAIN(Properties.CREATES_FLUID),
	/** event where something melts */
	MELTING(true, Properties.TRANSFORMS_OBJECT),
	/** event where something freezes */
	FREEZING(true, Properties.TRANSFORMS_OBJECT),
	/** event where smoething becomes wet */
	HYDRATION(true, Properties.AFFECTS_OBJECT),
	/** event where something loses liquid */
	DRYING(true, Properties.AFFECTS_OBJECT, Properties.REMOVES_FLUID),
	/** event of bearing young, or fruit from a tree */
	BEAR(true, Properties.HAS_SOURCE, Properties.CREATES_OBJECT);

	private EnumSet<Properties> props;
	private boolean isEvent;

	private PhenomenonType(Properties... properties) {
		this(Set.of(properties));
	}

	private PhenomenonType(boolean event, Properties... properties) {
		this(Set.of(properties));
		this.isEvent = event;
	}

	private PhenomenonType(Set<Properties> props) {
		this.props = EnumSet.copyOf(props);
	}

	@Override
	public boolean hasProperty(Properties property) {
		return props.contains(property);
	}

	@Override
	public EnumSet<Properties> getProperties() {
		return props;
	}

	@Override
	public Collection<ISensor> getPreferredSensesForHint(Property property) {
		// TODO prefsenses
		return IPhenomenonType.super.getPreferredSensesForHint(property);
	}

	@Override
	public IPropertyData getPropertyHint(Property property) {
		// TODO property hint
		return IPhenomenonType.super.getPropertyHint(property);
	}

	@Override
	public boolean isEvent() {
		return isEvent;
	}

	@Override
	public String getUniqueName() {
		return "phenom_type_" + this.name();
	}

	@Override
	public float averageUniqueness() {
		return 0.0001f;
	}

}
