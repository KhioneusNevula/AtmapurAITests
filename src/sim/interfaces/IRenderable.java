package sim.interfaces;

import main.WorldGraphics;

public interface IRenderable {

	public boolean canRender();

	public void draw(WorldGraphics g);
}
