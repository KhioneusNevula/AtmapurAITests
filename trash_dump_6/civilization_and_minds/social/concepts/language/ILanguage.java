package civilization_and_minds.social.concepts.language;

import sim.interfaces.IUniqueThing;

/**
 * A representation of language used by beings in the world
 * 
 * @author borah
 *
 */
public interface ILanguage extends IUniqueThing {

	/**
	 * The language that this one is descended from
	 * 
	 * @return
	 */
	public ILanguage getParent();
}
