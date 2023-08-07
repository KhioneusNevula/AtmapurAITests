package mind.thought_exp.type;

import java.util.Collection;
import java.util.Iterator;
import java.util.Map;

import actor.IUniqueExistence;
import humans.Food;
import mind.Culture;
import mind.concepts.PropertyController;
import mind.concepts.type.BasicProperties;
import mind.concepts.type.Profile;
import mind.concepts.type.Property;
import mind.goals.IGoal.Priority;
import mind.memory.IKnowledgeBase.Interest;
import mind.memory.IPropertyData;
import mind.thought_exp.ICanThink;
import mind.thought_exp.IThought;
import mind.thought_exp.ThoughtType;

public class ApplyPropertiesThought extends AbstractThought {

	private Collection<? extends IUniqueExistence> actors;
	private Collection<Property> properties;
	private Iterator<? extends IUniqueExistence> actorIterator;
	private boolean failed;
	private boolean complete;

	/**
	 * If property is null or empty, apply all properties
	 * 
	 * @param actors
	 * @param property
	 */
	public ApplyPropertiesThought(Collection<? extends IUniqueExistence> actors, Collection<Property> property) {
		this.actors = actors;
		this.properties = property;
	}

	@Override
	public IThoughtType getThoughtType() {
		return ThoughtType.EVALUATE_PROPERTY;
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
		this.actorIterator = this.actors.iterator();
	}

	@Override
	public boolean isFinished(ICanThink memory, int ticks, long worldTick) {
		return failed || complete;
	}

	@Override
	public Priority getPriority() {
		return Priority.TRIVIAL;
	}

	@Override
	public void thinkTick(ICanThink memory, int ticks, long worldTick) {
		for (int i = 0; i < memory.getMaxFocusObjects(); i++) {
			if (!actorIterator.hasNext()) {
				this.complete = true;
				break;
			}
			IUniqueExistence actor = actorIterator.next();
			for (Property property : this.properties) {
				PropertyController associations = memory.getKnowledgeBase().getPropertyAssociations(property);
				if (associations == null && memory.isMindMemory()) {
					for (Culture culture : memory.getMindMemory().cultures()) {
						PropertyController assoc2 = culture.getPropertyAssociations(property);
						if (associations == null && assoc2 != null) {
							associations = assoc2;
							break;
						}
					}
				}

				if (associations == null)
					throw new IllegalStateException(this + " unknown property:" + property + " " + this.actors + " "
							+ this.properties + " parent:" + this.parent);
				IPropertyData data = associations.getIdentifier().identifyInfo(property, actor, actor.getVisage(),
						memory);
				if (!data.isUnknown() && data.getKnownCount() > actor
						.getPropertyData(memory.getKnowledgeBase(), property).getKnownCount()) {
					actor.assignProperty(memory.getKnowledgeBase(), property, data);
					Profile aProf = memory.getKnowledgeBase().getProfileFor(actor);
					if (aProf != null) {
						memory.getKnowledgeBase().setProperty(aProf, property, data);
					}
				}
				if (memory.isMindMemory()) {
					Map<Culture, PropertyController> assoc = memory.getMindMemory()
							.getPropertyAssociationsFromCulture(property);
					for (Map.Entry<Culture, PropertyController> entry : assoc.entrySet()) {
						IPropertyData datau = entry.getValue().getIdentifier().identifyInfo(property, actor,
								actor.getVisage(), memory);
						if (!datau.isUnknown() && datau.getKnownCount() > actor
								.getPropertyData(entry.getKey(), property).getKnownCount()) {
							if (property == BasicProperties.FOOD && actor.getSpecies() == Food.FOOD_TYPE) {
								System.out.println(actor + " received food property");
							}
							actor.assignProperty(entry.getKey(), property, data = datau);
							Profile aProf = memory.getKnowledgeBase().getProfileFor(actor);
							if (aProf == null)
								aProf = entry.getKey().getProfileFor(actor);
							if (aProf != null) {
								memory.getKnowledgeBase().setProperty(aProf, property, data);
							}
						}
					}
				}

			}
		}
	}

	@Override
	public void getInfoFromChild(IThought childThought, boolean interrupted, int ticks) {

	}

	@Override
	public String displayText() {
		return "applying properties";
	}

	@Override
	public boolean equivalent(IThought other) {
		if (other instanceof ApplyPropertiesThought apt) {
			return (this.actors.containsAll(apt.actors)) && (properties.containsAll(apt.properties));
		}
		return this.equals(other);
	}

}
