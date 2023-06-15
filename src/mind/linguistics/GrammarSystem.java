package mind.linguistics;

import mind.concepts.type.IConcept;

public class GrammarSystem implements IConcept {

	private String identifier;

	public GrammarSystem(String identifier) {
		this.identifier = "grammar_" + /* language.getName() + "_" + */identifier;
	}

	@Override
	public String getUniqueName() {
		return identifier;
	}

}
