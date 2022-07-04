package abilities.types;

import abilities.EnergySystem;
import abilities.EntitySystem;
import abilities.ISystemHolder;
import energy.IEnergyUnit.EnergyUnit;
import entity.Eatable;

public class HungerSystem extends EnergySystem {

	private double hungerChance;
	private boolean canEat = true;

	protected HungerSystem(ISystemHolder owner, Object... args) {
		this(owner, (int) args[0], (double) args[1]);

	}

	public HungerSystem(ISystemHolder owner, double max, double chance) {

		super(SystemType.HUNGER, owner, EnergyUnit.LIFE, max, max);
		this.hungerChance = chance;
	}

	@Override
	public boolean isConstantUpdate() {
		return true;
	}

	/**
	 * if this entity is capable of eating TODO make this more general
	 * 
	 * @return
	 */
	public boolean canEat() {
		return canEat;
	}

	public void disableEating() {
		this.canEat = false;

	}

	public void enableEating() {
		this.canEat = true;
	}

	@Override
	public void update(long ticks) {

	}

	@Override
	protected void update(SystemType<?> type, EntitySystem other) {

		if (other instanceof LifeSystem life) {
			if (canEat && hungerChance >= (getOwner().rand().nextDouble() * 20)) {
				this.supplyEnergy(life, 1);
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
		if (this.getMaxEnergy() - (this.getEnergy() + edible.getNourishment()) < 0) {
			return -1;
		}
		this.addEnergy(edible.getNourishment());
		edible.getEaten(this.getOwner());
		return 1;
	}

}
