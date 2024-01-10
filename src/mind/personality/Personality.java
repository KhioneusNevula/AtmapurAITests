package mind.personality;

import java.util.Collection;
import java.util.EnumMap;
import java.util.Map;
import java.util.Random;
import java.util.TreeMap;
import java.util.function.ToDoubleFunction;

import main.MathHelp;

/**
 * Average values are 0 TODO allow culture to influence personality (math D:)
 * 
 * @author borah
 *
 */
@SuppressWarnings("unchecked")
public class Personality {

	public static interface IPersonalityTrait {

		/**
		 * Return a weighted personality value given an input Random object. The output
		 * SHOULD be from -1 to 1
		 * 
		 * @param inputRand
		 * @return
		 */
		double getValue(Random inputRand);

		int ordinalNumber();

	}

	public static enum BasicPersonalityTrait implements IPersonalityTrait {

		/**
		 * How likely this individual is to assume a trait applies to a group
		 */
		PREJUDGEMENT((a) -> MathHelp.clamp(0, 1, Math.pow(a.nextGaussian(), 3) * 0.4 + 0.55)),
		/**
		 * how willing an individual is to help others / negative: how willing they are
		 * to sacrifice others for themself
		 */
		SELFLESSNESS,
		/**
		 * how much an individual needs time with others / negative: how much an
		 * individual needs to remain alone
		 */
		EXTROVERSION,

		/**
		 * How much an individual is affected by stress
		 */
		ANXIETY((a) -> 0.8 * Math.pow(a, 3), true),
		/**
		 * how much an individual follows roles / negative: how much an individual
		 * breaks roles and rules
		 */
		DUTY((a) -> 1.4 * Math.sqrt(a + 1) - 1, true),
		/** how much an individual values fairness / negative: how biased they are */
		JUSTNESS,
		/**
		 * how well an individual sticks to their relationships / negative: how much
		 * they break vows
		 */
		COMMITMENT((a) -> 2 * Math.pow(0.5 * a + 0.5, 0.33333) - 1, true),
		/**
		 * how willing an individual is to expend resources / negative: how easily they
		 * give away
		 */
		STINGINESS,
		/**
		 * how high an individual's usual happiness is / negative: how low their usual
		 * happiness is, how high their usual disenjoyment is
		 */
		OPTIMISM((a) -> 1.4 * Math.sqrt(a + 1) - 1, true),
		/** how much an individual desires resources / negative: */
		GREED,
		/** how artistically inclined / negative: */
		CREATIVITY((a) -> -Math.sin(2.2 * Math.PI * Math.abs(a)) - Math.cos(6 * Math.PI * Math.abs(a)) + 0.7, true),
		/**
		 * how prone an individual is to supernatural beliefs / negative: how reality
		 * inclined an individual is
		 */
		MYSTICISM,
		/**
		 * how much an individual spends thinking and constructing beliefs / negative:
		 * how heuristic an individual's thoughts are
		 */
		PONDEROUSNESS((a) -> -Math.sin(2.2 * Math.PI * Math.abs(a)) - Math.cos(6 * Math.PI * Math.abs(a)) + 0.7, true),
		/**
		 * how strongly an individual clings to a group identity / negative: how
		 * assimilationistic an individual is with their approach to identity
		 */
		DISTINGUISHEDNESS,
		/**
		 * how much an individual likes to eat / negative: how much an individual avoids
		 * eating
		 */
		GLUTTONY,
		/**
		 * how much an individual desires power / negative: how much an individual
		 * avoids power
		 */
		AMBITION((a) -> -(0.8 * Math.sin(a)) * Math.sin(9.2 * Math.PI * Math.abs(a))
				- 0.25 * Math.cos(0.9 * Math.PI * Math.abs(a)), true),
		/**
		 * how curious an individual is for knowledge / negative: how much an individual
		 * avoids knowledge
		 */
		CURIOSITY,
		/**
		 * how much an individual fears their own mortality / negative: how accepting an
		 * individual is of death
		 */
		MORTAL_FEAR((a) -> -(0.8 * Math.sin(a)) * Math.sin(9.2 * Math.PI * Math.abs(a))
				- 0.25 * Math.cos(0.9 * Math.PI * Math.abs(a)), true);

		private ToDoubleFunction<Random> curve;

		private BasicPersonalityTrait() {
			this.curve = (a) -> MathHelp.clamp(-1, 1, a.nextGaussian() * 0.3);
		}

		private BasicPersonalityTrait(ToDoubleFunction<Random> curve) {
			this.curve = curve;
		}

		/**
		 * function that takes the output of a random Gaussian and applies it to a
		 * curve; extra boolean only for distinction
		 * 
		 * @param curve
		 */
		private BasicPersonalityTrait(ToDoubleFunction<Double> curve, boolean a) {
			this.curve = (r) -> MathHelp.clamp(-1, 1, curve.applyAsDouble(r.nextGaussian()));
		}

		/**
		 * Return a weighted personality value given an input Random object. The output
		 * SHOULD be from -1 to 1
		 * 
		 * @param inputRand
		 * @return
		 */
		@Override
		public double getValue(Random inputRand) {
			double baba = curve.applyAsDouble(inputRand);
			if (baba < -1 || baba > 1)
				throw new IllegalStateException(this + ": " + inputRand + " -> " + baba);
			return baba;
		}

		@Override
		public int ordinalNumber() {
			return this.ordinal() + this.name().hashCode();
		}
	}

	@SuppressWarnings({ "rawtypes" })
	private Map values = Map.of();
	private String owner;

	public Personality(String owner) {
		this.owner = owner;
	}

	public <T extends IPersonalityTrait> void randomizePersonality(Random rand, Collection<T> ops) {
		this.values = new TreeMap<T, Double>((a, b) -> a.ordinalNumber() - b.ordinalNumber());
		for (T trait : ops) {
			values.put(trait, Double.valueOf(trait.getValue(rand)));
		}
	}

	public <T extends Enum<T>> void randomizePersonality(Random rand, Class<T> enumClass) {
		values = new EnumMap<T, Double>(enumClass);
		for (T trait : enumClass.getEnumConstants()) {
			values.put(trait, Double.valueOf(((IPersonalityTrait) trait).getValue(rand)));
		}
	}

	public double getTrait(IPersonalityTrait trait) {
		return (double) values.getOrDefault(trait, 0.0);
	}

	public void changeTrait(IPersonalityTrait trait, double newTrait) {
		values.put(trait, Double.valueOf(newTrait));
	}

	@Override
	public String toString() {
		return "personality(" + owner + ")";
	}

	public String report() {
		return "personality(" + owner + "):" + this.values;
	}
}
