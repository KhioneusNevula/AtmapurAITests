package psychology.social.culture;

import java.util.Set;

import com.google.common.collect.Sets;

import psychology.IHasKnowledge;
import psychology.memory.associations.MemeAssociationMemory;
import psychology.perception.info.InfoKey;
import psychology.perception.memes.Meme;

/**
 * Top-level concept for a culture; a Culture is a type of Group
 * 
 * @author borah
 *
 */
public class Group implements IHasKnowledge {

	/**
	 * A culture or Group may be immersed in another one, which is its "parent".
	 * I.e. in real life, a small American town may have its own culture, but is
	 * part of the greater American cultural group.
	 */
	private Group parent;

	private MemeAssociationMemory memes = new MemeAssociationMemory(this);

	/**
	 * parent may be null, but only if this is the highest-level group
	 * 
	 * @param parent
	 */
	public Group(Group parent) {
		this.parent = parent;
	}

	public Group getParent() {
		return parent;
	}

	/**
	 * deepSearch - whether to search higher-order Groups as well
	 * 
	 * @param forUnit
	 * @param deepSearch
	 * @return
	 */
	public Set<Meme> getMemes(InfoKey forUnit, boolean deepSearch) {

		if (deepSearch) {
			Set<Meme> set = Sets.newHashSet();
			Group group = this;
			while (group != null) {

				set.addAll(group.memes.getAssociations(forUnit));

				group = group.parent;
			}
			return set;

		} else {
			return Sets.newHashSet(memes.getAssociations(forUnit));
		}
	}

	/**
	 * deepSearch - whether to check higher order groups
	 * 
	 * @param forUnit
	 * @param deepSearch
	 * @return
	 */
	public boolean knows(InfoKey forUnit, boolean deepSearch) {
		if (deepSearch) {
			Group group = this;
			while (group != null) {
				if (group.memes.knows(forUnit)) {
					return true;
				}
				group = group.parent;
			}
			return false;
		} else {
			return !memes.knows(forUnit);
		}
	}

	@Override
	public MemeAssociationMemory getMemeAssociationMemory() {
		return this.memes;
	}

}
