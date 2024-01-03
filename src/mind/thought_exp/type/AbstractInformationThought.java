package mind.thought_exp.type;

import java.awt.Color;

import mind.thought_exp.memory.IUpgradedKnowledgeBase.Interest;
import mind.thought_exp.ICanThink;

public abstract class AbstractInformationThought<T> extends AbstractThought {

	protected T information;

	public AbstractInformationThought() {
	}

	@Override
	public boolean returnsInformation() {
		return true;
	}

	protected void setInformation(T info) {
		this.information = info;
	}

	@Override
	public T getInformation() {
		return information;
	}

	@Override
	public Interest shouldBecomeMemory(ICanThink mind, int finishingTicks, long worldTicks) {
		return Interest.FORGET;
	}

	private static final Color BOX_COLOR = Color.yellow;

	@Override
	public Color getBoxColor() {
		return BOX_COLOR;
	}

}
