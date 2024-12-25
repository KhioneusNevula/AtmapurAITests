package civilization_and_minds.social.concepts.relation;

import civilization_and_minds.social.concepts.IConcept;
import civilization_and_minds.social.concepts.profile.Profile;
import utilities.RelationalGraph;

/**
 * A graph storage of knowledge in a mind(?), culture, etc. Does not allow bare
 * nodes, since a concept with no connections is fundamentally unknown.
 * 
 * @author borah
 *
 */
public class KnowledgeWeb
		extends RelationalGraph<IConcept, IConceptRelationType<? extends IConceptRelationType<?>>, IConcept> {

	private Profile selfProfile;

	/**
	 * 
	 * @param selfProfile the profile representing the self of the owner of this
	 *                    knowledge, made into a root node
	 */
	public KnowledgeWeb(Profile selfProfile) {
		super(selfProfile);
		this.selfProfile = selfProfile;
	}

	public Profile getSelfProfile() {
		return selfProfile;
	}

}
