package psych_first.perception.emotions;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.function.Function;
import java.util.stream.Collectors;

import main.ImmutableCollection;
import psych_first.mind.IMindPart;
import psych_first.mind.Mind;

public class EmotionManager implements IMindPart {

	private Map<ILevel, Set<Emotion>> emotionsByLevel = new TreeMap<>((a, b) -> a.getId().compareTo(b.getId()));
	private Map<EmotionType, Set<Emotion>> emotionsByType = new TreeMap<>();
	private Map<ILevel, Integer> levels = new HashMap<>();
	private Set<Emotion> allEmotions = new HashSet<>();
	private Map<Emotion, Integer> ticksLeft = new HashMap<>();
	private Set<EmotionType> possibleEmotions = new HashSet<>();

	private Mind mind;

	private static <T> Function<T, Set<Emotion>> set() {

		return (a) -> new HashSet<>();
	}

	public EmotionManager(Mind of, Collection<? extends ILevel> possibleLevels,
			Collection<EmotionType> possibleEmotions) {
		this.mind = of;
		for (ILevel lev : possibleLevels) {
			this.levels.put(lev, levelMid());
		}
		for (ILevel.Fundamental fund : ILevel.Fundamental.values()) {
			this.levels.put(fund, levelMid());
		}
		this.possibleEmotions = Set.copyOf(possibleEmotions);
	}

	private int levelMid() {
		return (ILevel.MAX + ILevel.MIN) / 2;
	}

	public Collection<ILevel> getPossibleLevels() {
		return new ImmutableCollection<>(this.levels.keySet());
	}

	public Collection<EmotionType> getPossibleEmotions() {
		return possibleEmotions;
	}

	public Collection<Emotion> getAllEmotions() {
		return new ImmutableCollection<>(this.allEmotions);
	}

	public int getTicksLeft(Emotion for_) {
		return this.ticksLeft.get(for_);
	}

	public void setTicksLeft(Emotion for_, int val) {
		this.ticksLeft.put(for_, val);
	}

	public void changeTicksLeft(Emotion for_, int by) {
		this.ticksLeft.put(for_, this.ticksLeft.get(for_) + by);
	}

	public Collection<Emotion> getEmotions(EmotionType type) {
		return new ImmutableCollection<>(this.emotionsByType.getOrDefault(type, Set.of()));
	}

	public void addEmotion(Emotion em) {
		if (em.getDuration() == null)
			throw new IllegalArgumentException(em.report() + " must have a duration");
		if (!possibleEmotions.contains(em.getEmotion())) {
			throw new IllegalArgumentException(em + " is not a possible emotion of " + this.mind
					+ " with possible emotions " + this.possibleEmotions);
		}
		this.allEmotions.add(em);
		this.ticksLeft.put(em, em.getDuration());
		this.emotionsByType.computeIfAbsent(em.getEmotion(), set()).add(em);
		for (ILevel lev : em.getEmotion().getAllLevels()) {
			this.emotionsByLevel.computeIfAbsent(lev, set()).add(em);
			this.recalculate(lev, em, false);
		}

	}

	/**
	 * return true if removed
	 * 
	 * @param em
	 */
	public boolean removeEmotion(Emotion em) {
		if (!this.allEmotions.remove(em))
			return false;
		this.ticksLeft.remove(em);
		Set<Emotion> ebt = this.emotionsByType.get(em.getEmotion());
		ebt.remove(em);
		if (ebt.isEmpty())
			emotionsByType.remove(em.getEmotion());

		for (ILevel lev : em.getEmotion().getAllLevels()) {
			this.emotionsByLevel.get(lev).remove(em);
			this.recalculate(lev, em, true);
		}
		return true;
	}

	/**
	 * clears literally every single emotion in this entity.
	 */
	public void clearEmotions() {
		this.allEmotions.clear();
		this.emotionsByLevel.clear();
		this.emotionsByType.clear();
		for (ILevel lev : this.levels.keySet()) {
			this.levels.put(lev, levelMid());
		}
	}

	/**
	 * Removes all emotions of the given type, return true if anything changed
	 * 
	 * @param type
	 * @return
	 */
	public boolean removeAllEmotionsOfType(EmotionType type) {
		Set<Emotion> ems = emotionsByType.remove(type);
		if (ems == null)
			return false;
		for (Emotion e : ems) {
			this.removeEmotion(e);
		}
		return true;
	}

	/**
	 * recalculates values of levels based on current emotions
	 * 
	 * @param level
	 * @param em
	 * @param remove
	 */
	private void recalculate(ILevel level, Emotion em, boolean remove) {
		double _change = em.getLevel() * em.getEmotion().getDegree(level).factor;
		int change = (remove ? -1 : 1) * (int) _change;
		this.levels.put(level, this.levels.get(level) + change);
	}

	/**
	 * gets the value of the given level; restricts its value to fit within a
	 * maximum of {@value ILevel#MAX} and minimum of {@value ILevel#MIN}
	 * 
	 * @param level
	 * @return
	 */
	public int getLevel(ILevel level) {
		return Math.min(ILevel.MAX, Math.max(ILevel.MIN, this.levels.get(level)));
	}

	/**
	 * gets the level without changing it to fit within constraints
	 * 
	 * @param level
	 * @return
	 */
	public int getLevelRaw(ILevel level) {
		return this.levels.get(level);
	}

	public Mind getMind() {
		return mind;
	}

	@Override
	public String report() {
		return "{emotions:" + allEmotions.stream().map((a) -> a.report() + "(" + ticksLeft.get(a) + " ticksleft)")
				.collect(Collectors.toSet()).toString() + ",levels:" + this.levels + "}";
	}

	@Override
	public void update(int ticks) {
		for (Emotion em : this.ticksLeft.keySet()) {
			if (this.getTicksLeft(em) <= 0) {
				this.removeEmotion(em);
			}
			this.changeTicksLeft(em, -1);
		}
	}

}
