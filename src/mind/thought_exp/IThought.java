package mind.thought_exp;

import java.awt.Color;
import java.util.Collection;

import main.Pair;
import mind.concepts.type.IMeme;
import mind.feeling.IFeeling;
import mind.goals.IGoal;
import mind.goals.IGoal.Priority;
import mind.memory.IKnowledgeBase.Interest;
import mind.thought_exp.actions.IActionThought;
import sim.WorldGraphics;
import sim.interfaces.IUnique;

/**
 * TODO (experimental) A thought is a mental process that enters the mind and
 * allows for the performance of something in the mind -- e.g. thinking about a
 * memory. Experiment with this; see if we can replace the previous systems a
 * bit. Note that by default two thoughts with the same uuid will register as
 * equal, but this can be changed
 * 
 * @author borah
 *
 */
public interface IThought extends IUnique, IMeme {

	/**
	 * Gets what type of thought this is
	 * 
	 * @return
	 */
	public IThoughtType getThoughtType();

	/**
	 * The different child thoughts of this thought, if it has them
	 * 
	 * @return
	 */
	public Collection<IThought> childThoughts();

	/**
	 * Obtains the next child produced by this thought without notifying the parent
	 * and causing it to be removed
	 * 
	 * @return
	 */
	public IThought peekNextPendingChildThought();

	/**
	 * Obtains the next child thought produced by this thought and notifies the
	 * parent thought that this child thought has been obtained by the Mind. Called
	 * after the end of a "tick"
	 * 
	 * @return
	 */
	public IThought popNextPendingChildThought();

	/**
	 * If this thought is waiting to produce a child thought
	 * 
	 * @return
	 */
	public boolean hasPendingChildThought();

	/**
	 * pauses the th@Override ought's process; return TRUE if the thought cannot be
	 * paused and if so, call "interrupt" on the thought. A pause may be called on
	 * actions, but also on thoughts which are expecting child thoughts that cannot
	 * be produced due to an oversaturated mind. By default, should have a
	 * loseFocusChance() chance of returning true and being interrupted, a la human
	 * focus
	 * 
	 * @return
	 */
	public boolean pauseThought(ICanThink memory, int ticks, long wTicks);

	/**
	 * If this thought has a parent thought, i.e. a thought that created it, return
	 * that
	 * 
	 * @return
	 */
	public IThought parentThought();

	/**
	 * Set the parent thought of this child thought
	 * 
	 * @param parent
	 */
	public void setParent(IThought parent);

	/**
	 * Possibly useless (?); this just notifies the child thought that its parent
	 * thought is completed, before setting its parent to null
	 */
	public default void notifyOfParentCompletion() {

	}

	/**
	 * Gets whatever information this thought was supposed to store. Note that
	 * obtaining the information shall have undefined behavior before the thought's
	 * isFinished method returns true
	 * 
	 * @return
	 */
	public Object getInformation();

	/**
	 * If this thought returns information
	 * 
	 * @return
	 */
	public boolean returnsInformation();

	/**
	 * Whether this thought does not contribute to the mind's thought cap; usually
	 * applies to most information-gatehring thoughts
	 * 
	 * @return
	 */
	public boolean isLightweight();

	/**
	 * If this thought has a goal, return the goal; else return null
	 * 
	 * @return
	 */
	public IGoal getGoal();

	/**
	 * If this thought should become a memory (called after the thought finishes
	 * without interruption; if the result is Forget, it will not become a memory)
	 * 
	 * @return
	 */
	public Interest shouldBecomeMemory(ICanThink mind, int finishingTicks, long worldTicks);

	/**
	 * Called if the thought should become a memory; return null if the default
	 * memory format should be used (ThoughtMemory object with the thought as its
	 * parameter).
	 * 
	 * @param mind
	 * @param finishingTicks
	 * @param worldTicks
	 */
	public default IThoughtMemory getMemory(ICanThink mind, int finishingTicks, long worldTicks) {

		return null;
	}

	/**
	 * start thinking the thought
	 * 
	 * @param memory
	 * @param ticks
	 */
	public void startThinking(ICanThink memory, long worldTick);

	/**
	 * If the thought is finished -- i.e. it can be deleted and/or the information
	 * it contains can be utilized elsewhere
	 * 
	 * @param memory
	 * @param ticks
	 */
	public boolean isFinished(ICanThink memory, int ticks, long worldTick);

	/**
	 * What to do if this thought is interrupted before it is finished; interruption
	 * prematurely finishes a thought. This is called before information is sent to
	 * a parent thought (if applicable)
	 * 
	 * @param memory
	 * @param ticks
	 */
	default void interruptThought(ICanThink memory, int ticks, long worldTick) {

	}

