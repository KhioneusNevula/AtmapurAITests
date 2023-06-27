package mind.linguistics;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.google.common.collect.ImmutableSet;
import com.google.common.collect.ImmutableTable;
import com.google.common.collect.Table;
import com.google.common.collect.TreeBasedTable;

import main.Pair;
import main.Triplet;
import mind.concepts.type.IMeme;
import mind.linguistics.Phonology.Phoneme.Consonant;
import mind.linguistics.Phonology.Phoneme.Frontness;
import mind.linguistics.Phonology.Phoneme.Height;
import mind.linguistics.Phonology.Phoneme.Manner;
import mind.linguistics.Phonology.Phoneme.Place;
import mind.linguistics.Phonology.Phoneme.Roundedness;
import mind.linguistics.Phonology.Phoneme.Voicing;
import mind.linguistics.Phonology.Phoneme.Vowel;

public class Phonology implements IMeme {

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

	/**
	 * if any rule disallows this specific environment
	 * 
	 * @return
	 */
	private boolean doesRuleDisallow(Predicate<Rule> pred) {
		for (Rule rule : rules) {
			if (pred.test(rule) && !rule.isAllowedInContext()) {
				return true;
			}
		}
		return false;
	}

	/**
	 * if the rule has a "previous phoneme" argument, get its index using the
	 * phoneme to be added as the argument; return -1 if it doesn't fit
	 * 
	 * @param rule
	 * @return
	 */
	public int getPrevFromEnvironment(Rule rule, List<Phoneme> list) {
		if (rule.beforePropsC == null && rule.beforePropsV == null)
			throw new IllegalArgumentException();
		if (list.isEmpty())
			return -1;
		for (int i = list.size() - 1; i >= 0; i--) {
			if (list.get(i) instanceof Consonant) {
				if (rule.beforePropsC != null) {
					Place p = rule.beforePropsC.getSecond();
					Manner m = rule.beforePropsC.getThird();
					Voicing v = rule.beforePropsC.getFirst();

				} else {
					continue;
				}
			} else if (list.get(i) instanceof Vowel) {
				if (rule.beforePropsV != null) {

				} else {
					continue;
				}
			}
		}
		return -1;
	}

	/**
	 * If the rule has a "next phoneme" argument, get the phoneme that would precede
	 * it, using the phoneme to be added as the "next phoneme" argument; return -1
	 * if it doesn't fit
	 * 
	 * @param rule
	 * @param list
	 * @return
	 */
	public int getArgumentFromEnvironemt(Rule rule, List<Phoneme> list) {
		return -1;
	}

	// TODO make this better sigh and use those argument things;
	/**
	 * for now, only two consonants max in beginning/end of syllable and two vowels
	 * in the middle max; also approximants are only between vowels and after
	 * consonants
	 * 
	 * @param length
	 * @return
	 */
	public List<Phoneme> generateWord(int syllables, Random rand) {
		if (this.consonants == null)
			throw new UnsupportedOperationException();
		List<Phoneme> phonemes = new ArrayList<>();
		String ms = "";
		boolean noc = true;
		boolean af = false;
		Phoneme prev = null;
		for (int i = 0; i < syllables; i++) {
			boolean it = noc ? true : rand.nextBoolean();
			noc = true;
			Phoneme p = null;
			// Onset
			if (it) {
				final boolean aaf = af;
				final Phoneme pre = prev;
				List<Consonant> cs = this.consonants.stream().filter((a) -> pre != null ? a != pre : true)
						.filter((a) -> aaf ? (((Consonant) a).manner != Manner.AFFRICATE) : true)
						.collect(Collectors.toList());
				p = cs.get(rand.nextInt(cs.size()));
				ms += p;
				phonemes.add(p);
			}
			p = this.vowels.stream().collect(Collectors.toList()).get(rand.nextInt(vowels.size()));
			ms += p;
			phonemes.add(p);

			it = rand.nextBoolean();
			// Coda
			if (it) {
				p = this.consonants.stream().collect(Collectors.toList()).get(rand.nextInt(consonants.size()));
				if (((Consonant) p).getManner() == Manner.AFFRICATE)
					af = true;
				ms += p;
				phonemes.add(p);
				noc = false;
				prev = p;
			}

		}
		return phonemes;
	}

