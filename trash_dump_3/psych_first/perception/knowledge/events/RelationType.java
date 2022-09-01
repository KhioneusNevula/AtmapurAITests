package psych_first.perception.knowledge.events;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import psych_first.perception.knowledge.IOccurrence;
import psych_first.perception.knowledge.Identity;
import sim.IHasProfile;
import sociology.IProfile;

public class RelationType {
	public static enum MemberType {
		CIRCUMSTANCE, INDIVIDUAL, EITHER, OTHER;

		public boolean allowsCircumstance() {
			return this == CIRCUMSTANCE || this == EITHER;
		}

		public boolean allowsIndividual() {
			return this == INDIVIDUAL || this == EITHER;
		}

		public boolean isOther() {
			return this == OTHER;
		}

		public boolean isCompatible(Object o) {
			if (o instanceof IOccurrence || (o instanceof IProfile ip && ip.getOwner() instanceof IOccurrence)) {
				return this.allowsCircumstance();
			} else if (o instanceof IHasProfile || o instanceof Identity || (o instanceof IProfile)) {
				return this.allowsIndividual();
			} else {
				if (this.isOther()) {
					return true;
				}
				throw new IllegalArgumentException();
			}
		}

	}

	public static enum TimeType {
		BEFORE, DURING, AFTER, BEFORE_AND_AFTER, BEFORE_AND_DURING, AFTER_AND_DURING, NOT_APPLICABLE;

		public boolean isNotApplicable() {
			return this == NOT_APPLICABLE;
		}

		public boolean isDuring() {
			return this == DURING || this == BEFORE_AND_DURING || this == AFTER_AND_DURING;
		}

		public boolean isBefore() {
			return this == BEFORE || this == BEFORE_AND_AFTER || this == BEFORE_AND_DURING;
		}

		public boolean isAfter() {
			return this == AFTER || this == BEFORE_AND_AFTER || this == AFTER_AND_DURING;
		}

		public RelationType.TimeType reverse() {
			switch (this) {
			case BEFORE:
				return AFTER;
			case AFTER:
				return BEFORE;
			case BEFORE_AND_DURING:
				return AFTER_AND_DURING;
			case AFTER_AND_DURING:
				return BEFORE_AND_DURING;
			default:
				return this;

			}
		}
	}

	/**
	 * represents the relationship of a circumstance that is true during an event or
	 * two events that are simultaneous
	 */
	public static final RelationType ALONG_WITH = of("ALONG_WITH", TimeType.DURING, false, true)
			.subjectType(MemberType.CIRCUMSTANCE).predType(MemberType.CIRCUMSTANCE).asStatic();
	public static final RelationType BEFORE = of("BEFORE", TimeType.BEFORE, false, "AFTER")
			.subjectType(MemberType.CIRCUMSTANCE).predType(MemberType.CIRCUMSTANCE).asStatic();
	public static final RelationType AFTER = BEFORE.converse();
	/**
	 * indicates both a subject and object role in an event, specifically the target
	 * of the event. E.g. the being who died for a death event, the sun for a sun
	 * rising event, etc
	 */
	public static final RelationType OCCURRING_TO = of("OCCURRING_TO", TimeType.NOT_APPLICABLE, false, false)
			.subjectType(MemberType.CIRCUMSTANCE);
	/**
	 * indicates what was locationally with this
	 */
	public static final RelationType PRESENT_WITH = of("PRESENT_WITH", TimeType.NOT_APPLICABLE, true, true).asStatic();
	/**
	 * indicates the origin of a given profile as from another
	 */
	public static final RelationType FROM = of("FROM", TimeType.AFTER_AND_DURING, true, false).asStatic();
	/**
	 * indicates an event as being done with a recipient in mind
	 */
	public static final RelationType FOR = of("FOR", TimeType.NOT_APPLICABLE, false, false)
			.subjectType(MemberType.CIRCUMSTANCE).predType(MemberType.EITHER).asStatic();

