package biology.systems.types;

import biology.systems.DoubleSystem;
import biology.systems.ESystem;
import biology.systems.ISystemHolder;
import biology.systems.SystemType;

/**
 * TODO have a way to check the medium the entity is in
 * 
 * @author borah
 *
 */
public class BreathSystem extends DoubleSystem {

	private boolean canBreathe = true;

	public static final int HOLD_BREATH_DURATION = 30 * 5;
	private int holdBreathDuration = HOLD_BREATH_DURATION;

	public BreathSystem(ISystemHolder owner, int max, String valname) {
		super(SystemType.BREATH, owner, max, 0, max, valname);

	}

	@Override
	public boolean canUpdate() {
		return true;
	}

	@Override
	protected void update(long ticks) {
		if (!this.canBreathe) {
			if (!this.isSuffocating()) {
				this.changeValue(-(this.getMaxValue() / ((double) holdBreathDuration)));
			}
		} else {
			if (!this.isMaxed()) {
				this.changeValue((this.getMaxValue() / ((double) 2 * holdBreathDuration)));

			}
		}
	}

	@Override
	protected void update(SystemType<?> type, ESystem other) {
		if (other instanceof HungerSystem hunger) {
			if (this.isMinimum()) {
				hunger.disableNourishing();
			} else {
				hunger.enableNourishing();
			}
		}
	}

	public int getHoldBreathDuration() {
		return holdBreathDuration;
	}

	public void setHoldBreathDuration(int holdBreathDuration) {
		this.holdBreathDuration = holdBreathDuration;
	}

	public void disableBreathing() {
		this.canBreathe = false;
	}

	public void enableBreathing() {
		this.canBreathe = true;
	}

	public boolean canBreathe() {
		return canBreathe;
	}

	/**
	 * whether this has run out of breath
	 */
	public boolean isSuffocating() {
		return this.isMinimum();
	}

	/**
	 * whether this is incapable of breathing or is suffocating
	 * 
	 * @return
	 */
	public boolean isUnableToBreathe() {
		return !this.canBreathe || this.isSuffocating();
	}
}
