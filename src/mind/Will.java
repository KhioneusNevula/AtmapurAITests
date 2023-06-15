package mind;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.Stack;
import java.util.TreeMap;
import java.util.TreeSet;

import actor.IComponentPart;
import actor.IPartAbility;
import mind.action.IAction;
import mind.action.IActionType;
import mind.goals.IGoal;
import mind.goals.ITaskGoal;
import mind.goals.ITaskHint;
import mind.need.INeed;
import mind.need.INeed.Degree;
import mind.need.INeed.INeedType;

public class Will {

	private Mind owner;

	private Map<ITaskGoal, ActionQueue> goals;

	private int focusCount = 3;

	private int maxActions = 10;

	private Map<IPartAbility, IAction> utilizedParts;
	private List<ActionQueue> currentActionQueues;
	private Map<IAction, Integer> currentActions;

	public Will(Mind owner) {
		this.owner = owner;
	}

	/**
	 * the max amount of goals focused on at once
	 * 
	 * @return
	 */
	public int getFocusCount() {
		return focusCount;
	}

	/**
	 * How many actions is maximum for a queue of actions
	 * 
	 * @return
	 */
	public int getMaxActions() {
		return maxActions;
	}

	/**
	 * Clears the action center
	 */
	public void clearMemory() {
		this.cancelActions();
		this.goals = null;
	}

	/**
	 * Cancels the currently executing actions and returns them in their queues
	 * 
	 * @return
	 */
	public Collection<ActionQueue> cancelActions() {
		List<ActionQueue> set = this.currentActionQueues == null ? List.of() : this.currentActionQueues;
		this.currentActions = null;
		this.utilizedParts = null;
		return set;
	}

	/**
	 * All action queues currently executing
	 * 
	 * @return
	 */
	public List<ActionQueue> getCurrentActionQueues() {
		return this.currentActionQueues == null ? List.of() : currentActionQueues;
	}

	public Collection<IAction> getCurrentActions() {
		return currentActions == null ? Set.of() : currentActions.keySet();
	}

	public int getTicksPassed(IAction action) {
		return this.currentActions.getOrDefault(action, -1);
	}

	/**
	 * Tick when actions are not being performed, only considered
	 */
	public void thinkTick(long tick) {
		if (this.utilizedParts != null && this.utilizedParts.isEmpty())
			utilizedParts = null;
		if (this.currentActionQueues != null && this.currentActionQueues.isEmpty())
			currentActionQueues = null;
		if (this.currentActions != null && this.currentActions.isEmpty())
			currentActions = null;
		if (this.goals != null && this.goals.isEmpty())
			this.goals = null;
		this.thinkAboutActions();
		if (tick % 10 < 4) {
			this.updateGoalsFromNeeds();
			this.updateGoals();
			this.decideActions();
		}
	}

	/**
	 * Tick executed when actions are being performed
	 */
	public void activeTick(long tick) {
		if (tick % 10 < 4)
			this.beginActions();
		this.updateActions();
	}

	/**
	 * Add actions to the currently executing queue from the map of goals for later
	 * execution TODO some form of prioritizing system
	 */
	public void decideActions() {
		if (goals != null) {
			for (ITaskGoal goal : goals.keySet()) {
				ActionQueue queue = goals.get(goal);
				if (currentActionQueues != null && currentActionQueues.size() > focusCount)
					return;
				if (queue.ready() && (currentActionQueues == null || !currentActionQueues.contains(queue))) {
					(currentActionQueues == null ? currentActionQueues = new ArrayList<>() : currentActionQueues)
							.add(queue);
				}
			}
		}
	}

