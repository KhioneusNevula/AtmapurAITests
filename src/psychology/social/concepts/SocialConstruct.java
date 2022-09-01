package psychology.social.concepts;

import psychology.perception.Profile;

/**
 * A basic unit of social understanding in a culture.
 * 
 * @author borah
 *
 */
public class SocialConstruct extends Concept {

	private Category category;

	public SocialConstruct(Category category, String name) {
		super(name);
		this.category = category;
	}

	@Override
	public Category getCategory() {
		return category;
	}

	@Override
	public boolean isSingle() {
		return false;
	}

	@Override
	public boolean hasCategory() {
		return true;
	}

	@Override
	public boolean hasValue(Profile toCheck) {
		return toCheck.hasSocialConstruct(this);
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof SocialConstruct sc) {
			return super.equals(obj) && this.category.equals(sc.category);
		}
		return false;
	}

	@Override
	public int hashCode() {
		return super.hashCode() + this.category.hashCode();
	}

}
