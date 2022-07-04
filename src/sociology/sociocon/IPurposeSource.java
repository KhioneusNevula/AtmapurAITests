package sociology.sociocon;

import java.lang.reflect.Array;
import java.util.Collection;
import java.util.function.Function;

import psych_first.action.types.Action;
import sociology.Profile;

public interface IPurposeSource {

	/**
	 * Returns the socioprops to be imbued into a given sociocon
	 * 
	 * @param socio
	 * @return
	 */
	public Collection<Socioprop<?>> getPropertiesFor(Sociocon socio);

	/**
	 * TODO Similar to imbuing properties in a sociocon, imbues possible actions
	 * into the sociocon
	 */
	public Collection<Action> getActionsFor(Sociocon socio);

	/**
	 * 
	 * @param <T>
	 * @param name
	 * @param type
	 * @param defaultValue
	 * @return
	 */
	public static <T> Socioprop<T> prop(String name, Class<T> type, Function<Profile, T> defaultValue) {
		return new Socioprop<>(name, type, defaultValue);
	}

	/**
	 * 
	 * @param <T>
	 * @param name
	 * @param type
	 * @param defaultValue
	 * @return
	 */
	public static <T> Socioprop<T> prop(String name, Class<T> type, T defaultValue) {
		return prop(name, type, (Function<Profile, T>) (a) -> defaultValue);
	}

	public static <T> Socioprop<T> prop(String name, Class<T> type) {
		return prop(name, type, (T) Array.get(Array.newInstance(type, 1), 0));
	}

	public static <T> Socioprop<T> prop(String name, T defaultValue) {
		return prop(name, (Class<T>) defaultValue.getClass(), defaultValue);
	}
}
