package mind.linguistics;

import java.util.HashSet;
import java.util.Random;
import java.util.Set;

import com.google.common.collect.Table;
import com.google.common.collect.TreeBasedTable;

import main.Pair;
import main.Triplet;
import mind.concepts.type.IConcept;
import mind.linguistics.Phonology.Phoneme.Consonant;
import mind.linguistics.Phonology.Phoneme.Frontness;
import mind.linguistics.Phonology.Phoneme.Height;
import mind.linguistics.Phonology.Phoneme.Manner;
import mind.linguistics.Phonology.Phoneme.Place;
import mind.linguistics.Phonology.Phoneme.Roundedness;
import mind.linguistics.Phonology.Phoneme.Voicing;
import mind.linguistics.Phonology.Phoneme.Vowel;

public class Phonology implements IConcept {

	private String uniqueName;
	private Set<Consonant> consonants;
	private Set<Vowel> vowels;
	private Set<Rule> rules;
	private Table<Place, Manner, Pair<Consonant, Consonant>> consonantsByProperty;
	private Table<Height, Frontness, Pair<Vowel, Vowel>> vowelsByProperty;

	public Phonology(String name) {
		this.uniqueName = "phono_" + name;
	}

	public String getUniqueName() {
		return uniqueName;
	}

	public void generatePhonology(int phonemes, Random rand) {
		this.generatePhonemes(phonemes, rand);
		this.generateRules(rand);
	}

	private void generatePhonemes(int phonemes, Random rand) {
		if (phonemes >= Consonant.VOICED_CONSONANTS.size() + Consonant.VOICELESS_CONSONANTS.size())
			throw new IllegalArgumentException();
		Consonant prev = null;
		HashSet<Consonant> cons = new HashSet<>();
		for (int i = 0; i < phonemes; i++) {
			Consonant gen = null;
			if (prev != null) {
				switch (rand.nextInt(3)) {
				case 0:
					for (Voicing voicing : Set.of(Voicing.values())) {
						if (voicing == prev.voicing)
							continue;
						if (Consonant.getConsonant(voicing, prev.place, prev.manner) != null) {
							gen = Consonant.getConsonant(voicing, prev.place, prev.manner);
						}
					}
					break;
				case 1:
					for (Place place : Set.of(Place.values())) {
						if (place == prev.place)
							continue;
						if (Consonant.getConsonant(prev.voicing, place, prev.manner) != null) {
							gen = Consonant.getConsonant(prev.voicing, place, prev.manner);
						}
					}
					break;
				default:
					for (Manner manner : Set.of(Manner.values())) {
						if (manner == prev.manner)
							continue;
						if (Consonant.getConsonant(prev.voicing, prev.place, manner) != null) {
							gen = Consonant.getConsonant(prev.voicing, prev.place, manner);
						}
					}
					break;
				}
			}
			if (prev == null || (prev != null && gen == null)) {
				Set<Consonant> allCons = new HashSet<>(Consonant.VOICED_CONSONANTS.values());
				allCons.addAll(Consonant.VOICELESS_CONSONANTS.values());
				while (gen == null) {
					gen = allCons.stream().findAny().get();
					if (cons.contains(gen)) {
						allCons.remove(gen);
						gen = null;
					}
				}
			}
			cons.add(gen);
			if (prev == null) {
				prev = gen;
			} else {
				prev = null;
			}
		}

	}

	private void generateRules(Random rand) {

	}

	public Consonant getConsonantByProperties(Place place, Manner manner, Voicing voicing) {
		Pair<Consonant, Consonant> pair = consonantsByProperty.get(place, manner);
		if (pair != null) {
			return voicing == Voicing.VOICED ? pair.getSecond() : pair.getFirst();
		}
		return null;
	}

	public Vowel getVowelByProperties(Height height, Frontness frontness, Roundedness roundedness) {
		Pair<Vowel, Vowel> pair = vowelsByProperty.get(height, frontness);
		if (pair != null) {
			return roundedness == Roundedness.ROUNDED ? pair.getSecond() : pair.getFirst();
		}
		return null;
	}

	public Set<Consonant> getConsonants() {
		return consonants;
	}

	public Set<Vowel> getVowels() {
		return vowels;
	}

