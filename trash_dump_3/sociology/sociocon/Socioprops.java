package sociology.sociocon;

import java.util.function.Function;

import entity.Actor;
import sociology.Profile;

public class Socioprops {

	public static final Socioprop<Integer> FOOD_NOURISHMENT = p("nourishment", int.class, 0);
	public static final Socioprop<Integer> DANGER_LEVEL = p("danger_level", int.class, 0);
	public static final Socioprop<Integer> SHELTER_LEVEL = p("shelter_level", int.class, 0);
	public static final Socioprop<Profile> ACTOR_HELD = p("held", Profile.class, (Profile) null)
			.getValueFunction((p) -> p.getOwner() instanceof Actor
					? (((Actor) p.getOwner()).getHeld() != null ? ((Actor) p.getOwner()).getHeld().getProfile() : null)
					: null);
	public static final Socioprop<Profile> ACTOR_WORN = p("worn", Profile.class, (Profile) null)
			.getValueFunction((p) -> p.getOwner() instanceof Actor
					? (((Actor) p.getOwner()).getClothing() != null ? ((Actor) p.getOwner()).getClothing().getProfile()
							: null)
					: null);

	private static <T> Socioprop<T> p(String name, Class<T> type, Function<Profile, T> defaultValue) {
		return new Socioprop<>(name, type, defaultValue);
	}

	private static <T> Socioprop<T> p(String name, Class<T> type, T defaultValue) {
		return new Socioprop<>(name, type, (a) -> defaultValue);
	}

}
