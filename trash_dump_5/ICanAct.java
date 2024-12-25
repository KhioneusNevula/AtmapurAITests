package mind;

import mind.memory.IHasKnowledge;

public interface ICanAct extends IHasKnowledge {

	/**
	 * The part of the mind that takes actions
	 */
	IWill getWill();

}
