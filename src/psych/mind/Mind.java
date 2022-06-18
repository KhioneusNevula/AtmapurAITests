package psych.mind;

import java.util.EnumMap;
import java.util.HashSet;
import java.util.Set;
import java.util.TreeMap;
import java.util.stream.Collectors;

import entity.Actor;
import psych.action.Action;
import psych.action.goal.Goal;
import psych.action.goal.NeedGoal;
import sociology.Profile;

public class Mind {

	private Actor owner;
	private static int SIGHT = 200;
	private Set<Profile> rememberedProfiles = new HashSet<>();
	private TreeMap<Need, Integer> needs = new TreeMap<>();
	private Set<Action> possibleActions = new HashSet<>();
	private Set<Goal> goals = new HashSet<>();
	private EnumMap<Need, NeedGoal> needGoals = new EnumMap<>(Need.class);
	private int ticks = 0;
	private Will personalWill = new Will(this);

	public Mind(Actor owner, Need... needs) {
		this.owner = owner;
		for (Need n : needs) {
			this.needs.put(n, 0);
		}

	}

	public Will getPersonalWill() {
		return personalWill;
	}

	public void addNeedGoal(Need need, int level) {
		NeedGoal existing = needGoals.get(need); // TODO probably universalize this;
													// currently checks which goal has the
													// higher need level and keeps that one
		if (existing == null) {
			NeedGoal g = new NeedGoal(need, level);
			needGoals.put(need, g);
			goals.add(g);
			return;
		}
		existing.setLevel(Math.max(existing.getLevel(), level));
	}

	/**
	 * Adds this goal to the set of goals
	 * 
	 * @param goal
	 */
	public void chooseGoal(Goal goal) {
		if (goal instanceof NeedGoal) {
			if (needGoals.containsKey(((NeedGoal) goal).getFocus())) {

				addNeedGoal(((NeedGoal) goal).getFocus(), ((NeedGoal) goal).getLevel());
			} else {
				needGoals.put(((NeedGoal) goal).getFocus(), (NeedGoal) goal);
				this.goals.add(goal);
			}
		} else
			this.goals.add(goal);
		System.out.println("added new goal " + goal);
	}

	public void giveUp(Goal goal) {
		this.goals.remove(goal);
		if (this.needGoals.containsValue(goal)) {
			this.needGoals.remove(((NeedGoal) goal).getFocus());
		}
		System.out.println("removed goal " + goal);
	}

	public void observe() {
		boolean nO = false;
		boolean pO = false;

		// observe needs
		for (Need n : this.needs.keySet()) {
			Integer nV = n.getNeedValue(this);

			if (nV != null && !nV.equals(needs.get(n))) {
				nO = true;
				needs.put(n, nV);

			}
			if (needs.get(n) < 10 && needGoals.get(n) == null) { // TODO obviously figure this out and make it more
																	// specific
				this.addNeedGoal(n, 10);
			}
		}
		// observe profiles
		for (Actor a : this.owner.getWorld().getActors()) {
			if (a == this.owner)
				continue;
			if (a.distance(this.owner) <= SIGHT && (!rememberedProfiles.contains(a.getProfile())
					|| !possibleActions.containsAll(a.getProfile().getAllActions()))) {
				pO = true;
				rememberedProfiles.add(a.getProfile());
				possibleActions.addAll(a.getProfile().getAllActions());
			}
		}

		if (ticks % 100 == 0) {
			System.out.println(this.toString() + mindReport());
		}
		ticks++;

	}

	public void act() {

		// update goal state
		for (Goal goal : new HashSet<>(this.goals)) {
			goal.goalUpdate(this);
			if (goal.isComplete())
				this.giveUp(goal);
		}
	}

	public Actor getOwner() {
		return owner;
	}

	public Set<Action> getPossibleActions() {
		return possibleActions;
	}

	public boolean hasNeed(Need n) {
		return needs.get(n) != null;
	}

	public Integer getNeed(Need n) {
		return needs.get(n);
	}

	public String mindReport() {
		return "{observations=" + this.rememberedProfiles.stream().map((a) -> a.getName()).collect(Collectors.toSet())
				+ ",needs" + this.needs + ",\n\tpossible actions=" + this.possibleActions + ",\n\t\tgoals=" + this.goals
				+ "}";
	}

	@Override
	public String toString() {
		return "<" + this.owner.getName() + "'s mind>";
	}
}
