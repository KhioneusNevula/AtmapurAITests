package mind.memory.events;

import java.util.Collection;
import java.util.Set;

import mind.action.IActionType;
import mind.action.IInteractionInstance;
import mind.concepts.type.IMeme;
import mind.concepts.type.Profile;
import phenomenon.IPhenomenonType;
import sim.Location;

public class ActionEvent extends AbstractEvent {

	private IActionType<?> actionType;

	private ActionEvent(IActionType<?> actionType, Profile doer, Location at, long time) {
		super("event_" + actionType.getName() + "_" + time, at, time);
		this.actionType = actionType;
		this.add(EventRole.CAUSER, Set.of(doer));
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
	public Collection<IMeme> phenomenonProperties() {
		return Set.of();
	}

	@Override
	public boolean isAction() {
		return true;
	}

	/**
	 * what is used to do the action
	 * 
	 * @return
	 */
	public Collection<IMeme> used() {
		return this.getForRole(EventRole.USED);
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
			event.add(EventRole.OBJECT, Set.of(obj));
			return this;
		}

		public Builder addUsed(IMeme... used) {
			event.add(EventRole.USED, Set.of(used));
			return this;
		}

	}

}
