package mind.linguistics;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.TreeMap;

import main.Pair;
import mind.Culture;
import mind.concepts.type.IMeme;
import mind.linguistics.Phonology.Phoneme;

public class Language implements IMeme, Comparable<Language> {

	private String identifier;

	private Phonology phonology;

	private Map<Pair<Culture, NameWord>, IMeme> names = new TreeMap<>(
			(a, b) -> (a.getFirst() != null && b.getFirst() != null ? a.getFirst().compareTo(b.getFirst()) : 0)
					+ a.getSecond().compareTo(b.getSecond()));
	private Map<Pair<IMeme, Culture>, NameWord> conceptToName = new TreeMap<>(
			(a, b) -> (a.getSecond() != null && b.getSecond() != null ? a.getSecond().compareTo(b.getSecond()) : 0)
					+ a.getFirst().getUniqueName().compareTo(b.getFirst().getUniqueName()));

	public Language(String identifier) {
		this.identifier = "lang_" + identifier;
		phonology = new Phonology(identifier);
	}

	/**
	 * generate features of this language
	 * 
	 * @param rand
	 */
	public void generate(Random rand) {
		System.out.println("generating language " + this.identifier);
		this.phonology.generatePhonology(10, 5, rand);
		System.out.println("finished generating language " + this.identifier);
	}

	/**
	 * Give a concept a random name, optionally in the given Cultures; if no
	 * cultures are specified, this is the "default name" for the language
	 * 
	 * @param conc
	 * @param rand
	 * @return
	 */
	public NameWord name(IMeme conc, Random rand, Collection<Culture> cultures) {
		int syllables = (int) (rand.nextGaussian() / 3 + 2 + 0.5) + (int) (Math.abs(rand.nextGaussian() / 3));
		NameWord word = generateWord(conc.getUniqueName(), syllables, rand);
		this.name(conc, word, cultures);
		return word;
	}

	/**
	 * Name this concept with the specific word, optionally in the given culture(s)
	 * 
	 * @param concept
	 * @param name
	 * @param cultures
	 */
	public void name(IMeme conc, NameWord word, Collection<Culture> cultures) {
		for (Culture culture : cultures) {
			this.names.put(Pair.of(culture, word), conc);
			this.conceptToName.put(Pair.of(conc, culture), word);
		}
		if (cultures == null || cultures.isEmpty()) {
			this.names.put(Pair.of(null, word), conc);
			this.conceptToName.put(Pair.of(conc, null), word);
		}
	}

	protected NameWord generateWord(String id, int syllables, Random rand) {
		List<Phoneme> gen = phonology.generateWord(syllables, rand);
		return new NameWord(id).setPhonemes(gen);
	}

	/**
	 * Gets the general word for the concept
	 * 
	 * @param forc
	 * @return
	 */
	public NameWord getWord(IMeme forc) {
		return conceptToName.get(Pair.of(forc, null));
	}

	public NameWord getWord(IMeme forc, Culture forC) {
		NameWord wor = conceptToName.get(Pair.of(forc, forC));
		if (wor == null)
			return conceptToName.get(Pair.of(forc, null));
		return wor;
	}

	public IMeme getConcept(NameWord forName, Culture forC) {
		IMeme mem = this.names.get(Pair.of(forC, forName));
		if (mem == null)
			return this.names.get(Pair.of(null, forName));
		return mem;
	}

	/**
	 * Gets the concept associated with a name without reference to a culture
	 * 
	 * @param forName
	 * @return
	 */
	public IMeme getConcept(NameWord forName) {
		return this.names.get(Pair.of(null, forName));
	}

	@Override
	public String getUniqueName() {
		return identifier;
	}

	@Override
	public int compareTo(Language o) {
		return this.identifier.compareTo(o.identifier);
	}

	@Override
	public IMemeType getMemeType() {
		return MemeType.LANGUAGE;
	}

}
