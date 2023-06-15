package mind.concepts.type;

/**
 * Some form of representation of categories or discrete identities; includes
 * Profiles (identities) and Categories and Properties
 * 
 * @author borah
 *
 */
public interface IConcept {

	/**
	 * Refers to whatever concept the goal is "Created" by, i.e. for a property who
	 * refers to itself (the food property has an eating goal referring to itself,
	 * for example). May be useless As F
	 */
	public static final IConcept SELF_REFERENCE = new IConcept() {
		@Override
		public String getUniqueName() {
			return "_self_reference";
		}
	};

	/**
	 * the unique name of this concept for storage purposes and identification
	 * 
	 * @return
	 */
	public String getUniqueName();

	/*
	 * public Traits getTraits(); // traits that can be used to identify the concept
	 * 
	 */

}
