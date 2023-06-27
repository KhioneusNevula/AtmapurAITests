package actor;

import sim.World;

public abstract class MultipartActor extends Actor {

	protected IMultipart body;

	public MultipartActor(World world, String name, IBlueprintTemplate template, int startX, int startY, int radius) {
		super(world, name, template, startX, startY, radius);
	}

	public IBlueprintTemplate getSpecies() {
		return (IBlueprintTemplate) super.getSpecies();
	}

	protected abstract void initBody();

	protected void setBody(IMultipart body) {
		this.body = body;
	}

	/**
	 * get the body
	 * 
	 * @return
	 */
	public IMultipart getBody() {
		return body;
	}

	/**
	 * For transformations; swap out the body
	 * 
	 * @param newBody
	 */
	public void transformBody(IMultipart newBody) {
		this.body = newBody;
	}

	@Override
	public IMultipart getVisage() {
		return body;
	}

}
