package civilization_and_minds.social.concepts;

import actor.construction.properties.ISensableTrait;
import civilization_and_minds.social.concepts.property.ConceptProperty;
import civilization_and_minds.social.concepts.relation.ISensePropertyRelationType.HasSensePropertyRelationType.IsSensePropertyOfRelationType;
import civilization_and_minds.social.concepts.relation.RelationType;

/**
 * A kind of knowledge; e.g. a recognized creature type is a type of concept.
 * Concepts are expected to be "self-contained," i.e. they don't depend on
 * references to multiple external objects, and they should have stable equality
 * checks and consistent hashcodes. As such, typically concepts are expected to
 * be <em> immutable </em>
 * 
 * @author borah
 *
 */
public interface IConcept {

	/**
	 * What kind of concept this is
	 * 
	 * @author borah
	 *
	 */
	public static enum ConceptType {

		/* === basic concept types === */

		/** a collection of multiple concepts */
		COLLECTION,
		/** a kind of property of something that can be sensed (e.g. color) */
		SENSABLE_PROPERTY,
		/** the value of a property that can be sensed */
		SENSABLE_TRAIT,
		/** a generic binary property that can be assigned to something */
		CONCEPT_PROPERTY,
		/** a concept representing a number or numeric value */
		NUMBER,
		/** a concept representing a word in a language */
		WORD,
		/** a concept representing a type of relation between two concepts */
		RELATION,
		/** a concept representing a kind of action */
		ACTION,
		/**
		 * a concept representing a specific need, e.g. a need for the replenishment of
		 * some amount of hunger
		 */
		NEED,
		/** a concept representing a kind of need, e.g. Hunger is a kind of need */
		NEED_CATEGORY,
		/**
		 * a concept representing a goal that an individual or group may strive toward
		 */
		GOAL,
		/** a type of intention a goal aims to satisfy */
		GOAL_INTENTION,
		/** a concept representing a tile on the map */
		MAP_TILE,
		/** a concept representing a group's uniting or defining factor */
		PURPOSE,

		/* === object types === */

		/** a concept representing a kind of actor as an object type */
		ACTOR_TYPE,
		/** a concept representing the world itself as an object type */
		WORLD_TYPE,
		/** a concept representing a kind of soul as an object type */
		SOUL_TYPE,
		/** a concept representing a kind of group as an object type */
		GROUP_TYPE,
		/** a concept representing a phenomenon as an object type */
		PHENOMENON_TYPE,

		/* === basic profile types === */

		/** a concept representing an individual entity's profile */
		INDIVIDUAL_PROFILE,
		/** a concept representing a group's profile */
		GROUP_PROFILE,
		/** a concept representing a specific item's profile */
		ITEM_PROFILE,
		/** a concept representing a Structure's profile */
		STRUCTURE_PROFILE,
		/** a concept representing a location's profile */
		PLACE_PROFILE,
		/** a concept representing a language's profile */
		LANGUAGE_PROFILE,
		/** a concept representing a map tile's profile */
		TILE_PROFILE,
		/** a concept representing the world's profile */
		WORLD_PROFILE,
		/** concept representing a phenomenon's profile */
		PHENOMENON_PROFILE,

		/* === miscellaneous types === */

		/** a concept representing a miscellaneous object type */
		OTHER_TYPE,
		/** a concept representing a miscellaneous profile */
		OTHER_PROFILE,
		/** a miscellaneous type of concept */
		OTHER;

		public boolean isProfileType() {
			return this.name().toLowerCase().endsWith("profile");
		}

		public boolean isObjectType() {
			return this.name().toLowerCase().endsWith("type") || this == MAP_TILE;
		}

		/**
		 * whether this concept type is of the "other" category
		 * 
		 * @return
		 */
		public boolean isUncategorizable() {
			return this.name().toLowerCase().startsWith("other");
		}
	}

	/**
	 * The specific unique name of this concept;
	 * 
	 * @return
	 */
	public String getUniqueName();

	/**
	 * Gets the type of this concept
	 * 
	 * @return
	 */
	public ConceptType getConceptType();

	/**
	 * If this concept is a property/trait marked by the
	 * {@link RelationType#HAS_PROPERTY} relation or the
	 * {@link IsSensePropertyOfRelationType}, i.e. either a {@link ConceptProperty}
	 * or a {@link ISensableTrait}
	 * 
	 * @return
	 */
	public default boolean isProperty() {
		return this instanceof ConceptProperty || this instanceof ISensableTrait;
	}
}
