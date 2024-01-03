package mind.memory.trend;

import mind.concepts.PropertyController;
import mind.concepts.type.Property;
import mind.thought_exp.memory.IUpgradedKnowledgeBase;

/**
 * A trend about a property; may have an empty data object if only awareness of
 * the property is needed
 * 
 * @author borah
 *
 */
public class PropertyTrend extends Trend {

	private PropertyController associations;

	public PropertyTrend(Property concept) {
		this(concept, new PropertyController(concept));
	}

	public PropertyTrend(Property concept, PropertyController associations) {
		super(concept);
		this.associations = associations;
	}

	@Override
	public PropertyController getData() {
		return associations;
	}

	@Override
	public Property getConcept() {
		return (Property) super.getConcept();
	}

	@Override
	public TrendType getType() {
		return TrendType.PROPERTY_KNOWLEDGE;
	}

	@Override
	public String toString() {
		return super.toString() + "_" + this.associations;
	}

	@Override
	protected void integrateTrend(IUpgradedKnowledgeBase know) {
		// TODO property trend

	}
}
