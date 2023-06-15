package mind.resource;

import mind.concepts.type.Property;
import mind.goals.IGoal;
import mind.goals.IResource;

public class FungibleResource implements IResource {

	private Property item;
	private int count;

	public FungibleResource(Property item, int count) {
		this.item = item;
		this.count = count;
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
