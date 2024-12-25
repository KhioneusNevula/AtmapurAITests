package civilization_and_minds.social.concepts._misc;

import civilization_and_minds.social.concepts.IConcept;

public abstract class NumberConcept<E extends Number> implements IConcept {

	private E value;

	private NumberConcept(E value) {
		this.value = value;
	}

	public E getValue() {
		return value;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof NumberConcept nc) {
			return this.value.equals(nc.value);
		}
		return super.equals(obj);
	}

	@Override
	public ConceptType getConceptType() {
		return ConceptType.NUMBER;
	}

	@Override
	public int hashCode() {
		return 1 + this.value.hashCode();
	}

	public static IntConcept create(int value) {
		return new IntConcept(value);
	}

	public static DoubleConcept create(double value) {
		return new DoubleConcept(value);
	}

	public static FloatConcept create(float value) {
		return new FloatConcept(value);
	}

	public static LongConcept create(long value) {
		return new LongConcept(value);
	}

	public static ByteConcept create(byte value) {
		return new ByteConcept(value);
	}

	public static class IntConcept extends NumberConcept<Integer> {
		private IntConcept(int value) {
			super(value);
		}

		public int getIntValue() {
			return super.getValue().intValue();
		}

		@Override
		public String getUniqueName() {
			return "integer" + super.getValue();
		}
	}

	public static class DoubleConcept extends NumberConcept<Double> {
		private DoubleConcept(double value) {
			super(value);
		}

		public double getDoubleValue() {
			return super.getValue().doubleValue();
		}

		@Override
		public String getUniqueName() {
			return "double" + super.getValue();
		}
	}

	public static class FloatConcept extends NumberConcept<Float> {
		private FloatConcept(float value) {
			super(value);
		}

		public float getFloatValue() {
			return super.getValue().floatValue();
		}

		@Override
		public String getUniqueName() {
			return "float" + super.getValue();
		}
	}

	public static class LongConcept extends NumberConcept<Long> {
		private LongConcept(long value) {
			super(value);
		}

		public long getLongValue() {
			return super.getValue().longValue();
		}

		@Override
		public String getUniqueName() {
			return "long" + super.getValue();
		}
	}

	public static class ByteConcept extends NumberConcept<Byte> {
		private ByteConcept(byte value) {
			super(value);
		}

		public byte getByteValue() {
			return super.getValue().byteValue();
		}

		@Override
		public String getUniqueName() {
			return "byte" + super.getValue();
		}
	}

}