	public void generatePhonology(int cons, int vowels, Random rand) {

		System.out.println("generating phonemes for " + this.uniqueName);
		this.generatePhonemes(cons, vowels, rand);
		System.out.println("finished phonemes " + this.uniqueName);
		this.generateRules(rand);
		System.out.println("finished rules " + this.uniqueName);
		System.out.println("phonology " + this.report());
	}

	private void generatePhonemes(int conc, int vowelc, Random rand) {
		if (conc >= Consonant.VOICED_CONSONANTS.size() + Consonant.VOICELESS_CONSONANTS.size() || vowelc < 3
				|| vowelc >= Vowel.ROUNDED_VOWELS.size() + Vowel.UNROUNDED_VOWELS.size())
			throw new IllegalArgumentException();
		Consonant prev = null;
		HashSet<Consonant> cons = new HashSet<>();
		HashSet<Vowel> vows = new HashSet<>();
		for (int i = 0; i < conc; i++) {
			Consonant gen = null;
			if (prev != null) {
				int checker = rand.nextInt(10);
				if (checker < 6) {
					for (int b = 0; b < 12 && gen == null; b++) {
						for (Voicing voicing : Set.of(Voicing.values())) {
							if (voicing == prev.voicing)
								continue;
							Consonant prop = Consonant.getConsonant(voicing, prev.place, prev.manner);
							if (prop != null && rand.nextDouble() * 1.5 <= prop.chance()) {
								gen = prop;
							}
						}
					}
				} else if (checker < 7) {
					for (int b = 0; b < 12 && gen == null; b++) {
						for (Place place : Set.of(Place.values())) {
							if (place == prev.place)
								continue;
							Consonant prop = Consonant.getConsonant(prev.voicing, place, prev.manner);
							if (prop != null && rand.nextDouble() * 1.5 <= prop.chance()) {
								gen = prop;
							}
						}
					}
				} else {
					for (int b = 0; b < 12 && gen == null; b++) {
						for (Manner manner : Set.of(Manner.values())) {
							if (manner == prev.manner)
								continue;
							Consonant prop = Consonant.getConsonant(prev.voicing, prev.place, manner);
							if (prop != null && rand.nextDouble() * 1.5 <= prop.chance()) {
								gen = prop;
							}
						}
					}
				}

			}
			if (prev == null || (prev != null && gen == null)) {
				Set<Consonant> allCons = new HashSet<>(Consonant.VOICED_CONSONANTS.values());
				allCons.addAll(Consonant.VOICELESS_CONSONANTS.values());
				for (int b = 0; b < 12 && gen == null; b++) {
					gen = null;
					for (Consonant con : allCons) {
						double chance = con.chance();
						if (rand.nextDouble() * 1.5 < chance) {
							gen = con;
							break;
						}
					}
					if (gen == null)
						continue;
					if (cons.contains(gen)) {
						allCons.remove(gen);
						gen = null;
					}
				}
			}
			if (gen != null)
				cons.add(gen);
			if (prev == null) {
				prev = gen;
			} else {
				prev = null;
			}
		}
		for (int i = 0; i < vowelc; i++) {
			Vowel gen = null;
			Set<Vowel> allVowels = new HashSet<>(Vowel.ROUNDED_VOWELS.values());
			allVowels.addAll(Vowel.UNROUNDED_VOWELS.values());
			for (int b = 0; b < 12 && gen == null; b++) {
				gen = null;
				for (Vowel vow : allVowels) {
					double chance = vow.chance();
					if (rand.nextDouble() * 2 < chance) {
						gen = vow;
						break;
					}
				}
				if (gen == null)
					continue;
				if (vows.contains(gen)) {
					allVowels.remove(gen);
					gen = null;
				}
				if (gen != null)
					vows.add(gen);
			}
		}
		this.vowels = ImmutableSet.copyOf(vows);
		this.consonants = ImmutableSet.copyOf(cons);
		Table<Place, Manner, Pair<Consonant, Consonant>> consT = TreeBasedTable.create();
		Table<Height, Frontness, Pair<Vowel, Vowel>> vowsT = TreeBasedTable.create();
		for (Consonant c : consonants) {
			Pair<Consonant, Consonant> pair = consT.get(c.place, c.manner);
			if (pair == null) {
				pair = Pair.of(null, null);
				consT.put(c.place, c.manner, pair);
			}
			if (c.voicing == Voicing.VOICED) {
				pair.setSecond(c);
			} else {
				pair.setFirst(c);
			}
		}
		for (Vowel v : vowels) {
			Pair<Vowel, Vowel> pair = vowsT.get(v.height, v.frontness);
			if (pair == null) {
				pair = Pair.of(null, null);
				vowsT.put(v.height, v.frontness, pair);
			}
			if (v.roundedness == Roundedness.ROUNDED) {
				pair.setSecond(v);
			} else {
				pair.setFirst(v);
			}
		}
		this.consonantsByProperty = ImmutableTable.copyOf(consT);
		this.vowelsByProperty = ImmutableTable.copyOf(vowsT);

	}

