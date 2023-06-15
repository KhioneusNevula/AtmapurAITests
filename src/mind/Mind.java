package mind;

import java.util.Collection;
import java.util.Random;
import java.util.UUID;

import actor.Actor;
import mind.memory.Memory;
import mind.relationships.IParty;
import mind.relationships.Relationship;

public class Mind implements IMind, IHasActor {

	private Actor owner;
	private UUID id;
	private Memory memory;
	private Will will;
	private boolean conscious = true;
	private int timeAwake;
	private int timeAsleep;
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
	public Mind(Actor owner) {
		this.owner = owner;
		this.id = owner.getUUID();
		this.memory = new Memory(this.id, "unit");
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
	public Actor getActor() {
		return owner;
	}

	public Memory getKnowledgeBase() {
		return memory;
	}

	@Override
	public Will getWill() {
		return this.will;
	}

	public Actor getOwner() {
		return owner;
	}

	@Override
	public UUID getUUID() {
		return id;
	}

	@Override
	public Collection<Relationship> getRelationshipsWith(IParty other) {
		return memory.getRelationshipsWith(other);
	}

	public boolean isPartOf(IGroup group) {
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
	public Collection<IParty> getAllPartiesWithRelationships() {
		return memory.getAllPartiesWithRelationships();
	}

	@Override
	public Collection<Relationship> getAllRelationships() {
		return memory.getAllRelationships();
	}

	@Override
	public void dissolveRelationship(IParty with, Relationship agreement) {
		memory.dissolveRelationship(with, agreement);
	}

	@Override
	public boolean hasRelationshipsWith(IParty other) {
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
		return "mind_" + this.owner.getName();
	}

	@Override
	public String report() {
		StringBuilder builder = new StringBuilder("Mind(" + this.owner.getName() + ")->{");
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
	public long worldTicks() {
		return owner.getWorld().getTicks();
	}

}
