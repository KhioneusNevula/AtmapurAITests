package mind.thought_exp.type;

import java.util.Collection;
import java.util.Collections;
import java.util.LinkedList;
import java.util.Set;
import java.util.UUID;

import com.google.common.collect.Multimap;
import com.google.common.collect.MultimapBuilder;

import mind.goals.IGoal;
import mind.thought_exp.ICanThink;
import mind.thought_exp.IThought;

public abstract class AbstractThought implements IThought {

	protected Multimap<IThoughtType, IThought> children;
	protected LinkedList<IThought> pendingChildren;
	private int lastChildThoughtPostedTick = -23;

	protected IThought parent;

	protected IGoal goal;
	protected UUID uuid;

	public AbstractThought() {
		this.setUUID(UUID.randomUUID());
	}

	@Override
	public UUID getUUID() {
		return uuid;
	}

	protected void setUUID(UUID uuid) {
		this.uuid = uuid;
	}

	@Override
	public Collection<IThought> childThoughts() {
		return children == null ? Set.of() : children.values();
	}

	protected Collection<IThought> childThoughts(IThoughtType forType) {
		return children == null ? Set.of() : children.get(forType);
	}

	@Override
	public void setParent(IThought parent) {
		this.parent = parent;
	}

	public IThought parentThought() {
		return parent;
	}

	@Override
	public boolean hasPendingChildThought() {
		return pendingChildren != null && !pendingChildren.isEmpty();
	}

	@Override
	public IThought peekNextPendingChildThought() {
		if (pendingChildren != null && !pendingChildren.isEmpty()) {
			return pendingChildren.get(0);
		}
		return null;
	}

	@Override
	public IThought popNextPendingChildThought() {
		if (pendingChildren != null && !pendingChildren.isEmpty()) {
			IThought thought = pendingChildren.remove(0);
			if (pendingChildren.isEmpty())
				pendingChildren = null;
			if (this.children == null) {
				children = MultimapBuilder.<IThoughtType>treeKeys((a, b) -> a.ordinalNumber() - b.ordinalNumber())
						.hashSetValues().build();
			}
			children.put(thought.getThoughtType(), thought);
			return thought;
		}
		return null;
	}

	@Override
	public boolean pauseThought(ICanThink memory, int ticks, long wTicks) {
		if (memory.rand().nextDouble() < memory.loseFocusChance()) {
			return true;
		}
		return false;
	}

	/**
	 * Only one child thought should be produced per tick; this is a soft condition
	 * however
	 * 
	 * @param thought
	 * @param tick
	 */
	protected void postChildThought(IThought thought, int tick) {
		if (tick == lastChildThoughtPostedTick) {
			// System.err.println(this + " posting child " + thought + " in same tick as
			// previous");
		}
		(this.pendingChildren == null ? pendingChildren = new LinkedList<>() : pendingChildren).add(thought);
		this.lastChildThoughtPostedTick = tick;
	}

	@Override
	public void endChildThought(IThought thought) {
		if (children != null) {
			children.values().removeAll(Collections.singleton(thought));
			if (children.isEmpty())
				children = null;
		}
	}

	protected void setGoal(IGoal goal) {
		this.goal = goal;
	}

	@Override
	public IGoal getGoal() {
		return goal;
	}

	@Override
	public Object getInformation() {
		return null;
	}

	@Override
	public boolean returnsInformation() {
		return false;
	}

	@Override
	public boolean equals(Object obj) {
		if (super.equals(obj))
			return true;
		if (obj instanceof IThought t) {
			if (t.getUUID().equals(this.uuid))
				return true;
		}
		return false;
	}

	@Override
	public int hashCode() {
		return this.getClass().hashCode();
	}

	@Override
	public String getUniqueName() {
		return "thought_" + this.getClass().getSimpleName().toLowerCase();
	}

}