	public static final RelationType TO = of("TO", TimeType.NOT_APPLICABLE, true, false);
	public static final RelationType AT = of("AT", TimeType.NOT_APPLICABLE, true, true).subjectType(MemberType.EITHER)
			.predType(MemberType.EITHER).asStatic();
	public static final RelationType INSIDE = of("INSIDE", TimeType.NOT_APPLICABLE, true, "CONTAINS").asStatic();
	/**
	 * converse of {@link #INSIDE}
	 */
	public static final RelationType CONTAINS = INSIDE.converse();
	public static final RelationType OUTSIDE = of("OUTSIDE", TimeType.NOT_APPLICABLE, true, true).asStatic();
	public static final RelationType CONSTITUTES = of("CONSTITUTES", TimeType.NOT_APPLICABLE, false, "CONSTITUTES_OF")
			.asStatic();
	/**
	 * converse of {@link #CONSTITUTES}
	 */
	public static final RelationType CONSTITUTES_OF = CONSTITUTES.converse();
	/**
	 * can also be interpreted as "by"
	 */
	public static final RelationType BECAUSE_OF = of("BECAUSE_OF", TimeType.AFTER, false, "CAUSED")
			.predType(MemberType.EITHER).subjectType(MemberType.CIRCUMSTANCE);
	/**
	 * converse of {@link #BECAUSE_OF}
	 */
	public static final RelationType CAUSED = BECAUSE_OF.converse();
	/*
	 * public static final RelationType AGAINST = of("AGAINST",
	 * TimeType.NOT_APPLICABLE, false, false)
	 * .subjectType(MemberType.CIRCUMSTANCE).predType(MemberType.EITHER);
	 */
	public static final RelationType USING = of("USING", TimeType.NOT_APPLICABLE, false, "USED_BY");
	/**
	 * converse of {@link #USING}
	 */
	public static final RelationType USED_BY = USING.converse();
	public static final RelationType ABOUT = of("ABOUT", TimeType.NOT_APPLICABLE, false, "IS_TOPIC_OF")
			.subjectType(MemberType.CIRCUMSTANCE).predType(MemberType.EITHER).asStatic();
	/**
	 * converse of {@link #ABOUT}
	 */
	public static final RelationType IS_TOPIC_OF = ABOUT.converse();

	/**
	 * for things which have a type-instance relationship, i.e. an event is an
	 * instance of an eventtype
	 */
	public static final RelationType IS_INSTANCE_OF = of("IS_INSTANCE_OF", TimeType.NOT_APPLICABLE, false,
			"IS_SUPERCATEGORY_OF").predType(MemberType.OTHER).subjectType(MemberType.EITHER);
	/**
	 * converse of {@link #IS_INSTANCE_OF}
	 */
	public static final RelationType IS_SUPERCATEGORY_OF = IS_INSTANCE_OF.converse();
	public static final RelationType FEELS_LIKE = of("FEELS_LIKE", TimeType.NOT_APPLICABLE, false, "IS_FELT_WITH")
			.predType(MemberType.OTHER).subjectType(MemberType.EITHER);

	/**
	 * indicates what time the first member ("from" member) takes place in relation
	 * to the second member ("to" member); can be not applicable
	 */
	private final RelationType.TimeType time;

	/**
	 * if this expresses a locational relationship
	 */
	private final boolean isLocational;

	/**
	 * if this expresses a relationship whose converse is itself
	 */
	private final boolean isBidirectional;

	private final RelationType not;

	private RelationType converse;

	private final String name;

	private final boolean isInversion;

	private final boolean isConverse;

	private boolean isStatic;

	private RelationType.MemberType fromMemberType = MemberType.INDIVIDUAL;
	private RelationType.MemberType toMemberType = MemberType.INDIVIDUAL;

	private Set<RelationType> implicitRelations = Collections.emptySet();

	/**
	 * marks a relationship as a static relationship, i.e. "at" indicates something
	 * is at a location, it does not indicate a change of any kind
	 * 
	 * @return
	 */
	public boolean isStatic() {
		return isStatic;
	}

	protected RelationType asStatic() {
		this.isStatic = true;
		if (this.converse != null) {
			this.converse.isStatic = true;
		}
		return this;
	}

	protected RelationType addImplicit(RelationType other) {
		Set<RelationType> im = new HashSet<>(this.implicitRelations);
		im.add(other);
		implicitRelations = Set.copyOf(im);
		return this;
	}

	protected RelationType subjectType(RelationType.MemberType type) {
		this.fromMemberType = type;
		if (this.converse != null) {
			this.converse.toMemberType = type;
		}
		return this;
	}

