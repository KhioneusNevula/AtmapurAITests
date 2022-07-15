package psych_first.mind;

import java.util.TreeMap;

import psych_first.action.goal.NeedGoal;

public class NeedManager implements IMindPart {

	private Mind mind;

	private TreeMap<Need, Integer> needs = new TreeMap<>();

	public NeedManager(Mind mind, Need... needs) {
		this.mind = mind;
		for (Need n : needs) {
			this.needs.put(n, 0);
		}
	}

	public NeedManager addNeeds(Need... nee) {
		for (Need n : nee) {
			this.needs.put(n, 0);

		}
		return this;
	}

	@Override
	public Mind getMind() {
		return mind;
	}

	@Override
	public void update(int ticks) {
		if (ticks % 5 != 0)
			return;
		for (Need n : this.needs.keySet()) {
			Integer nV = n.getNeedValue(this.mind);

			if (nV != null && !nV.equals(needs.get(n))) {
				needs.put(n, nV);
				// TODO consider needs

			}

			// TODO change this from an arbitrary value to a properly dynamic one
			if (needs.get(n) < 50) {
				if (!mind.getPersonalWill().hasTaskFor(n)) {
					mind.communicate(this, mind.getPersonalWill(), (w) -> {
						w.chooseGoal(new NeedGoal(n, 20), false);
					});
				}
			}

		}
	}

	public boolean hasNeed(Need n) {
		return needs.get(n) != null;
	}

	public Integer getNeed(Need n) {
		return needs.get(n);
	}

	@Override
	public String report() {
		return needs.toString();
	}

}
