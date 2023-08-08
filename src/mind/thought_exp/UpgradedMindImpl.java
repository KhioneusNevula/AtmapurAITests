package mind.thought_exp;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.UUID;
import java.util.stream.Collectors;

import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Iterables;
import com.google.common.collect.Multimap;
import com.google.common.collect.MultimapBuilder;
import com.google.common.collect.Multiset;
import com.google.common.collect.TreeMultiset;

import actor.Actor;
import actor.IComponentPart;
import actor.IComponentType;
import actor.IPartAbility;
import actor.UpgradedSentientActor;
import biology.systems.types.ISensor;
import main.Pair;
import mind.Culture;
import mind.IHasActor;
import mind.action.WillingnessMatrix;
import mind.action.WillingnessMatrix.Factor;
import mind.concepts.PropertyController;
import mind.concepts.type.IProfile;
import mind.concepts.type.Profile;
import mind.concepts.type.Property;
import mind.concepts.type.SenseProperty;
import mind.feeling.IFeeling;
import mind.goals.IGoal;
import mind.goals.IGoal.Priority;
import mind.goals.ITaskGoal;
import mind.linguistics.NameWord;
import mind.memory.IEmotions;
import mind.memory.IKnowledgeBase;
import mind.memory.Memory;
import mind.need.INeed;
import mind.personality.Personality;
import mind.personality.Personality.BasicPersonalityTrait;
import mind.relationships.IParty;
import mind.relationships.RelationType;
import mind.relationships.Relationship;
import mind.thought_exp.IThought.IThoughtType;
import mind.thought_exp.actions.IActionThought;
import mind.thought_exp.memory.UpgradedTraitsMemory;
import mind.thought_exp.type.ApplyPropertiesThought;
import mind.thought_exp.type.GoalIntentionThought;
import mind.thought_exp.type.InspirePropertyIdentifierThought;
import phenomenon.IPhenomenon;
import sim.World;

public class UpgradedMindImpl implements IUpgradedMind, IHasActor {

	private boolean active = true;
	private boolean asleep = false;
	private boolean conscious = true;
	private int consciousTicks = 0;
	private UpgradedSentientActor owner;
	private Multimap<IThoughtType, IThought> pausedThoughts;
	private Multimap<IThoughtType, IThought> activeThoughts;
	/** the amount of thoughts of each type that are NOT lightweight thoughts */
	private Multiset<IThoughtType> thoughtCount = TreeMultiset.create((a, b) -> a.ordinalNumber() - b.ordinalNumber());
	private Map<IThought, Integer> tickMap;
	private Set<IThought> unstartedThoughts;
	private Set<IThought> toInterrupt;
	private Set<IThought> toResume;
	private Set<IThought> toPause;
	private Set<IThought> toFinish;
	private Multiset<IThoughtType> maxThoughts = TreeMultiset.create((a, b) -> a.ordinalNumber() - b.ordinalNumber());
	private float defaultLoseFocusChance;
	private float loseFocusChance;
	private Memory memory;
	private Personality personality;
	private UUID id;
	private NameWord name;
	private Random rand;
	private int maxFocusObjects = 8;
	private Collection<ISensor> senses = Set.of();
	private Map<IProfile, Actor> sensedActors;
	private Map<IProfile, UpgradedTraitsMemory> sensedTraits;
	private Map<SenseProperty<?>, ?> environmentallySensed;
	private Map<IProfile, IPhenomenon> sensedPhenomena;
	/**
	 * For thoughts that encapsulate actions which may utilize certain parts
	 */
	private Map<IPartAbility, IThought> utilizedParts;
	/**
	 * TODO better model of sensing ability
	 */
	private int senseDistance = 50;

	public UpgradedMindImpl(UpgradedSentientActor actor) {
		this(actor.getUUID(), actor.getUnitString());
		this.setOwner(actor);
	}

	public UpgradedMindImpl(UUID id, String unitString) {
		this.defaultLoseFocusChance = 0.1f;
		this.loseFocusChance = this.defaultLoseFocusChance;
		this.id = id;
		this.memory = new Memory(id, unitString);
		this.personality = new Personality(this);
		this.rand = new Random(id.getLeastSignificantBits());
	}

