package mind.relationships;

import java.util.EnumSet;

import mind.goals.IGoal;

public enum RelationType {

	/** Give a resource */
	GIVE(true, false, null, IGoal.Type.FLOW),
	/** Get a resource */
	GET(false, true, GIVE, IGoal.Type.FLOW),
	/** Be a member of a group/ do a job in a role */
	BE(true, false, null, IGoal.Type.MEMBERSHIP),
	/** Include a member in this group */
	INCLUDE(false, true, BE, IGoal.Type.MEMBERSHIP),
	/** relationship involving performing a specific Role */
	PERFORM(true, false, null, IGoal.Type.CONDUCT),
	/** relationship that requires the performance of a specific Role */
	REQUIRE_CONDUCT(false, true, PERFORM, IGoal.Type.CONDUCT),
	/** relationship involving doing a specific task */
	DO(true, false, null, IGoal.Type.TASK),
	/** relationship requiring performance of a specific task */
	REQUIRE_TASK(false, true, DO, IGoal.Type.TASK),
	/** social relationship */
	FEEL(true, true, null, IGoal.Type.COMMUNITY),
	/**
	 * a relationship characterized by this party making decisions for the other
	 * party
	 */
	GOVERN(true, false, null, IGoal.Type.MEMBERSHIP),
	/** being governed by the other party */
	GOVERNED_BY(false, true, GOVERN, IGoal.Type.MEMBERSHIP);

	private boolean provides;
	private boolean benefits;
	private RelationType opposite;
	private EnumSet<IGoal.Type> relationshipTypes;

	private RelationType(boolean provide, boolean benefit, RelationType opposite, IGoal.Type... types) {
		provides = provide;
		benefits = benefit;

		if (types.length > 1) {
			relationshipTypes = EnumSet.of(types[0], types);
		} else {
			relationshipTypes = EnumSet.of(types[0]);
		}
		if (opposite != null) {
			this.opposite = opposite;
			opposite.opposite = this;
		} else if (provides && benefit) {
			this.opposite = this;
		}
	}

	/**
	 * whether the arrow of this relationship would point at the other party
	 * 
	 * @return
	 */
	public boolean provides() {
		return provides;
	}

	/**
	 * whether the arrow of this relationship would point at this party
	 * 
	 * @return
	 */
	public boolean benefits() {
		return benefits;
	}

	public RelationType opposite() {
		return opposite;
	}

	public EnumSet<IGoal.Type> getRelationshipTypes() {
		return relationshipTypes;
	}

	/**
	 * whether this relationship describes membership
	 * 
	 * @return
	 */
	public boolean isMembership() {
		return this == BE || this == INCLUDE;
	}

	/**
	 * whether this relationship describes resource flow
	 */
	public boolean isFlow() {
		return this == GIVE || this == GET;
	}
}
