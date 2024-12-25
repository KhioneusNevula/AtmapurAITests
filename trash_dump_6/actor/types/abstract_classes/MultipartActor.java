package actor.types.abstract_classes;

import actor.Actor;
import actor.construction.physical.IActorType;
import actor.construction.physical.IPhysicalActorObject;
import sim.GameMapTile;

public abstract class MultipartActor extends Actor {

	protected IPhysicalActorObject body;

	public MultipartActor(GameMapTile world, String name, IActorType template, int startX, int startY) {
		super(world, name, template, startX, startY);
	}

	public IActorType getObjectType() {
		return (IActorType) super.getObjectType();
	}

	protected abstract void initBody();

	protected void setBody(IPhysicalActorObject body) {
		this.body = body;
	}

	/**
	 * get the body
	 * 
	 * @return
	 */
	public IPhysicalActorObject getBody() {
		return body;
	}

	@Override
	public IPhysicalActorObject getPhysical() {
		return getBody();
	}

	/**
	 * For transformations; swap out the body
	 * 
	 * @param newBody
	 */
	public void transformBody(IPhysicalActorObject newBody) {
		this.body = newBody;
	}

	@Override
	public IPhysicalActorObject getVisage() {
		return body;
	}

}
