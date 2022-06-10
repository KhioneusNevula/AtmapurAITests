package _nonsense;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import _nonsense.Sociocontype.SocioconArgument;

public class Sociocon {

	private Sociocontype type;

	private String name;

	private Map<SocioconArgument, Object> values;

	List<Profile> members = new ArrayList<>();

	public Sociocon(String name, Sociocontype type) {
		this.type = type;
		values = type.makeValueMap();
		this.name = name;
	}

	public Sociocontype getType() {
		return type;
	}

	public String getName() {
		return name;
	}

	public <T> T getValue(SocioconArgument<T> valueType) {
		return (T) values.get(valueType);
	}

	public <T> void setValue(SocioconArgument<T> type, T value) {
		values.put(type, value);
	}

	public List<Profile> getMembers() {
		return new ArrayList<>(members);
	}

}
