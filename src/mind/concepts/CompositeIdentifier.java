package mind.concepts;

import java.util.Set;

import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Sets;

import actor.IUniqueExistence;
import actor.IVisage;
import mind.concepts.identifiers.IPropertyIdentifier;
import mind.concepts.type.Property;
import mind.thought_exp.IUpgradedHasKnowledge;
import mind.memory.IPropertyData;

public class CompositeIdentifier implements IPropertyIdentifier {
	private Set<IPropertyIdentifier> idens = Set.of();

	public CompositeIdentifier() {
	}

	@Override
	public boolean isUnknown() {
		return this.idens.isEmpty();
	}

	public CompositeIdentifier addIdentifier(IPropertyIdentifier... identifier) {
		idens = ImmutableSet.<IPropertyIdentifier>builder().addAll(Sets.union(idens, Set.of(identifier))).build();
		return this;
	}

	@Override
	public IPropertyData identifyInfo(Property property, IUniqueExistence forExistence, IVisage visage,
			IUpgradedHasKnowledge ihk) {

		IPropertyData dat = IPropertyData.UNKNOWN;
		for (IPropertyIdentifier id : idens) {
			IPropertyData datau = IPropertyData.UNKNOWN;
			if (!(datau = id.identifyInfo(property, forExistence, visage, ihk)).isUnknown()) {
				if (dat.isUnknown() || dat.getKnownCount() < datau.getKnownCount())
					dat = datau;
			}
		}
		return dat;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof CompositeIdentifier ci) {
			return this.idens.equals(ci.idens);
		} else if (obj instanceof IPropertyIdentifier pi) {
			if (this.idens.isEmpty()) {
				return pi.isUnknown();
			} else if (this.idens.size() == 1) {
				return this.idens.iterator().next().equals(pi);
			}
			return false;
		}
		return false;
	}

}
