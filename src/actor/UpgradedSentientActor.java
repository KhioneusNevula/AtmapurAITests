package actor;

import java.util.EnumSet;
import java.util.Set;

import biology.anatomy.ISpeciesTemplate;
import biology.systems.ESystem;
import biology.systems.types.ISensor;
import mind.need.INeed;
import mind.personality.Personality.BasicPersonalityTrait;
import mind.thought_exp.IUpgradedMind;
import mind.thought_exp.ThoughtType;
import mind.thought_exp.UpgradedMindImpl;
import mind.thought_exp.culture.UpgradedCulture;
import sim.World;

public class UpgradedSentientActor extends BodiedActor {

	private IUpgradedMind mind;

	/**
	 * 
	 * @param world
	 * @param name
	 * @param species can be null
	 * @param startX
	 * @param startY
	 * @param radius
	 */
	public UpgradedSentientActor(World world, String name, ISpeciesTemplate species, int startX, int startY,
			int radius) {
		super(world, name, species, startX, startY, radius);
		this.species = species;
	}

	protected void initMind() {
		this.mind = new UpgradedMindImpl(this).addSenses(Set.of(ISensor.SIGHT));

		mind.init(EnumSet.allOf(ThoughtType.class));
		UpgradedCulture a = this.getWorld().getOrGenDefaultCulture(this.getSpecies());
		this.mind.getMemory().addCulture(a);

		mind.personality().randomizePersonality(rand, BasicPersonalityTrait.class);
	}

	/**
	 * Returns the mind of this actor, if it has one
	 * 
	 * @return
	 */
	public IUpgradedMind getMind() {
		return mind;
	}

	/**
	 * Change the mind of this actor for a different mind
	 * 
	 * @param newMind
	 */
	public void swapMind(IUpgradedMind newMind) {
		this.mind = newMind;
	}

	protected void setMind(IUpgradedMind mind) {
		this.mind = mind;
	}

	@Override
	public void senseTick() {
		super.senseTick();
		if (mind != null) {
			for (ESystem sys : this.getSystems()) {
				if (!sys.getNeeds().isEmpty()) {
					for (INeed need : sys.getNeeds().values()) {
						if (!mind.getKnowledgeBase().getNeeds(need.getType()).contains(need))
							this.mind.getKnowledgeBase().addNeed(need);
					}
				}
			}
			this.mind.tickSenses(this.getWorld().getTicks());
		}
	}

	@Override
	public void thinkTick() {
		super.thinkTick();
		if (this.mind != null) {
			if (mind.conscious()) {
				this.mind.tickConsciousThoughts(this.getWorld().getTicks());
			} else {
				this.mind.tickUnconscious(this.getWorld().getTicks());
			}
		}
	}

	@Override
	public void movementTick() {
		super.movementTick();
	}

	@Override
	public void actionTick() {
		super.actionTick();
		if (this.mind != null && this.mind.conscious())
			this.mind.tickActions(getWorld().getTicks());
	}

	@Override
	public void finalTick() {
		super.finalTick();
	}

}
