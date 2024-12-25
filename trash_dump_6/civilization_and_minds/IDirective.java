package civilization_and_minds;

/**
 * A directive is any method of obtaining info or changing things. This includes
 * thoughts in a mind, actions in a mind, missions by a group, etc
 * 
 * @author borah
 *
 */
public interface IDirective<T extends IIntelligent> {

	/**
	 * Whether this directive has achieved its motivations
	 * 
	 * @param mind
	 * @param ticks
	 * @return
	 */
	boolean isComplete(T mind, long ticks);

	/**
	 * Do something with this directive every tick.
	 * 
	 * @param mind
	 * @param ticks
	 */
	void runTick(T mind, long ticks);

	/**
	 * Whether this directive checks the completion of a goal
	 * 
	 * @return
	 */
	public boolean checksGoals();

}
