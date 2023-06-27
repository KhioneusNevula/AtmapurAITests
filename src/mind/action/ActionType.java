package mind.action;

import java.util.Collection;
import java.util.Set;
import java.util.function.BiPredicate;
import java.util.function.Function;

import actor.IMultipart;
import actor.IPartAbility;
import biology.anatomy.IBodyPartType.Abilities;
import mind.action.types.EatFoodAction;
import mind.action.types.PickUpAction;
import mind.action.types.SearchAction;
import mind.action.types.SleepAction;
import mind.action.types.TalkAction;
import mind.action.types.WalkAction;
import mind.action.types.WanderAction;
import mind.concepts.type.IMeme;
import mind.goals.ITaskGoal;
import mind.goals.ITaskHint;
import mind.goals.TaskHint;
import mind.goals.question.Question.QuestionType;
import mind.memory.IHasKnowledge;

public class ActionType<T extends IAction> implements IActionType<T> {

	/** eating an item */
	public static final ActionType<EatFoodAction> EAT = new ActionType<EatFoodAction>("eat", TaskHint.CONSUME)
			.setGenerator(EatFoodAction::genAction).requireBody().setUsedAbilities(Abilities.EAT, Abilities.GRASP)
			.setRequiredConcepts((a) -> a.usedItem());
	/** walking or running somewhere */
	public static final ActionType<WalkAction> WALK = new ActionType<WalkAction>("walk", TaskHint.TRAVEL).requireBody()
			.setGenerator(WalkAction::new).setUsedAbilities(Abilities.WALK);
	/** sleeping */
	public static final ActionType<SleepAction> SLEEP = new ActionType<SleepAction>("sleep", TaskHint.REST)
			.requireBody().setGenerator(SleepAction::new).setUsedAbilities(Abilities.THINK)
			.setViabilityCondition((a, b) -> a.isMindMemory());
	/** picking up an item */
	public static final ActionType<PickUpAction> PICK_UP = new ActionType<PickUpAction>("pick_up", TaskHint.ACQUIRE)
			.requireBody().setViabilityCondition((a, b) -> a.isMindMemory()).setUsedAbilities(Abilities.GRASP)
			.setGenerator(PickUpAction::new);
	public static final ActionType<WanderAction> WANDER = new ActionType<WanderAction>("wander", TaskHint.LEARN)
			.requireBody().setViabilityCondition((a, b) -> a.isMindMemory() && b.learnTarget() == null)
			.setUsedAbilities(Abilities.WALK).setGenerator(WanderAction::new);

	public static final ActionType<SearchAction> SEARCH = new ActionType<SearchAction>("search", TaskHint.LEARN)
			.requireBody().setUsedAbilities(Abilities.WALK).setGenerator(SearchAction::new).setViabilityCondition(
					(ihk, tg) -> tg.learnTarget() != null && tg.learnTarget().getType() == QuestionType.LOCATION);

	public static final ActionType<TalkAction> TALK = new ActionType<TalkAction>("talk", TaskHint.COMMUNICATE)
			.requireBody().setUsedAbilities(Abilities.SPEAK).setGenerator(TalkAction::new);
	/** drinking an item */
	public static final ActionType DRINK = new ActionType("drink", TaskHint.CONSUME).requireBody()
			.setUsedAbilities(Abilities.EAT, Abilities.GRASP);
	/** riding a mount to a place */
	public static final ActionType RIDE = new ActionType("ride", TaskHint.TRAVEL).requireBody()
			.setUsedAbilities(Abilities.GRASP, Abilities.WALK);

	/** stowing an item in a stockpile/container */
	public static final ActionType STOW = new ActionType("stow", TaskHint.STOW).requireBody()
			.setUsedAbilities(Abilities.GRASP);
	/** dispose of an item */
	public static final ActionType DISPOSE = new ActionType("dispose", TaskHint.DESTROY).requireBody()
			.setUsedAbilities(Abilities.GRASP);
	/** striking something with a fist or weapon */
	public static final ActionType STRIKE = new ActionType("strike", TaskHint.ATTACK, TaskHint.DESTROY, TaskHint.KILL)
			.requireBody().setUsedAbilities(Abilities.GRASP);

