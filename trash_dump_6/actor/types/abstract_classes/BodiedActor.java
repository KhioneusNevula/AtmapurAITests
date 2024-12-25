package actor.types.abstract_classes;

import biology.anatomy.AbstractBody;
import biology.anatomy.ISpecies;
import biology.anatomy.MainBody;
import sim.GameMapTile;

public abstract class BodiedActor extends MultipartActor {

	public BodiedActor(GameMapTile world, String name, ISpecies template, int startX, int startY) {
		super(world, name, template, startX, startY);
		this.initBody();
	}

	protected void initBody() {
		if (species == null)
			throw new IllegalStateException("species is null");
		else {
			this.body = new MainBody(this, (ISpecies) species);

		}
		((MainBody) this.body).buildBody();
	}

	@Override
	public AbstractBody getBody() {
		return (AbstractBody) super.getBody();
	}

	@Override
	public AbstractBody getPhysical() {
		return (AbstractBody) super.getPhysical();
	}

	@Override
	public ISpecies getObjectType() {
		return (ISpecies) super.getObjectType();
	}

	@Override
	public AbstractBody getVisage() {
		return (AbstractBody) super.getVisage();
	}

	@Override
	public int physicality() {
		return this.getBody().physicalityMode();
	}

}
