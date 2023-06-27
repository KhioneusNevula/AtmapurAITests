package mind.action.types;

import java.util.Collection;

import mind.ICanAct;
import mind.action.IActionType;
import mind.action.IInteraction;
import mind.action.IInteractionInstance;
import mind.action.IInteractionType;
import mind.concepts.type.Profile;
import mind.goals.ITaskGoal;
import mind.speech.IUtterance;
import mind.speech.QuestionUtterance;

public class TalkAction implements IInteraction {

	private IUtterance utterance;
	private Profile target;

	public TalkAction(ITaskGoal goal) {
		if (goal.communicationInfo() != null) {
			utterance = goal.communicationInfo();
		} else if (goal.learnTarget() != null) {
			utterance = new QuestionUtterance(goal.learnTarget());
		} else {

		}
		this.target = goal.socialTarget();

	}

	@Override
	public boolean canExecuteIndividual(ICanAct user, boolean pondering) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void beginExecutingIndividual(ICanAct forUser) {
		// TODO Auto-generated method stub

	}

	@Override
	public Collection<ITaskGoal> genConditionGoal(ICanAct user) {
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
	public void receiveCommunication(ICanAct listener, ICanAct speaker, IUtterance commu) {
		// TODO Auto-generated method stub

	}

	@Override
	public void endInteraction(ICanAct ender) {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean isInteracting() {
		// TODO Auto-generated method stub
		return false;
	}
}
