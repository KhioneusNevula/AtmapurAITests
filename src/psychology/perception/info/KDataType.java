package psychology.perception.info;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import energy.Energy;
import psychology.perception.Profile;
import psychology.social.concepts.Concept;
import sim.Location;

public class KDataType<T> {

	private static final Map<String, KDataType<?>> alldt = new HashMap<>();

	public static final KDataType<Double> DECIMAL = new KDataType<>("decimal", double.class);
	public static final KDataType<Integer> INTEGER = new KDataType<>("integer", int.class);
	public static final KDataType<Boolean> BOOLEAN = new KDataType<>("boolean", boolean.class);
	public static final KDataType<Profile> PROFILE = new KDataType<>("profile", Profile.class);
	public static final KDataType<Concept> CONCEPT = new KDataType<>("concept", Concept.class);
	public static final KDataType<Location> LOCATION = new KDataType<>("location", Location.class);
	public static final KDataType<Energy> ENERGY = new KDataType<>("energy", Energy.class);
	public static final KDataType<Collection> COLLECTION = new KDataType<>("collection", Collection.class);

	private Class<T> valueClass;
	private String name;

	public static <T extends Enum<T>> KDataType<T> enumType(Class<T> en) {
		String name = "enum_" + en.getSimpleName();
		if (alldt.containsKey(name)) {
			return (KDataType<T>) alldt.get(name);
		} else {
			KDataType<T> kd = new KDataType<>(name, en);
			return kd;
		}
	}

	private KDataType(String name, Class<T> clazz) {
		this.name = name;
		this.valueClass = clazz;
		alldt.put(name, this);
	}

	public String getName() {
		return name;
	}

	public Class<T> getValueClass() {
		return valueClass;
	}

}
