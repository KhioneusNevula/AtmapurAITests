package mind.linguistics;

import mind.concepts.type.IConcept;

/**
 * a name for a concept in a language; a word
 * 
 * @author borah
 *
 */
public class NameWord implements IConcept {

	private String identifier;

	public NameWord(String identifier) {
		this.identifier = "name_" + /* language.getName() + "_" + */identifier;
	}

	@Override
	public String getUniqueName() {
		return identifier;
	}

}
