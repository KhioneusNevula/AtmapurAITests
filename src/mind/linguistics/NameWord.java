package mind.linguistics;

import java.util.List;

import mind.concepts.type.IMeme;
import mind.linguistics.Phonology.Phoneme;
import mind.linguistics.Phonology.Phoneme.Consonant;

/**
 * a name for a concept in a language; a word
 * 
 * @author borah
 *
 */
public class NameWord implements IMeme, Comparable<NameWord> {

	private String identifier;
	private List<Phoneme> phonemes;
	private String display;

	public NameWord(String identifier) {
		this.identifier = "nameword_" + /* language.getName() + "_" + */identifier;

	}

	@Override
	public IMemeType getMemeType() {
		return MemeType.NAME_WORD;
	}

	public NameWord setPhonemes(List<Phoneme> ph) {
		this.phonemes = List.copyOf(ph);
		StringBuilder word = new StringBuilder();
		for (int i = 0; i < ph.size(); i++) {
			Phoneme p = ph.get(i);
			final String rep = p.getRepresentation();
			String ac = "";
			int fs = rep.indexOf("<");
			int ss = rep.indexOf(">");
			boolean pc = (i > 0 ? ph.get(i - 1) instanceof Consonant : true);
			boolean nc = (i < ph.size() - 1 ? ph.get(i + 1) instanceof Consonant : false);
			if (fs == -1 && ss == -1) {
				ac = rep;
			} else if (fs == -1 && ss != -1) {
				ac += rep.substring(0, ss);
				if (!nc) {
					ac += rep.substring(ss + 1);
				}
			} else if (fs != -1 && ss == -1) {
				if (!pc) {
					ac += rep.substring(0, fs);
				}
				ac += rep.substring(fs + 1);

			} else if (fs != -1 && ss != -1) {
				if (!pc) {
					ac += rep.substring(0, fs);
				}
				ac += rep.substring(fs + 1, ss);
				if (!nc) {
					ac += rep.substring(ss + 1);
				}
			}
			word.append(ac);
		}
		this.display = word.toString();
		return this;
	}

	public List<Phoneme> getPhonemes() {
		return phonemes;
	}

	@Override
	public String getUniqueName() {
		return identifier;
	}

	public String getDisplay() {
		return display;
	}

	@Override
	public String toString() {
		return identifier + "{" + display + "}";
	}

	@Override
	public int compareTo(NameWord o) {
		return this.phonemes.toString().compareTo(o.phonemes.toString());
	}

}
