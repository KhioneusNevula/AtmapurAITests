package psych_first.mind;

import java.util.Collection;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

import main.ImmutableCollection;
import psych_first.mind.ILevel.ELevel;

/**
 * Guilt: Increases Punishment, Morality, decreases Reward, Strange<br>
 * Happiness: Increases Reward, decreases Lethargy, Stress, Aggression<br>
 * Sadness: Increases Lethargy, Punishment, decreases Focus, Strange<br>
 * Pain: Increases Punishment, Stress, Aggression, decreases Focus, Morality,
 * Dutifulness, Reward<br>
 * <br>
 * Admiration: Increases Reward, decreases Stress, Focus <br>
 * Adoration: Increases Reward, decreases Stress, Focus, Punishment <br>
 * Aesthetic appreciation: Increases Reward, decreases Stress, Focus <br>
 * Amusement: Increases Reward, decreases Stress, Focus <br>
 * Anger: Increases Aggression <br>
 * Anxiety: Increases Stress, decreases Focus <br>
 * Awe: Increases Reward, decreases Stress, Focus <br>
 * Awkwardness: Increases Stress, decreases Focus, Lethargy <br>
 * Boredom: Increases Lethargy, decreases Reward, Focus <br>
 * Calmness: Increases Focus, decreases Stress, Aggression <br>
 * Confusion: Decreases Focus, increases Stress <br>
 * Craving: Decreases Focus, increases Stress<br>
 * Disgust: Increases Stress, Punishment<br>
 * Entrancement: Increases Reward, decreases Focus<br>
 * Excitement: Increases Reward<br>
 * Fear: Increases Stress, decreases Morality, Dutifulness<br>
 * Horror: Increases Stress, Punishment, decreases Aggression, Strange,
 * Morality, Dutifulness<br>
 * Interest: Increases Focus, decreases Lethargy<br>
 * Nostalgia: Increases Reward, Punishment, Decreases Strange<br>
 * Relief: Increases Reward, Lethargy, decreases Punishment, Aggression<br>
 * Love: Increases Reward, Morality, decreases Punishment, Stress, Lethargy,
 * Aggression, Strange<br>
 * Satisfaction: Increases Reward, Decreases Punishment, Stress, Aggression<br>
 * Sexual desire:<br>
 * Surprise: Increases Stress<br>
 * Strange—All increase Strange except for Groundedness
 * <ul>
 * <li>Eldritch:
 * <li>Bizarreness:
 * <li>Elsewhen:
 * <li>Purple:
 * <li>Orange:
 * <li>Ephemerality:
 * <li>Otherworldliness:
 * <li>Possession: Increases Focus
 * <li>Groundedness: Decreases Strange
 * </ul>
 * <br>
 * Physical—
 * <ul>
 * <li>Hunger: Increases Lethargy, Stress
 * <li>
 * </ul>
 * 
 * @author borah
 *
 */
public class Emotion {

	private static final Map<String, Emotion> map = new TreeMap<>();
	public static final Emotion GUILT = e("guilt", Set.of(ILevel.Fundamental.PUNISHMENT, ELevel.MORALITY),
			Set.of(ILevel.Fundamental.REWARD, ELevel.STRANGE));
	public static final Emotion HAPPINESS = e("happiness", Set.of(ILevel.Fundamental.REWARD),
			Set.of(ELevel.LETHARGY, ELevel.STRESS, ELevel.AGGRESSION));
	public static final Emotion SADNESS = e("sadness", Set.of(ILevel.Fundamental.PUNISHMENT, ELevel.LETHARGY),
			Set.of(ELevel.FOCUS));

	private String id;
	private Set<ILevel> increases = new TreeSet<>((a, b) -> a.getId().compareTo(b.getId()));
	private Set<ILevel> decreases = new TreeSet<>((a, b) -> a.getId().compareTo(b.getId()));

	public Emotion(String id, Set<ILevel> inc, Set<ILevel> dec) {
		this.id = id;
		this.increases = Set.copyOf(inc);
		this.decreases = Set.copyOf(dec);
		map.put(id, this);
	}

	private static Emotion e(String id, Set<ILevel> inc, Set<ILevel> dec) {
		return new Emotion(id, inc, dec);
	}

	private static Emotion ei(String id, Set<ILevel> inc) {
		return new Emotion(id, inc, Set.of());
	}

	private static Emotion ed(String id, Set<ILevel> dec) {
		return new Emotion(id, Set.of(), dec);
	}

	public String getId() {
		return id;
	}

	public Set<ILevel> getIncreases() {
		return increases;
	}

	public Set<ILevel> getDecreases() {
		return decreases;
	}

	/**
	 * whether this emotion increases strangeness
	 * 
	 * @return
	 */
	public boolean isStrange() {
		return this.increases.contains(ILevel.ELevel.STRANGE);
	}

	/**
	 * whether this emotion can pull someone out of strangeness
	 * 
	 * @return
	 */
	public boolean isGrounding() {
		return this.decreases.contains(ILevel.ELevel.STRANGE);
	}

	public static Emotion get(String id) {
		return map.get(id);
	}

	public static Collection<Emotion> getAll() {
		return new ImmutableCollection<>(map.values());
	}
}
