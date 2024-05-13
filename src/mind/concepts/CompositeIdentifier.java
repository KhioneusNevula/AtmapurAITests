package mind.concepts;

import java.util.HashSet;
import java.util.Set;

import com.google.common.collect.ImmutableSet;

import actor.IUniqueExistence;
import actor.IVisage;
import mind.concepts.identifiers.IPropertyIdentifier;
import mind.concepts.type.Property;
import mind.memory.IPropertyData;
import mind.thought_exp.IUpgradedHasKnowledge;

public class CompositeIdentifier implements IPropertyIdentifier {
	private Set<IPropertyIdentifier> idens = Set.of();

	public CompositeIdentifier() {
	}

	@Override
	public boolean isUnknown() {
		return this.idens.isEmpty();
	}

	/**
	 * Adds identifiers; if any are Composite, add all of its constituents
	 * 
	 * @param identifier
	 * @return
	 */
	public CompositeIdentifier addIdentifier(IPropertyIdentifier... identifier) {
		Set<IPropertyIdentifier> ids = new HashSet<>();
		ids.addAll(idens);
		for (IPropertyIdentifier id : identifier) {
			if (id instanceof CompositeIdentifier) {
				ids.addAll(((CompositeIdentifier) id).idens);
			} else {
				ids.add(id);
			}
		}
		idens = ImmutableSet.<IPropertyIdentifier>builder().addAll(ids).build();
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

	@Override
	public int hashCode() {
		return this.idens.hashCode();
	}

	/**
	 * Remove a given identifier/set of identifiers from this identifier
	 * 
	 * @param identifier
	 * @return
	 */
	public boolean removeIdentifier(IPropertyIdentifier identifier) {
		if (identifier instanceof CompositeIdentifier) {
			CompositeIdentifier ci = (CompositeIdentifier) identifier;
			return idens.removeAll(ci.idens);
		}
		return idens.remove(identifier);
	}

	@Override
	public String toString() {
		return "CID" + idens + "";
	}

}
