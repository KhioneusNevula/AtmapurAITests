package civilization_and_minds.group.purpose;

import civilization_and_minds.social.concepts.IConcept;
import sim.interfaces.IObjectType;

/**
 * Denotes the foundational purpose of a group, e.g. a role or identity
 * 
 * @author borah
 *
 */
public interface IGroupPurpose extends IConcept {

	public static enum GroupType implements IObjectType {
		/**
		 * A group that encompasses an entire society, culture, ethnicity, or state
		 */
		CIVILIZATION,
		/**
		 * A group thatt encompasses a social role
		 */
		ROLE,
		/**
		 * A group that encompasses a small, somewhat miscellaneous entity such as a
		 * traveling troupe or something similar
		 */
		PARTY,
		/**
		 * A group encompassing all members of a settlement or site
		 */
		SETTLEMENT,
		/**
		 * A group encompassing a kinship entity such as a family
		 */
		HOUSEHOLD,
		/**
		 * Any other kind of group
		 */
		OTHER;

		@Override
		public String getUniqueName() {
			return "groupType_" + this.name().toLowerCase();
		}

		@Override
		public ConceptType getConceptType() {
			return ConceptType.GROUP_TYPE;
		}

		@Override
		public float averageUniqueness() {
			return 1.0f;
		}

		/**
		 * If this group denotes an ethnic group, society, state, or other organized
		 * system
		 * 
		 * @return
		 */
		public boolean isSociety() {
			return this == CIVILIZATION || this == SETTLEMENT;
		}
	}

	/**
	 * The type of this group
	 * 
	 * @return
	 */
	public GroupType getGroupType();

	/**
	 * The name of the group
	 * 
	 * @return
	 */
	public String getName();

	@Override
	default ConceptType getConceptType() {
		return ConceptType.PURPOSE;
	}
}
