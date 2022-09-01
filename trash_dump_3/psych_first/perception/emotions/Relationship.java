package psych_first.perception.emotions;

import psych_first.perception.knowledge.Identity;

public class Relationship {

	private Identity with;

	private double trust;

	private EmotionalState feelings = EmotionalState.begin();

	public Relationship(Identity with) {
		this.with = with;
	}

	public Identity getWith() {
		return with;
	}

	public double getTrust() {
		return trust;
	}

	public EmotionalState getFeelings() {
		return feelings;
	}

}
