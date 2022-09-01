package psychology.perception.criterion;

import com.google.common.base.Objects;

import psychology.perception.info.KDataType;

public class EqualityCriterion extends Criterion {

	private Object value;

	public EqualityCriterion(KDataType<?> dataType, boolean equal, Object toValue) {
		super(equal ? Equivalence.EQUALS : Equivalence.NOT_EQUALS, dataType);
		this.value = toValue;
	}

	public Object getValue() {
		return value;
	}

	@Override
	public boolean fits(Object obj) {
		return Objects.equal(value, obj);
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof EqualityCriterion ec) {
			return super.equals(obj) && ec.value.equals(this.value);
		}
		return false;
	}

}
