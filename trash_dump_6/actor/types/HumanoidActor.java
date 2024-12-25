package actor.types;

import actor.construction.NutritionType;
import actor.types.abstract_classes.BodiedActor;
import biology.anatomy.ISpecies;
import biology.systems.types.BreathSystem;
import biology.systems.types.HungerSystem;
import biology.systems.types.LifeSystem;
import sim.GameMapTile;

public class HumanoidActor extends BodiedActor {

	public HumanoidActor(GameMapTile world, String name, ISpecies template, int startX, int startY) {
		super(world, name, template, startX, startY);
		this.addSystems(new LifeSystem(this, 1000, 100), new BreathSystem(this, 300, "air"),
				new HungerSystem(this, 100, 0.01f, NutritionType.STANDARD_OMNIVORE));
	}

	@Override
	public String report() {
		return super.report()
				+ (this.getBody().getSoulReference() != null ? "\nsoul:" + this.getBody().getSoulReference().report()
						: "\nno body");
	}

}
