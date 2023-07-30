package mind.resource;

import mind.concepts.type.Property;
import mind.goals.IGoal;
import mind.goals.IResource;

public class FungibleResource implements IResource {

	private Property item;
	private Integer count;
	private Priority priority = Priority.NORMAL;
	private boolean approximate;

	/**
	 * count is an optional value
	 * 
	 * @param item
	 * @param count
	 * @param approximate if the count is an approximation
	 */
	public FungibleResource(Property item, Integer count, boolean approximate) {
		this.item = item;
		this.count = count;
		this.approximate = approximate;
	}

	public Priority getPriority() {
		return priority;
	}

	public FungibleResource setPriority(Priority priority) {
		this.priority = priority;
		return this;
	}

	@Override
	public IResourceType getType() {
		return ResourceType.FUNGIBLE;
	}

	@Override
	public Integer count() {
		return count;
	}

	@Override
	public boolean isApproximate() {
		return approximate;
	}

	@Override
	public Property getConcept() {
		return this.item;
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
		return "resource_fungible_" + item.getUniqueName() + "_" + count;
	}

	@Override
	public boolean equivalent(IGoal other) {
		return IResource.super.equivalent(other) && other instanceof FungibleResource
				&& ((FungibleResource) other).item.equals(this.item);
	}

}
