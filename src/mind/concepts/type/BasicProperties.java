package mind.concepts.type;

import java.util.Collection;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Supplier;

import com.google.common.collect.ImmutableMap;

import humans.Food;
import mind.concepts.PropertyController;
import mind.concepts.identifiers.PossessionIdentifier;
import mind.concepts.identifiers.PresencePredicateIdentifier;
import mind.goals.TaskHint;

public class BasicProperties {
	private static Map<Property, Supplier<PropertyController>> gens = Map.of();

	// TODO make food more dynamic; for now the identifier is just, the food entity
	public static final Property FOOD = reg(Property.builder("b_food").addIntProp().build(), (con) -> con
			.addPurpose(TaskHint.CONSUME).setIdentifier(new PresencePredicateIdentifier((a) -> a instanceof Food)));
	public static final Property POSSESSIONS = reg(Property.builder("b_possessions").addProfileListProp().build(),
			(con) -> con.setIdentifier(PossessionIdentifier.IDENTIFIER));
	public static final Property DANGER = reg(Property.create("b_danger"), (con) -> {
		/* TODO danger prop */});
	public static final Property SHELTER = reg(Property.create("b_shelter"), (con) -> {
		/* TODO shelter prop */});
	public static final Property AWESOME = reg(Property.create("b_awesome"), (con) -> {
		/* TODO awesome prop */});
	public static final Property PROTECTION = reg(Property.create("b_protection"), (con) -> {
		/* TODO protection prop */});
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
