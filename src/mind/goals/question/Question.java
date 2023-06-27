package mind.goals.question;

import java.util.Collection;
import java.util.LinkedList;
import java.util.Map;

import actor.Actor;
import mind.concepts.type.IMeme;
import mind.concepts.type.Profile;
import mind.concepts.type.Property;
import mind.goals.ITaskGoal;
import mind.linguistics.Language;
import mind.memory.IHasKnowledge;
import mind.memory.IKnowledgeBase;

/**
 * A Question is a "request for information" so to speak
 * 
 * @author borah
 *
 */
public class Question implements IMeme {

	private IMeme topic;

	private QuestionType type;

	private Map<String, Object> arguments;

	public static Question askLocation(IMeme topic) {
		return new Question(topic, QuestionType.LOCATION);
	}

	public static Question askProperty(Profile topic, Property property) {
		return new Question(topic, QuestionType.PROPERTY, Map.of("property", property));
	}

	public Question(IMeme topic, QuestionType type) {
		this(topic, type, Map.of());
	}

	public Question(IMeme topic, QuestionType type, Map<String, Object> args) {
		this.topic = topic;
		this.type = type;
		switch (type) {
		case LEARN_LANGUAGE:
			if (!(topic instanceof Language))
				throw new IllegalArgumentException();
			break;
		case LOCATION:
			break;
		default:
			if (!(topic instanceof Profile))
				throw new IllegalArgumentException();
		}
		this.arguments = Map.copyOf(args);
	}

	public boolean isAnswered(IHasKnowledge ihk) {
		IKnowledgeBase knowledge = ihk.getKnowledgeBase();
		if (!knowledge.isKnown(topic))
			throw new IllegalStateException(ihk + " does not know about " + topic);
		switch (type) {
		case LOCATION:
			if (topic instanceof Profile)
				return knowledge.knowsLocation((Profile) topic);
			else if (topic instanceof Property) {
				// System.out.println("Checking location question for " + ihk + " with topic " +
				// topic + "--");
				Collection<Profile> check = new LinkedList<>(knowledge.getProfilesWithProperty((Property) topic));
				if (ihk.isMindMemory()) {
					check.addAll(ihk.getMindMemory().getProfilesWithPropertyFromCulture((Property) topic).values());
				}
				for (Profile prof : check) {
					// System.out.print("Checking " + topic + " at " + prof + "--");
					if (knowledge.knowsLocation(prof)) {
						// System.out.println("Location known for " + topic + " at " + prof);
						return true;
					}
				}
				// System.out.println();
				if (ihk.isMindMemory()) {
					// System.out.println("Checking senses");
					check = ihk.getMindMemory().getSenses().getAllSensedProfiles();
					for (Profile prof : check) {
						Actor a = ihk.getMindMemory().getSenses().getActorFor(prof);
						if (a == null)
							continue;
						// System.out.print("Checking " + topic + " at " + prof + "--");
						if (ITaskGoal.getProperty(a, (Property) topic, ihk).isPresent()
								&& ihk.getMindMemory().getSenses().getSensedLocation(prof) != null) {
							// System.out.println("Location known for " + topic + " at " + prof);
							return true;

						}

					}
				}
				// TODO also constructs
			}
			return false;
		case PROPERTY:
			return !knowledge.getProperties((Profile) topic, (Property) arguments.get("property")).isUnknown();
		case RELATIONSHIP:
			// TODO relationship knowledge check
		case LANGUAGE:
			// TODO language knowledge check
		case LEARN_LANGUAGE:
			// TODO language learning knowledge check
		case EVENT:
			// TODO event knowledge check
		default:
			return false;
		}
	}

	public Map<String, Object> getArguments() {
		return arguments;
	}

	public IMeme getTopic() {
		return topic;
	}

	public QuestionType getType() {
		return type;
	}

	@Override
	public String getUniqueName() {
		return "question_" + this.type + "_" + this.topic;
	}

	@Override
	public String toString() {
		return "Question-" + this.type + "-of-" + this.topic + ":" + this.arguments + "?";
	}

	public static enum QuestionType {
		/**
		 * Asking about the location of a Concept (topic); no arguments needed
		 */
		LOCATION,
		/**
		 * asking about the data/presence of a specific property in the Profile (topic);
		 * Argument "property": Property to query.
		 */
		PROPERTY,
		/**
		 * asking about an event that the Profile (topic) was involved in;<br>
		 * Optional Event type "type": what type of event to ask about<br>
		 * Optional EventRole "role": what role the entity took in the event
		 */
		EVENT,
		/**
		 * asking about a relationship the Profile (topic) has. Arguments in order: <br>
		 * Profile "with": to query if the relationship is with another individual. <br>
		 * Optional boolean "askType": to query what type of relationship it is.
		 */
		RELATIONSHIP,
		/**
		 * Asking what language a Profile (topic) has/can speak/etc.<br>
		 * Optional language "language": to query if the Profile can speak that language
		 * specifically
		 */
		LANGUAGE,
		/**
		 * Query details about a specific Language (topic); by default gives a bit of
		 * teaching of the language's grammar or whatever<br>
		 * Optional concept "concept": query the language's name for this concept; can
		 * also (technically) query the name of something such as a civilization or
		 * person
		 */
		LEARN_LANGUAGE
	}

}
