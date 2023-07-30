package mind.thought_exp.memory;

import mind.concepts.type.IMeme;
import mind.feeling.Feeling;
import mind.feeling.IFeeling;
import mind.thought_exp.IThoughtMemory;

public abstract class AbstractMemory implements IThoughtMemory {

	private IFeeling feeling = Feeling.NONE;
	private IMeme topic;

	public AbstractMemory() {
	}

	protected void setFeeling(IFeeling feeling) {
		this.feeling = feeling;
	}

	protected void setTopic(IMeme topic) {
		this.topic = topic;
	}

	@Override
	public IFeeling getFeeling() {
		return feeling;
	}

	@Override
	public IMeme getTopic() {
		return topic;
	}

}
