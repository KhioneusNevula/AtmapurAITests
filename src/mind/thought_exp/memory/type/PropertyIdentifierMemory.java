package mind.thought_exp.memory.type;

import mind.concepts.CompositeIdentifier;
import mind.concepts.identifiers.IPropertyIdentifier;
import mind.concepts.type.Property;
import mind.thought_exp.IUpgradedMind;
import mind.thought_exp.memory.IBrainMemory;

public class PropertyIdentifierMemory extends AbstractMemory {

	Property property;
	IPropertyIdentifier ide;

	public PropertyIdentifierMemory(Property property, IPropertyIdentifier identifier) {
		this.property = property;
		this.ide = identifier;
	}

	@Override
	public boolean applyMemoryEffects(IBrainMemory toMind) {
		IPropertyIdentifier id = toMind.getPropertyIdentifier(property);
		if (id.isComposite()) {
			((CompositeIdentifier) id).addIdentifier(ide);
		} else if (id.isUnknown()) {
			toMind.learnPropertyIdentifier(property, ide);
		} else {
			CompositeIdentifier ci = new CompositeIdentifier();
			ci.addIdentifier(id, ide);
			toMind.learnPropertyIdentifier(property, ci);
		}
		return false;
	}

	@Override
	public void forgetMemoryEffects(IUpgradedMind toMind) {

	}

	@Override
	public MemoryCategory getType() {
		return MemoryCategory.AFFECT_PROPERTY;
	}

	@Override
	public int hashCode() {
		return property.hashCode() + ide.hashCode();
	}

	@Override
	public boolean equals(Object obj) {
		if (super.equals(obj))
			return true;
		if (obj instanceof PropertyIdentifierMemory pim) {
			return this.property.equals(pim.property) && this.ide.equals(pim.ide);
		}
		return false;
	}

	@Override
	public String toString() {
		return "property-identifier(" + property + "->" + ide + ")";
	}

}