	private static enum RuleArchetype {
		ASSIMILATE, PALATALIZE, VOWEL_HARMONY, REMOVAL, VOICING, DEVOICING, EPENTHESIS,
		ONE_CONSONANT_AT_END_OF_SYLLABLE, ONE_CONSONANT_AT_BEGINNING_OF_SYLLABLE, NO_CONSONANT_AT_END_OF_SYLLABLE
	}

	private void addConsonant(Consonant con) {
		this.consonants = ImmutableSet.<Consonant>builder().addAll(consonants).add(con).build();
		Pair<Consonant, Consonant> pair = consonantsByProperty.get(con.place, con.manner);
		if (pair == null)
			pair = Pair.of(con.voicing == Voicing.VOICELESS ? con : null, con.voicing == Voicing.VOICED ? con : null);
		this.consonantsByProperty = ImmutableTable.<Place, Manner, Pair<Consonant, Consonant>>builder()
				.putAll(consonantsByProperty).put(con.place, con.manner, pair).build();
	}

	private void addVowel(Vowel vow) {
		this.vowels = ImmutableSet.<Vowel>builder().addAll(vowels).add(vow).build();
		Pair<Vowel, Vowel> pair = vowelsByProperty.get(vow.height, vow.frontness);
		if (pair == null)
			pair = Pair.of(vow.roundedness == Roundedness.UNROUNDED ? vow : null,
					vow.roundedness == Roundedness.ROUNDED ? vow : null);
		this.vowelsByProperty = ImmutableTable.<Height, Frontness, Pair<Vowel, Vowel>>builder().putAll(vowelsByProperty)
				.put(vow.height, vow.frontness, pair).build();
	}