	public Set<Rule> getRules() {
		return rules;
	}

	@Override
	public String toString() {
		return this.uniqueName;
	}

	public String report() {
		return this.uniqueName + "{phonemes=" + this.consonants + this.vowels + ",rules=" + this.rules + "}";
	}

	public static abstract class Phoneme implements IConcept, Comparable<Phoneme> {

		private String representation;

		public Phoneme(String representation) {
			this.representation = representation;
		}

		@Override
		public String getUniqueName() {
			return "phoneme_" + representation;
		}

		public String getRepresentation() {
			return representation;
		}

		@Override
		public String toString() {
			return representation;
		}

		public abstract String getInfo();

		@Override
		public int compareTo(Phoneme o) {
			return this.getInfo().compareTo(o.getInfo());
		}

		public static class Consonant extends Phoneme {
			private Voicing voicing;
			private Place place;
			private Manner manner;

			private static Table<Place, Manner, Consonant> VOICED_CONSONANTS = TreeBasedTable.create();
			private static Table<Place, Manner, Consonant> VOICELESS_CONSONANTS = TreeBasedTable.create();

			public static final Consonant BL_P = new Consonant("p", Voicing.VOICELESS, Place.BILABIAL, Manner.PLOSIVE);
			public static final Consonant V_BL_P = new Consonant("b", Voicing.VOICED, Place.BILABIAL, Manner.PLOSIVE);
			public static final Consonant A_P = new Consonant("t", Voicing.VOICELESS, Place.ALVEOLAR, Manner.PLOSIVE);
			public static final Consonant V_A_P = new Consonant("d", Voicing.VOICED, Place.ALVEOLAR, Manner.PLOSIVE);
			public static final Consonant R_P = new Consonant("r<tt", Voicing.VOICELESS, Place.RETROFLEX,
					Manner.PLOSIVE);
			public static final Consonant V_R_P = new Consonant("r<dd", Voicing.VOICED, Place.RETROFLEX,
					Manner.PLOSIVE);
			public static final Consonant P_P = new Consonant("i<k>y", Voicing.VOICELESS, Place.PALATAL,
					Manner.PLOSIVE);
			public static final Consonant V_P_P = new Consonant("i<g>y", Voicing.VOICED, Place.PALATAL, Manner.PLOSIVE);
			public static final Consonant VE_P = new Consonant("k", Voicing.VOICELESS, Place.VELAR, Manner.PLOSIVE);
			public static final Consonant V_VE_P = new Consonant("g", Voicing.VOICED, Place.VELAR, Manner.PLOSIVE);
			public static final Consonant U_P = new Consonant("q", Voicing.VOICELESS, Place.UVULAR, Manner.PLOSIVE);
			public static final Consonant V_U_P = new Consonant("gq", Voicing.VOICED, Place.UVULAR, Manner.PLOSIVE);
			public static final Consonant G_P = new Consonant("'", Voicing.VOICELESS, Place.GLOTTAL, Manner.PLOSIVE);
			public static final Consonant BL_N = new Consonant("m", Voicing.VOICED, Place.BILABIAL, Manner.NASAL);
			public static final Consonant LD_N = new Consonant("wm", Voicing.VOICED, Place.LABIODENTAL, Manner.NASAL);
			public static final Consonant A_N = new Consonant("n", Voicing.VOICED, Place.ALVEOLAR, Manner.NASAL);
			public static final Consonant R_N = new Consonant("rn", Voicing.VOICED, Place.RETROFLEX, Manner.NASAL);
			public static final Consonant P_N = new Consonant("i<n>y", Voicing.VOICED, Place.PALATAL, Manner.NASAL);
			public static final Consonant VE_N = new Consonant("ng", Voicing.VOICED, Place.VELAR, Manner.NASAL);
			public static final Consonant U_N = new Consonant("ngq", Voicing.VOICED, Place.UVULAR, Manner.NASAL);
			public static final Consonant V_BL_TR = new Consonant("bb", Voicing.VOICED, Place.BILABIAL, Manner.TRILL);
			public static final Consonant V_A_TR = new Consonant("rr", Voicing.VOICED, Place.ALVEOLAR, Manner.TRILL);
			public static final Consonant V_U_TR = new Consonant("xr", Voicing.VOICED, Place.UVULAR, Manner.TRILL);
			public static final Consonant BL_F = new Consonant("ph", Voicing.VOICELESS, Place.BILABIAL,
					Manner.FRICATIVE);
			public static final Consonant V_BL_F = new Consonant("vh", Voicing.VOICED, Place.BILABIAL,
					Manner.FRICATIVE);
			public static final Consonant LD_F = new Consonant("f", Voicing.VOICELESS, Place.LABIODENTAL,
					Manner.FRICATIVE);
			public static final Consonant V_LD_F = new Consonant("v", Voicing.VOICED, Place.LABIODENTAL,
					Manner.FRICATIVE);
			public static final Consonant D_F = new Consonant("th", Voicing.VOICELESS, Place.DENTAL, Manner.FRICATIVE);
			public static final Consonant V_D_F = new Consonant("dh", Voicing.VOICED, Place.DENTAL, Manner.FRICATIVE);
			public static final Consonant A_F = new Consonant("s", Voicing.VOICELESS, Place.ALVEOLAR, Manner.FRICATIVE);
			public static final Consonant V_A_F = new Consonant("z", Voicing.VOICED, Place.ALVEOLAR, Manner.FRICATIVE);
			public static final Consonant PA_F = new Consonant("sh", Voicing.VOICELESS, Place.POSTALVEOLAR,
					Manner.FRICATIVE);
			public static final Consonant V_PA_F = new Consonant("zh", Voicing.VOICED, Place.POSTALVEOLAR,
					Manner.FRICATIVE);
			public static final Consonant AP_F = new Consonant("ssh", Voicing.VOICELESS, Place.ALVEOLO_DASH_PALATAL,
					Manner.FRICATIVE);
			public static final Consonant V_AP_F = new Consonant("sz", Voicing.VOICED, Place.ALVEOLO_DASH_PALATAL,
					Manner.FRICATIVE);
			public static final Consonant R_F = new Consonant("r<sh", Voicing.VOICELESS, Place.RETROFLEX,
					Manner.FRICATIVE);
			public static final Consonant V_R_F = new Consonant("r<z", Voicing.VOICED, Place.RETROFLEX,
					Manner.FRICATIVE);
			public static final Consonant P_F = new Consonant("i<sh>y", Voicing.VOICELESS, Place.PALATAL,
					Manner.FRICATIVE);
			public static final Consonant V_P_F = new Consonant("i<z>y", Voicing.VOICED, Place.PALATAL,
					Manner.FRICATIVE);
			public static final Consonant VE_F = new Consonant("x", Voicing.VOICELESS, Place.VELAR, Manner.FRICATIVE);
			public static final Consonant V_VE_F = new Consonant("gh", Voicing.VOICED, Place.VELAR, Manner.FRICATIVE);
			public static final Consonant U_F = new Consonant("xh", Voicing.VOICELESS, Place.UVULAR, Manner.FRICATIVE);
			public static final Consonant V_U_F = new Consonant("hr", Voicing.VOICED, Place.UVULAR, Manner.FRICATIVE);
			public static final Consonant V_PH_F = new Consonant("hh", Voicing.VOICED, Place.PHARYNGEAL,
					Manner.FRICATIVE);
			public static final Consonant G_F = new Consonant("h", Voicing.VOICELESS, Place.GLOTTAL, Manner.FRICATIVE);
			public static final Consonant A_LF = new Consonant("lh", Voicing.VOICELESS, Place.ALVEOLAR,
					Manner.LATERAL_FRICATIVE);
			public static final Consonant LD_APP = new Consonant("fw", Voicing.VOICELESS, Place.LABIODENTAL,
					Manner.APPROXIMANT);
			public static final Consonant V_LD_APP = new Consonant("vw", Voicing.VOICED, Place.LABIODENTAL,
					Manner.APPROXIMANT);
			public static final Consonant V_A_APP = new Consonant("r", Voicing.VOICED, Place.ALVEOLAR,
					Manner.APPROXIMANT);
			public static final Consonant V_R_APP = new Consonant("rh", Voicing.VOICED, Place.RETROFLEX,
					Manner.APPROXIMANT);
			public static final Consonant V_P_APP = new Consonant("y", Voicing.VOICED, Place.PALATAL,
					Manner.APPROXIMANT);
			public static final Consonant V_VE_APP = new Consonant("u", Voicing.VOICED, Place.VELAR,
					Manner.APPROXIMANT);
			public static final Consonant V_LV_APP = new Consonant("w", Voicing.VOICED, Place.LABIALIZED_VELAR,
					Manner.APPROXIMANT);
			public static final Consonant LV_APP = new Consonant("wh", Voicing.VOICELESS, Place.LABIALIZED_VELAR,
					Manner.APPROXIMANT);
			public static final Consonant V_A_LAPP = new Consonant("l", Voicing.VOICED, Place.ALVEOLAR,
					Manner.LATERAL_APPROXIMANT);
			public static final Consonant V_R_LAPP = new Consonant("rl", Voicing.VOICED, Place.RETROFLEX,
					Manner.LATERAL_APPROXIMANT);
			public static final Consonant V_P_LAPP = new Consonant("i<l>y", Voicing.VOICED, Place.PALATAL,
					Manner.LATERAL_APPROXIMANT);
			public static final Consonant A_AF = new Consonant("ts", Voicing.VOICELESS, Place.ALVEOLAR,
					Manner.AFFRICATE);
			public static final Consonant P_AF = new Consonant("ch", Voicing.VOICELESS, Place.PALATAL,
					Manner.AFFRICATE);
			public static final Consonant AP_AF = new Consonant("qh", Voicing.VOICELESS, Place.ALVEOLO_DASH_PALATAL,
					Manner.AFFRICATE);
			public static final Consonant R_AF = new Consonant("r<ch", Voicing.VOICELESS, Place.RETROFLEX,
					Manner.AFFRICATE);
			public static final Consonant V_A_AF = new Consonant("dz", Voicing.VOICED, Place.ALVEOLAR,
					Manner.AFFRICATE);
			public static final Consonant V_P_AF = new Consonant("dj", Voicing.VOICED, Place.PALATAL, Manner.AFFRICATE);
			public static final Consonant V_AP_AF = new Consonant("j", Voicing.VOICED, Place.ALVEOLO_DASH_PALATAL,
					Manner.AFFRICATE);
			public static final Consonant V_R_AF = new Consonant("r<dj", Voicing.VOICED, Place.RETROFLEX,
					Manner.AFFRICATE);

