package mind.thought_exp;

import mind.thought_exp.IThought.IThoughtType;

public enum ThoughtType implements IThoughtType {
	/** thoughts for deciding to act on a motive */
	INTENTION(10),
	/** a thought for planning for the future */
	MAKE_PLAN(1),
	/** a thought for planning an action */
	PLAN_ACTION(10),
	/** a simple thought for dwelling on a memory */
	DWELL_ON_MEMORY(1),
	/** a thought to go locate info in the memory and feed it to another thought */
	FIND_MEMORY_INFO(Integer.MAX_VALUE),
	/** a thought representing performing an action */
	ACTION(3),
	/**
	 * a thought representing the refinement of a belief (?) organizing mind (?) idk
	 */
	REFINE_BELIEFS(1),
	/** a thought to determine if something has a property */
	EVALUATE_PROPERTY(10),
	/** thoughts about sensory input, e.g. what is seen/heard/etc */
	SENSORY_INPUT(Integer.MAX_VALUE),
	/**
	 * focus on a specific thing/set of things in the environment
	 */
	FOCUS_ON_SENSE(1);

	private final int defaultCap;

	private ThoughtType(int dc) {
		this.defaultCap = dc;
	}

	@Override
	public int defaultCap() {
		return defaultCap;
	}

	@Override
	public int ordinalNumber() {
		return this.name().hashCode() + ordinal() * 10;
	}
}