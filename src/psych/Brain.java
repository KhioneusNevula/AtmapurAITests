package psych;

import entity.Thinker;

public class Brain {

	private ActionQueue queue;
	private Thinker owner;
	private Memory memory;

	public Brain(Thinker owner) {
		this.queue = new ActionQueue(this);
		this.owner = owner;
		this.memory = new Memory(this);
	}

	public Thinker getOwner() {
		return owner;
	}

	public ActionQueue getQueue() {
		return queue;
	}

	public Memory getMemory() {
		return memory;
	}
}