			public Consonant(String rep, Voicing voicing, Place place, Manner manner) {
				super(rep);
				this.voicing = voicing;
				this.place = place;
				this.manner = manner;
				(voicing == Voicing.VOICED ? VOICED_CONSONANTS : VOICELESS_CONSONANTS).put(place, manner, this);
			}

			public Manner getManner() {
				return manner;
			}

			public Place getPlace() {
				return place;
			}

			public Voicing getVoicing() {
				return voicing;
			}

			/**
			 * Returns the voicing, place, and manner of the consonant as a string
			 * 
			 * @return
			 */
			public String getInfo() {
				return this.voicing + " " + this.place + " " + this.manner + " CONSONANT " + this.getRepresentation();
			}

			public static Consonant getConsonant(Voicing voicing, Place place, Manner manner) {
				return (voicing == Voicing.VOICED ? VOICED_CONSONANTS : VOICELESS_CONSONANTS).get(place, manner);
			}

		}

		public static class Vowel extends Phoneme {

			private Height height;
			private Frontness frontness;
			private Roundedness roundedness;

			private static Table<Height, Frontness, Vowel> ROUNDED_VOWELS = TreeBasedTable.create();
			private static Table<Height, Frontness, Vowel> UNROUNDED_VOWELS = TreeBasedTable.create();

