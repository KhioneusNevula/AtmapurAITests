package metaphysical.soul.generator;

import java.util.Collection;
import java.util.Collections;

import actor.Actor;
import actor.construction.physical.IComponentPart;
import biology.anatomy.BodyAbility;
import metaphysical.soul.ISoul;

/**
 * A class for generating souls for beings. <br>
 * TODO Also is used to send souls to afterlives or something idk.
 * 
 * @author borah
 *
 */
public class SoulGenerator implements ISoulGenerator {

	public SoulGenerator() {
	}

	/**
	 * Called when entities are spawned for the first time
	 * 
	 * @param spawned
	 * @param firstSpawn
	 */
	@Override
	public void onSpawn(Actor spawned) {
		Collection<? extends IComponentPart> souledParts = spawned.getPhysical()
				.getPartsWithAbility(BodyAbility.HAVE_SOUL);
		for (IComponentPart part : souledParts) {
			ISoul soul = spawned.getPhysical().getObjectType().generateSoul(spawned, part);
			spawned.getPhysical().tetherSpirit(soul, Collections.singleton(part));

			System.out.println("Gave soul " + soul + " to " + spawned + " on " + part);
			spawned.getPhysical().onGiveFirstSoul(soul, this);
		}
	}

}
