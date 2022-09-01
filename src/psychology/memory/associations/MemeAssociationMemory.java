package psychology.memory.associations;

import java.util.Set;

import com.google.common.collect.MultimapBuilder;
import com.google.common.collect.SetMultimap;

import psychology.IHasKnowledge;
import psychology.perception.info.InfoKey;
import psychology.perception.memes.Meme;

public class MemeAssociationMemory {

	private SetMultimap<InfoKey, Meme> associations = MultimapBuilder.hashKeys().hashSetValues().build();

	private IHasKnowledge owner;

	public MemeAssociationMemory(IHasKnowledge owner) {
		this.owner = owner;

	}

	public IHasKnowledge getOwner() {
		return owner;
	}

	public Set<Meme> getAssociations(InfoKey forInfo) {
		return associations.get(forInfo);
	}

	public void addAssociation(InfoKey info, Meme meme) {
		associations.put(info, meme);
	}

	public boolean knows(InfoKey info) {
		return associations.containsKey(info);
	}

}
