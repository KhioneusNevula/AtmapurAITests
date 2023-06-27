package phenomenon;

import java.util.Collection;

import biology.systems.types.ISensor;
import mind.concepts.type.Property;
import mind.memory.IPropertyData;

public enum PhenomenonType implements IPhenomenonType {

	DEATH(true, true, false), FLAME(true), EXPLOSION, BURNING(true, true, false), BURN_UP(true, false, true), RAIN,
	MELTING, FREEZING, HYDRATION(true, true, false), DRYING(true, true, false);

	private boolean a;
	private boolean t;
	private boolean r;

	private PhenomenonType() {
	}

	private PhenomenonType(boolean ao) {
		this.a = ao;
	}

	private PhenomenonType(boolean a, boolean t, boolean r) {
		this.a = a;
		this.t = t;
		this.r = r;
	}

	@Override
	public boolean affectsObject() {
		return a;
	}

	@Override
	public boolean isTransformation() {
		return t;
	}

	@Override
	public boolean removesObject() {
		return r;
	}

	@Override
	public Collection<ISensor> getPreferredSensesForHint(Property property) {
		// TODO prefsenses
		return IPhenomenonType.super.getPreferredSensesForHint(property);
	}

	@Override
	public IPropertyData getPropertyHint(Property property) {
		// TODO property hint
		return IPhenomenonType.super.getPropertyHint(property);
	}
}
