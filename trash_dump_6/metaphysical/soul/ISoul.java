package metaphysical.soul;

import metaphysical.ISpiritObject;
import metaphysical.soul.generator.ISoulGenerator;

public interface ISoul extends ISpiritObject {

	/**
	 * Get the entity which generated the soul of this object
	 * 
	 * @return
	 */
	ISoulGenerator getSoulGenerator();

	/**
	 * return info about this soul
	 * 
	 * @return
	 */
	String report();

}