	public UpgradedMindImpl addSenses(Collection<ISensor> senses) {
		this.senses = ImmutableSet.<ISensor>builder().addAll(this.senses).addAll(senses).build();
		return this;
	}

	@Override
	public Collection<ISensor> getSenses() {
		return senses;
	}

	public UpgradedMindImpl setOwner(UpgradedSentientActor owner) {
		this.owner = owner;
		return this;
	}

	public UpgradedSentientActor getOwner() {
		return owner;
	}

	@Override
	public UpgradedMindImpl init(Collection<? extends IThoughtType> types) {
		// TODO refine this to change per individual or smthing
		this.personality.randomizePersonality(rand(), BasicPersonalityTrait.class);
		for (IThoughtType type : types) {
			maxThoughts.setCount(type, type.defaultCap());
		}
		return this;
	}

	@Override
	public boolean hasThought(IThought thought) {
		return (this.activeThoughts == null ? false : activeThoughts.containsValue(thought))
				|| (this.pausedThoughts == null ? false : pausedThoughts.containsValue(thought));
	}

	@Override
	public Iterable<IThought> thoughts() {
		return activeThoughts != null
				? (pausedThoughts != null ? Iterables.concat(activeThoughts.values(), pausedThoughts.values())
						: activeThoughts.values())
				: (pausedThoughts == null ? Set.of() : pausedThoughts.values());
	}

	@Override
	public Iterable<IThought> getThoughtsByType(IThoughtType type) {
		return activeThoughts != null
				? (pausedThoughts != null ? Iterables.concat(activeThoughts.get(type), pausedThoughts.get(type))
						: activeThoughts.get(type))
				: (pausedThoughts == null ? Set.of() : pausedThoughts.get(type));
	}

	@Override
	public boolean hasThoughtsOfType(IThoughtType type) {
		return (activeThoughts != null ? !activeThoughts.get(type).isEmpty() : false)
				|| (pausedThoughts != null ? !pausedThoughts.get(type).isEmpty() : false);
	}

	@Override
	public boolean canHaveMoreThoughts(IThoughtType type) {
		return this.thoughtCount.count(type) < this.maxThoughts.count(type);
	}

	/**
	 * TODO tbh this'll be hard to calculate sighhhh
	 */
	@Override
	public WillingnessMatrix calculateForce(IThought thought) {
		if (thought.getPriority() == Priority.OBSESSION)
			return WillingnessMatrix.ONE;
		int priorityWeight = 1;
		int emotionalWeight = 1;
		double stressWeight = 3 * ((this.personality().getTrait(BasicPersonalityTrait.ANXIETY) + 1) / 2);
		float priority = thought.getPriority() == Priority.VITAL ? 1f
				: 1 - (thought.getPriority().ordinal()) / (float) Priority.values().length;
		float feeling = (Math.abs(thought.associatedFeeling().enjoyment()) * 4
				+ Math.abs(thought.associatedFeeling().attraction()) * 1) / 5;
		WillingnessMatrix matrix = WillingnessMatrix.create();
		matrix.addFactor(Factor.STRESS, (float) stressWeight, thought.associatedFeeling().stress())
				.addFactor(Factor.PRIORITY, priorityWeight, priority)
				.addFactor(Factor.EMOTIONAL_FACTOR, emotionalWeight, feeling);
		return matrix.performCalculation();
	}

	/**
	 * Insert thought; if this fails, insert it but paused it
	 * 
	 * @param thought
	 * @param force
	 */
	@Override
	public void insertOrPauseThought(IThought thought, float force) {
		if (!this.insertThought(thought, force)) {
			(this.toPause == null ? toPause = new HashSet<>() : toPause).add(thought);
		}
	}

