package mind;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.Stack;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.WeakHashMap;
import java.util.stream.Collectors;

import actor.IComponentPart;
import actor.IPartAbility;
import mind.action.IAction;
import mind.action.IActionType;
import mind.action.IInteraction;
import mind.action.IInteractionInstance;
import mind.action.WillingnessMatrix;
import mind.feeling.IFeeling.Axis;
import mind.goals.IGoal;
import mind.goals.IGoal.Priority;
import mind.goals.ITaskGoal;
import mind.goals.ITaskHint;
import mind.need.INeed;
import mind.need.INeed.Degree;
import mind.need.INeed.INeedType;
import mind.relationships.IParty;

public class Will implements IWill {

	private Mind owner;

	private TreeMap<ITaskGoal, ActionQueue> goals;

	private int focusCount = 3;

	private int maxActions = 10;

	private Map<IPartAbility, IAction> utilizedParts;
	private WeakHashMap<ActionQueue, Integer> executionAttempts; // number of attempts to execute each queue;
	private Map<IAction, Integer> currentActions;
	private Map<IInteractionInstance, IAction> currentInteractions;

	public Will(Mind owner) {
		this.owner = owner;
	}

	public Mind getOwner() {
		return owner;
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
	@Override
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

	public void cancelActions() {
		this.currentActions = null;
		this.utilizedParts = null;
		this.executionAttempts = null;
	}

	@Override
	public ActionQueue getQueueForGoal(IGoal goal) {
		return goals != null ? goals.get(goal) : null;
	}

	@Override
	public Collection<IAction> getCurrentActions() {
		return currentActions == null ? Set.of() : currentActions.keySet();
	}

	@Override
	public boolean considerInteraction(IEntity offerer, IInteractionInstance interaction, IInteraction action,
			Priority priority) {
		// TODO consider relationships or whatever idk
		WillingnessMatrix matrix = willingness(action.getType(), action, null, offerer, offerer, Set.of());

		boolean willdo = this.owner.rand().nextDouble() < matrix.getResult();
		if (willdo) {
			if (this.currentInteractions == null) {
				currentInteractions = new HashMap<>();
			}
			currentInteractions.put(interaction, action);
		}

		return willdo;
	}

	/**
	 * TODO update this with 1) personality factors, 2) fine tune relationship
	 * factors
	 */
	@Override
	public WillingnessMatrix willingness(IActionType<?> actionType, IAction action, IGoal goal, IParty mainCompany,
			IParty offerer, Collection<Axis> importantAxes) {
		return WillingnessMatrix.ONE;
		/*
		 * Float priority = (goal != null ? Float.valueOf((float) Math
		 * .pow((Priority.values().length - goal.getPriority().ordinal()) /
		 * (Priority.values().length), 2)) : null); IEmotions emotions =
		 * this.owner.getEmotions(); float unwillingness = Math.min(0,
		 * emotions.unwillingness()); IPersonalRelationship personal = null; boolean
		 * promise = false; Profile beneProfile = mainCompany != null ?
		 * this.owner.getMindMemory().getProfileFor(mainCompany) : null; if (beneProfile
		 * != null) {
		 * 
		 * Relationship relationship = this.owner.getRelationship(beneProfile,
		 * RelationType.FEEL); personal = relationship != null ?
		 * relationship.getGoal().asPersonalRelationship() : null; if (goal != null) {
		 * relationship = this.owner.getRelationship(beneProfile, RelationType.DO);
		 * promise = relationship.getGoal().asTask().equivalent(goal); } } Profile
		 * offererProfile = offerer != null ?
		 * this.owner.getMindMemory().getProfileFor(offerer) : null;
		 * IPersonalRelationship manda = null; if (offererProfile != null) {
		 * Relationship relationship = this.owner.getRelationship(offererProfile,
		 * RelationType.FEEL); manda = relationship != null ?
		 * relationship.getGoal().asPersonalRelationship() : null; if (goal != null) {
		 * relationship = this.owner.getRelationship(offererProfile, RelationType.DO);
		 * promise = promise || relationship.getGoal().asTask().equivalent(goal); } }
		 * Personality personality = owner.personality();
		 * 
		 * float general = priority != null ? Math.min(0, priority - (1 - priority) *
		 * unwillingness) : unwillingness; float targethelp = 0; float generalFactor =
		 * 1; float selfHelpFactor = 0; float otherFactor = 0; float submissionFactor =
		 * 0; for (ITaskHint hint : actionType.getUsage()) { if (hint.helpsSelf())
		 * selfHelpFactor += 2; if (hint.harmsSelf()) selfHelpFactor -= 2; if
		 * (hint.helpsTarget()) targethelp++; if (hint.harmsTarget()) targethelp--; }
		 * selfHelpFactor = MathHelp.clamp(-2, 2, selfHelpFactor);
		 * 
		 * if (personal != null) { Float love = personal.feeling().love(); Float
		 * attraction = personal.feeling().attraction(); otherFactor += (love * 5); if
		 * (attraction >= 0) { selfHelpFactor += attraction; } else { selfHelpFactor +=
		 * 4 * attraction; } } if (manda != null) { Float obedience = manda.obedience();
		 * if (obedience > 0) { submissionFactor += 40 * Math.pow(obedience, 5) + 0.9; }
		 * else { submissionFactor += -1.3 * Math.sqrt(-obedience) + 0.9; } }
		 * 
		 * selfHelpFactor -= Math.abs(selfHelpFactor) Math.min(0,
		 * personality.getTrait(BasicPersonalityTrait.SELFLESSNESS));
		 * 
		 * otherFactor += 1.5 * Math.abs(otherFactor) *
		 * personality.getTrait(BasicPersonalityTrait.SELFLESSNESS);
		 * 
		 * return WillingnessMatrix.create().addFactor(Factor.PRIORITY, generalFactor,
		 * general) .addFactor(Factor.PERSONAL_GAIN, 1, selfHelpFactor)
		 * .addFactor(Factor.SELFLESS_GAIN, otherFactor, targethelp)
		 * .addFactor(Factor.POWER_DYNAMIC, submissionFactor, 1).performCalculation();
		 */
	}

	@Override
	public int getTicksPassed(IAction action) {
		return this.currentActions.getOrDefault(action, -1);
	}

	private void handleNullify() {
		if (this.utilizedParts != null && this.utilizedParts.isEmpty())
			utilizedParts = null;
		if (this.currentActions != null && this.currentActions.isEmpty())
			currentActions = null;
		if (this.goals != null && this.goals.isEmpty())
			this.goals = null;
	}

	/**
	 * Tick when actions are not being performed, only considered
	 */
	public void thinkTick(long tick) {
		this.handleNullify();

		if (tick % 10 > 4 && this.owner.rand().nextBoolean()) {
			this.thinkAboutActions();
		}
		if (tick % 10 < 8 && this.owner.rand().nextBoolean()) {
			this.updateGoalsFromNeeds();
			if (this.owner.rand().nextDouble() < 0.8)
				this.updateGoals();
		}
	}

	/**
	 * Tick executed when actions are being performed
	 */
	public void activeTick(long tick) {
		if (this.decideActions())
			this.updateActions();
	}

	/**
	 * return true if actions can be executed
	 */
	private boolean decideActions() {
		if (goals == null)
			return false;
		Iterator<ITaskGoal> goalsi = this.goals.keySet().stream()
				.sorted((o, t) -> o.getPriority().compareTo(t.getPriority())).iterator();
		ITaskGoal goal = null;
		while (goalsi.hasNext()) {
			goal = goalsi.next();

			if (this.currentActions != null && this.currentActions.size() > this.focusCount) {
				continue;
			}
			ActionQueue queue = goals.get(goal);
			for (IAction aaa : queue.actionQueue) {
				if (this.currentActions != null && this.currentActions.containsKey(aaa))
					continue;
			}
			boolean complete = goal.isComplete(owner);

			if (queue.clearCompleted() || complete) {
				/*
				 * if (!complete) System.out.println(this.owner + " " + goal + " " + queue);
				 */
				this.removeQueueFromCurrent(queue);
				if (complete)
					goals.remove(goal);

				continue;
			}
			if (!queue.ready())
				continue;
			Collection<IPartAbility> slots = this.requiredAbilitySlots(queue.getLatestAction());
			if (this.owner.getOwner().getName().equals("bobzy")) {
				System.out.print("");
			}
			boolean fail = false;
			for (IPartAbility ab : slots)
				if (this.utilizedParts != null && utilizedParts.get(ab) != null) {
					fail = true;
					break;
				}
			if (fail)
				continue;
			Boolean prep = queue.prepareLatestAction();
			if (prep == null || !prep) {
				if (failedAttempt(queue)) {
					queue.clearActions();
				}
				continue;
			} else {
				(this.currentActions == null ? currentActions = new HashMap<>() : currentActions)
						.put(queue.getLatestAction(), -1);
				for (IPartAbility ab : slots) {
					if (this.findUtilizedPartsMap().get(ab) != null)
						throw new IllegalStateException(
								"" + queue.getLatestAction() + " " + utilizedParts + " " + currentActions);
					this.findUtilizedPartsMap().put(ab, queue.getLatestAction());
				}
			}
			return true;
		}
		return currentActions != null && !currentActions.isEmpty();
	}

	/**
	 * return true if it has failed too many times
	 * 
	 * @return
	 */
	private boolean failedAttempt(ActionQueue queue) {
		int attempts = this.executionAttempts == null ? queue.getLatestAction().executionAttempts()
				: this.executionAttempts.getOrDefault(queue, queue.getLatestAction().executionAttempts());
		(this.executionAttempts == null ? executionAttempts = new WeakHashMap<>() : executionAttempts).put(queue,
				attempts - 1);
		if (attempts <= 0) {
			if (this.executionAttempts != null)
				this.executionAttempts.remove(queue);
			return true;
		}
		return false;
	}

	private void updateActions() {
		if (this.currentActions == null)
			return;
		for (Map.Entry<IAction, Integer> entry : Set.copyOf(this.currentActions.entrySet())) {
			IAction action = entry.getKey();
			Integer ticks = entry.getValue();
			if (ticks < 0) {
				action.beginExecutingIndividual(owner);
				entry.setValue(0);
			} else {
				if (action.canContinueExecutingIndividual(owner, ticks)) {
					action.executionTickIndividual(owner, ticks);
					entry.setValue(ticks + 1);
				} else {
					boolean succ = action.finishActionIndividual(owner, ticks);
					for (ActionQueue queue : this.goals.values()) {
						if (queue.getLatestAction() == action) {
							if (!succ)
								queue.tried(action.getType());
							queue.removeLatestAction();
						}
					}

					this.currentActions.remove(action);
					if (this.utilizedParts != null)
						this.utilizedParts.values().removeIf((a) -> a == action);
				}
			}
		}
	}

	private Collection<IPartAbility> requiredAbilitySlots(IAction action) {
		if (!action.getType().requiresMultipartBody())
			return Set.of();
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
		return blockedAbilities == null ? Set.of() : blockedAbilities;
	}

	private Set<IPartAbility> obtainAbilities(IComponentPart part) {
		Set<IPartAbility> abs = new TreeSet<>((on, tw) -> on.getName().compareTo(tw.getName()));
		abs.addAll(part.getType().abilities());
		for (IComponentPart child : part.getChildParts().values()) {
			abs.addAll(obtainAbilities(child));
		}
		return abs;
	}

	private void removeQueueFromCurrent(ActionQueue queue) {
		if (!queue.empty()) {
			if (this.currentActions != null)
				queue.actionQueue.forEach((a) -> currentActions.remove(a));
			if (this.utilizedParts != null)
				this.utilizedParts.values().removeAll(queue.actionQueue);
		}
		if (this.executionAttempts != null)
			this.executionAttempts.remove(queue);
	}

	/**
	 * Construct action queues
	 */
	public void thinkAboutActions() {
		if (goals != null) {
			/*
			 * Iterator<Map.Entry<ITaskGoal, ActionQueue>> goalIter =
			 * goals.entrySet().iterator(); if (goalIter.hasNext()) { for
			 * (Map.Entry<ITaskGoal, ActionQueue> ent = goalIter.next(); goalIter
			 * .hasNext(); ent = goalIter.next()) { if (ent.getKey().isComplete(owner)) {
			 * goalIter.remove(); this.removeQueueFromCurrent(ent.getValue()); } } }
			 */
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
	@Override
	public void updateGoalsFromNeeds() {
		for (Map.Entry<INeedType, Collection<INeed>> entry : Set
				.copyOf(this.owner.getMindMemory().getNeeds().asMap().entrySet())) {
			TreeSet<INeed> needs = new TreeSet<>((o1, o2) -> o1.getDegree().compareTo(o2.getDegree()));
			for (INeed need : Set.copyOf(entry.getValue())) {
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
	@Override
	public void updateGoals() {
		// TODO figure out how to prioritize goals or something idk

		Iterator<IGoal> iter = Set.copyOf(owner.getMindMemory().getGoals()).iterator();
		for (int i = 0; i < this.focusCount; i++) {
			if (goals != null && goals.size() >= this.maxActions) {
				return;
			}

			if (iter.hasNext()) {
				IGoal ofocus = iter.next();

				if (goals != null && goals.containsKey(ofocus)) {
					i--;
					continue;
				}

				if ((ofocus instanceof ITaskGoal)) {
					ITaskGoal focus = (ITaskGoal) ofocus;

					if ((goals != null && goals.keySet().stream().anyMatch((a) -> {
						boolean dumma = a.equivalent(ofocus);
						return dumma;
					})) || (focus.isComplete(owner)) || (focus.isInvalid(owner))) {
						ActionQueue trashq = goals != null ? this.goals.get(focus) : null;
						if (trashq != null) {
							this.removeQueueFromCurrent(trashq);
							this.goals.remove(focus);
						}
						owner.getMindMemory().forgetGoal(focus);

						i--;
						continue;
					}
					if (goals == null) {
						goals = new TreeMap<>((a1, a2) -> a1.getPriority().compareTo(a2.getPriority()));
					}
					ActionQueue aq = null;
					goals.put((ITaskGoal) focus, aq = new ActionQueue(focus));

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
		build.append("\n\tgoals:" + (this.goals == null ? null : this.goals.keySet()));
		build.append("\n\tactionqueues:" + (this.goals == null ? null : this.goals));
		build.append("\n\texecutingActions:" + this.currentActions);
		build.append("\n\tutilizedParts:" + this.utilizedParts);
		build.append("\n\texecutionAttempts:" + this.executionAttempts);
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
		private final ITaskGoal firstGoal;
		private Stack<IAction> actionQueue = new Stack<>();
		private Map<IActionType<?>, Integer> recentlyTried;
		/**
		 * This is staggered above the ActionQueue; the bottommost element of this is
		 * one "above" the bottommost element of the actionqueue
		 */
		private Stack<ITaskGoal> goalQueue = new Stack<>();

		public ActionQueue(ITaskGoal goal) {
			this.firstGoal = goal;
		}

		public void clearActions() {
			this.actionQueue.clear();
			this.goalQueue.clear();
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

		public boolean empty() {
			return this.actionQueue.empty();
		}

		/**
		 * Removes the latest action from the queue and the goal as well
		 */
		private IAction removeLatestAction() {
			this.goalQueue.pop();
			return actionQueue.pop();
		}

		/**
		 * clears actions/goals which are completed (aside from the first goal); return
		 * true if anything changed
		 */
		public boolean clearCompleted() {
			boolean clear = false;
			boolean ret = false;
			for (int i = 0; i < goalQueue.size(); i++) {
				if (clear) {
					goalQueue.remove(i);
					actionQueue.remove(i);
					ret = true;
					i--;
				} else if (goalQueue.get(i).isComplete(owner)) {
					clear = true;
				}
			}
			return ret;
		}

		public ITaskGoal getSecondToLastGoal() {
			if (goalQueue.empty())
				return null;
			if (goalQueue.size() == 1)
				return firstGoal;
			return goalQueue.elementAt(goalQueue.size() - 2);
		}

		public ITaskGoal getLatestGoal() {
			if (goalQueue.empty()) {
				return firstGoal;
			}
			return goalQueue.lastElement();
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
				removeLatestAction();
				tried(action.getType());
				return null;
			}
			if (action.canExecuteIndividual(owner, false)) {
				WillingnessMatrix willingness = willingness(action.getType(), action, goal, action.target(), owner,
						Set.of());
				if (owner.rand().nextDouble() < willingness.getResult()) {
					return true;
				} else {
					removeLatestAction();
					tried(action.getType());
					return null;
				}
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

		private void tried(IActionType<?> type) {
			(recentlyTried == null ? recentlyTried = new HashMap<>() : recentlyTried).put(type, 5);
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
			if (recentlyTried != null) {
				potentialActions.removeIf((a) -> {
					if (recentlyTried.getOrDefault(a, 0) > 0) {
						int o = 0;
						recentlyTried.put(a, o = recentlyTried.get(a) - 1);
						if (o <= 0)
							recentlyTried.remove(a);
						return true;
					}
					return false;
				});
			}
			for (IActionType<?> atype : Set.copyOf(potentialActions)) {
				if (!atype.isViable(owner, goal)) {
					potentialActions.remove(atype);
					tried(atype);
				}
			}

			// TODO priority system or somesh idk; also, what happens if we cant find
			// actions?
			if (potentialActions.isEmpty()) {
				if (!this.actionQueue.empty())
					this.removeLatestAction();
				return;
			}
			for (int i = 0; i < 5; i++) {

				IAction action = potentialActions.stream().collect(Collectors.toList())
						.get(Will.this.owner.rand().nextInt(potentialActions.size())).genAction(goal);
				if (action.canExecuteIndividual(owner, true)) {
					this.pushActionAndGoal(action, ITaskGoal.FINISHED);
					System.out.print("");
					break;
				} else {
					// TODO how to handle multiple actions/goals??
					ITaskGoal goala = action.genConditionGoal(owner);
					if (goala == null) {
						return;
					}
					this.pushActionAndGoal(action, goala);
					System.out.print("");
					break;
				}
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
					if (i == actionQueue.size() - 1) {
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
