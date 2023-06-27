package mind.concepts.identifiers;

import actor.IUniqueExistence;
import actor.IVisage;
import mind.concepts.type.Property;
import mind.memory.IPropertyData;

/**
 * Some kind of class which identifies properties in something
 * 
 * @author borah
 *
 */
public interface IPropertyIdentifier {

	public static final IPropertyIdentifier UNKNOWN = new IPropertyIdentifier() {
		@Override
		public IPropertyData identifyInfo(Property prop, IUniqueExistence forExistence, IVisage visage) {
			return IPropertyData.UNKNOWN;
		}

		@Override
		public String toString() {
			return "identifier_UNKNOWN";
		}
	};

	/**
	 * Identify whether this thing has the associated property, and the information
	 * related; return IProperty.UNKNOWN if unknown, IProperty.ABSENCE if it is not
	 * present
	 * 
	 * @return
	 */
	public IPropertyData identifyInfo(Property property, IUniqueExistence forExistence, IVisage visage);

	default boolean isUnknown() {
		return this == UNKNOWN;
	}

	/**
	 * Return if this predicate can only be checked for the self
	 * 
	 * @return
	 */
	default boolean canOnlySenseSelf() {
		return false;
	}

}
