package sim;

import mind.memory.IHasKnowledge;
import sim.interfaces.IRenderable;
import sim.interfaces.IRenderable;

public interface IMindDisplay extends IRenderable {

	public IHasKnowledge getContainedMind();
}
