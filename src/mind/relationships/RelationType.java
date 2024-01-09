package mind.relationships;

import java.util.EnumSet;

import mind.concepts.relations.IInvertibleRelationType;
import mind.goals.IGoal;

public enum RelationType implements IInvertibleRelationType<RelationType> {

	/** Give a resource */
	GIVE(true, false, false, null, IGoal.Type.FLOW),
	/** Get a resource */
	GET(false, true, false, GIVE, IGoal.Type.FLOW),
	/** Be a member of a group/ do a job in a role */
	BE(true, false, true, null, IGoal.Type.MEMBERSHIP),
	/** Include a member in this group */
	INCLUDE(false, true, true, BE, IGoal.Type.MEMBERSHIP),
	/** relationship involving performing a specific Role */
	PERFORM(true, false, true, null, IGoal.Type.CONDUCT),
	/** relationship that requires the performance of a specific Role */
	REQUIRE_CONDUCT(false, true, false, PERFORM, IGoal.Type.CONDUCT),
	/**
	 * relationship requiring doing a Task
	 */
	DO(true, false, false, null, IGoal.Type.TASK),
	/**
	 * relationship requiring performance of the Task
	 */
	REQUIRE_TASK(false, true, false, DO, IGoal.Type.TASK),
	/** social relationship */
	FEEL(true, true, true, null, IGoal.Type.PERSONAL),
	/**
	 * a relationship characterized by this party making decisions for the other
	 * party
	 */
	GOVERN(true, false, true, null, IGoal.Type.MEMBERSHIP),
	/** being governed by the other party */
	GOVERNED_BY(false, true, true, GOVERN, IGoal.Type.MEMBERSHIP);

	private boolean provides;
	private boolean benefits;
	private RelationType opposite;
	private EnumSet<IGoal.Type> relationshipTypes;
	private boolean singular;
	private boolean isInverse;

	private RelationType(boolean provide, boolean benefit, boolean singular, RelationType opposite,
			IGoal.Type... types) {
		provides = provide;
		benefits = benefit;
		this.singular = singular;

		if (types.length > 1) {
			relationshipTypes = EnumSet.of(types[0], types);
		} else {
			relationshipTypes = EnumSet.of(types[0]);
		}
		if (opposite != null) {
			this.opposite = opposite;
			opposite.opposite = this;
			this.isInverse = true;
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

	public RelationType inverse() {
		return opposite;
	}

	@Override
	public boolean isInverseType() {
		return isInverse;
	}

	public EnumSet<IGoal.Type> getRelationshipTypes() {
		return relationshipTypes;
	}

	/**
	 * If there can only be one of this type of relation with a given party
	 * 
	 * @return
	 */
	public boolean isSingular() {
		return singular;
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
