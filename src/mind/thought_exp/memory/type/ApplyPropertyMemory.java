package mind.thought_exp.memory.type;

import mind.concepts.type.Profile;
import mind.concepts.type.Property;
import mind.memory.IPropertyData;
import mind.thought_exp.IUpgradedMind;
import mind.thought_exp.memory.IBrainMemory;

public class ApplyPropertyMemory extends AbstractMemory {

	private Profile toProfile;
	private IPropertyData dat;

	public ApplyPropertyMemory(Profile toProfile, Property theProperty, IPropertyData data) {
		this.setTopic(theProperty);
		this.toProfile = toProfile;
		this.dat = data;
	}

	@Override
	public Property getTopic() {
		return (Property) super.getTopic();
	}

	@Override
	public boolean applyMemoryEffects(IBrainMemory toMind) {
		toMind.learnPropertyData(toProfile, getTopic(), dat);
		return false;
	}

	@Override
	public void forgetMemoryEffects(IUpgradedMind toMind) {

	}

	@Override
	public MemoryCategory getType() {
		return MemoryCategory.AFFECT_PROPERTY;
	}

	@Override
	public int hashCode() {
		return toProfile.hashCode() + dat.hashCode();
	}

	@Override
	public String toString() {
		return "apply-property-memory(" + this.getTopic() + "->" + this.toProfile + ")";
	}

}
