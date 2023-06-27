package mind.concepts;

import java.util.Set;

import com.google.common.collect.ImmutableSet;

import actor.IUniqueExistence;
import actor.IVisage;
import mind.concepts.identifiers.IPropertyIdentifier;
import mind.concepts.type.Property;
import mind.memory.IPropertyData;

public class CompositeIdentifier implements IPropertyIdentifier {
	private Set<IPropertyIdentifier> idens = Set.of();

	public CompositeIdentifier() {
	}

	public CompositeIdentifier addIdentifier(IPropertyIdentifier... identifier) {
		idens = ImmutableSet.<IPropertyIdentifier>builder().addAll(idens).add(identifier).build();
		return this;
	}

	@Override
	public IPropertyData identifyInfo(Property property, IUniqueExistence forExistence, IVisage visage) {

		IPropertyData dat = IPropertyData.UNKNOWN;
		for (IPropertyIdentifier id : idens) {
			IPropertyData datau = IPropertyData.UNKNOWN;
			if (!(datau = id.identifyInfo(property, forExistence, visage)).isUnknown()) {
				if (dat.isUnknown() || dat.getKnownCount() < datau.getKnownCount())
					dat = datau;
			}
		}
		return dat;
	}

}
