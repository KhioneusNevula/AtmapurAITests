package mind.goals;

public interface ITaskHint {

	public String getName();

	/**
	 * if the task helps the self
	 * 
	 * @return
	 */
	public boolean helpsSelf();

	/**
	 * if the task harms the self
	 * 
	 * @return
	 */
	public boolean harmsSelf();

	/**
	 * whether actions that have this hint are helpful to its beneficiaries or
	 * target(s) (which may be the self)
	 * 
	 * @return
	 */
	public boolean helpsTarget();

	/**
	 * whether actions with this hint are harmful to its beneficiaries or target(s)
	 * (which may be the self)
	 * 
	 * @return
	 */
	public boolean harmsTarget();

}