	/**
	 * Put actions from the queue list into the currently executing list
	 */
	public void beginActions() {
		if (this.currentActionQueues != null) {
			for (ActionQueue queue : List.copyOf(this.currentActionQueues)) {
				if (!queue.ready()) {
					// System.out.println("nr " + this.owner + " " + queue);
					currentActionQueues.remove(queue);
					if (utilizedParts != null)
						this.utilizedParts.values().removeIf((a) -> a == queue.getLatestAction());
					if (currentActions != null)
						currentActions.remove(queue.getLatestAction());
					continue;
				}
				if (!queue.canExecute()) {
					// System.out.println("cne " + this.owner + " " + queue);
					if (this.currentActionQueues.size() > this.focusCount || this.owner.rand().nextInt(10) < 3) {
						currentActionQueues.remove(queue);
						if (utilizedParts != null)
							this.utilizedParts.values().removeIf((a) -> a == queue.getLatestAction());
						if (currentActions != null)
							currentActions.remove(queue.getLatestAction());
					}
					continue;
				}
				IAction topA = queue.getLatestAction();
				if (currentActions != null && this.currentActions.containsKey(topA))
					continue;
				ITaskGoal topG = queue.getLatestGoal();
				if (topG.isComplete(owner)) {
					Boolean ready = queue.prepareLatestAction();
					queue.reserveAbilitySlots(topA);
					if (ready == Boolean.FALSE) {
						// System.out.println("fp " + this.owner + " " + queue);
						currentActionQueues.remove(queue);
						if (utilizedParts != null)
							this.utilizedParts.values().removeIf((a) -> a == queue.getLatestAction());
						if (currentActions != null)
							currentActions.remove(topA);
						// System.out.println("" + this.owner + " found action " + topA + " unenactable
						// because "+ topA.reasonForLastFailure());

						continue;
					} else if (ready == Boolean.TRUE) {
						topA.beginExecutingIndividual(owner);
						(this.currentActions == null ? this.currentActions = new HashMap<>() : currentActions).put(topA,
								0);
						// System.out.println("" + this.owner + " began action " + topA);
					} else {
						// System.out.println("bp " + this.owner + " " + queue);
						currentActionQueues.remove(queue);
						if (currentActions != null)
							currentActions.remove(topA);
						if (utilizedParts != null)
							this.utilizedParts.values().removeIf((a) -> a == queue.getLatestAction());
						queue.removeLatestAction();
						queue.removeLatestAction();
						// TODO remove two actions just in case? or no
						// System.out.println("" + this.owner + " found action " + topA + " unviable
						// because "+ topA.reasonForUnviability());

						continue;
					}
				} else {
					currentActionQueues.remove(queue);
					if (utilizedParts != null)
						this.utilizedParts.values().removeIf((a) -> a == queue.getLatestAction());
					if (currentActions != null)
						currentActions.remove(topA);
				}
			}
		}
	}

	/**
	 * Update currently executing actions
	 */
	public void updateActions() {
		if (this.currentActions == null)
			return;
		for (Map.Entry<IAction, Integer> entry : Set.copyOf(this.currentActions.entrySet())) {
			IAction action = entry.getKey();
			if (action.canContinueExecutingIndividual(owner, entry.getValue())) {
				action.executionTickIndividual(owner, entry.getValue());
				entry.setValue(entry.getValue() + 1);
			} else {
				boolean succ = action.finishActionIndividual(owner, entry.getValue());
				// System.out.println("action " + action + " finished for " + this.owner);
				this.currentActions.remove(entry.getKey());
				if (this.utilizedParts != null) {
					this.utilizedParts.values().removeIf((a) -> a == action);
					if (this.utilizedParts.isEmpty())
						utilizedParts = null;
				}
				ActionQueue queue = null;
				if (currentActionQueues != null) {
					for (ActionQueue queu : currentActionQueues) {
						if (queu.getLatestAction() == action) {
							queue = queu;
							break;
						}
					}
				}
				if (queue == null) {
					currentActions.remove(action);
					continue;
				}
				queue.removeLatestAction();

				currentActionQueues.remove(queue);
				if (queue.getFirstGoal().isComplete(owner)) {
					this.goals.remove(queue.getFirstGoal(), queue);
					if (this.goals.isEmpty())
						goals = null;
					if (this.currentActionQueues.isEmpty())
						currentActionQueues = null;
					this.owner.getKnowledgeBase().forgetGoal(queue.getFirstGoal());
				} else {
					currentActionQueues.add(0, queue); // Move the actionqueue to the front because now it is completed
														// its queue is first priority
				}
			}
		}
	}

	/**
	 * Construct action queues
	 */
	public void thinkAboutActions() {
		if (goals != null) {
			for (ActionQueue queue : goals.values()) {
				if (this.owner.rand().nextInt(10) < 3)
					continue;
				if (!queue.ready()) {
					queue.ponderActions();
				}
			}
		}
	}

	/**
	 * Gets the body parts currently in use
	 * 
	 * @return
	 */
	public Map<IPartAbility, IAction> getUtilizedParts() {
		return this.utilizedParts == null ? Map.of() : utilizedParts;
	}

	private Map<IPartAbility, IAction> findUtilizedPartsMap() {
		return this.utilizedParts == null
				? utilizedParts = new TreeMap<>((o1, o2) -> o1.getName().compareTo(o2.getName()))
				: utilizedParts;
	}

