/*
 * package mind.thought_exp.type;
 * 
 * import java.util.Map; import java.util.TreeMap;
 * 
 * import mind.action.IActionType; import mind.goals.IGoal.Priority; import
 * mind.goals.ITaskGoal; import mind.memory.IKnowledgeBase.Interest; import
 * mind.thought_exp.ICanThink; import mind.thought_exp.IThought; import
 * mind.thought_exp.ThoughtType;
 * 
 * public class GoalIntentionThought extends AbstractThought {
 * 
 * private boolean complete; private boolean checkCompletion; private int
 * failures = 0; private final int maxFailures = 5; private Map<IActionType<?>,
 * Integer> failedActions = new TreeMap<>( (a, b) ->
 * a.getUniqueName().compareTo(b.getUniqueName()));
 * 
 * public GoalIntentionThought(ITaskGoal goal) { this.goal = goal; }
 * 
 * @Override public ITaskGoal getGoal() { return (ITaskGoal) super.getGoal(); }
 * 
 * @Override public IThoughtType getThoughtType() { return
 * ThoughtType.INTENTION; }
 * 
 * @Override public boolean isLightweight() { return false; }
 * 
 * @Override public Interest shouldBecomeMemory(ICanThink mind, int
 * finishingTicks, long worldTicks) { return Interest.FORGET; // TODO remember
 * completing goals }
 * 
 * @Override public void startThinking(ICanThink memory, long worldTick) {
 * 
 * }
 * 
 * @Override public boolean isFinished(ICanThink memory, int ticks, long
 * worldTick) { return complete || failures > maxFailures; }
 * 
 * @Override public Priority getPriority() { return goal.getPriority(); }
 * 
 * @Override public void thinkTick(ICanThink memory, int ticks, long worldTick)
 * { if (checkCompletion) { complete = this.goal.isComplete(memory); } if
 * (!complete) { if (this.childThoughts(ThoughtType.PLAN_ACTION).isEmpty()) {
 * this.postChildThought(new
 * PlanActionThought(getGoal()).addFailedActions(this.failedActions.keySet()),
 * ticks); } else {
 * 
 * } } }
 * 
 * @Override public void getInfoFromChild(IThought childThought, boolean
 * interrupted, int ticks) { if (childThought instanceof PlanActionThought at) {
 * if (at.succeeded()) { } else { failures++;
 * this.failedActions.put(at.getActionType(), 4); } } }
 * 
 * @Override public String displayText() { return "intend to complete " +
 * this.goal; }
 * 
 * @Override public boolean equivalent(IThought other) { return other instanceof
 * GoalIntentionThought && this.goal.equivalent(other.getGoal()); }
 * 
 * }
 */
