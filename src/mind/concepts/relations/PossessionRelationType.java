package mind.concepts.relations;

public enum PossessionRelationType implements IConceptRelationType {
	HELD, WORN;

	private InverseType inverse = InverseType.from(this);

	@Override
	public boolean bidirectional() {
		return false;
	}

	@Override
	public boolean transformation() {
		return false;
	}

	@Override
	public boolean consumes() {
		return false;
	}

	@Override
	public boolean creates() {
		return false;
	}

	@Override
	public String idString() {
		return "possess_" + name();
	}

	@Override
	public IConceptRelationType inverse() {
		return inverse;
	}

}
