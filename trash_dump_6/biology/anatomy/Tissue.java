package biology.anatomy;

import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

import actor.construction.physical.IMaterialLayer;
import actor.construction.properties.ISensableTrait;
import actor.construction.properties.SenseProperty;
import sim.physicality.PhysicalState;

public class Tissue implements IMaterialLayer {
	private ITissueLayerType type;
	private TreeMap<ITissueLayerType, Tissue> subLayers;
	private Map<SenseProperty<?>, Object> sensables;
	private PhysicalState state;
	private boolean usual = true;

	public Tissue(ITissueLayerType type, Map<String, ITissueLayerType> tissueTypes) {
		this.type = type;
		state = type.initialState();
		sensables = new HashMap<>();
		for (SenseProperty<?> prop : type.getSensableProperties()) {
			sensables.put(prop, type.getProperty(prop));
		}
		if (!type.getSublayers().isEmpty()) {
			subLayers = new TreeMap<>(Comparator.reverseOrder());
			for (String sublayer : type.getSublayers()) {
				if (tissueTypes.get(sublayer) == null)
					throw new IllegalStateException("sublayer tissue key " + sublayer
							+ " does not correspond to any tissue type; error while building tissue " + type.getName());
				ITissueLayerType layertype = tissueTypes.get(sublayer);
				subLayers.put(layertype, new Tissue(layertype, tissueTypes));

			}

		}
	}

	public Tissue(Tissue tiss) {
		this.type = tiss.type;
		this.sensables = new HashMap<>(tiss.sensables != null ? tiss.sensables : Collections.emptyMap());
		this.state = tiss.state;
		this.usual = tiss.usual;
		this.subLayers = new TreeMap<>(Comparator.reverseOrder());
		if (tiss.subLayers != null) {
			for (Tissue tissa : tiss.subLayers.values()) {
				this.subLayers.put(tissa.type, tissa);
			}
		}
	}

	@Override
	public <A extends ISensableTrait> A getProperty(SenseProperty<A> property, boolean ignoreType) {
		A obj = sensables == null ? null : (A) sensables.get(property);
		if (obj == null && !ignoreType)
			return this.type.getTrait(property);
		return obj;
	}

	@Override
	public Collection<SenseProperty<?>> getSensableProperties() {
		return this.sensables.keySet();
	}

	public ITissueLayerType getType() {
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
		if (subLayers != null) {
			for (Tissue layer : this.subLayers.values()) {
				if (!layer.usual) {
					usual = false;
					break;
				}
			}
		}
	}

	public Map<ITissueLayerType, Tissue> getSubLayers() {
		return subLayers == null ? Map.of() : subLayers;
	}

	public boolean isUsual() {
		return usual;
	}

	@Override
	public String toString() {
		return this.type + "{" + this.state + "}";
	}
}