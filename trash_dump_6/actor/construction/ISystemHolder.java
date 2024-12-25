package actor.construction;

import java.util.Collection;
import java.util.Iterator;
import java.util.Random;

import biology.systems.ESystem;
import biology.systems.SystemType;

/**
 * Something which has one or more systems within it
 * 
 * @author borah
 *
 */
public interface ISystemHolder {

	public Collection<ESystem> getSystems();

	public Collection<SystemType<?>> getSystemTokens();

	public <T extends ESystem> T getSystem(SystemType<T> system);

	public boolean hasSystem(String name);

	public default boolean hasSystem(ESystem system) {
		return getSystems().contains(system);
	}

	public default boolean hasSystem(SystemType<?> system) {
		return getSystemTokens().contains(system);
	}

	public default String getSystemsReport() {
		StringBuilder s = new StringBuilder("{");
		Iterator<ESystem> iter = getSystems().iterator();
		ESystem rep = null;
		for (; iter.hasNext();) {
			rep = iter.next();
			s.append(rep.getType() + "=" + rep.report());
			if (iter.hasNext())
				s.append(", ");
		}
		return s.append("}").toString();

	}

	public Random rand();

}
