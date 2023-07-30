package mind.resource;

import mind.concepts.type.Profile;
import mind.goals.IGoal;
import mind.goals.IResource;

public class ItemResource implements IResource {

	private Profile item;

	private Priority priority = Priority.NORMAL;

	public ItemResource(Profile item) {
		this.item = item;
	}

	@Override
	public IResourceType getType() {
		return ResourceType.ITEM;
	}

	public Priority getPriority() {
		return priority;
	}

	public ItemResource setPriority(Priority priority) {
		this.priority = priority;
		return this;
	}

	@Override
	public Integer count() {
		return 1;
	}

	@Override
	public boolean isApproximate() {
		return false;
	}

	@Override
	public Profile getConcept() {
		return item;
	}

	@Override
	public boolean individualGoal() {
		return true;
	}

	@Override
	public boolean societalGoal() {
		return true;
	}

	@Override
	public String getUniqueName() {
		return "resource_item_" + this.item.getUniqueName();
	}

	@Override
	public boolean equivalent(IGoal other) {
		return IResource.super.equivalent(other) && other instanceof ItemResource
				&& ((ItemResource) other).item.equals(this.item);
	}

}
