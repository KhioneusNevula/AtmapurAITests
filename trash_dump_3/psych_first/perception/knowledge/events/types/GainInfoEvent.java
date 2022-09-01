package psych_first.perception.knowledge.events.types;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import main.ImmutableCollection;
import psych_first.perception.knowledge.events.AbstractEvent;
import psych_first.perception.knowledge.events.EventType;
import psych_first.perception.knowledge.events.RelationType;
import psych_first.perception.senses.Sense;
import sim.IHasProfile;
import sim.World;

public class GainInfoEvent extends AbstractEvent {

	private IHasProfile gainer;
	private IHasProfile source;
	private Set<IHasProfile> info = new HashSet<>();

	/**
	 * 
	 * @param eventType
	 * @param inWorld
	 * @param startTime
	 * @param gainer
	 * @param source
	 * @param info
	 * @param given     if the source gave this info; otherwise, assume the info was
	 *                  read/heard/etc
	 * @param sense     what senses the info was gained through
	 */
	public GainInfoEvent(EventType<?> eventType, World inWorld, long startTime, IHasProfile gainer, IHasProfile source,
			IHasProfile info, boolean given, Sense... senses) {
		super(eventType, inWorld, startTime);
		this.gainer = gainer;
		this.source = source;
		this.addKnowledge(info);
		if (given) {
			this.addRelationshipToEvent(RelationType.BECAUSE_OF, gainer);
		} else {
			this.addRelationshipToEvent(RelationType.BECAUSE_OF, source);
		}
		for (Sense sense : senses)
			this.addRelationship(gainer, RelationType.USING, sense);
	}

	public IHasProfile getGainer() {
		return gainer;
	}

	public IHasProfile getSource() {
		return source;
	}

	public Collection<IHasProfile> getInfo() {
		return new ImmutableCollection<>(info);
	}

	public GainInfoEvent addKnowledge(IHasProfile info) {
		this.info.add(info);
		this.addRelationship(info, RelationType.FROM, source);
		this.addRelationship(info, RelationType.TO, gainer);
		this.addRelationshipToEvent(RelationType.ABOUT, info);
		return this;
	}

}
