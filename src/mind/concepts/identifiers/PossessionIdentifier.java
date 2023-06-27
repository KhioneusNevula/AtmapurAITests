package mind.concepts.identifiers;

import java.util.Collection;
import java.util.stream.Collectors;

import actor.Actor;
import actor.IUniqueExistence;
import actor.IVisage;
import mind.concepts.type.Profile;
import mind.concepts.type.Property;
import mind.memory.IPropertyData;
import mind.memory.RememberedProperties;

public enum PossessionIdentifier implements IPropertyIdentifier {

	IDENTIFIER;

	@Override
	public IPropertyData identifyInfo(Property prop, IUniqueExistence forExistence, IVisage visage) {

		if (forExistence instanceof Actor) {
			IPropertyData data = new RememberedProperties(prop);
			Actor actor = (Actor) forExistence;
			Collection<Actor> poss = actor.getPossessed();
			if (!poss.isEmpty()) {
				// TODO check for concealment, etc
				data.addProfilesToList(poss.stream()
						.filter((a) -> a.getVisage() != null && !a.getVisage().isInvisible())
						.map((a) -> new Profile(a.getUUID(), a.getName().toLowerCase())).collect(Collectors.toSet()));
			}
			return data;
		}
		return IPropertyData.ABSENCE;
	}

}