	@Override
	public boolean insertThought(IThought thought, float force) {

		if (this.canHaveMoreThoughts(thought.getThoughtType()) || thought.isLightweight() || force >= 1f) {
			this.addThought(thought);
			return true;
		} else {
			if (activeThoughts != null) {
				for (IThought compareThought : Set.copyOf(this.activeThoughts.values())) {
					if (compareThought.equivalent(thought))
						return false;
					if (thought.equivalent(compareThought)) {
						(this.toFinish == null ? toFinish = new HashSet<>() : toFinish).add(compareThought);
						this.addThought(thought);
						return true;
					}
				}
				for (IThought compareThought : Set.copyOf(this.activeThoughts.values())) {
					WillingnessMatrix compareMatrix = this.calculateForce(compareThought);
					if (this.rand().nextDouble() <= force * force && compareMatrix.getResult() <= force) {
						(this.toPause == null ? toPause = new HashSet<>() : toPause).add(compareThought);
						this.addThought(thought);
						return true;
					}
				}
			}
			if (this.unstartedThoughts != null) {
				for (IThought compareThought : Set.copyOf(this.unstartedThoughts)) {
					if (compareThought.equivalent(thought))
						return false;
					if (thought.equivalent(compareThought)) {
						this.unstartedThoughts.remove(compareThought);
						this.addThought(thought);
						return true;
					}
				}
				for (IThought compareThought : Set.copyOf(this.unstartedThoughts)) {
					WillingnessMatrix compareMatrix = this.calculateForce(compareThought);
					if (this.rand().nextDouble() <= force * force && compareMatrix.getResult() <= force) {
						this.unstartedThoughts.remove(compareThought);
						this.addThought(thought);
						return true;
					}
				}
			}
			return false;
		}
	}

	private void addThought(IThought thought) {
		(this.unstartedThoughts == null ? unstartedThoughts = new HashSet<>() : unstartedThoughts).add(thought);
		if (!thought.isLightweight())
			this.thoughtCount.add(thought.getThoughtType());
		System.out.print("");
	}

	@Override
	public void forgetThought(IThought thought) {
		if (thought.parentThought() != null) {
			thought.parentThought().endChildThought(thought);
		}
		if (!thought.childThoughts().isEmpty()) {
			for (IThought child : thought.childThoughts()) {
				child.notifyOfParentCompletion();
				child.setParent(null);
			}
		}
		if (this.activeThoughts != null) {
			this.activeThoughts.remove(thought.getThoughtType(), thought);
			if (activeThoughts.isEmpty())
				activeThoughts = null;
		}
		if (this.pausedThoughts != null) {
			this.pausedThoughts.remove(thought.getThoughtType(), thought);
			if (pausedThoughts.isEmpty())
				pausedThoughts = null;
		}
		if (this.tickMap != null) {
			this.tickMap.remove(thought);
			if (tickMap.isEmpty())
				tickMap = null;
		}
		if (this.unstartedThoughts != null) {
			this.unstartedThoughts.remove(thought);
			if (unstartedThoughts.isEmpty())
				unstartedThoughts = null;
		}
		if (this.toInterrupt != null) {
			this.toInterrupt.remove(thought);
			if (toInterrupt.isEmpty())
				toInterrupt = null;
		}
		if (this.toFinish != null) {
			this.toFinish.remove(thought);
			if (toFinish.isEmpty())
				toFinish = null;
		}
		if (this.toPause != null) {
			this.toPause.remove(thought);
			if (toPause.isEmpty())
				toPause = null;
		}
		if (this.toResume != null) {
			this.toResume.remove(thought);
			if (toResume.isEmpty())
				toResume = null;
		}
		if (!thought.isLightweight()) {
			this.thoughtCount.remove(thought.getThoughtType());
		}

		this.releaseAbilitySlots(thought);
	}

	@Override
	public Collection<IThought> forgetAllThoughts(IThoughtType type) {
		Collection<IThought> thoughts = new HashSet<>();
		if (this.activeThoughts != null) {
			thoughts.addAll(activeThoughts.removeAll(type));
			if (activeThoughts.isEmpty())
				activeThoughts = null;

		}
		if (this.pausedThoughts != null) {
			thoughts.addAll(pausedThoughts.removeAll(type));
			if (pausedThoughts.isEmpty())
				pausedThoughts = null;
		}
		if (this.tickMap != null) {
			tickMap.keySet().removeAll(thoughts);
			if (tickMap.isEmpty())
				tickMap = null;
		}
		if (this.toFinish != null) {
			this.toFinish.removeAll(thoughts);
			if (toFinish.isEmpty())
				toFinish = null;
		}
		if (this.toInterrupt != null) {
			this.toInterrupt.removeAll(thoughts);
			if (toInterrupt.isEmpty())
				toInterrupt = null;
		}
		if (this.toPause != null) {
			this.toPause.removeAll(thoughts);
			if (toPause.isEmpty())
				toPause = null;
		}
		if (this.toResume != null) {
			this.toResume.removeAll(thoughts);
			if (toResume.isEmpty())
				toResume = null;
		}
		if (this.unstartedThoughts != null) {
			unstartedThoughts.removeAll(thoughts);
			if (unstartedThoughts.isEmpty())
				unstartedThoughts = null;
		}

		for (IThought thought : thoughts) {
			if (thought.parentThought() != null) {
				thought.parentThought().endChildThought(thought);
			}
			if (!thought.childThoughts().isEmpty()) {
				for (IThought child : thought.childThoughts()) {
					child.notifyOfParentCompletion();
					child.setParent(null);
				}
			}
			this.releaseAbilitySlots(thought);
		}
		return thoughts;
	}

