package psych.action;

import java.util.EnumSet;
import java.util.Set;

/**
 * Individual tasks of different task types can be performed at the same time;
 * two tasks of the same type cannot.
 * 
 * @author borah
 *
 */
public enum ActionType {

	/**
	 * not sure what this is for but anyway
	 */
	NONE(0),
	/**
	 * Not sure if this will be maaaaybe used to indicate the tasks involving
	 * searching through one's mind for some answer
	 */
	MEMORY(0b01),
	/** for tasks involving using the mind like idk magic?? **/
	MENTAL(0b10),
	/** for tasks that involve eating or speaking **/
	MOUTH(0b100),
	/** for tasks involving interacting with objects **/
	INTERACTION(0b1000),
	/** for tasks involving moving around **/
	MOTION(0b10000),
	/**
	 * maybe tentacle monsters can use appendages for tasks independently from other
	 * limbs?? idk
	 **/
	OTHER(0b100000);

	public final int bits;

	private ActionType(int bits) {
		this.bits = bits;
	}

	/**
	 * Return all task types of the given enum set as an int flag
	 * 
	 * @param types
	 * @return
	 */
	public static int taskTypesBitFlag(EnumSet<ActionType> types) {
		int flag = 0;
		for (ActionType type : types) {
			flag |= type.bits;
		}
		return flag;
	}

	/**
	 * Returns all task types in the given in flag
	 * 
	 * @param flag
	 * @return
	 */
	public static Set<ActionType> taskTypes(int flag) {
		EnumSet<ActionType> tasks = EnumSet.noneOf(ActionType.class);
		for (ActionType type : values()) {
			if ((type.bits & flag) == type.bits) {
				tasks.add(type);
			}
		}
		return tasks;
	}
}
