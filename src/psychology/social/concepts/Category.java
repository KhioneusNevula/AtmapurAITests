package psychology.social.concepts;

import psychology.perception.Profile;

/**
 * 
 * @author borah
 *
 */
public class Category extends Concept {

	private boolean singleton;

	public Category(String name, boolean singleton) {
		super(name);
		this.singleton = singleton;
	}

	@Override
	public boolean isSingle() {
		return singleton;
	}

	@Override
	public boolean hasCategory() {
		return false;
	}

	@Override
	public Category getCategory() {
		return this;
	}

	@Override
	public boolean hasValue(Profile toCheck) {
		return toCheck.hasCategory(this);
	}

}
