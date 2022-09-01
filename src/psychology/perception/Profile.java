package psychology.perception;

import java.util.HashMap;
import java.util.Map;

import psychology.perception.info.BruteTrait;
import psychology.perception.info.InfoKey;
import psychology.perception.info.SocialProperty;
import psychology.perception.info.Trait;
import psychology.social.concepts.Category;
import psychology.social.concepts.Concept;
import psychology.social.concepts.SocialConstruct;
import sim.interfaces.IHasProfile;

/**
 * the projection of an entity, phenomenon, or construct into the minds of
 * others; contains Concepts that are linked to the thing.
 * 
 * @author borah
 *
 */
public class Profile implements InfoKey {

	private IHasProfile owner;
	private String name;

	/**
	 * concept/categories which are singular just map to themselves
	 */
	private Map<Concept, Concept> concepts = new HashMap<>(0);

	private Map<SocialProperty<?>, Object> socialProperties = new HashMap<>(0);

	public Profile(IHasProfile owner) {
		this.owner = owner;
		this.name = owner.getName();

	}

	public IHasProfile getOwner() {
		return owner;
	}

	public <T> T getTrait(SocialProperty<T> forTrait) {

		return (T) socialProperties.get(forTrait);

	}

	public boolean hasTrait(Trait<?> tr) {
		if (tr instanceof SocialProperty) {
			return this.socialProperties.containsKey(tr);
		} else if (tr instanceof BruteTrait<?>bt) {
			return this.owner.hasTrait(bt);
		}
		throw new IllegalArgumentException(tr + " " + tr.getClass() + " " + this);
	}

	public boolean hasSocialConstruct(SocialConstruct con) {
		return this.concepts.containsValue(con);
	}

	public boolean hasCategory(Category cat) {
		return this.concepts.containsKey(cat);
	}

	/**
	 *
	 * @param concept concept to add; may override a previous concept that is of the
	 *                same category
	 */
	public void addConcept(Concept concept) {

	}

	@Override
	public int hashCode() {
		return this.owner.getUuid().hashCode();
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof Profile prof) {
			return this.owner.getUuid().equals(prof.owner.getUuid());
		}
		return super.equals(obj);
	}

	/**
	 * TODO this is a sub-profile for information that is known to a specific Group
	 * but is part of a larger culture, i.e. esoteric knowledge or secrets
	 * 
	 * @author borah
	 *
	 */
	public class SubProfile {
		/**
		 * concept/categories which are singular just map to themselves
		 */
		private Map<Concept, Concept> concepts = new HashMap<>(0);

		private Map<SocialProperty<?>, Object> socialProperties = new HashMap<>(0);

		protected SubProfile() {

		}

	}

}
