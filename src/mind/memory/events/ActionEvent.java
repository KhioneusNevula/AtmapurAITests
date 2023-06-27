package mind.memory.events;

import java.util.Set;

import com.google.common.collect.ImmutableSet;

import mind.action.IActionType;
import mind.action.IInteractionInstance;
import mind.concepts.type.IMeme;
import mind.concepts.type.Profile;
import phenomenon.IPhenomenonType;
import sim.Location;

public class ActionEvent extends AbstractEvent {

	private IActionType<?> actionType;
	private Set<IMeme> used = Set.of();

	private ActionEvent(IActionType<?> actionType, Profile doer, Location at, long time) {
		super("event_" + actionType.getName() + "_" + time, at, time);
		this.actionType = actionType;
		this.addCause(doer);
	}

	@Override
	public IPhenomenonType phenomenon() {
		return null;
	}

	@Override
	public boolean isPhenomenon() {
		return false;
	}

	@Override
	public boolean isAction() {
		return true;
	}

	@Override
	public IActionType<?> action() {
		return actionType;
	}

	@Override
	public IInteractionInstance interaction() {
		return null;
	}

	@Override
	public boolean isInteraction() {
		return false;
	}

	public static Builder builder(IActionType<?> type, Profile actorProf, Location loc, long time) {
		return new Builder(type, actorProf, loc, time);
	}

	public static class Builder {
		private ActionEvent event;

		private Builder(IActionType<?> type, Profile actorProf, Location loc, long time) {
			this.event = new ActionEvent(type, actorProf, loc, time);
		}

		public Builder addObject(Profile... obj) {
			event.addObject(obj);
			return this;
		}

		public Builder addUsed(IMeme... used) {
			event.used = ImmutableSet.<IMeme>builder().addAll(event.used).add(used).build();
			return this;
		}

	}

}
