package actor.construction.simple;

import java.util.Collection;
import java.util.Map;

import actor.construction.physical.IMaterialLayer;
import actor.construction.physical.IMaterialLayerType;
import actor.construction.properties.ISensableTrait;
import actor.construction.properties.SenseProperty;
import sim.physicality.PhysicalState;

public class SimpleMaterialLayer implements IMaterialLayer {
	private IMaterialLayerType type;
	private PhysicalState state;
	private boolean usual = true;

	public SimpleMaterialLayer(IMaterialLayerType type) {
		this.type = type;
		state = type.initialState();
	}

	public IMaterialLayerType getType() {
		return type;
	}

	public PhysicalState getState() {
		return state;
	}

	@Override
	public void changeState(PhysicalState newState) {
		this.state = newState;
		if (state != type.initialState()) {
			usual = false;
		} else {
			throw new IllegalArgumentException();
		}
	}

	@Override
	public <T extends ISensableTrait> T getProperty(SenseProperty<T> property, boolean ignoreType) {
		return ignoreType ? null : this.type.getTrait(property);
	}

	@Override
	public Collection<SenseProperty<?>> getSensableProperties() {
		return type.getSensableProperties();
	}

	public Map<IMaterialLayerType, SimpleMaterialLayer> getSubLayers() {
		return Map.of();
	}

	public boolean isUsual() {
		return usual;
	}

	@Override
	public String toString() {
		return this.type + "{" + this.state + "}";
	}
}