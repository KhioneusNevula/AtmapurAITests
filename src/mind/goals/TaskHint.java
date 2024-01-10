package mind.goals;

/**
 * These are hints for how/why a task can be used. They often correspond to a
 * need.
 * 
 * @author borah
 *
 */
public enum TaskHint implements ITaskHint {

	/** for traveling from place to place */
	TRAVEL("travel"),
	/** for sustaining a body (eating, etc) */
	CONSUME("consume", 1, 0),
	/** for directly healing something */
	HEAL("heal", 0, 1),
	/** for killing a being */
	KILL("kill", 0, -1),
	/** for dealing damage */
	ATTACK("attack", 0, -1),
	/** for sleeping */
	REST("rest", 1, 0),
	/** for protecting a target or making it feel safe */
	PROTECT("protect", 0, 1),
	/** for acquiring something (including making it) */
	ACQUIRE("acquire"),
	/** for transferring something in possession */
	TRANSFER("transfer"),
	/** destroy something */
	DESTROY("destroy"),
	/** for stowing a resource in a storage */
	STOW("stow"),
	/** for communicating */
	COMMUNICATE("talk"),
	/** for cleaning something */
	CLEAN("clean", 0, 1),
	/** for making yourself feel better */
	ENJOY("enjoy", 1, 0),
	/** for feeling the sense of community */
	SOCIALIZE("socialize"),
	/** to cause someone to feel punished */
	PUNISH("punish", 0, -1),
	/** for getting relief from some form of pain */
	RELIEVE("relieve", 1, 0),
	/** for doing something mystical and religious and all that */
	RITUALIZE("ritualize"),
	/** to gain knowledge */
	LEARN("learn", 1, 0),
	/** to teach knowledge */
	TEACH("teach", 0, 1),
	/** to record knowledge */
	RECORD("record"),
	/** to use the senses on something */
	SENSE("sense"),
	/** to produce art through creativity and artisticness */
	CREATE("create", 1, 0),
	/** to socially influence others */
	INFLUENCE("influence"),
	/** to gain social power */
	EMPOWER("empower", 1, 0),
	/** hone a skill */
	PRACTICE("practice", 1, 0),
	/** to gain physical power */
	STRENGTHEN("strengthen", 1, 0),
	/** to hide or conceal self */
	HIDE("hide"),
	/** to apply a major change to a target, i.e. immortality, omnipotence */
	TRANSFORM("transform"),
	/** for restoring a being to life */
	RESURRECT("resurrect", 0, 1),
	/** for producing offspring */
	PROCREATE("procreate"),
	// special task hints
	/** category for all task hints that are uncategorizable */
	OTHER("_other"),
	/**
	 * category for action hints that indicate this action can be used for any goal
	 */
	ALL("_all"),
	/** for placeholder tasks that don't accomplish anything */
	NONE("_none");

	private String name;
	private int helpsHarmsTarget;
	private int helpsHarmsSelf;

	/**
	 * name, default task? singleton class
	 * 
	 * @param name
	 */
	private TaskHint(String name) {
		this.name = name;
	}

	private TaskHint(String name, int helpsHarmsS, int helpsHarmsT) {
		this(name);
		this.helpsHarmsSelf = helpsHarmsS;
		this.helpsHarmsTarget = helpsHarmsT;
	}

	@Override
	public boolean helpsTarget() {
		return helpsHarmsTarget > 0;
	}

	@Override
	public boolean harmsTarget() {
		return helpsHarmsTarget < 0;
	}

	@Override
	public boolean harmsSelf() {
		return this.helpsHarmsSelf < 0;
	}

	@Override
	public boolean helpsSelf() {
		return this.helpsHarmsSelf > 0;
	}

	public String getName() {
		return name;
	}

	@Override
	public String getUniqueName() {
		return "taskhint_" + this.name;
	}

}