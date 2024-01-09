package mind.concepts.relations;

import java.util.HashMap;
import java.util.Map;

/**
 * any kind of relationship indicating someone (this end) utilizes something
 * (the other end) for a general purpose defined by the action argument.
 */
public class UseRelationType implements IConceptRelationType {

	private static final Map<String, UseRelationType> types = new HashMap<>();

	public static final UseRelationType EAT = make("eat", true);
	public static final UseRelationType BURN = make("burn", true);

	private String name;
	private InverseType inverse;
	private boolean consumes;

	private UseRelationType(String name, boolean consumes) {
		this.name = name;
		inverse = InverseType.from(this);
		this.consumes = consumes;
	}

	public static UseRelationType make(String name) {
		return make(name, false);
	}

	public static UseRelationType make(String name, boolean consumes) {
		if (types.containsKey(name))
			return types.get(name);
		UseRelationType t = new UseRelationType(name, consumes);
		types.put(name, t);
		return t;
	}

	public static UseRelationType fromName(String name) {
		return types.get(name);
	}

	@Override
	public boolean bidirectional() {
		return false;
	}

	@Override
	public boolean transformation() {
		return true;
	}

	@Override
	public boolean consumes() {
		return consumes;
	}

	@Override
	public boolean creates() {
		return false;
	}

	@Override
	public String idString() {
		return "use_" + name;
	}

	@Override
	public IConceptRelationType inverse() {
		return inverse;
	}

	@Override
	public boolean subtypeOf(IConceptRelationType other) {
		return this.equals(other) || other == ConceptRelationType.USES;
	}

}
