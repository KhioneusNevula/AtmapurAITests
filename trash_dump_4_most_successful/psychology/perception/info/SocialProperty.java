package psychology.perception.info;

import psychology.social.concepts.Concept;

public class SocialProperty<T> extends Trait<T> {

	private Concept source;

	public SocialProperty(String name, Concept source, KDataType<T> dt) {
		super(name, dt);
		this.source = source;
	}

	public Concept getSource() {
		return source;
	}

	@Override
	public boolean isSocial() {
		return true;
	}

	@Override
	public int hashCode() {
		return super.hashCode() + this.source.hashCode();
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof SocialProperty<?>socioprop) {
			return super.equals(obj) && this.source.equals(socioprop.source);
		}
		return false;
	}

}
