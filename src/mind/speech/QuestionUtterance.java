package mind.speech;

import mind.goals.question.Question;
import mind.linguistics.Language;

/**
 * A question expressed in a linguistic fashion
 * 
 * @author borah
 *
 */
public class QuestionUtterance extends Utterance {

	public QuestionUtterance(Question goal) {
		super(goal);
	}

	public QuestionUtterance(Language language, Question goal) {
		super(language, goal);
	}

	public Question getQuestion() {
		return mostImportantInfo();
	}

	@Override
	public Question mostImportantInfo() {
		return (Question) super.mostImportantInfo();
	}

}
