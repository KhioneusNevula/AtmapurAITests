package mind.speech;

import java.util.Collection;

import mind.concepts.type.IMeme;
import mind.linguistics.Language;

/**
 * A collection of information that is expressed to another individual
 * 
 * @author borah
 *
 */
public interface IUtterance {

	/**
	 * Information included in this utterance.
	 */
	Collection<IMeme> includedInfo();

	/**
	 * The most significant piece of information in this utterance, e.g. a question
	 * will have the Question object itself be its most significant info
	 * 
	 * @return
	 */
	IMeme mostImportantInfo();

	/**
	 * If this expression uses a language, return true.
	 * 
	 * @return
	 */
	boolean hasLanguage();

	/**
	 * Gets the language this expression is in
	 * 
	 * @return
	 */
	Language language();

	/**
	 * Returns the representation of this utterance
	 * 
	 * @return
	 */
	TranslationToken representation();

}
