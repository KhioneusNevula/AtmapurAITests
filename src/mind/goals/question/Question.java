package mind.goals.question;

import java.util.Iterator;
import java.util.Map;

import com.google.common.collect.Iterators;

import mind.concepts.relations.ConceptRelationType;
import mind.concepts.type.IMeme;
import mind.concepts.type.IProfile;
import mind.concepts.type.Profile;
import mind.concepts.type.Property;
import mind.linguistics.Language;
import mind.thought_exp.IThought;
import mind.thought_exp.IUpgradedHasKnowledge;
import mind.thought_exp.culture.UpgradedCulture;
import mind.thought_exp.memory.IUpgradedKnowledgeBase;

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

	@Override
	public IMemeType getMemeType() {
		return MemeType.QUESTION;
	}

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

	public IThought getAnsweringThought(IUpgradedHasKnowledge ihk) {
		switch (type) {
		default:
			return null; // TODO implement answerer thoughts
		}
	}

	/**
	 * Gets an answering thought for this question -- one which will remain in
	 * memory
	 * 
	 * @param ihk
	 * @return
	 */
	public IThought getAnsweringThoughtAndRemember(IUpgradedHasKnowledge ihk) {
		switch (type) {
		default:
			return null; // TODO implement answerer thoughts
		}
	}

	public boolean isAnsweredAccordingToThought(IThought thought) {
		switch (type) {
		default:
			return false;
		}
	}

	/**
	 * true = answered. false = not answered. null = use a thought to check answer
	 * 
	 * @param ihk
	 * @return
	 */
	public Boolean isAnswered(IUpgradedHasKnowledge ihk) {
		IUpgradedKnowledgeBase knowledge = ihk.getKnowledgeBase();
		if (!knowledge.isKnown(topic))
			throw new IllegalStateException(ihk + " does not know about " + topic);
		switch (type) {
		case LOCATION:
			if (topic instanceof Profile)
				return knowledge.hasRelation(topic, topic, ConceptRelationType.FOUND_AT);
			else if (topic instanceof Property) {
				Iterator<IProfile> check = knowledge.getProfilesWithProperty((Property) topic);
				if (ihk.isMindMemory()) {
					for (UpgradedCulture culture : ihk.getMindMemory().cultures()) {
						check = Iterators.concat(check, culture.getProfilesWithProperty((Property) topic));
					}
				}
				while (check.hasNext()) {
					IProfile prof = check.next();
					if (knowledge.hasDirectionalRelationOfType(prof, ConceptRelationType.FOUND_AT)) {
						return true;
					}
				}
				// TODO also constructs
			}
			return false;
		case PROPERTY:
			return !knowledge.getPropertyData((Profile) topic, (Property) arguments.get("property")).isUnknown();
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

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof Question qu) {
			return this.type.equals(qu.type) && this.topic.equals(qu.topic) && this.arguments.equals(qu.arguments);
		}
		return super.equals(obj);
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
