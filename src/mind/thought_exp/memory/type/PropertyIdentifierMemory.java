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
	public boolean apply(IBrainMemory toMind) {
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
	public void uponForgetting(IUpgradedMind toMind) {

	}

	@Override
	public MemoryCategory getType() {
		return MemoryCategory.AFFECT_PROPERTY;
	}

	@Override
	public int hashCode() {
		return property.hashCode();
	}

	@Override
	public String toString() {
		return "property-identifier(" + property + "->" + ide + ")";
	}

}
