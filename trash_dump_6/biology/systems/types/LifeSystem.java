package biology.systems.types;

import java.util.Collections;
import java.util.stream.Stream;

import actor.construction.ISystemHolder;
import biology.systems.EnergySystem;
import biology.systems.SystemType;
import civilization_and_minds.mind.goals.INeed;
import energy.IEnergyUnit.EnergyUnit;
import energy.IEnergyUser;

/**
 * by default, you have ten seconds to live if you are not absorbing energy and
 * have full stored energy
 * 
 * @author borah
 *
 */
public class LifeSystem extends EnergySystem implements IEnergyUser {

	private double severe;
	private double powerUse;

	public static enum HealthState {
		SURVIVING, DYING, DEAD;
	}

	public LifeSystem(ISystemHolder owner, double max, double severe) {

		super(SystemType.LIFE, owner, EnergyUnit.LIFE, max, max);
		this.severe = severe;
		this.powerUse = max / 1000.0;
	}

	public LifeSystem(ISystemHolder owner, int max) {
		this(owner, max, ((double) max / 4));
	}

	public double getSevere() {
		return severe;
	}

	public void setSevere(int severe) {
		this.severe = severe;
	}

	@Override
	public boolean canUpdate() {
		return true;
	}

	@Override
	protected void update(long ticks) {
		this.drainEnergy(getPowerUse());

	}

	public boolean isSevere() {
		return getEnergy() <= severe;
	}

	public boolean isDead() {
		return this.isEmpty();
	}

	@Override
	public double getPowerUse() {
		return this.powerUse;
	}

	@Override
	public boolean changedStateSinceLastUpdatedNeeds() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public Stream<? extends INeed> generateNeeds() {
		// TODO Auto-generated method stub
		return Collections.<INeed>emptySet().stream();
	}

}