	/**
	 * Generates goals from active needs TODO better prioritizing
	 */
	public void updateGoalsFromNeeds() {
		for (Map.Entry<INeedType, Collection<INeed>> entry : Set
				.copyOf(this.owner.getMindMemory().getNeeds().asMap().entrySet())) {
			TreeSet<INeed> needs = new TreeSet<>((o1, o2) -> o1.getDegree().compareTo(o2.getDegree()));
			for (INeed need : entry.getValue()) {
				if (need.getDegree() == Degree.BEYOND || need.getDegree() == Degree.SEVERE) {
					IGoal goal = need.genIndividualGoal();
					if (!goal.isComplete(owner)) {
						Optional<IGoal> poss = owner.getMindMemory().getGoals().stream()
								.filter((goal2) -> goal.equivalent(goal2)).findAny();
						if (poss.isEmpty()) {

						} else {
							owner.getMindMemory().forgetGoal(poss.get());
							// System.out.println("Removed goal " + poss.get() + " for " + this.owner + "
							// because new goal " + goal + " is too similar");
						}
						owner.getMindMemory().addGoal(goal); // TODO avoid duplicate goals
					}
					owner.getMindMemory().forgetNeed(need);
				} else {
					needs.add(need);
				}
			}
			if (!needs.isEmpty()) {
				owner.getMindMemory().addGoal(needs.last().genIndividualGoal());
				owner.getMindMemory().forgetNeed(needs.last());
			}
		}
	}

	/**
	 * Obtains goals from memory to register them for actions
	 */
	public void updateGoals() {
		// TODO figure out how to prioritize goals or something idk

		Iterator<IGoal> iter = owner.getMindMemory().getGoals().iterator();
		for (int i = 0; i < this.focusCount; i++) {
			if (goals != null && goals.size() >= this.maxActions) {
				return;
			}

			if (iter.hasNext()) {
				IGoal ofocus = iter.next();

				if ((ofocus instanceof ITaskGoal)) {
					if (goals != null && goals.keySet().stream().anyMatch((a) -> a.equivalent(ofocus))) {
						i--;
						continue;
					}
					ITaskGoal focus = (ITaskGoal) ofocus;
					if (focus.isInvalid(owner)) {
						owner.getMindMemory().forgetGoal(focus);
						i--;
						continue;
					} else {
						if (goals == null)
							goals = new HashMap<>();
						goals.put((ITaskGoal) focus, new ActionQueue(focus));
					}
				} else {
					i--;
					continue;
				}
			} else {
				break;
			}
		}
	}

	public String report() {
		StringBuilder build = new StringBuilder("Actions->{");
		build.append("\n\tfocusCount:" + this.focusCount);
		build.append("\n\tgoals:" + this.goals);
		build.append("\n\tcurrenctActionQueues:" + this.currentActionQueues);
		build.append("\n\texecutingActions:" + this.currentActions);
		build.append("\n\tutilizedParts:" + this.utilizedParts);
		build.append("}");
		return build.toString();
	}

	/**
	 * A single handler for the actions to complete one goal
	 * 
	 * @author borah
	 *
	 */
	public class ActionQueue {
		private ITaskGoal firstGoal;
		private Stack<IAction> actionQueue = new Stack<>();
		/**
		 * This is staggered above the ActionQueue; the bottommost element of this is
		 * one "above" the bottommost element of the actionqueue
		 */
		private Stack<ITaskGoal> goalQueue = new Stack<>();

		public ActionQueue(ITaskGoal goal) {
			this.firstGoal = goal;
		}

		/**
		 * Whether the actionqueue has reached a finished goal and is thereby "ready to
		 * go"
		 * 
		 * @return
		 */
		public boolean ready() {
			if (actionQueue.empty())
				return false;
			return this.getLatestGoal().isComplete(owner);
		}

		/**
		 * Whether there are a maximum number of actions in the queue
		 * 
		 * @return
		 */
		public boolean isMaximumComplexity() {
			return this.queueSize() >= maxActions;
		}

		/**
		 * If this actionhandler has too many actions and is incomplete
		 * 
		 * @return
		 */
		public boolean isUnviable() {
			return isMaximumComplexity() && !ready();
		}

		public IAction getLatestAction() {
			if (this.actionQueue.empty())
				return null;
			return actionQueue.peek();
		}

		/**
		 * Removes the latest action from the queue and the goal as well
		 */
		private IAction removeLatestAction() {
			this.goalQueue.pop();
			return actionQueue.pop();
		}

		public ITaskGoal getLatestGoal() {
			if (goalQueue.empty()) {
				return firstGoal;
			}
			return goalQueue.lastElement();
		}

		/**
		 * Whether the ability slots for this are open
		 * 
		 * @return
		 */
		public boolean canExecute() {
			for (IPartAbility ability : this.actionQueue.lastElement().usesAbilities()) {
				IAction a = getUtilizedParts().get(ability);
				if (a != null && a != this.getLatestAction()) {
					return false;
				}
			}
			return true;
		}