	/** striking something with a leg */
	public static final ActionType KICK = new ActionType("kick", TaskHint.ATTACK, TaskHint.DESTROY, TaskHint.KILL)
			.requireBody().setUsedAbilities(Abilities.WALK);
	/** shooting something with a ranged weapon */
	public static final ActionType SHOOT = new ActionType("shoot", TaskHint.ATTACK, TaskHint.DESTROY, TaskHint.KILL)
			.requireBody().setUsedAbilities(Abilities.GRASP);
	/** blocking with the limbs or a tool such as a shield */
	public static final ActionType BLOCK = new ActionType("block", TaskHint.PROTECT).requireBody()
			.setUsedAbilities(Abilities.GRASP);
	/** hunkering down to become less visible */
	public static final ActionType SNEAK = new ActionType("sneak", TaskHint.HIDE).requireBody()
			.setUsedAbilities(Abilities.WALK);
	/** giving an item to someone else */
	public static final ActionType GIVE = new ActionType("give", TaskHint.TRANSFER).requireBody()
			.setUsedAbilities(Abilities.GRASP);
	/** receiving an item from someone else */
	public static final ActionType RECEIVE = new ActionType("receive", TaskHint.ACQUIRE).requireBody()
			.setUsedAbilities(Abilities.GRASP);
	/** dousing an item in fluid to clean it */
	public static final ActionType WASH = new ActionType("wash", TaskHint.CLEAN).requireBody()
			.setUsedAbilities(Abilities.GRASP);
	/** wipe something to clean it */
	public static final ActionType WIPE = new ActionType("wipe", TaskHint.CLEAN).requireBody()
			.setUsedAbilities(Abilities.GRASP);
	/** lick something */
	public static final ActionType LICK = new ActionType("lick", TaskHint.CLEAN, TaskHint.SENSE).requireBody()
			.setUsedAbilities(Abilities.EAT);
	/** sniff something */
	public static final ActionType SNIFF = new ActionType("sniff", TaskHint.SENSE).requireBody()
			.setUsedAbilities(Abilities.THINK);
	/** stare at something */
	public static final ActionType STARE = new ActionType("stare", TaskHint.SENSE).requireBody()
			.setUsedAbilities(Abilities.THINK);
	/** listen intently for something */
	public static final ActionType LISTEN = new ActionType("listen", TaskHint.SENSE).requireBody()
			.setUsedAbilities(Abilities.THINK);
	/** touch something to feel it */
	public static final ActionType TOUCH = new ActionType("touch", TaskHint.SENSE).requireBody()
			.setUsedAbilities(Abilities.GRASP);
	/** talk with someone */
	public static final ActionType CHAT = new ActionType("talk", TaskHint.SOCIALIZE, TaskHint.LEARN, TaskHint.TEACH,
			TaskHint.INFLUENCE).setUsedAbilities(Abilities.SPEAK, Abilities.THINK);
	/** apply a substance which soothes pain */
	public static final ActionType SOOTHE = new ActionType("soothe", TaskHint.RELIEVE)
			.setUsedAbilities(Abilities.GRASP);
	/** generate actions which are done for ritualistic reasons */
	// public static final ActionType RITUAL = new ActionType("ritual",
	// TaskHint.RITUALIZE);
	/** mentally commune with gods */
	public static final ActionType PRAY = new ActionType("pray", TaskHint.RELIEVE, TaskHint.LEARN)
			.setUsedAbilities(Abilities.THINK);
	/** action of reading something */
	public static final ActionType READ = new ActionType("read", TaskHint.LEARN).requireBody()
			.setUsedAbilities(Abilities.THINK);
	/** action of drawing or engraving something */
	public static final ActionType DRAW = new ActionType("draw", TaskHint.CREATE).requireBody()
			.setUsedAbilities(Abilities.GRASP, Abilities.THINK);
	/** action of sculpting artwork */
	public static final ActionType SCULPT = new ActionType("sculpt", TaskHint.CREATE).requireBody()
			.setUsedAbilities(Abilities.GRASP, Abilities.THINK);
	/** action of singing */
	/*
	 * public static final ActionType SING = new ActionType("sing",
	 * TaskHint.CREATE).requireBody() .setFacilities(Facility.MOUTH, Facility.MIND);
	 */
	/** action of dancing */
	/*
	 * public static final ActionType DANCE = new ActionType("dance",
	 * TaskHint.CREATE).requireBody() .setFacilities(Facility.MANIPULATE,
	 * Facility.MOTION, Facility.MIND);
	 */
	/** action of performing an act */
	/*
	 * public static final ActionType PERFORM = new ActionType("perform",
	 * TaskHint.CREATE, TaskHint.INFLUENCE)
	 * .requireBody().setFacilities(Facility.MANIPULATE, Facility.MOTION,
	 * Facility.MOUTH, Facility.MIND);
	 */
	/** action of writing something */
	public static final ActionType WRITE = new ActionType("write", TaskHint.CREATE, TaskHint.TEACH, TaskHint.RECORD)
			.requireBody().setUsedAbilities(Abilities.THINK, Abilities.GRASP);
	/** do a skill to practice it */
	/*
	 * public static final ActionType PRACTICE = new ActionType("practice",
	 * TaskHint.LEARN) .setFacilities(Facility.MANIPULATE, Facility.MIND,
	 * Facility.MOTION);
	 */

