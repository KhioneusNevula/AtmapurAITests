package mind.concepts;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import com.google.common.collect.ImmutableSet;

import mind.concepts.identifiers.IPropertyIdentifier;
import mind.concepts.type.Property;
import mind.goals.ITaskHint;

public class PropertyController {

	private Property property;
	private IPropertyIdentifier identifier = IPropertyIdentifier.UNKNOWN;
	private Collection<ITaskHint> purposes = Set.of();
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

	public Collection<ITaskHint> getPurposes() {
		return purposes;
	}

	public PropertyController addPurpose(ITaskHint... purpose) {
		this.purposes = ImmutableSet.<ITaskHint>builder().addAll(purposes).add(purpose).build();
		return this;
	}

	public PropertyController removePurposes(ITaskHint... purpose) {
		HashSet<ITaskHint> goals = new HashSet<>(purposes);
		goals.removeAll(Set.of(purpose));
		if (goals.isEmpty())
			purposes = Set.of();
		else
			this.purposes = ImmutableSet.<ITaskHint>builder().addAll(goals).build();
		return this;
	}

	@Override
	public String toString() {
		return "{i:" + this.identifier + ",p:" + this.purposes + "}";
	}

}
