package mind.thought_exp.memory.type;

import mind.thought_exp.IThoughtMemory;

public class MemoryWrapper {
	private IThoughtMemory memory;
	private long time;

	public MemoryWrapper(IThoughtMemory memory, long time) {
		this.memory = memory;
		this.time = time;
	}

	public IThoughtMemory getMemory() {
		return memory;
	}

	public long getTime() {
		return time;
	}

	public void setTime(long time) {
		this.time = time;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof MemoryWrapper mw)
			return this.memory.equals(mw.memory) && this.time == mw.time;
		if (obj instanceof IThoughtMemory)
			return this.memory.equals(obj);
		return false;
	}

	@Override
	public String toString() {
		return this.memory + "(time=" + this.time + ")";
	}

	@Override
	public int hashCode() {
		return this.memory.hashCode();
	}

}
