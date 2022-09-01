package psych_first.perception.knowledge;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

import culture.CulturalContext;
import psych_first.mind.Mind;
import psych_first.perception.emotions.Relationship;
import sim.IHasInfo;
import sim.IHasProfile;

/**
 * An Identity is an association between the a being and its manifestation in
 * the memories of other beings. Every being marks specific facts of the
 * specific Profile as known or unknown or replaces them with a false belief
 * 
 * @author borah
 *
 */
public class Identity implements IInformation {
	/**
	 * avoid using this except for Noospheres
	 * 
	 * @author borah
	 *
	 */
	public static enum Fundamental implements IKnowledgeType<Identity> {
		IDENTITY;

		@Override
		public String getName() {
			return "identity";
		}

		@Override
		public Class<Identity> getValueClass() {
			return Identity.class;
		}

		@Override
		public double convertData(IHasInfo or, Identity o) {
			return o.getUuid().getMostSignificantBits();
		}

		/**
		 * ideally this shouldn't be called
		 */
		@Override
		public boolean isSocialKnowledge() {
			throw new UnsupportedOperationException(
					"Identity should not have been used in a context where this is relevant");
		}

		@Override
		public boolean isIdentitySpecific() {
			throw new UnsupportedOperationException(
					"Identity should not have been used in a context where this is relevant");
		}

	}

	private IHasProfile trueOwner;
	/**
	 * an object indicating that the information contained is completely true
	 */
	public static final Identity NO_ONE = new Identity(null);
	public static final Object TRUTH = new Object();
	private Map<IKnowledgeType<?>, Object> believedInfo = new HashMap<>();
	private Map<IKnowledgeType<?>, Integer> accessTimes = new HashMap<>(0);
	private SensoryRecognitionPattern recognitionPattern;
	private SocialIdentifier identifier;

	public Identity(IHasProfile trueOwner) {
		this.trueOwner = trueOwner;
		if (this.trueOwner != null) {
			if (!this.trueOwner.hasProfile()) {
				throw new IllegalArgumentException(trueOwner + " must have a profile!!!");
			}
			recognitionPattern = new SensoryRecognitionPattern(this);
			identifier = new SocialIdentifier(this);
		} else {
			if (NO_ONE != null) {
				Objects.requireNonNull(trueOwner);
			}
		}

	}


	@Override
	public Identity clone() {
		if (this == NO_ONE)
			return this;
		Identity n = new Identity(trueOwner);
		n.believedInfo.putAll(this.believedInfo);
		n.recognitionPattern = recognitionPattern.clone();
		n.identifier = this.identifier.clone();

		return n;
	}

	public boolean isNoOne() {
		return this.trueOwner == null;
	}

	/**
	 * gets the sensory identification scheme of this identity
	 * 
	 * @return
	 */
	public SensoryRecognitionPattern getRecognitionPattern() {
		if (this == NO_ONE)
			throw new UnsupportedOperationException();
		return recognitionPattern;
	}

	public SocialIdentifier getIdentifier() {
		if (this == NO_ONE)
			throw new UnsupportedOperationException();
		return identifier;
	}

	/**
	 * gets the value of this belief, returns TRUTH for true beliefs if
	 * "checkActual" is false; if checkActual is true, return the actual belief as
	 * directly checked from the owner of this identity; return null if unknown
	 * 
	 * @param <T>
	 * @param k
	 * @param access whether to mark the memory as having been accessed so it
	 *               doesn't get forgotten
	 * @return
	 */
	public <T> T getBelief(IKnowledgeType<T> k, boolean checkActual, boolean access, Mind accessor) {
		if (this == NO_ONE)
			throw new UnsupportedOperationException();
		Object get = this.believedInfo.get(k);
		if (checkActual && get == TRUTH) {
			return (T) this.trueOwner.getInfo(k, accessor.getCulture());
		}
		if (access) {
			this.access(k);
		}
		return (T) get;
	}

	/**
	 * removes the given belief, marking it as unknown
	 * 
	 * @param k
	 */
	public void removeBelief(IKnowledgeType<?> k) {
		if (this == NO_ONE)
			throw new UnsupportedOperationException();
		this.believedInfo.remove(k);
		this.accessTimes.remove(k);
		if (this.recognitionPattern.usesTrait(k)) {
			this.recognitionPattern.remove(k);
		}
	}

