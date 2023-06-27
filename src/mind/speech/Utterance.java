package mind.speech;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import mind.concepts.type.IMeme;
import mind.linguistics.Language;

public abstract class Utterance implements IUtterance {

	protected List<IMeme> info;
	private Language language;

	public Utterance(Language language, IMeme mostImportantInfo, Collection<IMeme> otherInfo) {
		this(language, mostImportantInfo);
		info.addAll(otherInfo);
	}

	public Utterance(Language language, IMeme importantInfo) {
		this(importantInfo);
		this.language = language;
	}

	public Utterance(IMeme importantInfo, Collection<IMeme> otherInfo) {
		this(importantInfo);
		info.addAll(otherInfo);

	}

	public Utterance(IMeme importantInfo) {
		this.info = new ArrayList<>();
		info.add(importantInfo);
	}

	@Override
	public Collection<IMeme> includedInfo() {
		return this.info;
	}

	@Override
	public IMeme mostImportantInfo() {
		return info.get(0);
	}

	@Override
	public boolean hasLanguage() {
		return this.language != null;
	}

	@Override
	public Language language() {
		return language;
	}

	@Override
	public TranslationToken representation() {
		return new TranslationToken(language).setString(info.toString());
	}

	@Override
	public boolean equals(Object obj) {
		return obj instanceof IUtterance && ((IUtterance) obj).mostImportantInfo().equals(this.mostImportantInfo());
	}

}
