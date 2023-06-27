package mind.linguistics;

import java.util.List;
import java.util.Random;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;

import mind.concepts.type.IMeme;
import mind.linguistics.Phonology.Phoneme;

public class Language implements IMeme, Comparable<Language> {

	private String identifier;

	private Phonology phonology;

	private BiMap<IMeme, NameWord> names = HashBiMap.create();

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
	 * Give a concept a name
	 * 
	 * @param conc
	 * @param rand
	 * @return
	 */
	public NameWord name(IMeme conc, Random rand) {
		int syllables = (int) (rand.nextGaussian() / 3 + 2 + 0.5) + (int) (Math.abs(rand.nextGaussian() / 3));
		NameWord word = generateWord(conc.getUniqueName(), syllables, rand);
		this.names.forcePut(conc, word);
		return word;
	}

	protected NameWord generateWord(String id, int syllables, Random rand) {
		List<Phoneme> gen = phonology.generateWord(syllables, rand);
		return new NameWord(id).setPhonemes(gen);
	}

	public NameWord getWord(IMeme forc) {
		return this.names.get(forc);
	}

	public IMeme getConcept(NameWord forName) {
		return this.names.inverse().get(forName);
	}

	@Override
	public String getUniqueName() {
		return identifier;
	}

	@Override
	public int compareTo(Language o) {
		return this.identifier.compareTo(o.identifier);
	}

}