			public static final Vowel H_F = new Vowel("ee", Roundedness.UNROUNDED, Height.HIGH, Frontness.FRONT);
			public static final Vowel R_H_F = new Vowel("y", Roundedness.ROUNDED, Height.HIGH, Frontness.FRONT);
			public static final Vowel H_B = new Vowel("uw", Roundedness.UNROUNDED, Height.HIGH, Frontness.BACK);
			public static final Vowel R_H_B = new Vowel("oo", Roundedness.ROUNDED, Height.HIGH, Frontness.BACK);
			public static final Vowel HM_FM = new Vowel("i", Roundedness.UNROUNDED, Height.HIGH_MID,
					Frontness.FRONT_MID);
			public static final Vowel HM_F = new Vowel("ei", Roundedness.UNROUNDED, Height.HIGH_MID, Frontness.FRONT);
			public static final Vowel R_HM_F = new Vowel("oe", Roundedness.ROUNDED, Height.HIGH_MID, Frontness.FRONT);
			public static final Vowel R_HM_B = new Vowel("ow", Roundedness.ROUNDED, Height.HIGH_MID, Frontness.BACK);
			public static final Vowel M_M = new Vowel("u>h", Roundedness.UNROUNDED, Height.MID, Frontness.MID);
			public static final Vowel LM_F = new Vowel("e", Roundedness.UNROUNDED, Height.LOW_MID, Frontness.FRONT);
			public static final Vowel R_LM_F = new Vowel("eu", Roundedness.ROUNDED, Height.LOW_MID, Frontness.FRONT);
			public static final Vowel LM_B = new Vowel("uh", Roundedness.UNROUNDED, Height.LOW_MID, Frontness.BACK);
			public static final Vowel R_LM_B = new Vowel("o", Roundedness.ROUNDED, Height.LOW_MID, Frontness.BACK);
			public static final Vowel LRM_F = new Vowel("ae", Roundedness.UNROUNDED, Height.LOWER_MID, Frontness.FRONT);
			public static final Vowel L_F = new Vowel("a", Roundedness.UNROUNDED, Height.LOW, Frontness.FRONT);
			public static final Vowel L_B = new Vowel("aw", Roundedness.UNROUNDED, Height.LOW, Frontness.BACK);
			public static final Vowel R_L_B = new Vowel("oh", Roundedness.ROUNDED, Height.LOW, Frontness.BACK);