	protected RelationType predType(RelationType.MemberType type) {
		this.toMemberType = type;
		if (this.converse != null) {
			this.converse.fromMemberType = type;
		}
		return this;
	}

	/**
	 * the first element of this relation, i.e. the member who would come first
	 * (subject) in an English sentence
	 * 
	 * @return
	 */
	public RelationType.MemberType getSubjectType() {
		return fromMemberType;
	}

	/**
	 * the second element of this relation, i.e. the member who would come second
	 * (predicate) in an English sentence
	 * 
	 * @return
	 */
	public RelationType.MemberType getPredicateType() {
		return toMemberType;
	}

	/**
	 * TODO figure this out? implicit relations, i.e. with implies at
	 * 
	 * @return
	 */
	public Set<RelationType> getImplicitRelations() {
		return implicitRelations;
	}

	public boolean canBeSubject(Object o) {
		return this.getSubjectType().isCompatible(o);
	}

	public boolean canBePredicate(Object o) {
		return this.getPredicateType().isCompatible(o);
	}

	public static RelationType of(String name, RelationType.TimeType temp, boolean loc, boolean bid) {
		return new RelationType(name, temp, loc, bid);
	}

	private RelationType(String name, RelationType.TimeType temporal, boolean locational, boolean bidirectional) {
		this.name = name;
		this.time = temporal;
		this.isLocational = locational;
		this.isBidirectional = bidirectional;
		this.isInversion = false;
		this.isConverse = false;

		this.converse = new RelationType(this, name + "_CONVERSE");
		this.not = new RelationType(this);
	}

	public static RelationType of(String name, RelationType.TimeType temp, boolean loc, String converseName) {
		RelationType ty = new RelationType(name, temp, loc, false);
		ty.converse = new RelationType(ty, converseName);

		return ty;
	}

	/**
	 * forms a converse relationship
	 * 
	 * @param conv
	 * @param converseName
	 */
	private RelationType(RelationType conv, String converseName) {
		this.name = converseName;
		this.time = conv.time.reverse();
		this.isLocational = conv.isLocational;
		this.isBidirectional = false;
		this.isInversion = false;
		this.isConverse = true;
		this.converse = conv;
		this.not = new RelationType(this);
	}

	/**
	 * forms a not relationship
	 * 
	 * @param other
	 */
	private RelationType(RelationType other) {
		this.name = "!" + other.name;
		this.isBidirectional = other.isBidirectional;
		this.isLocational = other.isLocational;
		this.time = other.time;
		this.converse = other.converse == null ? null : other.converse.not;
		this.isInversion = true;
		this.isConverse = other.isConverse;
		this.not = other;
	}

	/**
	 * returns whether this relationship goes in both directions, i.e. if A is WITH
	 * B, then B is WITH A. Bidirectional relationships can apply to more than two
	 * things, therefore
	 * 
	 * @return
	 */
	public boolean isBidirectional() {
		return isBidirectional;
	}

	/**
	 * returns whether this is a converse relationship to another kind, not sure if
	 * this is useful outside of internal use
	 * 
	 * @return
	 */
	public boolean isConverse() {
		return isConverse;
	}

	/**
	 * returns if this is a "not" relationship, i.e whether this expresses the
	 * inversion of a relationship (WITH -> WITHOUT, or rather, "!WITH")
	 * 
	 * @return
	 */
	public boolean isInversion() {
		return isInversion;
	}

	/**
	 * if this relationship is spatial in nature, e.g. being INSIDE or OUTSIDE
	 * 
	 * @return
	 */
	public boolean isLocational() {
		return isLocational;
	}

	/**
	 * returns what time the first member of this relationship happens with respect
	 * to the second, i.e. if A is BECAUSE_OF B, then A happened AFTER B
	 * 
	 * @return
	 */
	public RelationType.TimeType getTime() {
		return time;
	}

	public String getName() {
		return name;
	}

	/**
	 * return the inverted relationship to this one, not sure if it will be used
	 * 
	 * @return
	 */
	public RelationType not() {
		return not;
	}

	/**
	 * returns the converse relationship to this one
	 * 
	 * @return
	 */
	public RelationType converse() {
		return this.isBidirectional ? this : converse;
	}

	@Override
	public String toString() {
		return this.name;
	}

}