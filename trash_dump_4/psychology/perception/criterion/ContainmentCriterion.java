package psychology.perception.criterion;

import java.util.Collection;

import psychology.perception.info.KDataType;

public class ContainmentCriterion extends Criterion {

	private Collection<?> container;

	public ContainmentCriterion(KDataType<?> dataType, Collection<?> container) {
		super(Equivalence.CONTAINS, dataType);
		this.container = container;
	}

	public Collection<?> getContainer() {
		return container;
	}

	@Override
	public boolean fits(Object obj) {
		return container.contains(obj);
	}

}
