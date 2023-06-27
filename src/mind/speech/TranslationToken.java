package mind.speech;

import mind.linguistics.Language;

/**
 * A way to represent text using idk language interfaces(?) anyway idk but this
 * can be used to display text or smthing
 * 
 * @author borah
 *
 */
public class TranslationToken {

	private Language language;
	private String string;

	// TODO figure out how to represent expressions and stuff
	// for now, we just have the string and print it or whatever
	/**
	 * language can be null for an utterance without language
	 * 
	 * @param language
	 */
	public TranslationToken(Language language) {
		this.language = language;
	}

	public String getRepresentation() {
		return string;
	}

	public TranslationToken setString(String string) {
		this.string = string;
		return this;
	}

}