	@Override
	public void forgetAllThoughts() {
		this.activeThoughts = null;
		this.pausedThoughts = null;
		this.tickMap = null;
		this.unstartedThoughts = null;
		this.toInterrupt = null;
		this.toPause = null;
		this.toResume = null;
		this.toFinish = null;
	}

	private void nameSelf() {
		if (this.name == null) {
			if (this.memory.getMajorLanguage() != null) {
				System.out.print("naming self - " + this.owner.getName() + "...");
				this.name = this.memory.getMajorLanguage().name(this.memory.getSelfProfile(), this.rand(), Set.of());
				System.out.println("named self " + name.getDisplay());
			}
		}
	}

	@Override
	public void tickSenses(long worldTicks) {
		this.environmentallySensed = null;
		this.sensedActors = null;
		this.sensedPhenomena = null;
		this.sensedTraits = null;
	}

	@Override
	public void tickConsciousThoughts(long worldTicks) {
		this.nameSelf();

		int ticknumber = 5 + this.rand().nextInt(6);
		for (int i = 0; i < ticknumber; i++) {
			this.produceThoughts(worldTicks);
			this.initiateThoughts(worldTicks);
			this.resumeThoughts(worldTicks);
			this.tickActiveThoughts(worldTicks);

			this.handlePausedThoughts(worldTicks);
		}
		this.consciousTicks++;
	}

	@Override
	public void tickActions(long worldTicks) {
		for (IThought thought : this.getActiveThoughtsOfType(ThoughtType.ACTION)) {
			thought.actionTick(this, this.tickMap.getOrDefault(thought, 0), worldTicks);
		}
	}

	@Override
	public void tickUnconscious(long worldTicks) {
		// TODO unconscious ticks

	}

	/**
	 * Come up with thoughts based on things like, needs, memories, etc
	 * 
	 * @param worldTicks
	 */
	private void produceThoughts(long worldTicks) {
		if (rand().nextInt(6) <= worldTicks % 6)
			this.produceGoalsFromNeeds(worldTicks);
		if (rand().nextInt(5) <= worldTicks % 5)
			this.produceMotivesFromGoals(worldTicks);
		if (rand().nextInt(6) <= worldTicks % 6)
			evaluateProperties(worldTicks);

	}

	private void evaluateProperties(long worldTicks) {
		Collection<Actor> sensedActors = this.senseActors(this.getActor().getWorld(), worldTicks);
		Collection<IPhenomenon> sensedPhenomena = this.sensePhenomena(this.getActor().getWorld(), worldTicks);
		Collection<Property> properties = this.memory.getRecognizedProperties();
		IThought thought = new ApplyPropertiesThought(sensedActors, properties);
		this.insertOrPauseThought(thought, worldTicks);

		if (!sensedPhenomena.isEmpty()) {
			this.insertOrPauseThought(new ApplyPropertiesThought(sensedPhenomena, properties), worldTicks);
		}
		for (Culture culture : this.memory.cultures()) {
			properties = culture.getRecognizedProperties();
			thought = new ApplyPropertiesThought(sensedActors, properties);
			this.insertOrPauseThought(thought, worldTicks);
			if (!sensedPhenomena.isEmpty()) {
				this.insertOrPauseThought(new ApplyPropertiesThought(sensedPhenomena, properties), worldTicks);
			}
		}
		// TODO make this person-specific after communication is implemented, but for
		// now culture general
		if (this.getActiveThoughtsOfType(ThoughtType.REFINE_BELIEFS).isEmpty()) {
			for (IKnowledgeBase knowledge : Iterables.concat(Collections.singleton(memory), this.memory.cultures())) {
				Collection<Property> recoProps = new TreeSet<>((a, b) -> rand().nextBoolean() ? 1 : -1);
				recoProps.addAll(knowledge.getRecognizedProperties());
				for (Property prop : recoProps) {
					PropertyController con = knowledge.getPropertyAssociations(prop);
					if (con.getIdentifier().isUnknown()) {
						if (worldTicks % 20 >= rand().nextInt(20)) {
							this.insertThought(new InspirePropertyIdentifierThought(con, this.getActor().getWorld()),
									worldTicks);
							if (rand().nextInt(10) < 3) {
								break;
							}
						}
					}
				}
			}
		}
	}

