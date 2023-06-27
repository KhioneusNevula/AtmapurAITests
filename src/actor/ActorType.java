package actor;

import java.util.Collection;
import java.util.Map;
import java.util.Set;
import java.util.function.Supplier;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Multimap;
import com.google.common.collect.MultimapBuilder;

import biology.systems.types.ISensor;
import mind.concepts.type.Property;
import mind.memory.IPropertyData;

public class ActorType implements ITemplate {

	private String name;
	private Map<Property, Supplier<IPropertyData>> generatorFunctions = Map.of();
	private Multimap<Property, ISensor> propertyHintSenses;

	private ActorType(String name) {
		this.name = name;
	}

	@Override
	public String name() {
		return name;
	}

	@Override
	public IPropertyData getPropertyHint(Property property) {
		Supplier<IPropertyData> generator = generatorFunctions.get(property);
		if (generator == null)
			return IPropertyData.UNKNOWN;
		return generator.get();
	}

	@Override
	public Collection<ISensor> getPreferredSensesForHint(Property property) {
		return this.propertyHintSenses == null ? Set.of() : propertyHintSenses.get(property);
	}

	public static class Builder {
		private ActorType at;

		public static Builder start(String name) {
			Builder b = new Builder();
			b.at = new ActorType(name);
			return b;
		}

		public Builder addHint(Property forProp, Supplier<IPropertyData> generator, ISensor... prefSenses) {
			if (at == null)
				throw new UnsupportedOperationException();
			at.generatorFunctions = ImmutableMap.<Property, Supplier<IPropertyData>>builder()
					.putAll(at.generatorFunctions).put(forProp, generator).build();
			(at.propertyHintSenses == null ? at.propertyHintSenses = MultimapBuilder.treeKeys().hashSetValues().build()
					: at.propertyHintSenses).putAll(forProp, Set.of(prefSenses));
			return this;
		}

		public ActorType build() {
			ActorType lo = at;
			at = null;
			return lo;
		}
	}

}
