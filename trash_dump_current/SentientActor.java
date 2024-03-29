package actor;

import biology.anatomy.ISpeciesTemplate;
import biology.systems.ESystem;
import mind.Culture;
import mind.IIndividualMind;
import mind.Mind;
import mind.need.INeed;
import mind.personality.Personality.BasicPersonalityTrait;
import sim.World;

public class SentientActor extends BodiedActor {

	private IIndividualMind mind;

	/**
	 * 
	 * @param world
	 * @param name
	 * @param species can be null
	 * @param startX
	 * @param startY
	 * @param radius
	 */
	public SentientActor(World world, String name, ISpeciesTemplate species, int startX, int startY, int radius) {
		super(world, name, species, startX, startY, radius);
		this.species = species;
	}

	protected void initMind() {
		this.mind = new Mind(this);
		Culture a = this.getWorld().getOrGenDefaultCulture(this.getSpecies());
		this.mind.getMindMemory().addCulture(a);
		mind.personality().randomizePersonality(rand, BasicPersonalityTrait.class);
	}

	/**
	 * Returns the mind of this actor, if it has one
	 * 
	 * @return
	 */
	public IIndividualMind getMind() {
		return mind;
	}

	/**
	 * Change the mind of this actor for a different mind
	 * 
	 * @param newMind
	 */
	public void swapMind(IIndividualMind newMind) {
		this.mind = newMind;
	}

	protected void setMind(IIndividualMind mind) {
		this.mind = mind;
	}

	@Override
	public void senseTick() {
		super.senseTick();
		for (ESystem sys : this.getSystems()) {
			if (!sys.getNeeds().isEmpty()) {
				for (INeed need : sys.getNeeds().values()) {
					if (!mind.getKnowledgeBase().getNeeds().get(need.getType()).contains(need))
						this.mind.getKnowledgeBase().addNeed(need);
				}
			}
		}
	}

	@Override
	public void thinkTick() {
		super.thinkTick();
		if (this.mind != null) {
			this.mind.mindTick(this.getWorld().getTicks());
		}
	}

	@Override
	public void movementTick() {
		super.movementTick();
	}

	@Override
	public void actionTick() {
		super.actionTick();
		if (this.mind != null)
			this.mind.actionTicks(getWorld().getTicks());
	}

	@Override
	public void finalTick() {
		super.finalTick();
	}

}
