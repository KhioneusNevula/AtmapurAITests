package mind.thought_exp.type;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import com.google.common.collect.Iterators;

import actor.ITemplate;
import actor.IUniqueExistence;
import biology.systems.types.ISensor;
import mind.concepts.identifiers.TemplateBasedIdentifier;
import mind.concepts.type.Property;
import mind.goals.IGoal.Priority;
import mind.thought_exp.ICanThink;
import mind.thought_exp.IThought;
import mind.thought_exp.IThoughtMemory;
import mind.thought_exp.IThoughtMemory.Interest;
import mind.thought_exp.ThoughtType;
import mind.thought_exp.memory.type.PropertyIdentifierMemory;
import sim.World;

public class InspirePropertyIdentifierThought extends AbstractThought {

	private boolean done;
	private boolean failed;
	private Iterator<? extends IUniqueExistence> forEntities;
	private World senseworld;
	private Property property;
	private Map<ITemplate, TemplateBasedIdentifier> identifiers = new HashMap<>();

	public InspirePropertyIdentifierThought(Property property, World senseworld) {
		this.property = property;
		this.senseworld = senseworld;
	}

	@Override
	public IThoughtType getThoughtType() {
		return ThoughtType.REFINE_BELIEFS;
	}

	@Override
	public boolean isLightweight() {
		return false;
	}

	@Override
	public IThoughtMemory.Interest shouldBecomeMemory(ICanThink mind, int finishingTicks, long worldTicks) {
		return IThoughtMemory.Interest.FORGET;
	}

	@Override
	public void startThinking(ICanThink memory, long worldTick) {
		forEntities = Iterators.concat(memory.senseActors(senseworld, worldTick).iterator(),
				memory.sensePhenomena(senseworld, worldTick).iterator());
	}

	public boolean failed() {
		return failed;
	}

	@Override
	public boolean isFinished(ICanThink memory, int ticks, long worldTick) {
		return done;
	}

	@Override
	public Priority getPriority() {
		return Priority.NORMAL;
	}

	@Override
	public void thinkTick(ICanThink memory, int ticks, long worldTick) {
		if (forEntities.hasNext()) {
			for (int i = 0; i < memory.getMaxFocusObjects() && forEntities.hasNext(); i++) {
				IUniqueExistence iue = forEntities.next();
				ITemplate template = iue.getVisage().getSpecies();
				boolean senses = true;
				for (ISensor sensor : template.getPreferredSensesForHint(property)) {
					if (!memory.getSenses().contains(sensor)) {
						senses = false;
						break;
					}
				}
				senses = senses && !template.getPropertyHint(property).isUnknown();
				if (senses) {
					TemplateBasedIdentifier identifier = new TemplateBasedIdentifier(template, 1f);
					identifiers.put(template, identifier);
				}
			}
		} else {
			done = true;
		}
	}

	@Override
	public void getInfoFromChild(IThought childThought, boolean interrupted, int ticks) {

	}

	@Override
	public Map<IThoughtMemory, Interest> getMemory(ICanThink mind, int finishingTicks, long worldTicks) {
		Map<IThoughtMemory, Interest> ls = new HashMap<>();
		for (TemplateBasedIdentifier id : this.identifiers.values()) {
			ls.put(new PropertyIdentifierMemory(property, id), Interest.FORGET);
		}
		if (!ls.isEmpty()) {
			System.out.print("");
		}
		return ls;
	}

	@Override
	public String displayText() {
		return "inspiring property " + property;
	}

	@Override
	public boolean equivalent(IThought other) {
		return this.equals(other);
	}

}
