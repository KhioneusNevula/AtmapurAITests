package psych_first.perception.knowledge.facts;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Objects;

import psych_first.perception.knowledge.ICircumstance;
import psych_first.perception.knowledge.IInformation;
import psych_first.perception.knowledge.IKnowledgeCategory;
import psych_first.perception.knowledge.IKnowledgeType;
import sim.IHasProfile;

@SuppressWarnings({ "unchecked", "preview" })
public class Circumstances implements IInformation, ICircumstance {

	private IHasProfile about;

	private Map<IKnowledgeType<?>, Object> info = new HashMap<>(0);

	public Circumstances(IHasProfile about) {
		this.about = about;
	}

	public IHasProfile getAbout() {
		return about;
	}

	public <T> void removeInfo(IKnowledgeCategory<T> cat, Object o) {
		if (!cat.getElementClass().isAssignableFrom(info.getClass())) {
			throw new IllegalArgumentException(cat.getElementClass() + " " + info.getClass());
		}
		Collection<T> col = (Collection<T>) this.info.getOrDefault(cat, new HashSet<T>(0));
		col.remove(o);
	}

	public <T> T removeInfo(IKnowledgeType<T> infoType) {
		if (!this.hasInfo(infoType))
			return null;
		return (T) this.info.remove(infoType);
	}

	public Circumstances addInformation(IKnowledgeType<?> infoType, Object info) {

		if (infoType instanceof IKnowledgeCategory<?>ica) {
			return this.addToCategory(ica, Objects.requireNonNull(info));
		}

		if (!infoType.getValueClass().isAssignableFrom(Objects.requireNonNull(info, "h").getClass())) {
			throw new IllegalArgumentException(infoType.getValueClass() + " " + info.getClass());
		}
		this.info.put(infoType, info);
		return this;
	}

	public <T> Circumstances addToCategory(IKnowledgeCategory<T> cat, Object info) {
		if (!cat.getElementClass().isAssignableFrom(info.getClass())) {
			throw new IllegalArgumentException(cat.getElementClass() + " " + info.getClass());
		}

		Collection<T> col = (Collection<T>) this.info.computeIfAbsent(cat, (a) -> new HashSet<T>(1));
		col.add((T) info);

		return this;
	}

	public <T> T getInfo(IKnowledgeType<T> infoType) {
		return (T) info.get(infoType);
	}

	public boolean hasInfo(IKnowledgeType<?> infoType) {
		return info.containsKey(infoType);
	}

	public boolean hasInfo(IKnowledgeCategory<?> cat, Object o) {
		if (this.hasInfo(cat)) {
			return this.getInfo(cat).contains(o);
		}
		return false;
	}

	@Override
	public Circumstances clone() {
		Circumstances clone;
		try {
			clone = (Circumstances) super.clone();
		} catch (CloneNotSupportedException e) {
			return null;
		}
		clone.info = new HashMap<>(this.info);
		return clone;
	}

	@Override
	public String toString() {
		return "fact" + this.about;
	}

	@Override
	public boolean equals(Object obj) {
		return obj instanceof Circumstances ci && ci.about.equals(this.about) && ci.info.equals(this.info);
	}
}
