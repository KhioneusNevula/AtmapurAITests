package phenomenon;

import java.util.Collection;
import java.util.Map;

import com.google.common.collect.Multimap;

import actor.Actor;
import actor.IUniqueExistence;
import biology.systems.types.ISensor;
import mind.concepts.relations.IConceptRelationType;
import mind.concepts.type.IMeme;
import sim.World;
import sim.interfaces.IRenderable;

public interface IPhenomenon extends IUniqueExistence, IRenderable {

	/**
	 * Gets the first cause(s) (if any) of this phenomenon. E.g. a fire phenomenon
	 * may have been lit by a specific sentient entity. This is for when phenomena
	 * cause each other in a way that obscures the initial cause, so this tracks the
	 * initial cause. The map is to indicate whether the causal connection is clear
	 * to the given senses
	 * 
	 * @return
	 */
	public Multimap<IUniqueExistence, ISensor> cause();

	/**
	 * Gets the direct cause/source(s) (if any) of this phenomenon. This does not
	 * include a sentient entity source. E.g. a Burn Up event may have a Source
	 * which is an Explosion or a Flame
	 * 
	 * @return
	 */
	public Collection<IUniqueExistence> source();

	/**
	 * Gets the undergoer(s) (if any) of this phenomenon, if it is a phenomenon
	 * which is based on an actor
	 * 
	 * @return
	 */
	public Actor object();

	/**
	 * Gets the result of this phenomenon if it is
	 * transformative/destructive/creative
	 * 
	 * @return
	 */
	public Collection<IUniqueExistence> products();

	/**
	 * Gets the type of phenomenon this is
	 * 
	 * @return
	 */
	public IPhenomenonType type();

	/**
	 * Whether the phenomenon is complete and thereby ready to be removed
	 * 
	 * @return
	 */
	public boolean isComplete();

	/**
	 * Gets the directional and bidirectional relations that this phenomenon
	 * suggests between each of its causes and its object. If a relation exists, the
	 * collection may be empty to signify it has no arguments
	 * 
	 * @return
	 */
	default Map<IUniqueExistence, Map<IConceptRelationType, Collection<IMeme>>> causeToObjectRelations() {
		return Map.of();
	}

	/**
	 * Gets the directional relations that this phenomenon suggests between its
	 * object and each cause. If a relation exists, the collection may be empty to
	 * signify it has no arguments
	 * 
	 * @return
	 */
	default Map<IUniqueExistence, Map<IConceptRelationType, Collection<IMeme>>> objectToCauseRelations() {
		return Map.of();
	}

	/**
	 * What to do each tick
	 */
	public void tick();

	public World getWorld();

	public boolean isRelational();
}
