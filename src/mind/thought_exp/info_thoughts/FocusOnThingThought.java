package mind.thought_exp.info_thoughts;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.TreeSet;

import actor.IUniqueExistence;
import mind.goals.IGoal.Priority;
import mind.thought_exp.ICanThink;
import mind.thought_exp.IThought;
import mind.thought_exp.ThoughtType;
import mind.thought_exp.type.AbstractInformationThought;
import sim.World;
import sim.interfaces.IPhysicalExistence;

/**
 * Eventually we will make this thought focus on things based on some interest
 * score but for now it will just be random (?)
 * 
 * @author borah
 *
 */
public class FocusOnThingThought extends AbstractInformationThought<Collection<IUniqueExistence>> {

	private World world;

	public FocusOnThingThought(World sensingWorld) {
		this.world = sensingWorld;
	}

	@Override
	public IThoughtType getThoughtType() {
		return ThoughtType.FOCUS_ON_SENSE;
	}

	@Override
	public boolean isLightweight() {
		return false;
	}

	@Override
	public void startThinking(ICanThink memory, long worldTick) {
		Collection<IUniqueExistence> exist = null;
		if (memory.hasActor()) {
			exist = new TreeSet<>((a,
					b) -> (int) ((a instanceof IPhysicalExistence loc ? loc.distance(memory.getAsHasActor().getActor())
							: 100)
							- (b instanceof IPhysicalExistence loc ? loc.distance(memory.getAsHasActor().getActor())
									: 100)));
		} else {
			exist = new TreeSet<>((a, b) -> a.getUUID().compareTo(b.getUUID()));
		}
		exist.addAll(memory.senseActors(world, worldTick));
		exist.addAll(memory.sensePhenomena(world, worldTick));
		information = new ArrayList<>(Math.min(exist.size(), memory.getMaxFocusObjects()));
		Iterator<IUniqueExistence> it = exist.iterator();
		int i = 0;
		while (it.hasNext() && i < memory.getMaxFocusObjects()) {
			information.add(it.next());
		}
	}

	@Override
	public boolean isFinished(ICanThink memory, int ticks, long worldTick) {
		return true;
	}

	@Override
	public Priority getPriority() {
		return Priority.NORMAL;
	}

	@Override
	public void thinkTick(ICanThink memory, int ticks, long worldTick) {

	}

	@Override
	public void getInfoFromChild(ICanThink memory, IThought childThought, boolean interrupted, int ticks) {

	}

	@Override
	public String displayText() {
		return "focusing on things";
	}

	@Override
	public boolean equivalent(IThought other) {
		return this.equals(other);
	}

}
