package mind.thought_exp.memory;

import mind.concepts.type.Profile;
import mind.concepts.type.Property;
import mind.thought_exp.IUpgradedMind;

public class ApplyPropertyMemory extends AbstractMemory {

	private Profile toProfile;

	public ApplyPropertyMemory(Profile toProfile, Property theProperty) {
		this.setTopic(theProperty);
		this.toProfile = toProfile;
	}

	@Override
	public Property getTopic() {
		return (Property) super.getTopic();
	}

	@Override
	public boolean apply(IUpgradedMind toMind) {
		// TODO apply property memory

		return false;
	}

	@Override
	public Type getType() {
		return Type.AFFECT_PROPERTY;
	}

}
