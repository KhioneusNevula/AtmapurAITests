package mind.action.types;

import java.util.Collection;
import java.util.Map;

import com.google.common.collect.ImmutableMap;

import mind.IEntity;
import mind.action.IInteraction;
import mind.action.IInteractionInstance;
import mind.action.IInteractionType;
import mind.speech.IUtterance;
import sim.Location;

public class AbstractInteractionInstance implements IInteractionInstance {

	private IInteractionType type;
	private Location location;
	private IInteraction initiatingAction;
	private IEntity initiator;
	private Map<IEntity, IInteraction> participants = Map.of();

	public AbstractInteractionInstance(IInteractionType type, Location location, IEntity initiator,
			IInteraction initatorAction) {
		this.type = type;
		this.location = location;
		this.initiator = initiator;
		this.initiatingAction = initatorAction;
	}

	@Override
	public IInteractionType getType() {
		return type;
	}

	@Override
	public Location getLocation() {
		return location;
	}

	@Override
	public IInteraction initiatingAction() {
		return this.initiatingAction;
	}

	@Override
	public Collection<IInteraction> actions() {
		return participants.values();
	}

	@Override
	public IEntity initiator() {
		return initiator;
	}

	@Override
	public Collection<IEntity> participants() {
		return participants.keySet();
	}

	@Override
	public void addParticipant(IEntity participant, IInteraction action) {
		this.participants = ImmutableMap.<IEntity, IInteraction>builder().putAll(participants).put(participant, action)
				.build();
	}

	@Override
	public void addCommunication(IEntity source, IUtterance utterance) {
		for (Map.Entry<IEntity, IInteraction> entry : this.participants.entrySet()) {
			if (entry.getKey() == source)
				continue;
			entry.getValue().receiveCommunication(entry.getKey(), source, utterance);
		}
	}

	@Override
	public void leave(IEntity ender) {
		this.participants.remove(ender);
	}

	@Override
	public void end(IEntity ender) {
		for (Map.Entry<IEntity, IInteraction> entry : this.participants.entrySet()) {
			entry.getValue().endInteraction(ender);
		}
	}

	@Override
	public IInteraction getAssociatedActionForParticipant(IEntity participant) {
		return participants.get(participant);
	}

}
