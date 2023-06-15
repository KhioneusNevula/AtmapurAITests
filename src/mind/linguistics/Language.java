package mind.linguistics;

import mind.concepts.type.IConcept;

public class Language implements IConcept {

	private String identifier;

	public Language(String identifier) {
		this.identifier = "lang_" + identifier;
	}

	@Override
	public String getUniqueName() {
		return identifier;
	}

}
