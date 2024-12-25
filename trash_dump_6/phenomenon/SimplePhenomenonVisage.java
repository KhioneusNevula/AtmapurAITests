package phenomenon;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;

import actor.construction.physical.IComponentPart;
import actor.construction.physical.IVisage;
import actor.construction.properties.ISensableTrait;
import actor.construction.properties.SenseProperty;
import biology.sensing.ISense;
import civilization_and_minds.social.concepts.profile.Profile;
import civilization_and_minds.social.concepts.profile.ProfileType;
import sim.interfaces.IObjectType;
import sim.physicality.ExistencePlane;

public class SimplePhenomenonVisage implements IVisage {

	private IPhenomenon owner;
	private int visi = ExistencePlane.ALL_PLANES.primeFactor();
	private Map<SenseProperty<?>, ?> properties;
	private Profile profile;

	public SimplePhenomenonVisage(IPhenomenon owner, Map<SenseProperty<?>, ?> props) {
		this.owner = owner;
		this.properties = new HashMap<>(props);
		this.profile = new Profile(owner.getUUID(), ProfileType.PHENOMENON);
	}

	@Override
	public Profile getProfile() {
		return profile;
	}

	@Override
	public <A extends ISensableTrait> A getGeneralProperty(SenseProperty<A> property, boolean ignoreType) {
		Object p = properties.get(property);
		if (p == null)
			return this.getObjectType().getDefaultSensableProperty(property);
		return (A) p;
	}

	@Override
	public Stream<SenseProperty<?>> getGeneralSensableProperties() {
		return this.properties.keySet().stream();
	}

	@Override
	public IPhenomenon getOwner() {
		return owner;
	}

	@Override
	public IObjectType getObjectType() {
		return owner.getObjectType();
	}

	@Override
	public int sensabilityMode(ISense sense) {
		return visi;
	}

	@Override
	public Stream<? extends IComponentPart> getParts() {
		return Collections.<IComponentPart>emptySet().stream();
	}

	@Override
	public Stream<? extends IComponentPart> getOutermostParts() {
		return Collections.<IComponentPart>emptySet().stream();
	}

	@Override
	public Stream<? extends IComponentPart> getExposedParts() {
		return Collections.<IComponentPart>emptySet().stream();
	}

	@Override
	public boolean hasSinglePart() {
		return false;
	}

	@Override
	public IComponentPart mainComponent() {
		return null;
	}

	@Override
	public void changeSensability(int newVisibility) {
		this.visi = newVisibility;
	}

}
