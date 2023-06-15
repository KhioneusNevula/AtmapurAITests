package actor;

import biology.anatomy.Body;
import biology.anatomy.ISpeciesTemplate;
import biology.systems.ESystem;
import mind.IMind;
import mind.need.INeed;
import sim.World;

public class LivingActor extends MultipartActor {

	private IMind mind;

	/**
	 * 
	 * @param world
	 * @param name
	 * @param species can be null
	 * @param startX
	 * @param startY
	 * @param radius
	 */
	public LivingActor(World world, String name, ISpeciesTemplate species, int startX, int startY, int radius) {
		super(world, name, species, startX, startY, radius);
		this.species = species;
	}

	protected void initBody() {
		if (species == null)
			this.body = new Body(this.getUUID());
		else
			this.body = new Body(getUUID(), (ISpeciesTemplate) species);
		((Body) this.body).buildBody();
	}

	/**
	 * Returns the mind of this actor, if it has one
	 * 
	 * @return
	 */
	public IMind getMind() {
		return mind;
	}

	/**
	 * Change the mind of this actor for a different mind
	 * 
	 * @param newMind
	 */
	public void swapMind(IMind newMind) {
		this.mind = newMind;
	}

	protected void setMind(IMind mind) {
		this.mind = mind;
	}

	@Override
	public void senseTick() {
		super.senseTick();
		for (ESystem sys : this.getSystems()) {
			if (!sys.getNeeds().isEmpty()) {
				for (INeed need : sys.getNeeds().values()) {
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

	@Override
	public ISpeciesTemplate getSpecies() {
		return (ISpeciesTemplate) super.getSpecies();
	}

	@Override
	public Body getVisage() {
		return (Body) super.getVisage();
	}

}
