package mind;

import java.util.Collection;
import java.util.Random;
import java.util.Set;
import java.util.TreeSet;
import java.util.UUID;
import java.util.stream.Collectors;

import actor.SentientActor;
import mind.concepts.type.IProfile;
import mind.linguistics.NameWord;
import mind.memory.Memory;
import mind.memory.MemoryEmotions;
import mind.personality.Personality;
import mind.relationships.IParty;
import mind.relationships.RelationType;
import mind.relationships.Relationship;

public class Mind implements IIndividualMind, IHasActor {

	private SentientActor owner;
	private UUID id;
	private Memory memory;
	private Will will;
	private boolean conscious = true;
	private int timeAwake;
	private int timeAsleep;
	private NameWord name; // TODO make a better name system
	/**
	 * indicates whether the mind no longer exists for all intents and purposes
	 */
	private boolean dead;
	/**
	 * All cultures this mind is isolated from
	 */
	private Set<Culture> isolatedFrom;
	private Personality personality = new Personality(this);
	/**
	 * Number of ticks before the sense memories are all cleared and actions are all
	 * reset
	 */
	private static final int SENSE_CLEAR_TIME = 25;

	/**
	 * sets uuid to owner id
	 * 
	 * @param owner
	 */
	public Mind(SentientActor owner) {
		this.owner = owner;
		this.id = owner.getUUID();
		this.memory = new Memory(this.id, owner.getClass().getName().toLowerCase());
		this.will = new Will(this);
	}

	@Override
	public void consciousTick(long tick) {
		this.timeAwake++;
		this.memory.getSenses().cleanDeadActors();
		// TODO mind ticks
		if (tick % 5 == 0 && this.rand().nextInt(timeAwake) < 2) {
			this.memory.prune(this.rand().nextInt(2) + 1);
		}
		if (this.name == null) {
			if (this.memory.getMajorLanguage() != null) {
				System.out.println("naming self - " + this.owner.getName());
				this.name = this.memory.getMajorLanguage().name(this.memory.getSelfProfile(), this.rand(), Set.of());
				System.out.println("named self " + name.getDisplay());
			}
		}
		this.will.thinkTick(tick);

	}

	@Override
	public void actionTicks(long tick) {
		this.will.activeTick(tick); // TODO after testing thinking is done ok
	}

	@Override
	public void unconsciousTick(long tick) {
		// TODO mind ticks
		this.timeAsleep++;
		this.memory.getSenses().cleanDeadActors();
		this.memory.prune(timeAsleep * 2 / 3 + 1);
		if (timeAsleep >= SENSE_CLEAR_TIME) {
			this.memory.clearSenses();
		}
		if (timeAsleep == SENSE_CLEAR_TIME)
			this.will.clearMemory();
	}

	@Override
	public boolean isConscious() {
		return conscious;
	}

	/**
	 * Make the mind conscious
	 */
	@Override
	public void wakeUp(long time) {
		this.conscious = true;
		this.timeAwake = 0;

	}

	/**
	 * Put the mind to sleep; make it unconscious
	 */
	@Override
	public void sleep(long time) {
		this.conscious = false;
		this.timeAsleep = 0;

	}

	@Override
	public int getTimeAwake() {
		return this.timeAwake;
	}

	@Override
	public int getTimeAsleep() {
		return this.timeAsleep;
	}

	@Override
	public SentientActor getActor() {
		return owner;
	}

	public Memory getKnowledgeBase() {
		return memory;
	}

	@Override
	public Will getWill() {
		return this.will;
	}

	@Override
	public MemoryEmotions getEmotions() {
		return this.memory.getEmotions();
	}

	@Override
	public Personality personality() {
		return personality;
	}

	@Override
	public boolean hasEmotions() {
		return true;
	}

	public SentientActor getOwner() {
		return owner;
	}

	@Override
	public UUID getUUID() {
		return id;
	}

	@Override
	public Collection<Relationship> getRelationshipsWith(IProfile other) {
		return memory.getRelationshipsWith(other);
	}

	public boolean isPartOf(IProfile group) {
		return memory.isPartOf(group);
	}

	@Override
	public Relationship getRelationshipByID(UUID agreementID) {
		return memory.getRelationshipByID(agreementID);
	}

	@Override
	public void establishRelationship(IParty with, Relationship agreement) {
		memory.establishRelationship(with, agreement);
	}

	@Override
	public Collection<IProfile> getAllPartiesWithRelationships() {
		return memory.getAllPartiesWithRelationships();
	}

	@Override
	public Collection<Relationship> getAllRelationships() {
		return memory.getAllRelationships();
	}

	@Override
	public Relationship getRelationship(IProfile with, RelationType type) {
		return memory.getRelationship(with, type);
	}

	@Override
	public Collection<Relationship> getRelationshipsOfTypeWith(IProfile other, RelationType type) {
		return memory.getRelationshipsWith(other).stream().filter((a) -> a.getType() == type)
				.collect(Collectors.toUnmodifiableSet());
	}

	@Override
	public float getTrust(IProfile with) {
		return memory.getTrust(with);
	}

	@Override
	public void dissolveRelationship(IProfile with, Relationship agreement) {
		memory.dissolveRelationship(with, agreement);
	}

	@Override
	public boolean hasRelationshipsWith(IProfile other) {
		return memory.hasRelationshipsWith(other);
	}

	@Override
	public int hashCode() {
		return super.hashCode() + this.getUUID().hashCode();
	}

	@Override
	public Collection<Culture> cultures() {
		return this.memory.cultures();
	}

	@Override
	public String toString() {
		return (dead ? "dead_" : "") + "mind_" + this.owner.getName();
	}

	@Override
	public NameWord getNameWord() {
		return name;
	}

	@Override
	public String report() {
		StringBuilder builder = new StringBuilder((dead ? "DEAD_" : "") + "Mind_" + this.owner.getName()
				+ (this.name != null ? "(\"" + this.name.getDisplay() + "\")" : "") + "->{");
		builder.append("\n\tpersonality:" + this.personality.report());
		builder.append("\n\tMemory:" + this.memory.report());
		builder.append("\n\t" + this.will.report());
		builder.append("}");
		return builder.toString();
	}

	@Override
	public Random rand() {
		return owner.rand();
	}

	@Override
	public boolean isIsolatedFrom(Culture culture) {
		return isolatedFrom != null ? isolatedFrom.contains(culture) : false;
	}

	/**
	 * Isolates this mind from a culture
	 * 
	 * @param culture
	 */
	public void isolateFrom(Culture culture) {
		if (isolatedFrom == null)
			isolatedFrom = new TreeSet<>();
		isolatedFrom.add(culture);
	}

	/**
	 * Marks this mind as no longer isolated from this culture
	 */
	public void exitIsolation(Culture culture) {
		if (isolatedFrom == null)
			return;
		isolatedFrom.remove(culture);
	}

	/**
	 * Cultures this mind is isolated from
	 * 
	 * @return
	 */
	public Set<Culture> getIsolatedCultures() {
		return isolatedFrom == null ? Set.of() : isolatedFrom;
	}

	public boolean isDead() {
		return dead;
	}

	/**
	 * Registers this mind as dead
	 */
	public void kill() {
		this.dead = true;
		this.conscious = false;
	}

	@Override
	public boolean isNotViable() {
		return this.dead;
	}

}
