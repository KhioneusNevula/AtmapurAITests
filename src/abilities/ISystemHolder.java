package abilities;

import java.util.Collection;
import java.util.Iterator;
import java.util.Random;

import abilities.types.SystemType;

/**
 * Something which has one or more systems within it
 * 
 * @author borah
 *
 */
public interface ISystemHolder {

	public Collection<EntitySystem> getSystems();

	public Collection<SystemType<?>> getSystemTokens();

	public <T extends EntitySystem> T getSystem(SystemType<T> system);

	public boolean hasSystem(String name);

	public default boolean hasSystem(EntitySystem system) {
		return getSystems().contains(system);
	}

	public default boolean hasSystem(SystemType<?> system) {
		return getSystemTokens().contains(system);
	}

	public default String getSystemsReport() {
		StringBuilder s = new StringBuilder("{");
		Iterator<EntitySystem> iter = getSystems().iterator();
		EntitySystem rep = null;
		for (; iter.hasNext();) {
			rep = iter.next();
			s.append(rep.toString() + "=" + rep.report());
			if (iter.hasNext())
				s.append(", ");
		}
		return s.append("}").toString();

	}

	public Random rand();

}
