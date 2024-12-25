package biology.sensing;

import java.util.Collection;
import java.util.Map;
import java.util.stream.Stream;

import actor.Actor;
import actor.IUniqueEntity;
import actor.construction.physical.IComponentPart;
import actor.construction.physical.IMaterialLayer;
import actor.construction.physical.IPartAbility;
import actor.construction.physical.IVisage;
import phenomenon.IPhenomenon;

/**
 * A method of sensing which obtains world info, as employed by an individual
 * entity.
 * 
 * @author borah
 *
 */
public interface ISense extends IPartAbility {

	/**
	 * Get all visible world phenomena (i.e. celestial bodies and such things) in
	 * order of salience (i.e. uniqueness)
	 * 
	 * @param sensingActor
	 * @param using
	 * @return
	 */
	public Stream<IPhenomenon> getWorldPhenomena(Actor sensingActor, IComponentPart usingPart);

	/**
	 * Get all visible phenomenoa that have positions in the world, in order of
	 * salience (i.e. closeness)
	 * 
	 * @param sensingActor
	 * @param usingPart
	 * @return
	 */
	public Stream<IPhenomenon> getPhysicalPhenomena(Actor sensingActor, IComponentPart usingPart);

	/**
	 * Get all visible actors this can sense, in order of salience (i.e. closeness)
	 * 
	 * @param sensingActor
	 * @param usingPart
	 * @return
	 */
	public Stream<Actor> getActorVisages(Actor sensingActor, IComponentPart usingPart);

	/**
	 * Gets all visible entities, phenomena, etc this can sense using the given
	 * part. Return in order of salience, which is usually just closeness of
	 * distance
	 * 
	 * @param sensingActor
	 * @param usingPart
	 * @return
	 */
	public Stream<IUniqueEntity> getSensedVisages(Actor sensingActor, IComponentPart usingPart);

	/**
	 * Returns all parts AND material layers that are sensed from a given *visage*,
	 * when sensing actors. Note that sensable properties are extracted based on
	 * what senses *they* express to. A visage is passed in, not an actor, since
	 * 
	 * @param sensingActor the being using senses
	 * @param usingPart    the body part that is sensing
	 * @param forEntity    the entity being sensed
	 * @return
	 */
	public Map<? extends IComponentPart, ? extends Collection<? extends IMaterialLayer>> getSensableParts(
			Actor sensingActor, IComponentPart usingPart, IVisage forEntity);

}
