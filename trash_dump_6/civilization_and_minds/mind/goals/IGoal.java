package civilization_and_minds.mind.goals;

import java.util.Collection;

import civilization_and_minds.IDirective;
import civilization_and_minds.social.concepts.IConcept;
import civilization_and_minds.social.concepts.profile.Profile;

/**
 * A condition to be satisfied, focused on by an intelligent agent.
 * 
 * @author borah
 *
 */
public interface IGoal extends IConcept {

	public static enum IntentionType implements IConcept {
		/** if this goal involves the patient consuming the theme */
		CONSUME,
		/** if this goal involves the patient acquiring the theme */
		ACQUIRE,
		/**
		 * if this goal involves transferring the theme from the patient to the
		 * beneficiary.
		 */
		TRANSFER,
		/** if this goal involves the patient traveling in some fashion */
		TRAVEL,
		/**
		 * if this goal involves fixing the theme
		 */
		FIX,
		/** if this goal involves increasing the patient's well-being */
		HEALTH,
		/**
		 * if this goal involves satiating the patient's hunger, thirst, or something
		 * similar, possibly with the theme
		 */
		SATIATION,
		/** if this goal involves protecting the patient */
		PROTECTION,
		/** if this goal involves empowering the patient */
		POWER_UP,
		/** if this goal involves resurrecting the patient */
		RESURRECT,
		/**
		 * if this goal involves hurting (or killing) the patient
		 */
		DAMAGE,
		/**
		 * if this goal involves making the patient have a certain feeling
		 */
		FEELING,
		/** if this goal involves the patient doing a specific action */
		SPECIFIC_ACTION, OTHER;

		@Override
		public ConceptType getConceptType() {
			return ConceptType.GOAL_INTENTION;
		}

		@Override
		public String getUniqueName() {
			return "intentiontype_" + this.name().toLowerCase();
		}
	}

	/**
	 * Return what kind of results this goal intents
	 * 
	 * @return
	 */
	public Collection<IntentionType> getGoalIntentionTypes();

	/**
	 * For very specific goals, the beneficiary is an individual, group, etc who
	 * receives an action, i.e. the receiver of a give action
	 * 
	 * @return
	 */
	public Profile getBeneficiary();

	/**
	 * Return the patient(s), i.e. the individual or group or other category that
	 * the goal is meant to affect. Typically the self.
	 * 
	 * @return
	 */
	public Collection<Profile> getPatient();

	/**
	 * If this goal has one or more themes, i.e. an object(s) which is manipulated
	 * or changed by the goal, return it
	 * 
	 * @return
	 */
	public Collection<? extends IConcept> getTheme();

	/**
	 * Make a directive which checks if this goal is complete
	 * 
	 * @return
	 */
	public IDirective<?> makeGoalChecker();

	/**
	 * How pressing this goal is
	 * 
	 * @return
	 */
	public Necessity getNecessity();

}
