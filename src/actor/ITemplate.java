package actor;

import java.util.Collection;
import java.util.Set;

import biology.systems.types.ISensor;
import mind.concepts.type.Property;
import mind.memory.IPropertyData;

public interface ITemplate {

	public static ITemplate WORLD = new ITemplate() {

		@Override
		public String name() {
			return "world";
		}

	};

	public String name();

	default IPropertyData getPropertyHint(Property property) {
		return IPropertyData.UNKNOWN;
	}

	/**
	 * Returns a set of preferred sensors to sense this property (
	 * 
	 * @param property
	 * @return
	 */
	default Collection<ISensor> getPreferredSensesForHint(Property property) {
		return Set.of();
	}

}
