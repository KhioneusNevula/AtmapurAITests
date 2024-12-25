package actor.types;

import java.util.Map;

import actor.Actor;
import actor.construction.physical.IComponentPart;
import actor.construction.physical.IComponentType;
import biology.anatomy.IBodyPartType;
import biology.anatomy.ISpecies;
import biology.anatomy.ITissueLayerType;
import biology.anatomy.SeveredBodyPart;
import metaphysical.soul.ISoul;
import sim.GameMapTile;

public class SeveredBodyPartActor extends Actor {

	private SeveredBodyPart parts;

	public static final ISpecies SEVERED_TYPE = new ISpecies() {

		@Override
		public Map<String, ITissueLayerType> tissueTypes() {
			return Map.of();
		}

		@Override
		public String getUniqueName() {
			return "species_severed_part";
		}

		@Override
		public float averageUniqueness() {
			return 0.5f;
		}

		@Override
		public Map<String, IBodyPartType> partTypes() {
			return Map.of();
		}

		@Override
		public String name() {
			return "severed_part";
		}

		@Override
		public boolean mustBeGivenSoul() {
			return false;
		}

		@Override
		public IComponentType mainComponent() {
			return null;
		}

		@Override
		public boolean hasSinglePartType() {
			return false;
		}

		@Override
		public ISoul generateSoul(Actor a, IComponentPart forPart) {
			return null;
		}
	};

	public SeveredBodyPartActor(GameMapTile world, SeveredBodyPart parts, int startX, int startY) {
		super(world, parts.getRootPart().getType().getName(), SEVERED_TYPE, startX, startY);
		this.parts = parts;
	}

	@Override
	public void onSpawn() {
		super.onSpawn();
	}

	@Override
	public SeveredBodyPart getVisage() {
		return parts;
	}

	@Override
	public SeveredBodyPart getPhysical() {
		return parts;
	}

}
