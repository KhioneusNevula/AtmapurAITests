package sim;

import abilities.ISystemHolder;
import psych_first.mind.Mind;

public interface ICanHaveMind extends IHasProfile, ISystemHolder {

	public boolean hasMind();

	public Mind getMind();

	public World getWorld();
}
