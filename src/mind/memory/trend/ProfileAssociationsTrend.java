package mind.memory.trend;

import java.util.Map;

import mind.concepts.type.Profile;
import mind.concepts.type.Property;
import mind.memory.IPropertyData;

/**
 * A trend of spreading profile associations. Alternatively, if no associations
 * are given, this is only a recognition trend/ deletion trend
 * 
 * @author borah
 *
 */
public class ProfileAssociationsTrend extends Trend {

	private Map<Property, IPropertyData> props = Map.of();

	public ProfileAssociationsTrend(Profile concept) {
		super(concept);
	}

	public ProfileAssociationsTrend(Profile concept, Map<Property, IPropertyData> props) {
		super(concept);
		this.props = props;
	}

	@Override
	public Profile getConcept() {

		return (Profile) super.getConcept();
	}

	public IPropertyData getProperty(Property prop) {
		return props.get(prop);
	}

	@Override
	public Map<Property, IPropertyData> getData() {
		return props;
	}

	@Override
	public TrendType getType() {
		return TrendType.PROFILE_ASSOCIATIONS;
	}

	@Override
	public String toString() {
		return super.toString() + ":" + this.props;
	}

}
