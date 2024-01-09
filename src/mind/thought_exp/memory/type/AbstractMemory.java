package mind.thought_exp.memory.type;

import mind.concepts.type.IMeme;
import mind.feeling.Feeling;
import mind.feeling.IFeeling;
import mind.thought_exp.IThoughtMemory;

public abstract class AbstractMemory implements IThoughtMemory {

	private IFeeling feeling = Feeling.NONE;
	private IMeme topic;
	private int feelingDuration = 0;

	public AbstractMemory() {
	}

	protected void setFeeling(IFeeling feeling) {
		this.feeling = feeling;
	}

	protected void setTopic(IMeme topic) {
		this.topic = topic;
	}

	protected void setFeelingDuration(int duration) {
		this.feelingDuration = duration;
	}

	@Override
	public IFeeling getFeeling() {
		return feeling;
	}

	@Override
	public int getFeelingDuration() {
		return feelingDuration;
	}

	@Override
	public IMeme getTopic() {
		return topic;
	}

	@Override
	public String toString() {
		return this.getClass().getSimpleName().toLowerCase() + (topic != null ? "(" + this.topic + ")" : "");
	}

}
