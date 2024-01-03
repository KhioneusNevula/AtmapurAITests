package mind.concepts.type;

import java.util.Collection;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Supplier;

import com.google.common.collect.ImmutableMap;

import mind.concepts.PropertyController;
import mind.concepts.identifiers.IsHeldByIdentifier;

public class BasicProperties {
	private static Map<Property, Supplier<PropertyController>> gens = Map.of();

	/**
	 * to identify things consumed for sustenance
	 */
	public static final Property FOOD = reg(Property.create("b_food"), (con) -> {
	});
	/**
	 * identify what something is held by
	 */
	public static final Property HELD = reg(Property.builder("b_held").addProfileProp().build(), (con) -> {
		con.editIdentifier(IsHeldByIdentifier.HELD);
	});

	/** identify what something is worn by */
	public static final Property WORN = reg(Property.builder("b_worn").addProfileProp().build(), (con) -> {
		con.editIdentifier(IsHeldByIdentifier.WORN);
	});

	/**
	 * to identify things that harm
	 */
	public static final Property DANGER = reg(Property.create("b_danger"), (con) -> {
		/* TODO danger prop */});
	/**
	 * to identify things that are dead
	 */
	public static final Property DEAD = reg(Property.create("b_dead"), (con) -> {
	});
	/**
	 * to identify things that protect from the environment
	 */
	public static final Property SHELTER = reg(Property.create("b_shelter"), (con) -> {
		/* TODO shelter prop */});
	/**
	 * to identify things worthy of note
	 */
	public static final Property AWESOME = reg(Property.create("b_awesome"), (con) -> {
		/* TODO awesome prop */});
	/**
	 * to identify things that protect from dangers
	 */
	public static final Property PROTECTION = reg(Property.create("b_protection"), (con) -> {
		/* TODO protection prop */});
	/**
	 * to identify those that speak certain languages
	 */
	public static final Property SPEAKS_LANGUAGE = reg(Property.create("b_speaks_language"), (con) -> {
	});
	/**
	 * The basic property for someone being part of the in-group
	 */
	public static final Property US = Property.create("us");

	private static Property reg(Property pro, Consumer<PropertyController> sup) {
		gens = ImmutableMap.<Property, Supplier<PropertyController>>builder().putAll(gens).put(Map.entry(pro, () -> {
			PropertyController p = new PropertyController(pro);
			sup.accept(p);
			return p;
		})).build();
		return pro;
	}

	public static Collection<Property> getAll() {
		return gens.keySet();
	}

	public static PropertyController genAssociations(Property prop) {
		if (!gens.containsKey(prop))
			throw new IllegalArgumentException();
		return gens.get(prop).get();
	}

}