			public Vowel(String rep, Roundedness roundedness, Height height, Frontness frontness) {
				super(rep);
				this.height = height;
				this.frontness = frontness;
				this.roundedness = roundedness;
				(roundedness == Roundedness.ROUNDED ? ROUNDED_VOWELS : UNROUNDED_VOWELS).put(height, frontness, this);
			}

			public Height getHeight() {
				return height;
			}

			public Frontness getFrontness() {
				return frontness;
			}

			public Roundedness getRoundedness() {
				return roundedness;
			}

			/**
			 * Returns the voicing, place, and manner of the consonant as a string
			 * 
			 * @return
			 */
			public String getInfo() {
				return this.roundedness + " " + this.height + " " + this.frontness + " VOWEL "
						+ this.getRepresentation();
			}

			public static Vowel getVowel(Roundedness roundedness, Height height, Frontness frontness) {
				return (roundedness == Roundedness.ROUNDED ? ROUNDED_VOWELS : UNROUNDED_VOWELS).get(height, frontness);
			}
		}

		public static enum Voicing {
			VOICED, VOICELESS;
		}

		public static enum Place {
			BILABIAL, LABIALIZED_VELAR, LABIODENTAL(0.2f), DENTAL(0.2f), ALVEOLO_DASH_PALATAL(0.3f), ALVEOLAR,
			POSTALVEOLAR, PALATAL, VELAR, RETROFLEX(0.1f), UVULAR(0.2f), PHARYNGEAL(0.1f), GLOTTAL;

			private float chance = 0.9f;

			private Place() {
			}

			private Place(float chance) {
				this.chance = chance;
			}

			public float getChance() {
				return chance;
			}

			@Override
			public String toString() {
				return super.toString().replace("_DASH_", "-").replace("_", " ");
			}
		}

		public static enum Manner {
			PLOSIVE, NASAL, TRILL(0.4f), FRICATIVE, AFFRICATE, APPROXIMANT, LATERAL_APPROXIMANT,
			LATERAL_FRICATIVE(0.4f);

			private float chance = 0.9f;

			private Manner() {
			}

			private Manner(float chance) {
				this.chance = chance;
			}

			public float getChance() {
				return chance;
			}

		}

