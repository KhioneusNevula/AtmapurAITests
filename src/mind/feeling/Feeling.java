package mind.feeling;

import java.util.EnumMap;

/**
 * This class IS mutable.
 * 
 * @author borah
 *
 */
public class Feeling implements IFeeling, Cloneable {

	private EnumMap<Axis, Float> axes = new EnumMap<>(Axis.class);

	public Feeling() {
	}

	@Override
	public Feeling clone() {
		try {
			return (Feeling) super.clone();
		} catch (CloneNotSupportedException e) {
			return null;
		}
	}

	private float check(float num) {
		if (num > 1 || num < 0)
			throw new IllegalArgumentException("" + num);
		return num;
	}

	private float checkPN(float num) {
		if (num > 1 || num < -1)
			throw new IllegalArgumentException("" + num);
		return num;
	}

	/**
	 * Sets the value of the given specific axis in this feeling
	 * 
	 * @param dis
	 * @return
	 */
	public Feeling setForAxis(Axis axis, float trait) {
		axes.put(axis, axis.canBeNegative() ? checkPN(trait) : check(trait));
		return this;
	}

	@Override
	public float enjoyment() {
		return axes.getOrDefault(Axis.ENJOYMENT, 0f);
	}

	@Override
	public float unhappiness() {

		return -axes.getOrDefault(Axis.ENJOYMENT, 0f);
	}

	@Override
	public float aggression() {
		return axes.getOrDefault(Axis.AGGRESSION, 0f);
	}

	@Override
	public float attraction() {
		return axes.getOrDefault(Axis.ATTRACTION, 0f);
	}

	@Override
	public float aversion() {
		return -axes.getOrDefault(Axis.ATTRACTION, 0f);
	}

	@Override
	public float stress() {
		return axes.getOrDefault(Axis.STRESS, 0f);
	}

	@Override
	public float excitement() {
		return axes.getOrDefault(Axis.EXCITEMENT, 0f);
	}

	@Override
	public float unwillingness() {
		return -axes.getOrDefault(Axis.EXCITEMENT, 0f);
	}

	@Override
	public float confusion() {
		return axes.getOrDefault(Axis.CONFUSION, 0f);
	}

	@Override
	public float pleasure() {
		return axes.getOrDefault(Axis.PLEASURE, 0f);
	}

	@Override
	public float discomfort() {
		return axes.getOrDefault(Axis.DISCOMFORT, 0f);
	}

	@Override
	public float pain() {
		return axes.getOrDefault(Axis.PAIN, 0f);
	}

	@Override
	public float weird() {
		return axes.getOrDefault(Axis.WEIRD, 0f);
	}

	@Override
	public float hate() {
		return -axes.getOrDefault(Axis.LOVE, 0f);
	}

	@Override
	public float love() {
		return axes.getOrDefault(Axis.LOVE, 0f);
	}

	@Override
	public float get(Axis axis) {
		return this.axes.getOrDefault(axis, 0f);
	}

	@Override
	public String getUniqueName() {
		String ata = "";
		for (Axis axis : this.axes.keySet()) {
			ata += axis + "_" + axes.getOrDefault(axis, 0f) + "_";
		}
		return "feeling_" + ata;
	}

	private static int combo(IFeeling fee) {
		float c = 0;
		for (Axis axis : Axis.values()) {
			c += fee.get(axis);
		}
		return (int) (c + 0.5);
	}

	@Override
	public int compareTo(IFeeling o) {
		return combo(this) - combo(o);
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof IFeeling feel) {
			for (Axis a : this.axes.keySet()) {
				if (Math.abs(this.get(a) - feel.get(a)) > 0.0001) {
					return false;
				}
			}
			return true;
		}
		return false;
	}

}