	/**
	 * Adds a belief with the assumption that it is true
	 * 
	 * @param k
	 */
	public void addTrueBelief(IKnowledgeType<?> k) {
		if (this == NO_ONE)
			throw new UnsupportedOperationException();
		this.addBelief(k, TRUTH);
		this.accessTimes.put(k, 1);
	}

	/**
	 * adds a belief for the given knowledge type
	 * 
	 * @param k
	 * @param b
	 */
	public void addBelief(IKnowledgeType<?> k, Object b) {
		if (this == NO_ONE)
			throw new UnsupportedOperationException();
		Objects.requireNonNull(b);
		if (!k.isIdentitySpecific()) {
			throw new IllegalArgumentException("Knowledge " + k + " is not identity-specific");
		}
		if (b != TRUTH && !k.getValueClass().isAssignableFrom(b.getClass())) {
			throw new IllegalArgumentException(
					"Knowledge " + k.getName() + " has been attempted to be assigned to value of type " + b.getClass()
							+ " which doesn't match its proper type of " + k.getValueClass());
		}
		this.believedInfo.put(k, b);
		this.accessTimes.put(k, 1);
	}

	/**
	 * if this information is "known", whether truly or falsely
	 * 
	 * @param info
	 * @return
	 */
	public boolean isKnown(IKnowledgeType<?> info) {
		if (this == NO_ONE)
			return false;
		return believedInfo.containsKey(info);
	}

	/**
	 * whether this info is known in its true state rather than a believed other
	 * value; return false if unknown or not truly known. Check if the info is
	 * actually known or an exception will be thrown
	 * 
	 * @param info
	 * @param checkActual whether to actually check the value to ensure that it is
	 *                    true
	 * @return
	 */
	public boolean isTrue(IKnowledgeType<?> info, boolean checkActual) {

		if (this == NO_ONE)
			return false;
		Object a;
		return (a = Objects.requireNonNull(believedInfo.get(info))) == TRUTH
				|| (checkActual ? a.equals(this.trueOwner.getInfo(info, CulturalContext.getUniversal())) : false);
	}

	/**
	 * whether this info is false; checks the actual value to verify. Check if the
	 * info exists before using, or an exception will be thrown
	 */
	public boolean isFalse(IKnowledgeType<?> info) {

		if (this == NO_ONE)
			return false;
		Object a;
		return (a = Objects.requireNonNull(believedInfo.get(info))) != TRUTH
				&& (!a.equals(this.trueOwner.getInfo(info, CulturalContext.getUniversal())));
	}

	/**
	 * directly caches all truths in this identity, i.e. takes its proper value and
	 * directly stores it instead of the placeholder value
	 */
	public void cacheAllTruths(CulturalContext ctxt) {
		if (this == NO_ONE)
			return;
		for (IKnowledgeType<?> t : this.believedInfo.keySet()) {
			if (this.believedInfo.get(t) == TRUTH) {
				this.believedInfo.put(t, this.trueOwner.getInfo(t, ctxt));
			}
		}
	}

	/**
	 * whether this info is known but not verifiably known; return false if it is
	 * unknown or known truly
	 * 
	 * @param info
	 * @return
	 */
	public boolean isBelievedToBeKnown(IKnowledgeType<?> info) {

		if (this == NO_ONE)
			return false;
		Object a;
		return (a = believedInfo.get(info)) != null && a != TRUTH;
	}

	@Override
	public String toString() {
		return "identity for " + this.trueOwner;
	}

	public UUID getUuid() {

		if (this == NO_ONE)
			throw new UnsupportedOperationException();
		return this.trueOwner.getUuid();
	}

	public int accessCount(IKnowledgeType<?> forKnowledge) {
		return this.accessTimes.getOrDefault(forKnowledge, 0);
	}

	/**
	 * marks the memory as being accessed
	 * 
	 * @param memory
	 */
	public void access(IKnowledgeType<?> memory) {
		if (this.accessTimes.containsKey(memory)) {
			this.accessTimes.put(memory, this.accessCount(memory) + 1);
		}

	}

	@Override
	public boolean equals(Object obj) {

		if (obj instanceof Identity id)
			return this.believedInfo.equals(id.believedInfo) && this.identifier.equals(id.identifier)
					&& this.recognitionPattern.equals(id.recognitionPattern);
		else
			return false;
	}

}
