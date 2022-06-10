package _nonsense;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public enum Sociocontype {
	FOOD(SocioconArgument.FOOD_NOURISHMENT), ATTIRE(), PERSON();

	private Map<String, SocioconArgument<?>> values = new HashMap<>();

	private Sociocontype(SocioconArgument<?>... values) {
		for (SocioconArgument<?> arg : values) {
			this.values.put(arg.name, arg);
			arg.parent = this;
		}
	}

	public Map<SocioconArgument, Object> makeValueMap() {
		Map<SocioconArgument, Object> map = new HashMap<>();
		for (SocioconArgument<?> arg : values.values()) {
			map.put(arg, null);
		}
		return map;

	}

	public List<SocioconArgument<?>> getArguments() {
		return new ArrayList<>(values.values());
	}

	public <T> SocioconArgument<?> getArgument(String name) {
		return values.get(name);
	}

	private static <T> SocioconArgument<T> arg(String name, Class<T> clazz) {
		return new SocioconArgument<>(name, clazz);
	}

	public static class SocioconArgument<T> implements Comparable<SocioconArgument<?>> {

		private String name;
		private Class<T> type;
		private Sociocontype parent;

		public static final SocioconArgument<Integer> FOOD_NOURISHMENT = arg("nourishment", int.class);

		private SocioconArgument(String name, Class<T> type) {
			this.name = name;
			this.type = type;
		}

		public String getName() {
			return name;
		}

		public Class<T> getType() {
			return type;
		}

		public Sociocontype getParent() {
			return parent;
		}

		@Override
		public int compareTo(SocioconArgument<?> o) {
			return name.compareTo(o.name);
		}
	}
}
