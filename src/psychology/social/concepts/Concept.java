package psychology.social.concepts;

import psychology.perception.info.InfoUnit.InfoUnitOfPresence;

/**
 * A socially constructed designation for something
 * 
 * @author borah
 *
 */
public abstract class Concept implements InfoUnitOfPresence {

	protected String name;

	public Concept(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	/**
	 * if true, this is a concept that also acts as a category
	 * 
	 * @return
	 */
	public abstract boolean isSingle();

	/**
	 * whether this concept/category has a category of its own
	 * 
	 * @return
	 */
	public abstract boolean hasCategory();

	/**
	 * gets the category of this concept/category, if it has a category, or self if
	 * it doesn't
	 * 
	 * @return
	 */
	public abstract Category getCategory();

	@Override
	public boolean isSocial() {
		return true;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof Concept c) {
			return this.name.equals(c.name);
		}
		return super.equals(obj);
	}

	@Override
	public int hashCode() {
		return this.name.hashCode();
	}

}