	/** make children ig */
	public static final ActionType PROCREATE = new ActionType("procreate", TaskHint.PROCREATE).requireBody()
			.setUsedAbilities(Abilities.WALK, Abilities.GRASP, Abilities.FERTILIZE, Abilities.STORE_SEED,
					Abilities.STORE_EGGS, Abilities.GIVE_BIRTH);

	// special actions
	/** the action which is used to organize info ? maybe not use */
	public static final ActionType CONTEMPLATE = new ActionType("contemplate", TaskHint.LEARN)
			.setUsedAbilities(Abilities.THINK);

	/** the action used to form agreements */
	public static final ActionType OFFER_AGREEMENT = new ActionType("offer_agreement", TaskHint.ALL)
			.setUsedAbilities(Abilities.THINK, Abilities.SPEAK);
	/** the action used to consider and accept/reject agreements */
	public static final ActionType CONSIDER_AGREEMENT = new ActionType("consider_agreement", TaskHint.ALL)
			.setUsedAbilities(Abilities.THINK, Abilities.SPEAK);
	/**
	 * the category of actions which are used to decide actions to complete a goal
	 * through discussion with another
	 */
	public static final ActionType DISCUSS_ACTION = new ActionType("discuss_action", TaskHint.ALL)
			.setUsedAbilities(Abilities.THINK, Abilities.SPEAK);
	/**
	 * the category of actions which produce other actions to fulfill a goal, e.g.
	 * considering a hobby, etc
	 */
	public static final ActionType CONSIDER_ACTION = new ActionType("consider_action", TaskHint.ALL)
			.setUsedAbilities(Abilities.THINK);

	private String name;
	private Set<ITaskHint> usage;
	private Function<ITaskGoal, T> actionGen;
	private boolean requiresBody;
	private BiPredicate<IHasKnowledge, ITaskGoal> viabilityCondition = (a, b) -> true;
	private Set<? extends IPartAbility> facilities = Set.of();
	private Function<ITaskGoal, Collection<IMeme>> requiredKnown = (a) -> Set.of();

	private ActionType(String name, ITaskHint... usage) {
		this.name = name;
		this.usage = Set.of(usage);
	}

	private ActionType<T> setRequiredConcepts(Function<ITaskGoal, Collection<IMeme>> requiredKnown) {
		this.requiredKnown = requiredKnown;
		return this;
	}

	private ActionType<T> setGenerator(Function<ITaskGoal, T> gen) {
		this.actionGen = gen;
		return this;
	}

	private ActionType<T> setUsedAbilities(IPartAbility... facilities) {
		this.facilities = Set.of(facilities);
		return this;
	}

	private ActionType<T> setViabilityCondition(BiPredicate<IHasKnowledge, ITaskGoal> pred) {
		this.viabilityCondition = pred;
		return this;
	}

	private ActionType<T> requireBody() {
		requiresBody = true;
		return this;
	}

	public boolean requiresActor() {
		return requiresBody;
	}

	public boolean isGenerated() {
		return false;
	}

	private boolean requireBodyCondition(IHasKnowledge user) {
		if (!requiresBody)
			return true;
		if (requiresBody && user.hasActor()) {
			if (this.facilities.isEmpty()) {
				return true;
			}
			if (!user.hasMultipartActor()) {
				return false;
			}
			IMultipart body = user.getAsHasActor().getActorAsMultipart().getBody();
			for (IPartAbility ability : this.facilities) {
				if (body.getPartsWithAbility(ability).isEmpty()) {
					return false;
				}
			}
			return true;
		}
		return false;
	}

	@Override
	public boolean isViable(IHasKnowledge user, ITaskGoal goal) {
		return requireBodyCondition(user) && viabilityCondition.test(user, goal);
	}

	@Override
	public Collection<? extends IPartAbility> getUsedAbilities() {
		return facilities;
	}

	public T genAction(ITaskGoal fromNeed) {
		return actionGen.apply(fromNeed);
	}

	public String getName() {
		return name;
	}

	public Set<ITaskHint> getUsage() {
		return usage;
	}

	@Override
	public Collection<IMeme> requiredConcepts(ITaskGoal forGoal) {
		Collection<IMeme> conc = this.requiredKnown.apply(forGoal);
		if (conc == null)
			return Set.of();
		return Set.copyOf(conc);
	}

	@Override
	public String toString() {
		return "action_" + this.name + "{for:" + this.usage + ""
				+ (this.facilities.isEmpty() ? "" : ",facilities:" + this.facilities) + "}";
	}
}