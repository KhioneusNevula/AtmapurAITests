package mind.memory.trend;

import java.util.Map;

import mind.concepts.type.Profile;
import mind.concepts.type.Property;
import mind.memory.RememberedProperties;

/**
 * A trend of spreading profile associations
 * 
 * @author borah
 *
 */
public class ProfileAssociationsTrend extends Trend {

	private Map<Property, RememberedProperties> props;

	public ProfileAssociationsTrend(Profile concept, Map<Property, RememberedProperties> props) {
		super(concept);
		this.props = props;
	}

	@Override
	public Profile getConcept() {

		return (Profile) super.getConcept();
	}

	public RememberedProperties getProperty(Property prop) {
		return props.get(prop);
	}

	@Override
	public Map<Property, RememberedProperties> getData() {
		return props;
	}

}
