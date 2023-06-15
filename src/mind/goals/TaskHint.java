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
	CONSUME("consume"),
	/** for directly healing something */
	HEAL("heal"),
	/** for killing a being */
	KILL("kill"),
	/** for dealing damage */
	ATTACK("attack"),
	/** for sleeping */
	REST("rest"),
	/** for protecting a target or making it feel safe */
	PROTECT("protect"),
	/** for acquiring something (including making it) */
	ACQUIRE("acquire"),
	/** for transferring something in possession */
	TRANSFER("transfer"),
	/** destroy something */
	DESTROY("destroy"),
	/** for stowing a resource in a storage */
	STOW("stow"),
	/** for cleaning something */
	CLEAN("clean"),
	/** for making yourself feel better */
	ENJOY("enjoy"),
	/** for feeling the sense of community */
	SOCIALIZE("socialize"),
	/** to cause someone to feel punished */
	PUNISH("punish"),
	/** for getting relief from some form of pain */
	RELIEVE("relieve"),
	/** for doing something mystical and religious and all that */
	RITUALIZE("ritualize"),
	/** to gain knowledge */
	LEARN("learn"),
	/** to teach knowledge */
	TEACH("teach"),
	/** to record knowledge */
	RECORD("record"),
	/** to use the senses on something */
	SENSE("sense"),
	/** to produce art through creativity and artisticness */
	CREATE("create"),
	/** to socially influence others */
	INFLUENCE("influence"),
	/** to gain social power */
	EMPOWER("empower"),
	/** hone a skill */
	PRACTICE("practice"),
	/** to gain physical power */
	STRENGTHEN("strengthen"),
	/** to hide or conceal self */
	HIDE("hide"),
	/** to apply a major change to a target, i.e. immortality, omnipotence */
	TRANSFORM("transform"),
	/** for restoring a being to life */
	RESURRECT("resurrect"),
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

	/**
	 * name, default task? singleton class
	 * 
	 * @param name
	 */
	private TaskHint(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

}