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

		@Override
		public String getUniqueName() {
			return "template_world";
		}

		@Override
		public float averageUniqueness() {
			return 1f;
		}

	};

	public String name();

	/**
	 * For treemap sorting
	 * 
	 * @return
	 */
	public String getUniqueName();

	/**
	 * The average unusualness of any distinctive example of this template
	 * 
	 * @return
	 */
	public float averageUniqueness();

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