	/**
	 * Gets a thought that is produced subsequently from this thought, if applicable
	 * 
	 * @param interrupted if the thought was interrupted; typically must mean the
	 *                    second thought is not generated and this returns null
	 * @return
	 */
	default IThought getSubsequentThought(boolean interrupted) {
		return null;
	}

	/**
	 * Gets the priority of this thought
	 * 
	 * @return
	 */
	public Priority getPriority();

	/**
	 * called right before this thought is moved from paused to active again; does
	 * not count as a tick. Return true if the thought should remain paused
	 * 
	 * @param memory
	 * @param ticks
	 * @param worldTick
	 */
	default boolean resume(ICanThink memory, int ticks, long worldTick) {
		return false;
	}

	/**
	 * one tick of the thought process; starts at 0 ticks. Note that multiple
	 * thought ticks may occur in the same world tick, based on a mind's settings;
	 * usually ~ 5 ticks. If the thought is finished, a tick is not called
	 * 
	 * @param worldTick
	 */
	public void thinkTick(ICanThink memory, int ticks, long worldTick);

	/**
	 * whether this thought is not highly noticeable to the thinker (?) possibly
	 * useless idk
	 * 
	 * @return
	 */
	public default boolean isSubconscious() {
		return isLightweight();
	}

	/**
	 * TODO different types of thoughts; has a "default cap," which is the normal
	 * maximum number of this form of thought a mind can have.
	 * 
	 * @author borah
	 *
	 */
	public static interface IThoughtType {
		public int ordinalNumber();

		/**
		 * default maximum amount of this kind of thought. return Integer.MAX_VALUE if
		 * this thought can usually be infinitely many
		 * 
		 * @return
		 */
		public int defaultCap();
	}

	/**
	 * Called right before the child thought is deleted, designed for this thought
	 * to obtain any needed info from a given child thought.
	 * 
	 * @param childThought
	 */
	public void getInfoFromChild(IThought childThought, boolean interrupted, int ticks);

	/**
	 * Called after a child thought is finished, intended to delete it from the
	 * parent thought's temporary memory
	 * 
	 * @param thought
	 */
	void endChildThought(IThought thought);

	/**
	 * What to display on the box in the mind representing this Thought
	 * 
	 * @return
	 */
	public String displayText();

	/**
	 * Returns the main topic of this thought, if the thought is about something
	 * specific. By default, returns the goal (which may be null)
	 * 
	 * @return
	 */
	public default IMeme thoughtTopic() {
		return this.getGoal();
	}

	/**
	 * Gets the feeling associated with this thought; this should be a static
	 * feeling, as opposed to a dynamic one
	 * 
	 * @return
	 */
	public default IFeeling associatedFeeling() {
		return IFeeling.NONE;
	}

	/**
	 * Gets the feeling this thought creates once it ends and the time it lasts for
	 * 
	 * @return
	 */
	public default Pair<IFeeling, Integer> finalFeeling() {
		return Pair.of(IFeeling.NONE, 0);
	}

	@Override
	default IMemeType getMemeType() {
		return MemeType.THOUGHT;
	}

	/**
	 * Display will automatically be translated
	 * 
	 * @param g
	 * @param boxWidth  width of the display box
	 * @param boxHeight height of the display box
	 */
	public default void renderThoughtView(WorldGraphics g, int boxWidth, int boxHeight) {
		String tostr = this.displayText();
		g.textSize(30);
		float w = g.textWidth(tostr);
		float h = g.textAscent() + g.textDescent();
		g.text(tostr, boxWidth / 2 - w / 2, boxHeight / 2 + h / 2);
	}

	public static final Color DEFAULT_RENDER_COLOR = new Color(0, 255, 50);

	/**
	 * What color to show the thought box as in the general mind screen
	 * 
	 * @return
	 */
	public default Color getBoxColor() {
		return DEFAULT_RENDER_COLOR;
	}

	/**
	 * Unlike the normal tick method, this is called only once per tick, and is
	 * called during the "actionTick" portion.
	 * 
	 * @param ticks     this parameter is NOT the number of action-ticks that have
	 *                  been executed, but the number of regular thinking ticks
	 * @param memory
	 * @param worldTick
	 */
	default void actionTick(ICanThink memory, int ticks, long worldTick) {

	}

	/**
	 * Whether this thought is equivalent to the given thought; also return true if
	 * the argument would be replaced in a replace operation
	 * 
	 * @param other
	 * @return
	 */
	public boolean equivalent(IThought other);

	public default boolean isAction() {
		return this instanceof IActionThought;
	}

	public default IActionThought asAction() {
		return (IActionThought) this;
	}

	/**
	 * Whether this thought *wants* to pause
	 * 
	 * @param mind
	 * @param ticks
	 * @param worldTicks
	 * @return
	 */
	public default boolean shouldPause(ICanThink mind, int ticks, long worldTicks) {
		return false;
	}

}
