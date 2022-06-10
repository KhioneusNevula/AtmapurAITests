package _nonsense;

public abstract class ActionCondition {

	private ConditionType<?> type;

	public ActionCondition(ConditionType<?> type) {
		this.type = type;
	}

	public ConditionType<?> getType() {
		return type;
	}

	public boolean isAll() {
		return this.type == ConditionType.ALL_PROFILE || this.type == ConditionType.ALL_RELATIONAL;
	}

	public boolean matches(ActionCondition other) {
		return this.type == other.type || other.isAll() || this.isAll();
	}

}
