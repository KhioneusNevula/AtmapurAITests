package biology.sensing;

import biology.sensing.senses.HearingSense;
import biology.sensing.senses.SightSense;
import biology.sensing.senses.SmellSense;
import biology.sensing.senses.TasteSense;

public final class BasicSenses {

	private BasicSenses() {
	}

	public static final SightSense SIGHT = new SightSense("sight");
	public static final HearingSense HEARING = new HearingSense("hearing");
	public static final SmellSense SMELL = new SmellSense("smell");
	public static final TasteSense TASTE = new TasteSense("taste");

}