		public static enum Height {
			HIGH, HIGH_MID(0.6f), MID(0.4f), LOW_MID(0.6f), LOWER_MID(0.2f), LOW;

			private float chance = 0.9f;

			private Height() {
			}

			private Height(float chance) {
				this.chance = chance;
			}

			public float getChance() {
				return chance;
			}
		}

		public static enum Frontness {
			FRONT, FRONT_MID(0.1f), MID(0.4f), MID_BACK, BACK;

			private float chance = 0.9f;

			private Frontness() {
			}

			private Frontness(float chance) {
				this.chance = chance;
			}

			public float getChance() {
				return chance;
			}
		}

		public static enum Roundedness {
			ROUNDED, UNROUNDED
		}
	}

	public static abstract class Rule implements IConcept {
		private Triplet<Voicing, Place, Manner> argumentPropsC;
		private Triplet<Roundedness, Height, Frontness> argumentPropsV;
		private Triplet<Voicing, Place, Manner> beforePropsC;
		private Triplet<Roundedness, Height, Frontness> beforePropsV;
		private Triplet<Voicing, Place, Manner> afterPropsC;
		private Triplet<Roundedness, Height, Frontness> afterPropsV;
		private boolean allowed = true;
		private Context context;

		private Rule(Roundedness r, Height h, Frontness f, Context c) {
			this.argumentPropsV = Triplet.of(r, h, f);
			this.context = c;
		}

		private Rule(Voicing v, Place p, Manner m, Context c) {
			this.argumentPropsC = Triplet.of(v, p, m);
			this.context = c;
		}

		private Rule disallow() {
			this.allowed = false;
			return this;
		}

		private Rule addBeforeArg(Roundedness r, Height h, Frontness f) {
			if (!context.hasBeforeArg)
				throw new UnsupportedOperationException();
			this.beforePropsV = Triplet.of(r, h, f);
			return this;
		}

		private Rule addAfterArg(Roundedness r, Height h, Frontness f) {
			if (!context.hasAfterArg)
				throw new UnsupportedOperationException();
			this.afterPropsV = Triplet.of(r, h, f);
			return this;
		}

		private Rule addBeforeArg(Voicing v, Place p, Manner m) {
			if (!context.hasBeforeArg)
				throw new UnsupportedOperationException();
			this.beforePropsC = Triplet.of(v, p, m);
			return this;
		}

		private Rule addAfterArg(Voicing v, Place p, Manner m) {
			if (!context.hasAfterArg)
				throw new UnsupportedOperationException();
			this.afterPropsC = Triplet.of(v, p, m);
			return this;
		}

		public boolean isAllowedInContext() {
			return allowed;
		}

		public Triplet<Voicing, Place, Manner> getAfterPropsC() {
			return afterPropsC;
		}

		public Triplet<Roundedness, Height, Frontness> getAfterPropsV() {
			return afterPropsV;
		}

		public Triplet<Roundedness, Height, Frontness> getArgumentPropsV() {
			return argumentPropsV;
		}

		public Triplet<Voicing, Place, Manner> getArgumentPropsC() {
			return argumentPropsC;
		}

		public Triplet<Voicing, Place, Manner> getBeforePropsC() {
			return beforePropsC;
		}

		public Triplet<Roundedness, Height, Frontness> getBeforePropsV() {
			return beforePropsV;
		}

		public Context getContext() {
			return context;
		}

		@Override
		public String getUniqueName() {
			StringBuilder builder = new StringBuilder("phono_rule_" + context.toString().toLowerCase());
			// TODO put all the stuff together
			return builder.toString();
		}
	}

	public static enum Context {
		WORD_INITIAL, WORD_FINAL, SYLLABLE_INITIAL, SYLLABLE_FINAL, BEFORE(true, false), AFTER(false, true),
		BETWEEN(true, true);

		private boolean hasAfterArg;
		private boolean hasBeforeArg;

		private Context() {
		}

		private Context(boolean hasBeforeArg, boolean hasAfterArg) {
			this.hasBeforeArg = hasBeforeArg;
			this.hasAfterArg = hasAfterArg;
		}

		public boolean hasBeforeArg() {
			return hasBeforeArg;
		}

		public boolean hasAfterArg() {
			return hasAfterArg;
		}

	}

}
