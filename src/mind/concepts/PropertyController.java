package mind.concepts;

import mind.concepts.identifiers.IPropertyIdentifier;
import mind.concepts.type.Property;

public class PropertyController {

	private Property property;
	private IPropertyIdentifier identifier = IPropertyIdentifier.UNKNOWN;
	// other associations?

	public PropertyController(Property property) {
		this.property = property;
	}

	public Property getProperty() {
		return property;
	}

	public IPropertyIdentifier getIdentifier() {
		return identifier;
	}

	public CompositeIdentifier editIdentifier(IPropertyIdentifier... identifiers) {
		if (identifier == IPropertyIdentifier.UNKNOWN) {
			return (CompositeIdentifier) (identifier = new CompositeIdentifier().addIdentifier(identifiers));
		}
		return (CompositeIdentifier) identifier;
	}

	@Override
	public String toString() {
		return "{i:" + this.identifier + "}";
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof PropertyController con) {
			return this.property.equals(con.property) && this.identifier.equals(con.identifier);
		}
		return false;
	}

}
