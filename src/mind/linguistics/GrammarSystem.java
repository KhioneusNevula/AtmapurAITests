package mind.linguistics;

import mind.concepts.type.IMeme;

public class GrammarSystem implements IMeme {

	private String identifier;

	public GrammarSystem(String identifier) {
		this.identifier = "grammar_" + /* language.getName() + "_" + */identifier;
	}

	@Override
	public String getUniqueName() {
		return identifier;
	}

}
