package psych_first.perception.emotions;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.stream.Collectors;

import main.ImmutableCollection;

public class EmotionalState implements Iterable<Emotion> {

	private static final EmotionalState EMPTY = new EmotionalState().complete();

	private Map<EmotionType, Set<Emotion>> emotions = new TreeMap<>();
	private Set<Emotion> emotionset;

	private boolean immutable = false;

	private EmotionalState() {

	}

	private EmotionalState(Map<EmotionType, Set<Emotion>> emotions) {
		for (EmotionType t : emotions.keySet()) {
			emotions.put(t, new HashSet<>(emotions.get(t)));
		}
	}

	public static EmotionalState empty() {
		return EMPTY;
	}

	public static EmotionalState begin() {
		return new EmotionalState();
	}

	public Set<Emotion> getEmotions(EmotionType type) {

		return this.emotions.get(type);
	}

	/**
	 * clears emotions of the given type
	 * 
	 * @param em
	 * @return
	 */
	public EmotionalState clear(EmotionType em) {

		if (immutable)
			throw new IllegalStateException("Cannot change this state anymore");

		Set<Emotion> se = this.emotions.remove(em);
		if (se != null) {
			this.emotionset.removeAll(se);
		}

		return this;
	}

	public EmotionalState remove(Emotion em) {

		if (immutable)
			throw new IllegalStateException("Cannot change this state anymore");
		this.emotionset.remove(em);
		this.emotions.getOrDefault(em, new HashSet<>()).remove(em);

		return this;
	}

	/**
	 * creates a copy of this state to edit
	 * 
	 * @return
	 */
	public EmotionalState edit() {
		if (!immutable)
			throw new IllegalStateException("Already editable");
		return new EmotionalState(this.emotions);
	}

	public EmotionalState add(Emotion em) {
		if (immutable)
			throw new IllegalStateException("Cannot change this state anymore");
		emotions.computeIfAbsent(em.getEmotion(), (a) -> new HashSet<>()).add(em);
		emotionset.add(em);

		return this;
	}

	public Set<Emotion> getEmotionSet() throws IllegalStateException {
		if (!immutable)
			throw new IllegalStateException("Cannot access full emotionset unless completed");
		return emotionset;
	}

	public EmotionalState complete() {
		if (this.emotionset.isEmpty())
			return EMPTY;
		immutable = true;
		for (EmotionType tp : this.emotions.keySet()) {
			this.emotions.put(tp, Set.copyOf(this.emotions.get(tp)));
		}
		emotionset = Set.copyOf(emotions.values().stream().flatMap((a) -> a.stream()).collect(Collectors.toSet()));
		return this;
	}

	@Override
	public Iterator<Emotion> iterator() {
		return new ImmutableCollection<>(this.emotionset).iterator();
	}

}
