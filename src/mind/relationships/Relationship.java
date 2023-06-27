package mind.relationships;

import java.util.UUID;

import mind.concepts.type.IMeme;
import mind.goals.IConduct;
import mind.goals.IGoal;
import mind.goals.IPersonalRelationship;
import mind.goals.IResource;
import mind.goals.ITaskGoal;

public class Relationship implements Comparable<Relationship>, IMeme {

	private UUID relationshipID;
	private RelationType type;
	private IGoal goal;

	public static Relationship govern() {
		return new Relationship(UUID.randomUUID(), RelationType.GOVERN, null);
	}

	public static Relationship governedBy() {
		return new Relationship(UUID.randomUUID(), RelationType.GOVERNED_BY, null);
	}

	public static Relationship social(IPersonalRelationship personalRelation) {
		return new Relationship(UUID.randomUUID(), RelationType.FEEL, personalRelation);
	}

	public static Relationship requireTask(ITaskGoal task) {
		return new Relationship(UUID.randomUUID(), RelationType.REQUIRE_TASK, task);
	}

	public static Relationship doTask(ITaskGoal task) {
		return new Relationship(UUID.randomUUID(), RelationType.DO, task);
	}

	public static Relationship requireConduct(IConduct conduct) {
		return new Relationship(UUID.randomUUID(), RelationType.REQUIRE_CONDUCT, conduct);
	}

	public static Relationship doConduct(IConduct conduct) {
		return new Relationship(UUID.randomUUID(), RelationType.PERFORM, conduct);
	}

	public static Relationship get(IResource resource) {
		return new Relationship(UUID.randomUUID(), RelationType.GET, resource);
	}

	public static Relationship be() {
		return new Relationship(UUID.randomUUID(), RelationType.BE, null);
	}

	public static Relationship include() {
		return new Relationship(UUID.randomUUID(), RelationType.INCLUDE, null);
	}

	public static Relationship give(IResource resource) {
		return new Relationship(UUID.randomUUID(), RelationType.GIVE, resource);
	}

	/**
	 * Agreement constructor; addition of properties to the agreement done later
	 * with methods The agreement ID is the unique identity of thte agreement
	 * 
	 * @param with
	 */
	private Relationship(UUID agreementId, RelationType type, IGoal resource) {
		this.relationshipID = agreementId;
		this.type = type;
		this.goal = resource;
	}

	/**
	 * gets the identity of this specific agreement
	 * 
	 * @return
	 */
	public UUID getAgreementID() {
		return relationshipID;
	}

	public IGoal getGoal() {
		return goal;
	}

	public RelationType getType() {
		return type;
	}

	/**
	 * Whether this relationship points resource/membership toward its owner
	 * 
	 * @return
	 */
	public boolean pointsHere() {
		return type.benefits();
	}

	/**
	 * Whether this relationship points resource/membership away from its owner
	 * 
	 * @return
	 */
	public boolean pointsAway() {
		return type.provides();
	}

	/**
	 * Use this to compare UUID's of the relationship
	 */
	@Override
	public int compareTo(Relationship o) {
		return this.relationshipID.compareTo(o.relationshipID);
	}

	/**
	 * Use this to compare properties of the relationship
	 */
	@Override
	public boolean equals(Object obj) {
		Relationship other = (Relationship) obj;
		return other.type == this.type && (this.goal == null ? other.goal == null : other.goal.equals(this.goal));
	}

	@Override
	public String getUniqueName() {
		return "rel" + this.relationshipID;
	}

	@Override
	public String toString() {
		return this.type + "_" + this.getUniqueName();
	}

}
