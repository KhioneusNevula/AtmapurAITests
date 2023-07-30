package mind.action;

import java.util.EnumMap;
import java.util.Map;

import main.Pair;

public class WillingnessMatrix {

	public static final WillingnessMatrix ONE = new WillingnessMatrix() {
		@Override
		public float getResult() {
			return 1f;
		}

		@Override
		public WillingnessMatrix addFactor(Factor factor, float weight, float value) {
			throw new UnsupportedOperationException();
		}

		@Override
		public WillingnessMatrix performCalculation() {
			return this;
		}

		@Override
		public String toString() {
			return "WM_ONE";
		}
	};

	public static final WillingnessMatrix ZERO = new WillingnessMatrix() {
		@Override
		public float getResult() {
			return 0f;
		}

		@Override
		public WillingnessMatrix addFactor(Factor factor, float weight, float value) {
			throw new UnsupportedOperationException();
		}

		@Override
		public WillingnessMatrix performCalculation() {
			return this;
		}

		@Override
		public String toString() {
			return "WM_ZERO";
		}
	};

	/**
	 * Weight, Factor
	 */
	private EnumMap<Factor, Pair<Float, Float>> factors = new EnumMap<>(Factor.class);
	private float result;

	public static WillingnessMatrix create() {
		WillingnessMatrix matrix = new WillingnessMatrix();
		return matrix;
	}

	public WillingnessMatrix addFactor(Factor factor, float weight, float value) {
		factors.put(factor, Pair.of(weight, value));
		return this;
	}

	private WillingnessMatrix() {
	}

	public float getResult() {
		return result;
	}

	public float getResultSquared() {
		return result * result;
	}

	public WillingnessMatrix performCalculation() {
		float sum = 0;
		float denom = 0;
		for (Factor factor : factors.keySet()) {
			Pair<Float, Float> pair = factors.get(factor);
			if (pair == null)
				continue;
			denom += pair.getFirst();
			sum += pair.getFirst() * pair.getSecond();
		}

		result = sum / denom;
		return this;
	}

	@Override
	public String toString() {
		StringBuilder output = new StringBuilder("[");
		boolean first = true;
		for (Map.Entry<Factor, Pair<Float, Float>> entry : factors.entrySet()) {
			if (entry.getValue() == null)
				continue;
			if (!first) {
				output.append(" + ");
			}
			output.append(entry.getKey()).append("(").append(entry.getValue().getFirst());
			output.append("w").append(" * ").append(entry.getValue().getSecond()).append(")");
			first = false;
		}
		output.append("]=").append(result);
		return output.toString();
	}

	public static enum Factor {
		PRIORITY, EMOTIONAL_FACTOR, SELFLESS_GAIN, PERSONAL_GAIN, POWER_DYNAMIC, STRESS
	}

}
