package civilization_and_minds.mind.memories;

/**
 * Store a memory and its time of formation
 * 
 * @author borah
 *
 */
public class MemoryWrapper {

	private IMemoryItem memory;
	private long time;

	public static MemoryWrapper of(IMemoryItem memory, long time) {
		return new MemoryWrapper(memory, time);
	}

	public MemoryWrapper(IMemoryItem memory, long time) {
		this.memory = memory;
		this.time = time;
	}

	public IMemoryItem getMemory() {
		return memory;
	}

	public long getTime() {
		return time;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof IMemoryItem)
			return this.memory.equals(obj);
		else if (obj instanceof MemoryWrapper wrapper)
			return this.memory.equals(wrapper.memory) && this.time == wrapper.time;
		return super.equals(obj);
	}

	@Override
	public int hashCode() {
		return this.memory.hashCode() + (int) this.time;
	}

	@Override
	public String toString() {
		return "{" + this.memory + ",time=" + this.time + "}";
	}

}
