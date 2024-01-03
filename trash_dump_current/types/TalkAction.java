package mind.action.types;

import mind.IEntity;
import mind.action.IActionType;
import mind.action.IInteraction;
import mind.action.IInteractionInstance;
import mind.action.IInteractionType;
import mind.concepts.type.Profile;
import mind.goals.ITaskGoal;
import mind.memory.IHasKnowledge;
import mind.speech.IUtterance;
import mind.speech.QuestionUtterance;

public class TalkAction implements IInteraction {

	private IUtterance utterance;
	private Profile target;

	public TalkAction(ITaskGoal goal) {
		if (goal.communicationInfo() != null) {
			utterance = goal.communicationInfo();
		} else if (goal.learnInfo() != null) {
			utterance = new QuestionUtterance(goal.learnInfo());
		} else {

		}
		this.target = goal.socialTarget();

	}

	@Override
	public boolean canExecuteIndividual(IHasKnowledge user, boolean pondering) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void beginExecutingIndividual(IHasKnowledge forUser) {
		// TODO Auto-generated method stub

	}

	@Override
	public ITaskGoal genConditionGoal(IHasKnowledge user) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public IActionType<?> getType() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean isInitiator() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public IInteractionInstance getInteractionInstance() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public IInteractionType getInteractionType() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void receiveCommunication(IEntity listener, IEntity speaker, IUtterance commu) {
		// TODO Auto-generated method stub

	}

	@Override
	public void endInteraction(IEntity ender) {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean isInteracting() {
		// TODO Auto-generated method stub
		return false;
	}
}
