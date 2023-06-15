package mind.need;

import mind.goals.IGoal;
import mind.relationships.Role;

public class AccessibilityNeed extends AbstractNeed {

	private Role role;

	public AccessibilityNeed(Degree degree, Role role) {
		super(NeedType.ACCESSIBILITY, degree);
		this.role = role;
	}

	public Role getRole() {
		return role;
	}

	@Override
	public IGoal genIndividualGoal() {
		// TODO accessibiltiy goals
		return null;
	}

	@Override
	public IGoal genSocietalGoal() {
		return null;
	}

}
