package mind.thought_exp.type;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import mind.action.IActionType;
import mind.concepts.relations.ConceptRelationType;
import mind.concepts.type.IMeme;
import mind.goals.IGoal.Priority;
import mind.goals.ITaskGoal;
import mind.thought_exp.ICanThink;
import mind.thought_exp.IThought;
import mind.thought_exp.IThoughtMemory;
import mind.thought_exp.ThoughtType;
import mind.thought_exp.actions.IActionThought;

public class IntentionThought extends AbstractThought {

	private boolean done = false;
	private boolean firstIntention;
	private IActionThought action;
	private Set<IActionType<?>> disqualifiedActionTypes = new HashSet<>();

	public IntentionThought(ITaskGoal goal, boolean first) {
		this.setGoal(goal);
		this.firstIntention = first;
	}

	@Override
	public ITaskGoal getGoal() {
		return (ITaskGoal) super.getGoal();
	}

	@Override
	public IThoughtType getThoughtType() {
		return firstIntention ? ThoughtType.INTENTION : ThoughtType.PLAN_ACTION;
	}

	@Override
	public boolean isLightweight() {
		return false;
	}

	@Override
	public IThoughtMemory.Interest shouldBecomeMemory(ICanThink mind, int finishingTicks, long worldTicks) {
		return IThoughtMemory.Interest.SHORT_TERM;
	}

	@Override
	public void startThinking(ICanThink memory, long worldTick) {
		// TODO Auto-generated method stub

	}

	@Override
	public void thinkTick(ICanThink memory, int ticks, long worldTick) {
		if (action == null) {
			Collection<IMeme> actions = memory.getKnowledgeBase()
					.getConceptsWithRelation(this.getGoal().getActionHint(), ConceptRelationType.USED_FOR.inverse());

		}
	}

	@Override
	public void getInfoFromChild(IThought childThought, boolean interrupted, int ticks) {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean isFinished(ICanThink memory, int ticks, long worldTick) {
		return done;
	}

	@Override
	public Priority getPriority() {
		return goal.getPriority();
	}

	@Override
	public String displayText() {
		return "intend to complete " + this.goal;
	}

	@Override
	public boolean equivalent(IThought other) {
		return other.getGoal() != null && this.getGoal().equivalent(other.getGoal());
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof IntentionThought it) {
			return this.getGoal().equals(it.getGoal())
					&& (this.action == null ? it.action == null : (it.action != null && this.action.equals(it.action)));
		}
		return false;
	}

}
