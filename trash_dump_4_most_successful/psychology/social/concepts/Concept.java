package psychology.social.concepts;

import java.util.Objects;

import psychology.perception.info.InfoUnit.InfoUnitOfPresence;
import psychology.social.culture.Group;

/**
 * A socially constructed designation for something.
 * 
 * @author borah
 *
 */
public abstract class Concept implements InfoUnitOfPresence {

	protected String name;
	protected Group culture;

	public Concept(String name, Group culture) {
		this.name = name;
		this.culture = culture;
	}

	public Group getCulture() {
		return culture;
	}

	public boolean isCultureBound() {
		return culture != null;
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
			return this.name.equals(c.name) && Objects.equals(this.culture, c.culture);
		}
		return super.equals(obj);
	}

	@Override
	public int hashCode() {
		return this.name.hashCode();
	}

}