	private void produceMotivesFromGoals(long worldTicks) {
		Iterator<IGoal> goals = this.memory.getGoals().iterator();
		if (goals.hasNext()) {
			int max = (int) (this.getMaxThoughtsFor(ThoughtType.INTENTION) / 2.0 + 0.5);
			while (goals.hasNext()) {
				IGoal g = goals.next();
				if (g.isComplete(this)) {
					goals.remove();
					continue;
				}
				if (max >= 0) {
					if (!(g instanceof ITaskGoal))
						continue;
					ITaskGoal goal = g.asTask();
					GoalIntentionThought thought = new GoalIntentionThought(goal);
					boolean unnecessary = false;
					for (IThought comparison : this.getThoughtsByType(ThoughtType.INTENTION)) {
						if (comparison.equivalent(thought)) {
							unnecessary = true;
							break;
						}
					}
					if (unnecessary)
						continue;
					if (unstartedThoughts != null) {
						for (IThought comparison : this.unstartedThoughts) {
							if (comparison.equivalent(thought)) {
								unnecessary = true;
								break;
							}
						}
					}
					if (unnecessary)
						continue;
					WillingnessMatrix willingness = this.calculateForce(thought);
					if (this.insertThought(thought, willingness.getResult())) {
						max--;
					}
				}
			}
		}
	}

	private void produceGoalsFromNeeds(long worldTicks) {
		Iterator<INeed> needs = Set.copyOf(this.memory.getNeeds().values()).iterator();
		if (needs.hasNext()) {
			int max = 4;
			while (needs.hasNext() && max > 0) {
				INeed need = needs.next();
				Iterable<IGoal> pGoal = need.genIndividualGoals();
				for (IGoal goal : pGoal) {
					this.memory.addGoal(goal);

				}
				memory.forgetNeed(need);

				max--;
			}
		}
	}

	private void initiateThoughts(long worldTicks) {
		if (this.unstartedThoughts != null) {
			Iterator<IThought> thoughtIter = unstartedThoughts.iterator();

			while (thoughtIter.hasNext()) {
				IThought thought = thoughtIter.next();
				thought.startThinking(this, worldTicks);
				thoughtIter.remove();
				this.findMultimap(true).put(thought.getThoughtType(), thought);
				this.findTickmap().put(thought, 0);
				System.out.print("");
			}
			unstartedThoughts = null;

		}
	}

	private void resumeThoughts(long worldTicks) {
		if (this.toResume != null) {
			Iterator<IThought> thoughtIter = toResume.iterator();

			while (thoughtIter.hasNext()) {
				IThought thought = thoughtIter.next();
				this.findMultimap(true).put(thought.getThoughtType(), thought);
				thoughtIter.remove();
				this.pausedThoughts.values().removeAll(Collections.singleton(thought));
			}
			toResume = null;
		}
	}

	private void handlePausedThoughts(long worldTicks) {
		if (this.pausedThoughts != null && !this.pausedThoughts.isEmpty()) {
			Iterator<IThought> iterator = pausedThoughts.values().iterator();
			while (iterator.hasNext()) {
				IThought thought = iterator.next();
				int ticks = this.tickMap.getOrDefault(thought, 0);
				if (thought.isFinished(this, ticks, worldTicks)) {
					iterator.remove();
					this.finishThought(thought, worldTicks, ticks, false);
				} else if (thought.isLightweight() || this.canHaveMoreThoughts(thought.getThoughtType())
						&& !thought.resume(this, this.findTickmap().getOrDefault(thought, 0), worldTicks)) {
					(toResume == null ? toResume = new HashSet<>() : toResume).add(thought);

				}
			}
			if (this.pausedThoughts.isEmpty()) {
				pausedThoughts = null;
			}
		}
		if (toPause != null) {
			Iterator<IThought> iterator = toPause.iterator();
			while (iterator.hasNext()) {
				IThought thought = iterator.next();
				this.tryPauseThought(thought, this.tickMap.getOrDefault(thought, 0), worldTicks);
				iterator.remove();
			}
			toPause = null;
		}
	}

