package mind.speech;

import mind.goals.IGoal;
import mind.linguistics.Language;

/**
 * A request to someone else to complete a goal
 * 
 * @author borah
 *
 */
public class RequestUtterance extends Utterance {

	public RequestUtterance(IGoal goal) {
		super(goal);
	}

	public RequestUtterance(Language language, IGoal goal) {
		super(language, goal);
	}

	public IGoal getGoal() {
		return mostImportantInfo();
	}

	@Override
	public IGoal mostImportantInfo() {
		return (IGoal) super.mostImportantInfo();
	}

}
