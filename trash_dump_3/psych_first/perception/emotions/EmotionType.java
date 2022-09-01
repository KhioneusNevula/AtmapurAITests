package psych_first.perception.emotions;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import main.ImmutableCollection;
import psych_first.perception.emotions.ILevel.Degree;
import psych_first.perception.emotions.ILevel.ELevel;
import psych_first.perception.emotions.ILevel.Fundamental;
import psych_first.perception.emotions.ILevel.LevelIndicator;

/**
 * Guilt: Increases Punishment, Morality, decreases Reward, Strange<br>
 * Joy: Increases Reward, decreases Lethargy, Stress, Aggression<br>
 * Sadness: Increases Lethargy, Punishment, decreases Focus, Strange<br>
 * Anger: Increases Aggression, decreases lethargy <br>
 * --------<br>
 * Pain: Increases Punishment, Stress, Aggression, decreases Focus, Morality,
 * Dutifulness, Reward<br>
 * <br>
 * Admiration: Increases Reward, decreases Stress, Focus <br>
 * Adoration: Increases Reward, decreases Stress, Focus, Punishment <br>
 * Aesthetic appreciation: Increases Reward, decreases Stress, Focus <br>
 * Amusement: Increases Reward, decreases Stress, Focus <br>
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
 * Grief: Increases Punishment, Lethargy<br>
 * Depression: Increases Punishment, Lethargy, decreases Focus, Dutifulness<br>
 *
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
public class EmotionType implements Comparable<EmotionType> {

	private static final Map<String, EmotionType> map = new TreeMap<>();
	public static final EmotionType GUILT = e("guilt", ILevel.Fundamental.PUNISHMENT.deg(Degree.VERY_HIGH).inc(),
			ELevel.MORALITY.deg(Degree.HIGH).inc(), ILevel.Fundamental.REWARD.deg(Degree.HIGH).dec(),
			ELevel.STRANGE.deg(Degree.HIGH).dec());
	public static final EmotionType JOY = e("joy", Fundamental.REWARD.deg(Degree.HIGH).inc(),
			ELevel.LETHARGY.deg(Degree.MEDIUM).dec(), ELevel.STRESS.deg(Degree.MEDIUM).dec(),
			ELevel.AGGRESSION.deg(Degree.LOW).dec());
	public static final EmotionType SADNESS = e("sadness", ILevel.Fundamental.PUNISHMENT.deg(Degree.HIGH).inc(),
			ELevel.LETHARGY.deg(Degree.HIGH).inc(), ELevel.FOCUS.deg(Degree.MEDIUM).dec());
	public static final EmotionType ANGER = e("anger", ELevel.AGGRESSION.deg(Degree.HIGH).inc(),
			ELevel.LETHARGY.deg(Degree.MEDIUM).dec());

	public static final EmotionType PAIN = e("pain", Fundamental.PUNISHMENT.deg(Degree.HIGH).inc(),
			ELevel.STRESS.deg(Degree.HIGH).inc(), ELevel.AGGRESSION.deg(Degree.LOW).inc(),
			ELevel.FOCUS.deg(Degree.MEDIUM).dec(), ELevel.MORALITY.deg(Degree.LOW).dec(),
			ELevel.DUTIFULNESS.deg(Degree.LOW).dec(), Fundamental.REWARD.deg(Degree.HIGH).dec());

	private String id;
	private Set<ILevel> increases = new HashSet<>();
	private Set<ILevel> decreases = new HashSet<>();
	private Map<ILevel, Degree> byDegree = new HashMap<>();
	private Collection<ILevel> alllevs;
	private boolean isPositive;
	private boolean isNegative;
	private boolean isStrange;
	private boolean isGrounding;

	public EmotionType(String id, LevelIndicator... li) {
		this.id = id;

		for (LevelIndicator i : li) {
			if (i.increases()) {
				this.increases.add(i.type);
			} else {
				this.decreases.add(i.type);
			}
			this.byDegree.put(i.type, i.deg);
		}
		this.alllevs = new ImmutableCollection<>(byDegree.keySet());

		this.increases = Set.copyOf(increases);
		this.decreases = Set.copyOf(decreases);

		this.figureOutPositivity();
		isStrange = this.increases.contains(ILevel.ELevel.STRANGE);
		isGrounding = this.decreases.contains(ILevel.ELevel.STRANGE);
		map.put(id, this);
	}

	private void figureOutPositivity() {
		boolean decre = false;
		boolean incre = false;
		boolean incpu = false;
		boolean decpu = false;
		for (ILevel em : this.increases) {
			if (em == Fundamental.REWARD) {
				incre = true;
				continue;
			} else if (em == Fundamental.PUNISHMENT) {
				incpu = true;
				continue;
			}
			if (em instanceof ELevel el) {
				if (el.increasesReward())
					incre = true;
				if (el.increasesPunishment())
					incpu = true;
			}
		}
		for (ILevel em : this.decreases) {
			if (em == Fundamental.REWARD) {
				decre = true;
				continue;
			} else if (em == Fundamental.PUNISHMENT) {
				decpu = true;
				continue;
			}
			if (em instanceof ELevel el) {
				if (el.decreasesReward())
					decre = true;
				if (el.decreasesPunishment())
					decpu = true;
			}
		}
		if (incre) {
			isPositive = true;
		} else if (decpu && !incpu) {
			isPositive = true;
		}
		if (incpu || decre && !decpu) {
			isNegative = true;
		}

	}

	@Override
	public String toString() {
		return this.id;
	}

	public boolean isPositive() {
		return isPositive;
	}

	public boolean isNegative() {
		return isNegative;
	}

	private static EmotionType e(String id, LevelIndicator... li) {
		return new EmotionType(id, li);
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

	public Collection<ILevel> getAllLevels() {
		return this.alllevs;
	}

	public Degree getDegree(ILevel for_) {
		return this.byDegree.get(for_);
	}

	/**
	 * whether this emotion increases strangeness
	 * 
	 * @return
	 */
	public boolean isStrange() {
		return isStrange;
	}

	/**
	 * whether this emotion can pull someone out of strangeness
	 * 
	 * @return
	 */
	public boolean isGrounding() {
		return isGrounding;
	}

	public static EmotionType get(String id) {
		return map.get(id);
	}

	public static Collection<EmotionType> getAll() {
		return new ImmutableCollection<>(map.values());
	}

	@Override
	public int compareTo(EmotionType o) {
		return this.id.compareTo(o.id);
	}
}
