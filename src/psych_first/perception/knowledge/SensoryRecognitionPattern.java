package psych_first.perception.knowledge;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import main.ImmutableCollection;
import psych_first.mind.Mind;
import psych_first.perception.senses.SensoryAttribute;
import psych_first.perception.senses.SensoryAttribute.RecognitionLevel;
import psych_first.perception.senses.SensoryInput;

public class SensoryRecognitionPattern implements Cloneable {

	public static final int MAX_IDENTIFIERS = 5;
	private Identity identity;
	private Map<SensoryAttribute<?>, RecognitionLevel> recognizers = new HashMap<>(MAX_IDENTIFIERS, 1);

	public SensoryRecognitionPattern(Identity forIdentity) {
		this.identity = forIdentity;
	}

	public Identity getIdentity() {
		return identity;
	}

	/**
	 * returns a grade of recognizability for the given input. TODO make this have
	 * varying grades; for now it just recognizes if all traits are satisfied or one
	 * Certain trait is
	 * 
	 * @param input
	 * @param actual whether to "access" all the traits used for recognition
	 * @return
	 */
	public RecognitionLevel recognizes(SensoryInput input, boolean actual, Mind sensor) {
		int counter = 0;
		for (SensoryAttribute<?> att : recognizers.keySet()) {
			if (!input.hasAttribute(att))
				return RecognitionLevel.UNRECOGNIZABLE;
			if (!this.identity.isKnown(att)) {
				throw new IllegalStateException(identity + " has no trait " + att);
			}
			boolean mat = input.getAttribute(att).equals(this.getRequiredValue(att, actual, sensor));

			if (this.recognizers.get(att) == RecognitionLevel.CERTAIN && mat) {
				return RecognitionLevel.CERTAIN;
			} else if (mat) {
				counter++;
			}
		}
		if (counter >= this.howMany()) {
			return RecognitionLevel.CERTAIN;
		} else {
			return RecognitionLevel.UNRECOGNIZABLE;
		}
	}

	@Override
	public SensoryRecognitionPattern clone() throws CloneNotSupportedException {
		SensoryRecognitionPattern copy = new SensoryRecognitionPattern(identity);
		copy.recognizers.putAll(this.recognizers);
		return copy;
	}

	public int howMany() {
		return recognizers.size();
	}

	/**
	 * if this recognition pattern uses the given trait
	 * 
	 * @param trait
	 * @return
	 */
	public boolean usesTrait(IKnowledgeType<?> trait) {
		return recognizers.containsKey(trait);
	}

	/**
	 * gets all attributes used to recognize
	 * 
	 * @return
	 */
	public Collection<SensoryAttribute<?>> getAttributes() {
		return new ImmutableCollection<>(recognizers.keySet());
	}

	/**
	 * gets the required value from the underlying identity
	 * 
	 * @param <T>
	 * @param trait
	 * @param access whether to mark the value a having been accessed
	 * @return
	 */
	public <T> T getRequiredValue(SensoryAttribute<T> trait, boolean access, Mind sensor) {
		return (T) this.identity.getBelief(trait, true, access, sensor);
	}

	/**
	 * gets recognizability of the given trait
	 * 
	 * @param forTrait
	 * @return
	 */
	public RecognitionLevel getRecognizability(SensoryAttribute<?> forTrait) {
		return this.recognizers.get(forTrait);
	}

	/**
	 * removes this trait as being used to recognize
	 * 
	 * @param trait
	 * @return
	 */
	public RecognitionLevel remove(IKnowledgeType<?> trait) {
		return this.recognizers.remove(trait);
	}

	/**
	 * 
	 * @param trait
	 * @param force whether to put the value in the map even if its size is >
	 *              {@value #MAX_IDENTIFIERS}
	 * @return
	 */
	public SensoryRecognitionPattern add(SensoryAttribute<?> trait, boolean force) {

		if (trait.getRecognizability() == RecognitionLevel.UNRECOGNIZABLE) {
			throw new IllegalArgumentException("Trait " + trait + " cannot be unrecognizable dummy");
		}

		if (force || this.howMany() < MAX_IDENTIFIERS) {

			recognizers.put(trait, trait.getRecognizability());
		}
		return this;
	}

	/**
	 * allows a given trait to have its recognition pattern forced to be changed,
	 * e.g. a visual trait being made artificially more dubious due to low light at
	 * the time, or becoming more certain due to higher light levels later. cannot
	 * be unrecognizable, cannot be more recognizable than the highest recognition
	 * level of the trait
	 * 
	 * @param trait
	 * @param level
	 * @return
	 */
	public SensoryRecognitionPattern changeRecognition(SensoryAttribute<?> trait, RecognitionLevel level) {
		if (level.moreRecognizableThan(trait.getRecognizability())) {
			throw new IllegalArgumentException(level + " is more recognizable than max recognizability "
					+ trait.getRecognizability() + " for trait " + trait);
		} else if (level == RecognitionLevel.UNRECOGNIZABLE) {
			throw new IllegalArgumentException("Cannot use unrecognizable recognition level");
		}
		if (!this.recognizers.containsKey(trait)) {
			throw new IllegalArgumentException("No such trait in map: " + trait);
		}
		this.recognizers.put(trait, level);
		return this;
	}

}
