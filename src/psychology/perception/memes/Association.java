package psychology.perception.memes;

import psychology.perception.criterion.Criterion;
import psychology.perception.inclination.Inclination;
import psychology.perception.info.InfoKey;

/***
 * An association determines what thought someone has when a certain piece of
 * information is recognized. It causes an observation to be associated with an
 * Inclination. An Inclination is either: A social construct which is then
 * applied to the profile; An emotion which is then evoked as a result of
 * interaction; An opinion which may influence other actions or behaviors, such
 * as causing a behavior or negating one.
 * 
 * Examples: <br>
 * > BruteTrait:Fangs --> SocialConstruct:Danger <br>
 * > SocialConstruct:Wealthy --> Opinion:Superior <br>
 * > SocialConstruct:Poor --> Opinion:Inferior
 * 
 * @author borah
 *
 */
public class Association<T> implements Meme {

	private InfoKey observation;
	private Criterion criterion;

	private Inclination inclination;

	public Association(InfoKey observation, Criterion criterion, Inclination inclination) {
		this.observation = observation;
		this.criterion = criterion;
		this.inclination = inclination;
	}

	public Inclination getInclination() {
		return inclination;
	}

	public InfoKey getObservation() {
		return observation;
	}

	public Criterion getCriterion() {
		return criterion;
	}

	@Override
	public String printDescription() {
		// TODO print description for association
		return "";
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof Association a) {
			return this.observation.equals(a.observation) && this.criterion.equals(a.criterion)
					&& this.inclination.equals(a.inclination);
		}
		return super.equals(obj);
	}

	@Override
	public int hashCode() {
		return observation.hashCode() + criterion.hashCode() + inclination.hashCode();
	}
}
