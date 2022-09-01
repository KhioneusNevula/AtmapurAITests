package abilities.types;

import java.util.Map;

import com.google.common.collect.ImmutableMap;

import abilities.ESystem;
import abilities.EnergySystem;
import abilities.ISystemHolder;
import abilities.SystemType;
import actor.Eatable;
import energy.IEnergyUnit.EnergyUnit;
import psychology.perception.info.BruteTrait;
import psychology.perception.info.KDataType;

public class HungerSystem extends EnergySystem {

	private double hungerChance;
	private boolean canEat = true;
	private boolean canNourish = true;

	public static enum HungerLevel {
		FULL, WELL_FED, STABLE, HUNGRY, MALNOURISHED, STARVING;

	}

	public static final KDataType<HungerLevel> HUNGER_LEVEL_DATA_TYPE = KDataType.enumType(HungerLevel.class);

	public static final BruteTrait<HungerLevel> HUNGER_TRAIT = new BruteTrait<>("hunger", HUNGER_LEVEL_DATA_TYPE);

	@Override
	public Map<BruteTrait<?>, Object> initTraits() {
		return ImmutableMap.of(HUNGER_TRAIT, HungerLevel.FULL);
	}

	@Override
	public <T> T updateTrait(BruteTrait<T> trait, Object oldval) {
		if (trait == HUNGER_TRAIT) {
			double frac = getPercent();
			if (isEmpty()) {
				return (T) HungerLevel.STARVING;
			} else if (frac <= 0.25) {
				return (T) HungerLevel.MALNOURISHED;
			} else if (frac < 0.5) {
				return (T) HungerLevel.HUNGRY;
			} else if (frac <= 0.75) {
				return (T) HungerLevel.STABLE;
			} else if (this.isFull() || frac > 0.95) {
				return (T) HungerLevel.FULL;
			} else {
				return (T) HungerLevel.WELL_FED;
			}
		}

		return super.updateTrait(trait, oldval);
	}

	/**
	 * chance out of 20
	 * 
	 * @param owner
	 * @param max
	 * @param chance
	 */
	public HungerSystem(ISystemHolder owner, double max, double chance) {

		super(SystemType.HUNGER, owner, EnergyUnit.LIFE, max, max);
		this.hungerChance = chance;
	}

	@Override
	public boolean canUpdate() {
		return canNourish;
	}

	/**
	 * if this entity is capable of eating TODO make eating more general
	 * 
	 * @return
	 */
	public boolean canEat() {
		return canEat;
	}

	/**
	 * if this entity can supply its life system with energy
	 * 
	 * @return
	 */
	public boolean canNourish() {
		return canNourish;
	}

	public void disableEating() {
		this.canEat = false;

	}

	public void enableEating() {
		this.canEat = true;
	}

	public void disableNourishing() {
		this.canNourish = false;
	}

	public void enableNourishing() {
		this.canNourish = true;
	}

	@Override
	protected void update(SystemType<?> type, ESystem other) {

		if (other instanceof LifeSystem life) {
			if (canNourish) {
				this.supplyEnergy(life, life.getPowerUse() * hungerChance / 20.0 + 1);
			}

		}
	}

	public double getHunger() {
		return this.getEnergy();
	}

	public double getHungerChance() {
		return hungerChance;
	}

	/**
	 * eat the food, return whether it can be eaten; -2 means this entity is
	 * incapable of eating, -1 means it's too big, 0 means it provides no
	 * nourishment, 1 means it can be eaten TODO make this more of a continuum
	 */
	public int eat(Eatable edible) {
		if (!this.canEat) {
			return -2;
		}
		if (edible.getNourishment() <= 0)
			return 0;
		if (this.getMaxCapacity() - (this.getEnergy() + edible.getNourishment()) < 0) {
			return -1;
		}
		this.supplyEnergy(edible.getNourishment());
		edible.getEaten(this.getOwner());
		return 1;
	}

}
