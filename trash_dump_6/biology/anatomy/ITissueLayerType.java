package biology.anatomy;

import actor.construction.physical.IMaterialLayerType;

/**
 * TODO materials, <br>
 * magic stuff ? idk
 * 
 * @author borah
 *
 */
public interface ITissueLayerType extends IMaterialLayerType {

	/**
	 * Whether this tissue can feel sensations
	 * 
	 * @return
	 */
	public boolean hasNerves();

	/**
	 * Whether this tissue can be used for motion
	 * 
	 * @return
	 */
	public boolean isMuscular();

	/**
	 * Whether this tissue has life essence/blood flowing in it
	 * 
	 * @return
	 */
	public boolean hasLifeEssence();

	/**
	 * Whether this tissue behaves as life essence
	 * 
	 * @return
	 */
	public boolean isLifeEssence();

	/* TODO public Material material(); */
}
