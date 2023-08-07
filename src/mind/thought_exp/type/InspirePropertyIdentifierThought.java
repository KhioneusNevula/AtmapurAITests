package mind.thought_exp.type;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import com.google.common.collect.Iterators;

import actor.ITemplate;
import actor.IUniqueExistence;
import biology.systems.types.ISensor;
import humans.Food;
import mind.concepts.PropertyController;
import mind.concepts.identifiers.TemplateBasedIdentifier;
import mind.concepts.type.BasicProperties;
import mind.goals.IGoal.Priority;
import mind.memory.IKnowledgeBase.Interest;
import mind.thought_exp.ICanThink;
import mind.thought_exp.IThought;
import mind.thought_exp.ThoughtType;
import sim.World;

public class InspirePropertyIdentifierThought extends AbstractThought {

	private boolean done;
	private boolean failed;
	private Iterator<? extends IUniqueExistence> forEntities;
	private World senseworld;
	private PropertyController controller;
	private Map<ITemplate, Integer> identifiers = new HashMap<>();
	private int actorCount = 1;

	public InspirePropertyIdentifierThought(PropertyController property, World senseworld) {
		this.controller = property;
		this.senseworld = senseworld;
	}

	@Override
	public IThoughtType getThoughtType() {
		return ThoughtType.REFINE_BELIEFS;
	}

	@Override
	public boolean isLightweight() {
		return true;
	}

	@Override
	public Interest shouldBecomeMemory(ICanThink mind, int finishingTicks, long worldTicks) {
		return Interest.FORGET;
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
				ITemplate template = iue.getSpecies();
				if (this.controller.getProperty() == BasicProperties.FOOD && template == Food.FOOD_TYPE)
					System.out.print("");
				boolean senses = true;
				for (ISensor sensor : template.getPreferredSensesForHint(controller.getProperty())) {
					if (!memory.getSenses().contains(sensor)) {
						senses = false;
						break;
					}
				}
				senses = senses && !template.getPropertyHint(controller.getProperty()).isUnknown();
				if (senses) {
					int count = this.identifiers.getOrDefault(template, 0);
					this.identifiers.put(template, ++count);
					i -= 2;
					if (count == actorCount) {
						TemplateBasedIdentifier identifier = new TemplateBasedIdentifier(template,
								(a) -> template.getPropertyHint(controller.getProperty()), 1f);
						controller.editIdentifier(identifier);
					}
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
	public String displayText() {
		return "inspiring property " + this.controller.getProperty();
	}

	@Override
	public boolean equivalent(IThought other) {
		return this.equals(other);
	}

}
