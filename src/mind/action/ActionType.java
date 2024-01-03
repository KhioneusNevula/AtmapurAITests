package mind.action;

import java.util.Collection;
import java.util.Set;
import java.util.function.BiPredicate;
import java.util.function.Function;

import actor.IMultipart;
import actor.IPartAbility;
import biology.anatomy.IBodyPartType.Abilities;
import mind.concepts.type.IMeme;
import mind.goals.ITaskGoal;
import mind.goals.ITaskHint;
import mind.goals.TaskHint;
import mind.goals.question.Question.QuestionType;
import mind.thought_exp.IUpgradedHasKnowledge;
import mind.thought_exp.IUpgradedMind;
import mind.thought_exp.actions.EatActionThought;
import mind.thought_exp.actions.IActionThought;
import mind.thought_exp.actions.PickupActionThought;
import mind.thought_exp.actions.SearchActionThought;
import mind.thought_exp.actions.SleepActionThought;
import mind.thought_exp.actions.WalkActionThought;

public class ActionType<T extends IActionThought> implements IActionType<T> {

	/** eating an item */
	public static final ActionType<EatActionThought> EAT = new ActionType<EatActionThought>("eat", TaskHint.CONSUME)
			.setThoughtGenerator(EatActionThought::new).requireBody().setUsedAbilities(Abilities.EAT, Abilities.GRASP)
			.setRequiredConcepts((a) -> a.usedItem());
	/** walking or running somewhere */
	public static final ActionType<WalkActionThought> WALK = new ActionType<WalkActionThought>("walk", TaskHint.TRAVEL)
			.requireBody().setThoughtGenerator(WalkActionThought::new).setUsedAbilities(Abilities.WALK);
	/** sleeping */
	public static final ActionType<SleepActionThought> SLEEP = new ActionType<SleepActionThought>("sleep",
			TaskHint.REST).requireBody().setUsedAbilities(Abilities.THINK)
					.setViabilityCondition((a, b) -> a instanceof IUpgradedMind);
	/** picking up an item */
	public static final ActionType<PickupActionThought> PICK_UP = new ActionType<PickupActionThought>("pick_up",
			TaskHint.ACQUIRE).requireBody()
					.setViabilityCondition((a, b) -> a.isMindMemory() || a instanceof IUpgradedMind)
					.setUsedAbilities(Abilities.GRASP).setThoughtGenerator(PickupActionThought::new);
	public static final ActionType<SearchActionThought> SEARCH = new ActionType<SearchActionThought>("search",
			TaskHint.LEARN).requireBody().setUsedAbilities(Abilities.WALK).setThoughtGenerator(SearchActionThought::new)
					.setViabilityCondition(
							(ihk, tg) -> tg.learnInfo() != null && tg.learnInfo().getType() == QuestionType.LOCATION);

	public static final ActionType WANDER = new ActionType<>("wander", TaskHint.LEARN)
			.setViabilityCondition((a, b) -> (a instanceof IUpgradedMind) && b.learnInfo() == null).requireBody()
			.setUsedAbilities(Abilities.WALK);

	public static final ActionType TALK = new ActionType<>("talk", TaskHint.COMMUNICATE).requireBody().interaction()
			.setUsedAbilities(Abilities.SPEAK);
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
	private boolean requiresBody;
	private BiPredicate<IUpgradedHasKnowledge, ITaskGoal> viabilityCondition = (a, b) -> true;
	private Set<? extends IPartAbility> facilities = Set.of();
	private Function<ITaskGoal, Collection<IMeme>> requiredKnown = (a) -> Set.of();
	private boolean interaction;
	private Function<ITaskGoal, T> actionThoughtGen = (a) -> null;

	private ActionType(String name, ITaskHint... usage) {
		this.name = name;
		this.usage = Set.of(usage);
	}

	private ActionType<T> setRequiredConcepts(Function<ITaskGoal, Collection<IMeme>> requiredKnown) {
		this.requiredKnown = requiredKnown;
		return this;
	}

	private ActionType<T> setThoughtGenerator(Function<ITaskGoal, T> gen) {
		this.actionThoughtGen = gen;
		return this;
	}

	private ActionType<T> setUsedAbilities(IPartAbility... facilities) {
		this.facilities = Set.of(facilities);
		return this;
	}

	private ActionType<T> setViabilityCondition(BiPredicate<IUpgradedHasKnowledge, ITaskGoal> pred) {
		this.viabilityCondition = pred;
		return this;
	}

	private ActionType<T> requireBody() {
		requiresBody = true;
		return this;
	}

	private ActionType<T> interaction() {
		interaction = true;
		return this;
	}

	public boolean isInteraction() {
		return interaction;
	}

	public boolean requiresActor() {
		return requiresBody;
	}

	public boolean isGenerated() {
		return false;
	}

	private boolean requireBodyCondition(IUpgradedHasKnowledge user) {
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
	public boolean isViable(IUpgradedHasKnowledge user, ITaskGoal goal) {
		return requireBodyCondition(user) && viabilityCondition.test(user, goal);
	}

	@Override
	public Collection<? extends IPartAbility> getUsedAbilities() {
		return facilities;
	}

	@Override
	public T genActionThought(ITaskGoal fromNeed) {
		return actionThoughtGen.apply(fromNeed);
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