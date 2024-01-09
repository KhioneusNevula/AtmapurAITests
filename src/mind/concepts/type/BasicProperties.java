package mind.concepts.type;

import java.util.Collection;
import java.util.Map;
import java.util.function.Supplier;

import com.google.common.collect.ImmutableMap;

import mind.concepts.identifiers.IPropertyIdentifier;
import mind.concepts.identifiers.IsHeldByIdentifier;

public class BasicProperties {
	private static Map<Property, Supplier<IPropertyIdentifier>> gens = Map.of();

	/**
	 * to identify things consumed for sustenance
	 */
	public static final Property FOOD = reg(Property.create("b_food"), () -> null);
	/**
	 * identify what something is held by
	 */
	public static final Property HELD = reg(Property.builder("b_held").addProfileProp().build(), () -> {
		return IsHeldByIdentifier.HELD;
	});

	/** identify what something is worn by */
	public static final Property WORN = reg(Property.builder("b_worn").addProfileProp().build(), () -> {
		return IsHeldByIdentifier.WORN;
	});

	/**
	 * to identify things that harm
	 */
	public static final Property DANGER = reg(Property.create("b_danger"), () -> null);
	/**
	 * to identify things that are dead
	 */
	public static final Property DEAD = reg(Property.create("b_dead"), () -> null);
	/**
	 * to identify things that protect from the environment
	 */
	public static final Property SHELTER = reg(Property.create("b_shelter"), () -> null);
	/**
	 * to identify things worthy of note
	 */
	public static final Property AWESOME = reg(Property.create("b_awesome"), () -> null);
	/**
	 * to identify things that protect from dangers
	 */
	public static final Property PROTECTION = reg(Property.create("b_protection"), () -> null);
	/**
	 * to identify those that speak certain languages
	 */
	public static final Property SPEAKS_LANGUAGE = reg(Property.create("b_speaks_language"), () -> null);
	/**
	 * The basic property for someone being part of the in-group
	 */
	public static final Property US = Property.create("us");

	private static Property reg(Property pro, Supplier<IPropertyIdentifier> sup) {
		gens = ImmutableMap.<Property, Supplier<IPropertyIdentifier>>builder().putAll(gens).put(Map.entry(pro, sup))
				.build();
		return pro;
	}

	public static Collection<Property> getAll() {
		return gens.keySet();
	}

	public static IPropertyIdentifier genAssociations(Property prop) {
		if (!gens.containsKey(prop))
			throw new IllegalArgumentException();
		return gens.get(prop).get();
	}

}
