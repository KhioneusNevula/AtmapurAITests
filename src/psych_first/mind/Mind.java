package psych_first.mind;

import java.util.Random;
import java.util.function.Consumer;

import culture.CulturalContext;
import culture.Culture;
import entity.Actor;
import psych_first.action.types.Action;
import psych_first.mind.Memory.MemorySection;
import psych_first.mind.Memory.MemoryType;
import sim.ICanHaveMind;
import sociology.Profile;

public class Mind {

	private ICanHaveMind owner;
	private static final int SIGHT = 200;
	private NeedManager needs;
	private int ticks = 0; // mostly just to allow for a periodic internal clock
	private Will personalWill;
	private Memory memory;
	private Random rand = new Random();
	private CulturalContext culture;

	public Mind(ICanHaveMind owner, Culture... cultures) {
		this.owner = owner;
		this.memory = new Memory(this);
		this.needs = new NeedManager(this);
		this.personalWill = new Will(this);
		// TODO make this more generalized
		memory.initializeSections(MemoryType.PROFILE, MemoryType.POSSIBLE_ACTIONS);
		this.rememberFundamentalActions();
		this.culture = CulturalContext.of(owner.getWorld(), cultures);
		if (culture.isUniversal())
			culture = culture.add(owner.getWorld().getCulture(Culture.ROOT));
	}

	public Mind initNeeds(Need... needs) {
		this.needs.addNeeds(needs);
		return this;
	}

	/** TODO make this more generalized, culture, etc */
	private void rememberFundamentalActions() {
		atm(Action.COOK);
		atm(Action.EAT);
		atm(Action.MOVE);
		atm(Action.PICKUP);
		atm(Action.SEARCH);
	}

	/**
	 * adds the action to memory
	 * 
	 * @param act
	 */
	private void atm(Action act) {
		this.getPossibleActions().add(act);
		this.getPossibleActions().makeEternalFor(act);
	}

	public Will getPersonalWill() {
		return personalWill;
	}

	public Memory getMemory() {
		return memory;
	}

	public void observe() {

		// observe needs
		this.needs.update(ticks);

		updateRememberedProfiles();

		if (ticks % 100 == 0) {
			// System.out.println(this.toString() + this.memory.report());
		}
		ticks++;

	}

	/**
	 * TODO maybe use this for when parts of the mind try to make other parts of the
	 * mind do things so we can intercept such communications; return true if
	 * successful
	 */
	public <F extends IMindPart, T extends IMindPart> boolean communicate(F from, T to, Consumer<T> request) {
		request.accept(to);
		return true;
	}

	public void think() {
		this.memory.update(ticks);
		this.personalWill.update(ticks);
	}

	public void updateRememberedProfiles() {
		// observe profiles TODO only actor update is currently implemented lol
		if (this.owner instanceof Actor owner) {
			for (Actor a : owner.getWorld().getActors()) {
				if (a == owner)
					continue;
				if (a.distance(owner) <= SIGHT) {
					if (getRememberedProfiles().getRefreshesFor(a.getProfile()) <= 1)
						getRememberedProfiles().refreshFor(a.getProfile());
				}
			}
		}
	}

	public MemorySection<Profile> getRememberedProfiles() {
		return memory.getMemories(MemoryType.PROFILE);
	}

	public void act() {

		// TODO update goal state

	}

	public Random rand() {
		return rand;
	}

	public ICanHaveMind getOwner() {
		return owner;
	}

	public MemorySection<Action> getPossibleActions() {
		return memory.getMemories(MemoryType.POSSIBLE_ACTIONS);
	}

	public String mindReport() {
		return "{memories=" + this.memory.report() + ",needs" + this.needs.report() + "\n\twill="
				+ this.personalWill.report() + "}";
	}

	public NeedManager getNeeds() {
		return needs;
	}

	@Override
	public String toString() {
		return "<" + this.owner.getName() + "'s mind, " + this.culture + ">";
	}

	/**
	 * gets the cultural background of this individual
	 * 
	 * @return
	 */
	public CulturalContext getCulture() {
		return culture;
	}
}
