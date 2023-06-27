package mind.goals;

import mind.concepts.type.IMeme;
import mind.memory.IHasKnowledge;

/**
 * A type of physical (i.e., either physically creatable, destroyable, and
 * possibly movable, but otherwise independently existing) or abstract (i.e.,
 * managed through jobs performed or other sources, rather than physical flows)
 * thing that can be given and received; the goal is, formally, the obtainment
 * or transfer of this resource as a constant thing over time
 * 
 * @author borah
 *
 */
public interface IResource extends IGoal {

	public static interface IResourceType {

		/**
		 * if this resource type is (usually) managed through human allocation<br>
		 * > specifics: if this resource needs humans to actively perform it as, like, a
		 * job, rather than being a standalone thing
		 * 
		 * @return
		 */
		public boolean isJob();

		/**
		 * if the resource type (usually) has some form which can be physically
		 * traded<br>
		 * > specifics: if this resource type can be 'given', i.e. humans don't need to
		 * be assigned to 'perform' it as a task
		 * 
		 * @return
		 */
		public boolean isPhysical();

		/**
		 * If this resource can (usually) move from place to place<br>
		 * > specifics: if this resource can be picked up and dropped off to other
		 * places, as opposed to needing to be relinquished or simply being too abstract
		 * to be 'moved'
		 * 
		 * @return
		 */
		public boolean isMovable();
	}

	public static enum ResourceType implements IResourceType {
		/** transferrable physical resource needs, from food to building material */
		FUNGIBLE,
		/**
		 * non-fungible transferrable physical resource needs
		 */
		ITEM,
		/** physical buildings and structures to protect from the environment */
		SHELTER(false, true, false),
		/** physical space for things */
		LAND(false, true, false),
		/**
		 * managed through sharing knowledge, discovering knowledge, or receptacles of
		 * knowledge as a resource
		 */
		KNOWLEDGE(true, true, true),
		/**
		 * a resource flow of those who do some specific type of work, such as protect
		 */
		WORK(true, false, true),
		/** influence over others or other groups, as a resource */
		INFLUENCE(true, false, false)

		;

		private boolean isJob;
		private boolean isPhysical;
		private boolean isMovable;

		private ResourceType() {
			this(false, true, true);
		}

		private ResourceType(boolean abstr, boolean phys, boolean mov) {
			this.isJob = abstr;
			this.isMovable = mov;
			this.isPhysical = phys;
		}

		public boolean isJob() {
			return isJob;
		}

		public boolean isPhysical() {
			return isPhysical;
		}

		public boolean isMovable() {
			return isMovable;
		}

	}

	/**
	 * the type of resource
	 * 
	 * @return
	 */
	public IResourceType getType();

	/**
	 * Whether this resource is a form of human resource and requires humans to
	 * perform it as a job
	 * 
	 * @return
	 */
	public default boolean isJob() {
		return getType().isJob();
	}

	/**
	 * Whether this resource can be bought and sold, as opposed to one that can only
	 * be worked as a job (i.e. safety is a result of human action)
	 * 
	 * @return
	 */
	public default boolean canBeTraded() {
		return getType().isPhysical();
	}

	/**
	 * whether this resource can be moved around when bought and sold
	 * 
	 * @return
	 */
	public default boolean canBeMoved() {
		return getType().isMovable();
	}

	/**
	 * for fungible items, this is the amount of resource that is contained; null
	 * for resources that are not tracked by count; 1 for resources that are
	 * singular
	 * 
	 * @return
	 */
	public Integer count();

	@Override
	default boolean isComplete(IHasKnowledge entity) {
		return false;
	}

	/**
	 * gets the category or profile of the resource
	 * 
	 * @return
	 */
	public IMeme getConcept();

	@Override
	public default IGoal.Type getGoalType() {
		return IGoal.Type.FLOW;
	}

}
