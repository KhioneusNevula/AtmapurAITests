package mind.action.types;

import java.util.Collection;
import java.util.Map;

import com.google.common.collect.ImmutableMap;

import mind.ICanAct;
import mind.action.IInteraction;
import mind.action.IInteractionInstance;
import mind.action.IInteractionType;
import mind.speech.IUtterance;
import sim.Location;

public class AbstractInteractionInstance implements IInteractionInstance {

	private IInteractionType type;
	private Location location;
	private IInteraction initiatingAction;
	private ICanAct initiator;
	private Map<ICanAct, IInteraction> participants = Map.of();

	public AbstractInteractionInstance(IInteractionType type, Location location, ICanAct initiator,
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
	public ICanAct initiator() {
		return initiator;
	}

	@Override
	public Collection<ICanAct> participants() {
		return participants.keySet();
	}

	@Override
	public void addParticipant(ICanAct participant, IInteraction action) {
		this.participants = ImmutableMap.<ICanAct, IInteraction>builder().putAll(participants).put(participant, action)
				.build();
	}

	@Override
	public void addCommunication(ICanAct source, IUtterance utterance) {
		for (Map.Entry<ICanAct, IInteraction> entry : this.participants.entrySet()) {
			if (entry.getKey() == source)
				continue;
			entry.getValue().receiveCommunication(entry.getKey(), source, utterance);
		}
	}

	@Override
	public void leave(ICanAct ender) {
		this.participants.remove(ender);
	}

	@Override
	public void end(ICanAct ender) {
		for (Map.Entry<ICanAct, IInteraction> entry : this.participants.entrySet()) {
			entry.getValue().endInteraction(ender);
		}
	}

	@Override
	public IInteraction getAssociatedActionForParticipant(ICanAct participant) {
		return participants.get(participant);
	}

}
