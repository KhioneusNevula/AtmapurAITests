package sim.interfaces;

import sim.WorldGraphics;

public interface IRenderable {

	public boolean canRender();

	public void draw(WorldGraphics g);
}