		private void reserveAbilitySlots(IAction action) {
			if (!action.getType().requiresMultipartBody())
				return;
			Set<IPartAbility> blockedAbilities = null;
			for (IPartAbility ability : action.usesAbilities()) {
				(blockedAbilities == null
						? blockedAbilities = new TreeSet<>((o1, o2) -> o1.getName().compareTo(o2.getName()))
						: blockedAbilities).add(ability);
				IComponentPart part = null;
				Iterator<? extends IComponentPart> iterator = Will.this.owner.getActorAsMultipart().getBody()
						.getPartsWithAbility(ability).iterator();
				if (!iterator.hasNext())
					throw new IllegalStateException("Body does not have correct abilities");
				part = iterator.next();
				blockedAbilities.addAll(obtainAbilities(part));
			}
			for (IPartAbility ability : blockedAbilities) {
				findUtilizedPartsMap().put(ability, action);
			}
		}

		private Set<IPartAbility> obtainAbilities(IComponentPart part) {
			Set<IPartAbility> abs = new TreeSet<>((on, tw) -> on.getName().compareTo(tw.getName()));
			abs.addAll(part.getType().abilities());
			for (IComponentPart child : part.getChildParts().values()) {
				abs.addAll(obtainAbilities(child));
			}
			return abs;
		}

		/**
		 * Prepares the action at the end of the queue (without actually executing it);
		 * assumes Readiness; return false if the next action is not ready (e.g.
		 * conditions changed and goals have to be regenerated); return null if the
		 * action is now fully non-viable
		 */
		private Boolean prepareLatestAction() {
			if (actionQueue.empty())
				throw new IllegalStateException("action queue cannot be empty");
			IAction action = actionQueue.peek();
			ITaskGoal goal = goalQueue.size() <= 1 ? firstGoal : goalQueue.get(goalQueue.size() - 2);
			if (!action.getType().isViable(owner, goal)) {
				return null;
			}
			if (action.canExecuteIndividual(owner, false)) {
				return true;
			} else {
				return false;
			}
		}

		private void pushActionAndGoal(IAction action, ITaskGoal goal) {
			this.actionQueue.push(action);
			this.goalQueue.push(goal);
		}

		/**
		 * How many actions are in the action queue
		 * 
		 * @return
		 */
		public int queueSize() {
			return actionQueue.size();
		}

		private void ponderActions() {
			if (ready())
				throw new IllegalStateException("No more actions to add; stack is complete");
			if (queueSize() >= maxActions)
				throw new IllegalStateException("Cannot add more actions; too many are present");
			ITaskGoal goal = this.getLatestGoal();
			ITaskHint hint = goal.getActionHint();
			Set<IActionType<?>> potentialActions = new HashSet<>(owner.getMindMemory().getPossibleActions(hint));

			potentialActions.addAll(owner.getMindMemory().getPossibleActionsFromCulture(hint).values());
			for (IActionType<?> atype : Set.copyOf(potentialActions)) {
				if (!atype.isViable(owner, goal)) {
					potentialActions.remove(atype);
				}
			}

			// TODO priority system or somesh idk; also, what happens if we cant find
			// actions?
			if (potentialActions.isEmpty()) {

				return;
			}
			IAction action = potentialActions.stream().findAny().get().genAction(goal);
			if (action.canExecuteIndividual(owner, true)) {
				this.pushActionAndGoal(action, ITaskGoal.FINISHED);
			} else {
				// TODO how to handle multiple actions/goals??
				Collection<ITaskGoal> goals = action.genConditionGoal(owner);
				if (goals.isEmpty()) {
					return;
				}
				this.pushActionAndGoal(action, goals.stream().findAny().get());
			}

		}

		public Stack<IAction> getActionQueue() {
			return actionQueue;
		}

		public ITaskGoal getFirstGoal() {
			return firstGoal;
		}

		public Stack<ITaskGoal> getGoalQueue() {
			return goalQueue;
		}

		@Override
		public String toString() {
			StringBuilder builder = new StringBuilder("ActionQueue{");
			builder.append("[" + this.firstGoal + "]->");
			for (int i = 0; i < actionQueue.size(); i++) {
				try {
					builder.append("|" + actionQueue.get(i) + "|");
					ITaskGoal ngoal = goalQueue.get(i);
					boolean c = ngoal.isComplete(owner);
					if (!ngoal.isDone()) {
						builder.append((c ? "-//" : "->[") + ngoal + (c ? "\\\\" : "]->"));
					} else {
						builder.append("->FINISH");
					}
					if (!c && i == actionQueue.size() - 1) {
						builder.append("::" + actionQueue.get(i).reasonForLastFailure());
					}
				} catch (Exception e) {
					throw new RuntimeException(e.getMessage() + ":: for ::" + builder.toString());
				}
			}
			return builder.append("}").toString();
		}
	}

}