	private void tickActiveThoughts(long worldTicks) {
		if (this.activeThoughts != null) {
			for (IThought thought : Set.copyOf(this.activeThoughts.values())) {
				int tickCount = tickMap.getOrDefault(thought, 0);
				boolean finished = this.toFinish != null && this.toFinish.contains(thought);
				boolean interrupted = this.toInterrupt != null && this.toInterrupt.contains(thought);
				if (finished || interrupted || thought.isFinished(this, tickCount, worldTicks)) {
					this.finishThought(thought, worldTicks, tickCount, interrupted);
				} else {
					if (thought.shouldPause(this, tickCount, worldTicks)) {
						(toPause == null ? toPause = new HashSet<>() : toPause).add(thought);
					} else {
						this.tickThought(thought, tickCount, worldTicks);
						tickMap.put(thought, tickCount + 1);
						System.out.print("");
					}
				}

			}
		}
	}

	private void finishThought(IThought thought, long worldTicks, int ticks, boolean interrupted) {
		if (interrupted) {
			thought.interruptThought(this, ticks, worldTicks);
			this.toInterrupt.remove(thought);
			if (toInterrupt.isEmpty())
				toInterrupt = null;
		}
		if (thought.parentThought() != null) {
			thought.parentThought().getInfoFromChild(thought, interrupted, ticks);
		}
		this.forgetThought(thought);
		IThought subsequentThought = thought.getSubsequentThought(interrupted);
		if (subsequentThought != null) {
			WillingnessMatrix matrix = this.calculateForce(subsequentThought);
			this.insertThought(subsequentThought, matrix.getResult());
		}
		Pair<IFeeling, Integer> feeling = thought.finalFeeling();
		this.emotions().add(feeling.getFirst(), feeling.getSecond(), thought);
	}

	private void tickThought(IThought thought, int ticks, long worldTicks) {

		thought.thinkTick(this, ticks, worldTicks);
		if (thought.hasPendingChildThought()) {
			IThought child = thought.peekNextPendingChildThought();
			WillingnessMatrix matrix = this.calculateForce(child);
			child.setParent(thought);
			if (this.insertThought(child, matrix.getResult())) {
				child = thought.popNextPendingChildThought();
			} else {
				this.tryPauseThought(thought, ticks, worldTicks);
			}

		}
	}

	private void tryPauseThought(IThought thought, int ticks, long worldTicks) {
		if (thought.pauseThought(this, ticks, worldTicks)) {
			(this.toInterrupt == null ? this.toInterrupt = new HashSet<>() : toInterrupt).add(thought);
		} else {
			this.findMultimap(false).put(thought.getThoughtType(), thought);
		}
		this.activeThoughts.remove(thought.getThoughtType(), thought);
		this.releaseAbilitySlots(thought);
	}

	private Map<IThought, Integer> findTickmap() {
		return (this.tickMap == null ? tickMap = new HashMap<>() : tickMap);
	}

	private Multimap<IThoughtType, IThought> findMultimap(boolean forActive) {
		if ((forActive ? activeThoughts : pausedThoughts) == null) {
			Multimap<IThoughtType, IThought> multi = MultimapBuilder
					.<IThoughtType>treeKeys((a, b) -> a.ordinalNumber() - b.ordinalNumber()).linkedListValues().build();
			if (forActive)
				activeThoughts = multi;
			else
				pausedThoughts = multi;
		}
		return forActive ? activeThoughts : pausedThoughts;
	}

	@Override
	public void releaseAbilitySlots(IThought thought) {
		if (this.utilizedParts != null) {
			for (IPartAbility a : Set.copyOf(this.utilizedParts.keySet())) {
				utilizedParts.remove(a, thought);
			}
			if (this.utilizedParts.isEmpty())
				utilizedParts = null;
		}
	}

