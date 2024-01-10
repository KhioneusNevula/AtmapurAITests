package mind.concepts.relations;

import java.util.Collection;

import mind.concepts.type.IMeme;

public class RelationsGraph extends AbstractRelationalGraph<IConceptRelationType, Collection<IMeme>, IMeme> {

	public RelationsGraph() {
		this(false);
	}

	public RelationsGraph(boolean allowBare) {
		super(allowBare);
	}

	@Override
	protected int compare(IConceptRelationType one, IConceptRelationType two) {
		return one.idString().compareTo(two.idString());
	}

}
