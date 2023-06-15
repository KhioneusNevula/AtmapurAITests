package biology.anatomy;

import java.util.Comparator;
import java.util.Map;
import java.util.TreeMap;

import sim.PhysicalState;

public class Tissue {
	private ITissueLayerType type;
	private TreeMap<ITissueLayerType, Tissue> subLayers;
	private PhysicalState state;
	private boolean usual = true;

	public Tissue(ITissueLayerType type, Map<String, ITissueLayerType> tissueTypes) {
		this.type = type;
		state = type.initialState();
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

	public ITissueLayerType getType() {
		return type;
	}

	public PhysicalState getState() {
		return state;
	}

	public void changeState(PhysicalState newState) {
		this.state = newState;
		if (state != type.initialState())
			usual = false;
		else {
			usual = true;
			if (subLayers != null) {
				for (Tissue layer : this.subLayers.values()) {
					if (!layer.usual) {
						usual = false;
						break;
					}
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