	@Override
	public void reserveAbilitySlots(Collection<IPartAbility> slots, IThought thought) {
		if (this.utilizedParts == null)
			this.utilizedParts = new TreeMap<>((a, b) -> a.getName().compareTo(b.getName()));
		for (IPartAbility ab : slots) {
			this.utilizedParts.put(ab, thought);
		}
	}

	@Override
	public boolean isAnySlotFull(Collection<IPartAbility> slots) {
		if (this.utilizedParts == null)
			return false;
		for (IPartAbility ab : slots) {
			if (this.utilizedParts.get(ab) != null)
				return true;
		}
		return false;
	}

	@Override
	public Collection<IPartAbility> getRequiredAbilitySlots(IActionThought action) {
		if (!action.getType().requiresMultipartBody())
			return Set.of();
		Set<IPartAbility> blockedAbilities = null;
		for (IPartAbility ability : action.usesAbilities()) {
			(blockedAbilities == null
					? blockedAbilities = new TreeSet<>((o1, o2) -> o1.getName().compareTo(o2.getName()))
					: blockedAbilities).add(ability);
			IComponentPart part = null;
			Iterator<? extends IComponentPart> iterator = this.owner.getBody().getPartsWithAbility(ability).iterator();
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

	@Override
	public Memory getMemory() {
		return this.memory;
	}

	@Override
	public IKnowledgeBase getKnowledgeBase() {
		return this.memory;
	}

	@Override
	public String report() {
		return "mind of " + this.owner + " with thoughts: " + this.activeThoughts + ", paused: " + this.pausedThoughts
				+ " and memory: " + this.memory.report();
	}

	@Override
	public Personality personality() {
		return personality;
	}

	@Override
	public IEmotions emotions() {
		return this.memory.getEmotions();
	}

	@Override
	public int getMaxThoughtsFor(IThoughtType type) {
		return this.maxThoughts.count(type);
	}

	@Override
	public Collection<IThought> getPausedThoughts() {
		return this.pausedThoughts == null ? Set.of() : this.pausedThoughts.values();
	}

	@Override
	public boolean isPaused(IThought thought) {
		return pausedThoughts == null ? false : this.pausedThoughts.containsValue(thought);
	}

	@Override
	public Collection<IThought> getActiveThoughts() {
		return this.activeThoughts == null ? Set.of() : this.activeThoughts.values();
	}

	@Override
	public Collection<IThought> getPausedThoughtsOfType(IThoughtType type) {
		return this.pausedThoughts == null ? Set.of() : this.pausedThoughts.get(type);
	}

	@Override
	public Collection<IThought> getActiveThoughtsOfType(IThoughtType type) {
		return this.activeThoughts == null ? Set.of() : this.activeThoughts.get(type);
	}

	@Override
	public float loseFocusChance() {
		return this.loseFocusChance;
	}

	@Override
	public Random rand() {
		return rand;
	}

	@Override
	public boolean isActive() {
		return active;
	}

	@Override
	public void deactivate() {
		this.active = false;
	}

	@Override
	public void kill() {
		this.deactivate();
	}

	@Override
	public void reactivate() {
		active = true;
	}

	@Override
	public boolean isNotViable() {
		return !active;
	}

	@Override
	public boolean conscious() {
		return this.conscious;
	}

	@Override
	public boolean asleep() {
		return this.asleep;
	}

	@Override
	public void sleep() {
		this.conscious = false;
		this.asleep = true;
	}

	@Override
	public void knockOut() {
		this.conscious = false;
		this.asleep = false;
	}

	@Override
	public void wake() {
		this.conscious = true;
		this.asleep = false;
		this.consciousTicks = 0;
	}

	@Override
	public int ticksSinceLastRest() {
		return consciousTicks;
	}

	@Override
	public void dissolveRelationship(IProfile with, Relationship agreement) {
		this.memory.dissolveRelationship(with, agreement);
	}

	@Override
	public void establishRelationship(IParty with, Relationship agreement) {
		this.memory.establishRelationship(with, agreement);
	}

	@Override
	public Collection<IProfile> getAllPartiesWithRelationships() {
		return this.memory.getAllPartiesWithRelationships();
	}

	@Override
	public Collection<Relationship> getAllRelationships() {
		return this.memory.getAllRelationships();
	}

	@Override
	public Relationship getRelationship(IProfile with, RelationType type) {
		return this.memory.getRelationship(with, type);
	}

	@Override
	public Relationship getRelationshipByID(UUID agreementID) {
		return this.memory.getRelationshipByID(agreementID);
	}

	@Override
	public Collection<Relationship> getRelationshipsOfTypeWith(IProfile other, RelationType type) {
		return this.memory.getRelationshipsWith(other).stream().filter((a) -> a.getType() == type)
				.collect(Collectors.toSet());
	}

	@Override
	public Collection<Relationship> getRelationshipsWith(IProfile other) {
		return this.memory.getRelationshipsWith(other);
	}

	@Override
	public float getTrust(IProfile with) {
		return this.memory.getTrust(with);
	}

	@Override
	public boolean hasRelationshipsWith(IProfile other) {
		return this.memory.hasRelationshipsWith(other);
	}

	@Override
	public UUID getUUID() {
		return id;
	}

	@Override
	public NameWord getNameWord() {
		return name;
	}

	@Override
	public Actor getActor() {
		return this.owner;
	}

	@Override
	public int getMaxFocusObjects() {
		return maxFocusObjects;
	}

	@Override
	public Collection<Actor> senseActors(World world, long worldTick) {
		if (sensedActors != null)
			return sensedActors.values();

		Map<IProfile, Actor> map;
		if (!this.hasActor()) {
			map = (sensedActors = new TreeMap<>());
		} else {
			map = new TreeMap<>();
		}
		for (Actor ac : world.getActors()) {
			if (ac.getVisage().isInvisible() || ac.distance(this.owner) > this.senseDistance) {
				continue;
			}
			Profile prof = new Profile(ac);
			map.put(prof, ac);
		}
		if (this.hasActor()) {
			sensedActors = new TreeMap<>(
					(a, b) -> (int) (map.get(a).distance(this.owner) - map.get(b).distance(this.owner)));
			sensedActors.putAll(map);

		}
		return this.sensedActors.values();
	}

	@Override
	public Collection<IPhenomenon> sensePhenomena(World world, long worldTick) {
		// TODO sense phenomena
		return Set.of();
	}

	@Override
	public <T> T senseThings(SenseProperty<T> property, World world, long worldTick) {
		// TODO sense Things
		return null;
	}

	@Override
	public UpgradedTraitsMemory senseTraits(Actor actor, World world, long worldTick) {
		if (this.sensedActors == null) {
			this.senseActors(world, worldTick);
		}
		if (!this.sensedActors.containsValue(actor))
			throw new IllegalArgumentException();
		Profile prof = new Profile(actor);
		if (this.sensedTraits == null) {
			this.sensedTraits = new TreeMap<>();
		}
		UpgradedTraitsMemory traits = sensedTraits.get(prof);
		if (traits == null) {
			this.sensedTraits.put(prof, traits = new UpgradedTraitsMemory(prof));
		}
		for (ISensor sense : this.senses) {
			for (SenseProperty<?> prop : actor.getVisage().getSensableTraits(sense)) {
				Object sensed = actor.getVisage().getTrait(prop);
				if (sensed != null) {
					traits.getGeneralTraits().learnTrait(prop, sensed);
				}
			}
			if (actor.isMultipart()) {
				for (IComponentType type : actor.getAsMultipart().getBody().getPartTypes().values()) {
					for (SenseProperty<?> prop : type.getSensableProperties(sense)) {
						Object sensed = sense.getSensedTrait(actor.getAsMultipart(), prop, type, world, null, actor);
						if (sensed != null) {
							traits.getOrInitTraits(type).learnTrait(prop, sensed);
						}
					}
				}
				for (IComponentPart part : actor.getAsMultipart().getBody().getOutermostParts().values()) {
					for (SenseProperty<?> prop : part.getSensableProperties(sense)) {
						Object sensed = sense.getSpecificSensedTrait(actor.getAsMultipart(), prop, part, world, null,
								actor);
						if (sensed != null) {
							traits.getOrInitTraits(part).learnTrait(prop, sensed);
						}

					}
				}
			}
		}
		return traits;
	}

	@Override
	public Actor getSensedActor(IProfile profile) {
		if (this.sensedActors != null) {
			return sensedActors.get(profile);
		}
		return null;
	}

}
