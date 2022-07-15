package psych_first.perception.emotions;

import psych_first.perception.knowledge.IOccurrence;

public class Emotion {

	private EmotionType emotion;
	private int level;
	private Integer duration;
	private IOccurrence reason;

	public Emotion(EmotionType emotion, int level, IOccurrence reason) {
		this.emotion = emotion;
		this.level = level;
		this.reason = reason;
	}

	/**
	 * For emotions that are felt in the now instead of in memories
	 * 
	 * @param duration
	 * @return
	 */
	public Emotion setDuration(Integer duration) {
		this.duration = duration;
		return this;
	}

	public Integer getDuration() {
		return duration;
	}

	public IOccurrence getReason() {
		return reason;
	}

	public EmotionType getEmotion() {
		return emotion;
	}

	public int getLevel() {
		return level;
	}

	public String toString() {
		return this.emotion + ":" + this.level;
	}

	public String report() {
		return "{" + this.toString() + ",dur:" + this.duration + ",reason:" + this.reason.report() + "}";
	}

	@Override
	public boolean equals(Object obj) {
		return obj instanceof Emotion e && this.report().equals(e.report());
	}

}
