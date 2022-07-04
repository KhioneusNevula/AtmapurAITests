package psych_first.mind;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Stack;
import java.util.stream.Collectors;

import psych_first.action.ActionType;
import psych_first.action.goal.Goal;
import psych_first.action.types.Action;
import psych_first.actionstates.states.State;
import psych_first.actionstates.states.StateBuilder;
import psych_first.mind.Memory.MemorySection;
import psych_first.mind.Memory.MemoryType;

public class WillRedo implements IMindPart {

	private static final int UNIVERSAL_MAX_ACTIONS = 25;
	private int maxActions = UNIVERSAL_MAX_ACTIONS;
	private static final int UNIVERSAL_MAX_GOALS = 10;
	private int maxGoals = UNIVERSAL_MAX_GOALS;
	private static final int UNIVERSAL_MAX_FOCUS = 3;
	private int maxFocus = UNIVERSAL_MAX_FOCUS;
	private Mind mind;
	private Stack<ActionContext> actions = new Stack<>();
	private Stack<Goal> goals = new Stack<>();
	/**
	 * goals whose actions are currently executing
	 */
	private Set<Goal> focuses = new HashSet<>();
	private Map<ActionType, ActionContext> executingActions = new EnumMap<>(ActionType.class);
	private Map<Action, Integer> actionTicks = new HashMap<>();

	public WillRedo(Mind mind) {
		this.mind = mind;
	}

	@Override
	public void update(int ticks) {
		// TODO Auto-generated method stub

	}

	@Override
	public String report() {
		// TODO Auto-generated method stub
		return null;
	}

	public Goal chooseNewGoalToFocus() {
		return this.goals.isEmpty() ? null : this.goals.pop();
	}

	/**
	 * tries executing a new action, and if it can't then choose a new action.
	 * Return true if any action was executed (if not, then this method should be
	 * repeated as necessary to derive executable actions)
	 */
	public boolean tryContinueActions() {
		boolean acted = tryDoAction();
		if (!acted) {
			Goal focal = null;
			if (this.actions.empty()) {
				focal = chooseNewGoalToFocus();
			} else if (this.actions.peek().requirement.isEmpty() || this.actions.peek().requirement.isEnd()) {
				focal = chooseNewGoalToFocus();
			} else {
				focal = actions.peek().requirement;
			}
			if (focal == null)
				return false;
			ActionContext con = this.chooseAction(focal);
			if (con != null) {
				actions.push(con);
			}
			return false;
		} else {
			return true;
		}
	}

	/**
	 * Tries to do the first action on the stack; if it can't, return false
	 * 
	 * @return
	 */
	public boolean tryDoAction() {
		if (!this.actions.isEmpty()) {
			ActionContext a = this.actions.peek();
			for (ActionType type : this.executingActions.keySet()) {
				if ((type.bits | a.action.getActionType()) == a.action.getActionType()) {
					return false;
				}
			}
			if (a.action._canExecute(this.mind.getOwner(), this, null, null, null)) {

				int ticks = this.actions.pop().action._startExecution(this.mind.getOwner(), null, null, null, null);
				this.actionTicks.put(a.action, ticks);
				for (ActionType type : ActionType.taskTypes(a.action.getActionType())) {
					this.executingActions.put(type, a);
				}
			}
		}
		return false;
	}

	public ActionContext chooseAction(Goal forGoal) {
		MemorySection<Action> possible = this.mind.getMemory().getMemories(MemoryType.POSSIBLE_ACTIONS);
		List<ActionContext> cons = new ArrayList<>();
		for (Action a : possible) {

			ActionContext gen = a.deriveContext(this, forGoal, StateBuilder.start());
			if (gen != null) {
				cons.add(gen);
			}
		}

		// TODO make cost-benefit analysis, perhaps make that an action of its own?

		System.out.println(this.mind + " choosing between "
				+ cons.stream().map((a) -> a.action + " " + a.sideEffect).collect(Collectors.toSet()));
		if (cons.size() == 0)
			return null;
		return cons.get(mind.rand().nextInt(cons.size()));
	}

	public Iterable<ActionContext> getActions() {
		return actions;
	}

	public Iterable<Goal> getGoals() {
		return goals;
	}

	public Iterable<Goal> getFocuses() {
		return focuses;
	}

	public ActionContext getExecutingAction(ActionType forType) {
		return executingActions.get(forType);
	}

	public Iterable<ActionContext> getExecutingActions() {
		return executingActions.values();
	}

	public WillRedo setMaxFocus(int mf) {
		if (mf > ActionType.values().length)
			throw new IllegalArgumentException(
					mf + " is greater than complete maximum of " + ActionType.values().length);
		this.maxFocus = mf;
		return this;
	}

	public WillRedo setMaxActions(int ma) {
		this.maxActions = ma;
		return this;
	}

	public WillRedo setMaxGoals(int maxGoals) {
		this.maxGoals = maxGoals;
		return this;
	}

	/**
	 * max actions that can be focused on at once;
	 * 
	 * @return
	 */
	public int getMaxFocus() {
		return maxFocus;
	}

	public int getMaxActions() {
		return maxActions;
	}

	public int getMaxGoals() {
		return maxGoals;
	}

	@Override
	public Mind getMind() {
		return mind;
	}

	public static class ActionContext {
		public final Action action;
		public final Goal requirement;
		/**
		 * effects of this action that need to be considered for cost-benefit analysis
		 */
		public final State sideEffect;
		/**
		 * the goal that initially spawned this action
		 */
		public final Goal rootGoal;

		/**
		 * side effect can be null
		 * 
		 * @param action
		 * @param requirement
		 * @param result
		 * @param sideEffect
		 */
		public ActionContext(Action action, Goal requirement, Goal rootGoal, State sideEffect) {
			this.action = action;
			this.requirement = requirement;
			this.sideEffect = sideEffect;
			this.rootGoal = rootGoal;
		}

		public boolean hasSideEffect() {
			return sideEffect != null && !sideEffect.isEmpty();
		}
	}
}
