package civilization_and_minds.mind.goals.type;

import java.util.Collection;
import java.util.Collections;

import actor.Actor;
import civilization_and_minds.mind.IMind;
import civilization_and_minds.mind.goals.Necessity;
import civilization_and_minds.mind.memories.IMemoryItem.Section;
import civilization_and_minds.mind.memories.MemoryWrapper;
import civilization_and_minds.mind.memories.type.GoalMemory;
import civilization_and_minds.mind.memories.type.GoalMemory.CompletionState;
import civilization_and_minds.mind.thoughts.IThought;
import civilization_and_minds.social.concepts.IConcept;
import civilization_and_minds.social.concepts.profile.Profile;
import sim.Tile;

public class TravelGoal extends AbstractGoal {

	public static class TravelCheckerThought implements IThought {

		private TravelGoal goal;
		private boolean complete;

		public TravelCheckerThought(TravelGoal goal) {
			this.goal = goal;
		}

		@Override
		public boolean isComplete(IMind mind, long ticks) {
			return complete;
		}

		@Override
		public void runTick(IMind mind, long ticks) {
			CompletionState completion = CompletionState.INCOMPLETE;
			Actor a = mind.getContainingSoul().getContainerEntity();
			if (a == null)
				completion = CompletionState.IMPOSSIBLE;
			else {
				if (a.getWorld().getTile().equals(goal.targetTile)) {
					if (goal.reachDistance) {
						if (a.reachable(goal.x, goal.y))
							completion = CompletionState.COMPLETE;
					} else {
						if (a.getX() == goal.x && a.getY() == goal.y)
							completion = CompletionState.COMPLETE;
					}
				}
			}
			mind.getKnowledge().pushMemory(Section.SHORT_TERM,
					new MemoryWrapper(new GoalMemory(goal, completion), ticks));
			complete = true;
		}

		@Override
		public boolean checksGoals() {
			return true;
		}

	}

	private int x;
	private int y;
	private boolean reachDistance;
	private Tile targetTile;

	public TravelGoal(Necessity severity, Profile patient, int x, int y, Tile targetTile, boolean reachDistance) {
		super(severity, Collections.singleton(patient), IntentionType.TRAVEL);
		this.x = x;
		this.y = y;
		this.reachDistance = reachDistance;
		this.targetTile = targetTile;
	}

	public Tile getTargetTile() {
		return targetTile;
	}

	@Override
	public Profile getBeneficiary() {
		return null;
	}

	@Override
	public Collection<? extends IConcept> getTheme() {
		return Collections.emptySet();
	}

	@Override
	public TravelCheckerThought makeGoalChecker() {
		return new TravelCheckerThought(this);
	}

	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}

	@Override
	public String getUniqueName() {
		return "goal_travel_" + x + "_" + y;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof TravelGoal tg) {
			return this.x == tg.x && this.y == tg.y && this.reachDistance == tg.reachDistance
					&& this.targetTile.equals(tg.targetTile);
		}
		return super.equals(obj);
	}

	@Override
	public int hashCode() {
		return (this.x + this.y) + Boolean.valueOf(reachDistance).hashCode() + targetTile.hashCode();
	}

}
