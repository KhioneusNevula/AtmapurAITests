package _nonsense;

import java.util.UUID;

import _nonsense.ConditionType.RelationalType;
import entity.Actor;

public class RelationalCondition extends ActionCondition implements Comparable<RelationalCondition> {

	public static final RelationalCondition ALL = new RelationalCondition();
	private UUID id = UUID.randomUUID();

	public RelationalCondition(ConditionType.RelationalType type) {
		super(type);
	}

	private RelationalCondition() {
		super(RelationalType.ALL_RELATIONAL);
	}

	@Override
	public ConditionType.RelationalType getType() {
		return (RelationalType) super.getType();
	}

	public boolean check(Actor a, ProfileCondition<?> con) {
		return this.getType().check(a, con);
	}

	@Override
	public int compareTo(RelationalCondition o) {
		return id.compareTo(o.id);
	}

}
