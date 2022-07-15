package psych_first.perception.knowledge;

public class IdentityKnowledge<T> implements IKnowledgeType<T> {

	private String name;
	private Class<T> clazz;

	public IdentityKnowledge(String name, Class<T> clazz) {
		this.name = name;
		this.clazz = clazz;
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public Class<T> getValueClass() {
		return clazz;
	}

	@Override
	public boolean isSocialKnowledge() {
		return false;
	}

	@Override
	public boolean isIdentitySpecific() {
		return true;
	}
	
}
