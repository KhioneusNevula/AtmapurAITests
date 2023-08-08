package mind.concepts.identifiers;

import actor.Actor;
import actor.IUniqueExistence;
import actor.IVisage;
import actor.PossessState;
import mind.concepts.type.Profile;
import mind.concepts.type.Property;
import mind.memory.IHasKnowledge;
import mind.memory.IPropertyData;
import mind.memory.RememberedProperties;

public enum IsHeldByIdentifier implements IPropertyIdentifier {

	HELD(PossessState.HOLD), WORN(PossessState.WEAR);

	private PossessState state;

	private IsHeldByIdentifier(PossessState state) {
		this.state = state;
	}

	@Override
	public IPropertyData identifyInfo(Property property, IUniqueExistence forExistence, IVisage visage,
			IHasKnowledge base) {
		if (forExistence instanceof Actor a && a.getPossessState() == state && a.getPossessor() != null) {
			return new RememberedProperties(property).setProfile(new Profile(a.getPossessor()));
		} else
			return IPropertyData.ABSENCE;
	}

}
