package psych_first.perception.knowledge.events.types;

import psych_first.perception.knowledge.events.AbstractEvent;
import psych_first.perception.knowledge.events.EventType;
import psych_first.perception.knowledge.events.RelationType;
import sim.IHasProfile;
import sim.World;

public class AnimateEvent extends AbstractEvent {

	private IHasProfile animator;
	private IHasProfile animated;
	private IHasProfile animationResult;

	/**
	 * the animation of an object, e.g. a corpse
	 * 
	 * @param eventType
	 * @param inWorld
	 * @param startTime
	 * @param animated
	 * @param animator  can be null?
	 */
	public AnimateEvent(World inWorld, long startTime, IHasProfile animated, IHasProfile animator,
			IHasProfile animationResult) {
		super(EventType.ANIMATE, inWorld, startTime);
		this.animated = animated;
		this.animator = animator;

		this.addRelationshipToEvent(RelationType.OCCURRING_TO, animated);
		if (animated != animationResult) {

			this.addRelationshipToEvent(animationResult, RelationType.BECAUSE_OF);
			this.addRelationship(animationResult, RelationType.FROM, animated);
			this.addRelationship(animated, RelationType.TO, animationResult);
		}
		if (this.animator != null) {
			this.addAnimator(animator);
		}
	}

	public IHasProfile getAnimated() {
		return animated;
	}

	public IHasProfile getAnimator() {
		return animator;
	}

	public IHasProfile getAnimationResult() {
		return animationResult;
	}

	public AnimateEvent addAnimator(IHasProfile animator) {
		this.addRelationshipToEvent(RelationType.BECAUSE_OF, animator);
		return this;
	}

	public AnimateEvent addTool(IHasProfile tool, IHasProfile... usedBy) {

		for (IHasProfile user : usedBy) {
			this.addRelationship(user, RelationType.USING, tool);
		}

		return this;
	}

}
