package mind.concepts.identifiers;

import java.util.function.Predicate;

import actor.IUniqueExistence;
import actor.IVisage;
import actor.SentientActor;
import actor.MultipartActor;
import mind.concepts.type.Property;
import mind.memory.IPropertyData;

/**
 * Returns Presence if the predicate returns true
 * 
 * @author borah
 *
 */
public class PresencePredicateIdentifier implements IPropertyIdentifier {

	public static final PresencePredicateIdentifier IS_SENTIENT = new PresencePredicateIdentifier(
			(a) -> a instanceof SentientActor);
	public static final PresencePredicateIdentifier HAS_MULTIPART = new PresencePredicateIdentifier(
			(a) -> a instanceof MultipartActor);

	private Predicate<IUniqueExistence> predicate;

	public PresencePredicateIdentifier(Predicate<IUniqueExistence> pred) {
		this.predicate = pred;
	}

	@Override
	public IPropertyData identifyInfo(Property prop, IUniqueExistence forExistence, IVisage visage) {
		return predicate.test(forExistence) ? IPropertyData.PRESENCE : IPropertyData.ABSENCE;
	}

	/**
	 * Returns an "and" combination of these two predicates
	 * 
	 * @param other
	 * @return
	 */
	public PresencePredicateIdentifier and(PresencePredicateIdentifier other) {
		PresencePredicateIdentifier newI = new PresencePredicateIdentifier(this.predicate.and(other.predicate));
		return newI;
	}

	public PresencePredicateIdentifier negate() {
		return new PresencePredicateIdentifier(predicate.negate());
	}

	public PresencePredicateIdentifier or(PresencePredicateIdentifier other) {
		return new PresencePredicateIdentifier(predicate.or(other.predicate));
	}

}
