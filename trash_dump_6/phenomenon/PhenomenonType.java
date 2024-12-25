package phenomenon;

import java.util.Collection;
import java.util.EnumSet;
import java.util.Map;
import java.util.Set;

import com.google.common.collect.ImmutableMap;

import actor.construction.properties.ISensableTrait;
import actor.construction.properties.SenseProperty;
import actor.construction.properties.SenseProperty.BasicColor;
import actor.construction.properties.SenseProperty.BasicShape;
import actor.construction.properties.SenseProperty.BasicSmell;
import actor.construction.properties.SenseProperty.BasicSound;
import actor.construction.properties.SenseProperty.BasicTexture;

public enum PhenomenonType implements IPhenomenonType {
	/**
	 * the daytime
	 */
	DAY(true, Properties.WORLD_PHENOMENON, Properties.AMBIENT_LIGHT),
	/**
	 * Nighttime
	 */
	NIGHT(true, Properties.WORLD_PHENOMENON, Properties.AMBIENT_DARK),
	/** the/a sun in the sky */
	SUN(Map.of(SenseProperty.COLOR, BasicColor.YELLOW, SenseProperty.SHAPE, BasicShape.CIRCLE),
			Properties.WORLD_PHENOMENON, Properties.IN_SKY, Properties.AMBIENT_LIGHT),
	/** the/a moon in the sky */
	MOON(Map.of(SenseProperty.COLOR, BasicColor.GRAY, SenseProperty.SHAPE, BasicShape.CIRCLE),
			Properties.WORLD_PHENOMENON, Properties.IN_SKY, Properties.AMBIENT_LIGHT),
	/** damage to a creature */
	DAMAGE(true, Properties.AFFECTS_OBJECT, Properties.HAS_SOURCE, Properties.TRANSFORMS_OBJECT),
	/** death of an animal or plant */
	DEATH(true, Properties.AFFECTS_OBJECT, Properties.TRANSFORMS_OBJECT),
	/** existence of fire on an object */
	FLAME(Map.of(SenseProperty.COLOR, BasicColor.ORANGE, SenseProperty.SMELL, BasicSmell.BURNING, SenseProperty.SOUND,
			BasicSound.CRACKLING), Properties.AFFECTS_OBJECT),
	/** explosion occurrence */
	EXPLOSION(true, Map.of(SenseProperty.SOUND, BasicSound.BOOM), Properties.REMOVES_OBJECT),
	/** the event where something transforms by burning, e.g. wood charring */
	BURNING(true, Map.of(SenseProperty.SMELL, BasicSmell.BURNING), Properties.AFFECTS_OBJECT,
			Properties.TRANSFORMS_OBJECT),
	/**
	 * the event where something is removed by burning, e.g. grass burning up
	 */
	BURN_UP(true, Map.of(SenseProperty.SMELL, BasicSmell.BURNING), Properties.AFFECTS_OBJECT,
			Properties.REMOVES_OBJECT),
	/** existence of rain somewhere */
	RAIN(Map.of(SenseProperty.SMELL, BasicSmell.WATERY, SenseProperty.SOUND, BasicSound.RAIN, SenseProperty.TEXTURE,
			BasicTexture.WET), Properties.CREATES_FLUID),
	/** event where something melts */
	MELTING(true, Properties.TRANSFORMS_OBJECT),
	/** event where something freezes */
	FREEZING(true, Properties.TRANSFORMS_OBJECT),
	/** event where smoething becomes wet */
	HYDRATION(true, Properties.AFFECTS_OBJECT),
	/** event where something loses liquid */
	DRYING(true, Properties.AFFECTS_OBJECT, Properties.REMOVES_FLUID),
	/** event of bearing young, or fruit from a tree */
	PRODUCE(true, Properties.HAS_SOURCE, Properties.CREATES_OBJECT);

	private EnumSet<Properties> props;
	private boolean isEvent;
	private Map<SenseProperty<?>, ?> sensables;

	private PhenomenonType(boolean event, Properties... properties) {
		this(event, Map.of(), properties);
	}

	private PhenomenonType(Map<SenseProperty<?>, ?> sensables, Properties... properties) {
		this(sensables, Set.of(properties));
	}

	private PhenomenonType(boolean event, Map<SenseProperty<?>, ?> sensables, Properties... properties) {
		this(sensables, Set.of(properties));
		this.isEvent = event;
	}

	private PhenomenonType(Map<SenseProperty<?>, ?> sensables, Set<Properties> props) {
		this.props = EnumSet.copyOf(props);
		this.sensables = ImmutableMap.copyOf(sensables);
	}

	@Override
	public Collection<SenseProperty<?>> getDefaultSensableProperties() {
		return this.sensables.keySet();
	}

	@Override
	public <A extends ISensableTrait> A getDefaultSensableProperty(SenseProperty<A> property) {
		return (A) sensables.get(property);
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
