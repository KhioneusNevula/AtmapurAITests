package psychology.emotion;

import psychology.ISoulPart;
import psychology.Soul;

public class EmotionHandler implements ISoulPart {

	private Soul soul;

	public EmotionHandler(Soul soul) {
		this.soul = soul;
	}

	@Override
	public Soul getSoul() {
		return soul;
	}

	@Override
	public String report() {
		// TODO EmotionHandler report()
		return null;
	}

}