	private void generateRules(Random rand) {
		if (rules == null) {
			rules = new HashSet<>();
			return;
		} // TODO no rules for now tbh
		int passes = rand.nextInt(10) + 3;
		for (int i = 0; i < passes; i++) {
			Rule rule = null;
			while (rule == null) {
				switch (RuleArchetype.values()[rand.nextInt(RuleArchetype.values().length)]) {
				case ASSIMILATE: {
					// TODO some of these ig idk
					break;
				}
				case PALATALIZE: {
					break;
				}
				case VOWEL_HARMONY: {
					break;
				}
				case REMOVAL: {
					break;
				}
				case VOICING: {
					break;
				}
				case DEVOICING: {
					break;
				}
				case EPENTHESIS: {
					boolean vow = rand.nextBoolean();
					if (!vow) {
						Place p = Stream.of(Place.values()).filter((a) -> a != Place.BILABIAL).findAny().get();
						if (this.getConsonantByProperties(Place.BILABIAL, Manner.NASAL, Voicing.VOICED) == null)
							continue;
						if (this.getConsonantByProperties(p, Manner.PLOSIVE, Voicing.VOICELESS) == null)
							continue;
						rule = new Rule(Context.BETWEEN).addBeforeArg(Voicing.VOICED, Place.BILABIAL, Manner.NASAL)
								.addAfterArg(Voicing.VOICELESS, p, Manner.PLOSIVE)
								.become(Voicing.VOICELESS, Place.BILABIAL, Manner.PLOSIVE); // voicing from second,
																							// place
																							// from first
						if (this.getConsonantByProperties(Place.BILABIAL, Manner.PLOSIVE, Voicing.VOICELESS) == null) {
							this.addConsonant(Consonant.BL_P);
						}
					} else {
						Voicing v1 = rand.nextInt(3) < 2 ? null
								: (rand.nextBoolean() ? Voicing.VOICED : Voicing.VOICELESS);
						Voicing v2 = rand.nextInt(3) < 2 ? null
								: (rand.nextBoolean() ? Voicing.VOICED : Voicing.VOICELESS);
						Place p1 = rand.nextInt(5) < 4 ? null : Stream.of(Place.values()).findAny().get();
						Place p2 = rand.nextInt(5) < 4 ? null : Stream.of(Place.values()).findAny().get();
						Manner m1 = rand.nextInt(5) < 4 ? null : Stream.of(Manner.values()).findAny().get();
						Manner m2 = rand.nextInt(5) < 4 ? null : Stream.of(Manner.values()).findAny().get();
						if (this.getConsonantByProperties(p1, m1, v1) == null)
							continue;
						if (this.getConsonantByProperties(p2, m2, v2) == null)
							continue;
						Roundedness r = rand.nextBoolean() ? Roundedness.UNROUNDED : null;
						Frontness f = rand.nextInt(5) < 4 ? (rand.nextInt(3) < 2 ? Frontness.MID
								: rand.nextInt(4) < 2 ? Frontness.BACK : Frontness.MID_BACK) : null;
						Height h = rand.nextInt(5) < 4 ? (rand.nextInt(3) < 2 ? Height.MID
								: (rand.nextInt(4) < 2 ? Height.HIGH : Height.HIGH_MID)) : null;
						if (Vowel.getVowel(r, h, f) == null)
							continue;
						if (this.getVowelByProperties(h, f, r) == null) {
							this.addVowel(Vowel.getVowel(r, h, f));
						}

						rule = new Rule(Context.BETWEEN).addBeforeArg(v1, p1, m1).addAfterArg(v2, p2, m2).become(r, h,
								f);
					}
					break;
				}
				case ONE_CONSONANT_AT_END_OF_SYLLABLE: {
					rule = new Rule((Voicing) null, null, null, Context.SYLLABLE_FINAL_AFTER)
							.addBeforeArg((Voicing) null, null, null).disallow();

					break;
				}
				case ONE_CONSONANT_AT_BEGINNING_OF_SYLLABLE: {
					rule = new Rule((Voicing) null, null, null, Context.SYLLABLE_INITIAL_BEFORE)
							.addAfterArg((Voicing) null, null, null).disallow();

					break;
				}
				case NO_CONSONANT_AT_END_OF_SYLLABLE: {
					rule = new Rule((Voicing) null, null, null, Context.SYLLABLE_FINAL).disallow();

					break;
				}
				}

			}
		}
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

	public static abstract class Phoneme implements IMeme, Comparable<Phoneme> {

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

		public abstract double chance();

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
			public static final Consonant R_P = new Consonant("rt<>t", Voicing.VOICELESS, Place.RETROFLEX,
					Manner.PLOSIVE);
			public static final Consonant V_R_P = new Consonant("rd<>d", Voicing.VOICED, Place.RETROFLEX,
					Manner.PLOSIVE);
			public static final Consonant P_P = new Consonant("i<k>y", Voicing.VOICELESS, Place.PALATAL,
					Manner.PLOSIVE);
			public static final Consonant V_P_P = new Consonant("i<g>y", Voicing.VOICED, Place.PALATAL, Manner.PLOSIVE);
			public static final Consonant VE_P = new Consonant("k", Voicing.VOICELESS, Place.VELAR, Manner.PLOSIVE);
			public static final Consonant V_VE_P = new Consonant("g", Voicing.VOICED, Place.VELAR, Manner.PLOSIVE);
			public static final Consonant U_P = new Consonant("q", Voicing.VOICELESS, Place.UVULAR, Manner.PLOSIVE);
			public static final Consonant V_U_P = new Consonant("g<>q", Voicing.VOICED, Place.UVULAR, Manner.PLOSIVE);
			public static final Consonant G_P = new Consonant("'<", Voicing.VOICELESS, Place.GLOTTAL, Manner.PLOSIVE);
			public static final Consonant BL_N = new Consonant("m", Voicing.VOICED, Place.BILABIAL, Manner.NASAL);
			public static final Consonant LD_N = new Consonant("w<m", Voicing.VOICED, Place.LABIODENTAL, Manner.NASAL);
			public static final Consonant A_N = new Consonant("n", Voicing.VOICED, Place.ALVEOLAR, Manner.NASAL);
			public static final Consonant R_N = new Consonant("r<n", Voicing.VOICED, Place.RETROFLEX, Manner.NASAL);
			public static final Consonant P_N = new Consonant("i<n>y", Voicing.VOICED, Place.PALATAL, Manner.NASAL);
			public static final Consonant VE_N = new Consonant("ng", Voicing.VOICED, Place.VELAR, Manner.NASAL);
			public static final Consonant U_N = new Consonant("nq", Voicing.VOICED, Place.UVULAR, Manner.NASAL);
			public static final Consonant V_BL_TR = new Consonant("b<>b", Voicing.VOICED, Place.BILABIAL, Manner.TRILL);
			public static final Consonant V_A_TR = new Consonant("r<r", Voicing.VOICED, Place.ALVEOLAR, Manner.TRILL);
			public static final Consonant V_U_TR = new Consonant("r<r", Voicing.VOICED, Place.UVULAR, Manner.TRILL);
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
			public static final Consonant AP_F = new Consonant("s<s>h", Voicing.VOICELESS, Place.ALVEOLO_DASH_PALATAL,
					Manner.FRICATIVE);
			public static final Consonant V_AP_F = new Consonant("z<>z", Voicing.VOICED, Place.ALVEOLO_DASH_PALATAL,
					Manner.FRICATIVE);
			public static final Consonant R_F = new Consonant("r<sh", Voicing.VOICELESS, Place.RETROFLEX,
					Manner.FRICATIVE);
			public static final Consonant V_R_F = new Consonant("r<z", Voicing.VOICED, Place.RETROFLEX,
					Manner.FRICATIVE);
			public static final Consonant P_F = new Consonant("i<sh>y", Voicing.VOICELESS, Place.PALATAL,
					Manner.FRICATIVE);
			public static final Consonant V_P_F = new Consonant("i<z>y", Voicing.VOICED, Place.PALATAL,
					Manner.FRICATIVE);
			public static final Consonant VE_F = new Consonant("kh", Voicing.VOICELESS, Place.VELAR, Manner.FRICATIVE);
			public static final Consonant V_VE_F = new Consonant("gh", Voicing.VOICED, Place.VELAR, Manner.FRICATIVE);
			public static final Consonant U_F = new Consonant("h<>h", Voicing.VOICELESS, Place.UVULAR,
					Manner.FRICATIVE);
			public static final Consonant V_U_F = new Consonant("hr", Voicing.VOICED, Place.UVULAR, Manner.FRICATIVE);
			public static final Consonant V_PH_F = new Consonant("h<>h", Voicing.VOICED, Place.PHARYNGEAL,
					Manner.FRICATIVE);
			public static final Consonant G_F = new Consonant("h", Voicing.VOICELESS, Place.GLOTTAL, Manner.FRICATIVE);
			public static final Consonant A_LF = new Consonant("l>h", Voicing.VOICELESS, Place.ALVEOLAR,
					Manner.LATERAL_FRICATIVE);
			public static final Consonant LD_APP = new Consonant("f<>w", Voicing.VOICELESS, Place.LABIODENTAL,
					Manner.APPROXIMANT);
			public static final Consonant V_LD_APP = new Consonant("v<>w", Voicing.VOICED, Place.LABIODENTAL,
					Manner.APPROXIMANT);
			public static final Consonant V_A_APP = new Consonant("r", Voicing.VOICED, Place.ALVEOLAR,
					Manner.APPROXIMANT);
			public static final Consonant V_R_APP = new Consonant("rh", Voicing.VOICED, Place.RETROFLEX,
					Manner.APPROXIMANT);
			public static final Consonant V_P_APP = new Consonant("y", Voicing.VOICED, Place.PALATAL,
					Manner.APPROXIMANT);
			public static final Consonant V_VE_APP = new Consonant("u<>w", Voicing.VOICED, Place.VELAR,
					Manner.APPROXIMANT);
			public static final Consonant V_LV_APP = new Consonant("w", Voicing.VOICED, Place.LABIALIZED_VELAR,
					Manner.APPROXIMANT);
			public static final Consonant LV_APP = new Consonant("w>h", Voicing.VOICELESS, Place.LABIALIZED_VELAR,
					Manner.APPROXIMANT);
			public static final Consonant V_A_LAPP = new Consonant("l", Voicing.VOICED, Place.ALVEOLAR,
					Manner.LATERAL_APPROXIMANT);
			public static final Consonant V_R_LAPP = new Consonant("r<l", Voicing.VOICED, Place.RETROFLEX,
					Manner.LATERAL_APPROXIMANT);
			public static final Consonant V_P_LAPP = new Consonant("i<l>y", Voicing.VOICED, Place.PALATAL,
					Manner.LATERAL_APPROXIMANT);
			public static final Consonant A_AF = new Consonant("ts", Voicing.VOICELESS, Place.ALVEOLAR,
					Manner.AFFRICATE);
			public static final Consonant P_AF = new Consonant("ch", Voicing.VOICELESS, Place.PALATAL,
					Manner.AFFRICATE);
			public static final Consonant AP_AF = new Consonant("q>i", Voicing.VOICELESS, Place.ALVEOLO_DASH_PALATAL,
					Manner.AFFRICATE);
			public static final Consonant R_AF = new Consonant("r<ch", Voicing.VOICELESS, Place.RETROFLEX,
					Manner.AFFRICATE);
			public static final Consonant V_A_AF = new Consonant("dz", Voicing.VOICED, Place.ALVEOLAR,
					Manner.AFFRICATE);
			public static final Consonant V_P_AF = new Consonant("j", Voicing.VOICED, Place.PALATAL, Manner.AFFRICATE);
			public static final Consonant V_AP_AF = new Consonant("j>i", Voicing.VOICED, Place.ALVEOLO_DASH_PALATAL,
					Manner.AFFRICATE);
			public static final Consonant V_R_AF = new Consonant("r<j", Voicing.VOICED, Place.RETROFLEX,
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

			@Override
			public double chance() {
				return 0.5 * manner.chance + place.chance;
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

			public static final Vowel H_F = new Vowel("i", Roundedness.UNROUNDED, Height.HIGH, Frontness.FRONT);
			public static final Vowel R_H_F = new Vowel("w<i", Roundedness.ROUNDED, Height.HIGH, Frontness.FRONT);
			public static final Vowel H_B = new Vowel("u>w", Roundedness.UNROUNDED, Height.HIGH, Frontness.BACK);
			public static final Vowel R_H_B = new Vowel("u", Roundedness.ROUNDED, Height.HIGH, Frontness.BACK);
			public static final Vowel HM_FM = new Vowel("i>h", Roundedness.UNROUNDED, Height.HIGH_MID,
					Frontness.FRONT_MID);
			public static final Vowel HM_F = new Vowel("ei", Roundedness.UNROUNDED, Height.HIGH_MID, Frontness.FRONT);
			public static final Vowel R_HM_F = new Vowel("oi", Roundedness.ROUNDED, Height.HIGH_MID, Frontness.FRONT);
			public static final Vowel R_HM_B = new Vowel("o>w", Roundedness.ROUNDED, Height.HIGH_MID, Frontness.BACK);
			public static final Vowel M_M = new Vowel("u>h", Roundedness.UNROUNDED, Height.MID, Frontness.MID);
			public static final Vowel LM_F = new Vowel("e", Roundedness.UNROUNDED, Height.LOW_MID, Frontness.FRONT);
			public static final Vowel R_LM_F = new Vowel("w<e", Roundedness.ROUNDED, Height.LOW_MID, Frontness.FRONT);
			public static final Vowel LM_B = new Vowel("u>h", Roundedness.UNROUNDED, Height.LOW_MID, Frontness.BACK);
			public static final Vowel R_LM_B = new Vowel("o", Roundedness.ROUNDED, Height.LOW_MID, Frontness.BACK);
			public static final Vowel LRM_F = new Vowel("ae>h", Roundedness.UNROUNDED, Height.LOWER_MID,
					Frontness.FRONT);
			public static final Vowel L_F = new Vowel("a", Roundedness.UNROUNDED, Height.LOW, Frontness.FRONT);
			public static final Vowel L_B = new Vowel("aw", Roundedness.UNROUNDED, Height.LOW, Frontness.BACK);
			public static final Vowel R_L_B = new Vowel("o>h", Roundedness.ROUNDED, Height.LOW, Frontness.BACK);

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

			@Override
			public double chance() {
				return this.height.chance + this.frontness.chance;
			}

			public static Vowel getVowel(Roundedness roundedness, Height height, Frontness frontness) {
				return (roundedness == Roundedness.ROUNDED ? ROUNDED_VOWELS : UNROUNDED_VOWELS).get(height, frontness);
			}
		}

		public static enum Voicing {
			VOICED, VOICELESS;

			public String getAbbr() {
				return this == VOICED ? "V" : "VL";
			}
		}

		public static enum Place {
			BILABIAL("BL"), LABIALIZED_VELAR("LV"), LABIODENTAL("LD", 0.2f), DENTAL("D", 0.2f),
			ALVEOLO_DASH_PALATAL("AP", 0.3f), ALVEOLAR("A"), POSTALVEOLAR("PA"), PALATAL("P"), VELAR("VE"),
			RETROFLEX("R", 0.05f), UVULAR("U", 0.1f), PHARYNGEAL("PH", 0.05f), GLOTTAL("G");

			private float chance = 0.9f;

			private String abbr;

			private Place(String abbr) {
				this.abbr = abbr;
			}

			private Place(String abbr, float chance) {
				this.abbr = abbr;
				this.chance = chance;
			}

			public float getChance() {
				return chance;
			}

			@Override
			public String toString() {
				return super.toString().replace("_DASH_", "-").replace("_", " ");
			}

			public String getAbbr() {
				return abbr;
			}
		}

		public static enum Manner {
			PLOSIVE("P", 0.8f), NASAL("N"), TRILL("TR", 0.4f), FRICATIVE("F"), AFFRICATE("AF", 0.5f),
			APPROXIMANT("APP"), LATERAL_APPROXIMANT("LAPP", 0.8f), LATERAL_FRICATIVE("LF", 0.4f);

			private float chance = 0.9f;
			private String abbr;

			private Manner(String abbr) {
				this.abbr = abbr;
			}

			public String getAbbr() {
				return abbr;
			}

			private Manner(String abbr, float chance) {
				this.abbr = abbr;
				this.chance = chance;
			}

			public float getChance() {
				return chance;
			}

		}

		public static enum Height {
			HIGH("H"), HIGH_MID("HM", 0.6f), MID("M", 0.4f), LOW_MID("LM", 0.6f), LOWER_MID("LM", 0.2f), LOW("L");

			private float chance = 0.9f;

			private String abbr;

			private Height(String abbr) {
				this.abbr = abbr;
			}

			public String getAbbr() {
				return abbr;
			}

			private Height(String abbr, float chance) {
				this.abbr = abbr;
				this.chance = chance;
			}

			public float getChance() {
				return chance;
			}
		}

		public static enum Frontness {
			FRONT("F"), FRONT_MID("FM", 0.1f), MID("M", 0.4f), MID_BACK("MB"), BACK("B");

			private float chance = 0.9f;
			private String abbr;

			private Frontness(String abbr) {
				this.abbr = abbr;
			}

			public String getAbbr() {
				return abbr;
			}

			private Frontness(String abbr, float chance) {
				this.abbr = abbr;
				this.chance = chance;
			}

			public float getChance() {
				return chance;
			}
		}

		public static enum Roundedness {
			ROUNDED, UNROUNDED;

			public String getAbbr() {
				return this == ROUNDED ? "R" : "UR";
			}
		}
	}

	public static class Rule implements IMeme {
		private Triplet<Voicing, Place, Manner> argumentPropsC;
		private Triplet<Roundedness, Height, Frontness> argumentPropsV;
		private Triplet<Voicing, Place, Manner> beforePropsC;
		private Triplet<Roundedness, Height, Frontness> beforePropsV;
		private Triplet<Voicing, Place, Manner> afterPropsC;
		private Triplet<Roundedness, Height, Frontness> afterPropsV;
		private Triplet<Roundedness, Height, Frontness> becomeV;
		private Triplet<Voicing, Place, Manner> becomeC;
		private boolean allowed = true;
		private Context context;
		private boolean vowelBased;
		private boolean consonantBased;

		private Rule(Context c) {
			this.context = c;
		}

		private Rule(Roundedness r, Height h, Frontness f, Context c) {
			this.argumentPropsV = Triplet.of(r, h, f);
			this.vowelBased = true;
			this.context = c;
		}

		private Rule(Voicing v, Place p, Manner m, Context c) {
			this.argumentPropsC = Triplet.of(v, p, m);
			this.consonantBased = true;
			this.context = c;
		}

		private Rule disallow() {
			if (this.becomeC != null || this.becomeV != null)
				throw new UnsupportedOperationException();
			this.allowed = false;
			return this;
		}

		private Rule become(Voicing v, Place p, Manner m) {
			if (!this.allowed || this.vowelBased)
				throw new UnsupportedOperationException();
			this.becomeC = Triplet.of(v, p, m);
			return this;
		}

		private Rule become(Roundedness r, Height h, Frontness f) {
			if (!this.allowed || this.consonantBased)
				throw new UnsupportedOperationException();
			this.becomeV = Triplet.of(r, h, f);
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

		public boolean isVowelBased() {
			return vowelBased;
		}

		public boolean isConsonantBased() {
			return consonantBased;
		}

		public boolean isEpenthetic() {
			return !this.consonantBased && !this.vowelBased;
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

		public Triplet<Voicing, Place, Manner> getBecomeC() {
			return becomeC;
		}

		public Triplet<Roundedness, Height, Frontness> getBecomeV() {
			return becomeV;
		}

		public Context getContext() {
			return context;
		}

		@Override
		public String getUniqueName() {
			StringBuilder builder = new StringBuilder("phono_rule_" + context.toString().toLowerCase() + "_");
			if (!allowed)
				builder.append("dis_");
			// TODO put all the stuff together
			return builder.toString();
		}
	}

	public static enum Context {
		WORD_INITIAL, WORD_FINAL, WORD_INITIAL_BEFORE(false, true), WORD_FINAL_AFTER(true, false), SYLLABLE_INITIAL,
		SYLLABLE_INITIAL_BEFORE(false, true), SYLLABLE_FINAL, SYLLABLE_FINAL_AFTER(true, false), BEFORE(false, true),
		AFTER(true, false), LONG_DISTANCE_BEFORE(false, true), LONG_DISTANCE_AFTER(true, false), BETWEEN(true, true),
		LONG_DISTANCE_BETWEEN(true, true);

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
