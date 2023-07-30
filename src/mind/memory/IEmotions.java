package mind.memory;

import mind.concepts.type.IMeme;
import mind.feeling.IFeeling;
import mind.feeling.IFeeling.Axis;

public interface IEmotions extends IRecordable {

	/**
	 * -1 if the cause is not present as a feeling
	 * 
	 * @param forF
	 * @return
	 */
	int getTime(IMeme forF);

	void changeTimeBy(IMeme forF, int time);

	void setTime(IMeme forF, int time);

	/**
	 * Causes this feeling to be associated with this cause, replacing the previous
	 * feeling there; returns the feeling
	 * 
	 * @param feeling
	 * @param cause
	 */
	IFeeling changeFeeling(IFeeling feeling, IMeme cause);

	void add(IFeeling feeling, int time, IMeme cause);

	/**
	 * Removes a feeling and returns its cause
	 * 
	 * @param feeling
	 * @return
	 */
	IMeme remove(IMeme feeling);

	/**
	 * the factor of emotional happiness of this emotion (e.g. happiness has 1.0f
	 * enjoyment factor). Enjoyment increases the desirability of whatever produces
	 * this emotion.
	 */
	public float enjoyment();

	/**
	 * the factor of unhappiness of this emotion (e.g. sadness has 1.0f
	 * unhappiness). Unhappiness decreases the desirability of whatever produces
	 * this emotion. It is equivalent to -enjoyment.
	 * 
	 * @return
	 */
	public float unhappiness();

	/**
	 * the factor of aggression of this emotion (e.g. anger has 1.0f aggression)
	 * 
	 * @return
	 */
	public float aggression();

	/**
	 * the factor of stress of this emotion e.g. how much trauma it causes
	 * 
	 * @return
	 */
	public float stress();

	/**
	 * The factor of excitement of this emotion, e.g. the factor of mental arousal.
	 * Adds extra willingness and speed and whatever
	 * 
	 * @return
	 */
	public float excitement();

	/**
	 * The factor of unwilligness of this emotion, e.g. the factor of mental
	 * unwillingness/laziness. It is equivalent to -excitement
	 * 
	 * @return
	 */
	public float unwillingness();

	/**
	 * The factor of confusion of this emotion
	 * 
	 * @return
	 */
	public float confusion();

	/**
	 * The factor of physical pleasure of this emotion. Attraction + pleasure is
	 * essentially equivalent to lust(?)
	 * 
	 * @return
	 */
	public float pleasure();

	/**
	 * the factor of physical discomfort of this emotion
	 * 
	 * @return
	 */
	public float discomfort();

	/**
	 * The factor of physical pain of this emotion
	 * 
	 * @return
	 */
	public float pain();

	/**
	 * We can just keep this for now to represent, idk, supernatural feeligns or
	 * some thing
	 * 
	 * @return
	 */
	public float weird();

	public float get(Axis axis);

}
