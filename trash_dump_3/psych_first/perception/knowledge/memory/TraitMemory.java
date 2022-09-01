package psych_first.perception.knowledge.memory;

import psych_first.mind.IMindPart;
import psych_first.mind.Mind;

public class TraitMemory implements IMindPart {

	private Mind mind;

	public TraitMemory(Mind mind) {
		this.mind = mind;
	}

	@Override
	public Mind getMind() {
		return mind;
	}

	@Override
	public void update(int ticks) {
		// TODO Auto-generated method stub

	}

	@Override
	public String report() {
		// TODO Auto-generated method stub
		return null;
	}

}
