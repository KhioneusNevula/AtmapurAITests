package mind.memory;

import java.util.EnumMap;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import mind.concepts.type.IMeme;
import mind.feeling.IFeeling;
import mind.feeling.IFeeling.Axis;

/**
 * Store a feeling, as well as its cause and the number of ticks it is remaining
 * 
 * @author borah
 *
 */
public class MemoryEmotions implements IEmotions {

	private Map<IFeeling.Axis, Float> axes = new EnumMap<>(IFeeling.Axis.class);
	private Map<IMeme, IFeeling> feelingCauses = new HashMap<>();
	private Map<IMeme, Integer> feelingTime = new TreeMap<>();

	public MemoryEmotions() {
	}

	public void clear() {
		axes.clear();
		feelingCauses.clear();
		feelingTime.clear();
	}

	/**
	 * Reduces the ticks of each feeling, deleting feelings which reach 0
	 */
	public void tick() {
		for (IMeme meme : Set.copyOf(feelingTime.keySet())) {
			IFeeling feeling = feelingCauses.get(meme);
			int time = feelingTime.getOrDefault(feeling, 0);
			if (time <= 0) {
				this.remove(meme);
			} else {
				feelingTime.put(feeling, time - 1);
			}
		}
	}

	/**
	 * -1 if the feeling is not present
	 * 
	 * @param forF
	 * @return
	 */
	@Override
	public int getTime(IMeme forF) {
		return feelingTime.getOrDefault(feelingCauses.get(forF), -1);
	}

	@Override
	public void changeTimeBy(IMeme forF, int time) {
		int t = getTime(forF);
		setTime(forF, time + (t < 0 ? 0 : t));
	}

	@Override
	public void setTime(IMeme forF, int time) {
		if (!this.feelingTime.containsKey(this.feelingCauses.get(forF)))
			throw new IllegalArgumentException();
		feelingTime.put(this.feelingCauses.get(forF), time);
	}

	/**
	 * Causes this feeling to be associated with this cause, replacing the previous
	 * feeling there; returns the feeling
	 * 
	 * @param feeling
	 * @param cause
	 */
	@Override
	public IFeeling changeFeeling(IFeeling feeling, IMeme cause) {
		return feelingCauses.put(cause, feeling);

	}

	@Override
	public void add(IFeeling feeling, int time, IMeme cause) {
		if (time < 0)
			throw new IllegalArgumentException();
		if (time == 0)
			return;
		this.feelingTime.put(cause, time);
		if (cause != null)
			this.feelingCauses.put(cause, feeling);
		for (IFeeling.Axis axis : IFeeling.Axis.values()) {
			float g = feeling.get(axis);
			if (g != 0)
				axes.put(axis, axes.getOrDefault(axis, 0f) + g);
		}
	}

	/**
	 * Removes a feeling and returns its cause
	 * 
	 * @param feeling
	 * @return
	 */
	@Override
	public IMeme remove(IMeme cause) {
		if (feelingTime.remove(cause) != null) {
			IFeeling feeling = feelingCauses.remove(cause);
			if (feeling != null) {
				for (IFeeling.Axis axis : IFeeling.Axis.values()) {
					float g = feeling.get(axis);
					if (g != 0)
						axes.put(axis, axes.getOrDefault(axis, 0f) - g);
				}
			}
			return feeling;
		}
		return null;
	}

	@Override
	public float enjoyment() {
		return axes.getOrDefault(Axis.ENJOYMENT, 0f);
	}

	@Override
	public float unhappiness() {

		return -axes.getOrDefault(Axis.ENJOYMENT, 0f);
	}

	public float aggression() {
		return axes.getOrDefault(Axis.AGGRESSION, 0f);
	}

	public float stress() {
		return axes.getOrDefault(Axis.STRESS, 0f);
	}

	public float excitement() {
		return axes.getOrDefault(Axis.EXCITEMENT, 0f);
	}

	public float unwillingness() {
		return -axes.getOrDefault(Axis.EXCITEMENT, 0f);
	}

	public float confusion() {
		return axes.getOrDefault(Axis.CONFUSION, 0f);
	}

	public float pleasure() {
		return axes.getOrDefault(Axis.PLEASURE, 0f);
	}

	public float discomfort() {
		return axes.getOrDefault(Axis.DISCOMFORT, 0f);
	}

	public float pain() {
		return axes.getOrDefault(Axis.PAIN, 0f);
	}

	public float weird() {
		return axes.getOrDefault(Axis.WEIRD, 0f);
	}

	public float get(Axis axis) {
		return this.axes.getOrDefault(axis, 0f);
	}

}
