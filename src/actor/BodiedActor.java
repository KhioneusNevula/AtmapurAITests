package actor;

import biology.anatomy.Body;
import biology.anatomy.ISpeciesTemplate;
import sim.World;

public class BodiedActor extends MultipartActor {

	public BodiedActor(World world, String name, ISpeciesTemplate template, int startX, int startY, int radius) {
		super(world, name, template, startX, startY, radius);
		// TODO Auto-generated constructor stub
	}

	protected void initBody() {
		if (species == null)
			this.body = new Body(this);
		else {
			this.body = new Body(this, (ISpeciesTemplate) species);

		}
		((Body) this.body).buildBody();
	}

	@Override
	public Body getBody() {
		return (Body) super.getBody();
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
