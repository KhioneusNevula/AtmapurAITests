package mind.thought_exp.type;

import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import actor.IUniqueExistence;
import mind.concepts.identifiers.IPropertyIdentifier;
import mind.concepts.type.Profile;
import mind.concepts.type.Property;
import mind.goals.IGoal.Priority;
import mind.memory.IPropertyData;
import mind.thought_exp.ICanThink;
import mind.thought_exp.IThought;
import mind.thought_exp.IThoughtMemory;
import mind.thought_exp.IThoughtMemory.Interest;
import mind.thought_exp.ThoughtType;
import mind.thought_exp.culture.UpgradedCulture;
import mind.thought_exp.memory.type.ApplyPropertyMemory;

public class ApplyPropertiesThought extends AbstractThought {

	private Collection<? extends IUniqueExistence> actors;
	private Collection<Property> properties;
	private Iterator<? extends IUniqueExistence> actorIterator;
	private Map<IThoughtMemory, Interest> memories;
	private boolean failed;
	private boolean complete;
	/**
	 * whether to apply the property within memory
	 */
	private boolean keepInMemory;

	/**
	 * whether to apply hte property to the actor's property-list
	 */
	private boolean applyToActor;

	public ApplyPropertiesThought(Collection<? extends IUniqueExistence> actors, Collection<Property> property,
			boolean keepInMemory, boolean applyToActor) {
		this.actors = actors;
		this.properties = property;
		this.applyToActor = applyToActor;
		if (this.keepInMemory = keepInMemory) {
			memories = new HashMap<>();
		}
	}

	/**
	 * If property is null or empty, apply all properties
	 * 
	 * @param actors
	 * @param property
	 */
	public ApplyPropertiesThought(Collection<? extends IUniqueExistence> actors, Collection<Property> property) {
		this(actors, property, false, true);
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
	public IThoughtMemory.Interest shouldProduceRecentThoughtMemory(ICanThink mind, int finishingTicks, long worldTicks) {
		return IThoughtMemory.Interest.FORGET;
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
				IPropertyIdentifier associations = memory.getKnowledgeBase().getPropertyIdentifier(property);
				if (associations.isUnknown() && memory.isMindMemory()) {
					for (UpgradedCulture culture : memory.getMindMemory().cultures()) {
						IPropertyIdentifier assoc2 = culture.getPropertyIdentifier(property);
						if (associations.isUnknown() && !assoc2.isUnknown()) {
							associations = assoc2;
							break;
						}
					}
				}

				if (associations.isUnknown())
					continue;
				IPropertyData data = associations.identifyInfo(property, actor, actor.getVisage(), memory);
				if (data.isPresent() && data.getKnownCount() > actor
						.getPropertyData(memory.getKnowledgeBase(), property, true).getKnownCount()) {
					if (applyToActor)
						actor.assignProperty(memory.getKnowledgeBase(), property, data);
					Profile aProf = new Profile(actor);
					if (keepInMemory) {
						memories.put(new ApplyPropertyMemory(aProf, property, data), Interest.FORGET);
					}
				}
				if (memory.isMindMemory() && applyToActor) {
					for (UpgradedCulture culture : memory.getMindMemory().cultures()) {
						IPropertyIdentifier id = culture.getPropertyIdentifier(property);
						IPropertyData datau = id.identifyInfo(property, actor, actor.getVisage(), memory);
						if (datau.isPresent() && datau.getKnownCount() > actor.getPropertyData(culture, property, true)
								.getKnownCount()) {
							actor.assignProperty(culture, property, data = datau);
						}
					}
				}

			}
		}
	}

	@Override
	public void getInfoFromChild(ICanThink mind, IThought childThought, boolean interrupted, int ticks) {

	}

	@Override
	public Map<IThoughtMemory, Interest> produceMemories(ICanThink mind, int finishingTicks, long worldTicks) {
		return memories == null ? Map.of() : memories;
	}

	@Override
	public String displayText() {
		return "applying properties";
	}

	@Override
	public boolean equivalent(IThought other) {
		if (other instanceof ApplyPropertiesThought apt) {
			return ((this.applyToActor == apt.applyToActor) && (this.keepInMemory == apt.keepInMemory))
					&& (this.actors.containsAll(apt.actors)) && (properties.containsAll(apt.properties));
		}
		return this.equals(other);
	}

}
