package sim.interfaces;

import java.util.UUID;

import psychology.perception.Profile;
import psychology.perception.info.BruteTrait;

public interface IHasProfile extends IHasInfo, ITyped {

	/**
	 * TODO complete IHasProfile
	 */

	public UUID getUuid();

	public Profile getProfile();

	public String getName();

	public <T> T getTrait(BruteTrait<T> trait, boolean update);

	public boolean hasTrait(BruteTrait<?> trait);

}
