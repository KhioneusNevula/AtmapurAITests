package psych_first.perception.knowledge;

import java.util.Collection;

import culture.Culture;
import psych_first.mind.IMindPart;
import psych_first.mind.Mind;
import psych_first.perception.knowledge.events.IEvent;
import psych_first.perception.senses.SensoryInput;
import sociology.Profile;
import sociology.sociocon.PropertyHolder;
import sociology.sociocon.Sociocat;
import sociology.sociocon.Sociocon;

public class SocialAwareness implements IMindPart {

	private Mind mind;

	public SocialAwareness(Mind mind) {
		this.mind = mind;
	}

	@Override
	public Mind getMind() {
		return mind;
	}

	@Override
	public void update(int ticks) {
		if (ticks % 10 == 0) {
			for (Profile prof : this.mind.getSenses().getSensedProfiles()) {
				this.assignSociocons(prof, null);
			}
		}
	}

	/**
	 * assigns sociocons to the given profile
	 * 
	 * @param prof
	 * @param event
	 */
	public void assignSociocons(Profile prof, IEvent event) {
		if (event != null) {
			// TODO event related sociocons
		}
		SensoryInput in = this.mind.getSenses().getSensory(prof);
		if (in == null)
			return;

		for (Culture cul : this.mind.getCulture()) {
			for (Sociocat cat : cul.getSociocats()) {
				for (Sociocon con : cul.getSocioconMap(cat).values()) {
					for (Association assoc : con.getAssociations()) {
						Collection<PropertyHolder<?>> props = assoc.associate(in, mind, event);
						if (props == null) {
							prof.removeSociocon(con);

						} else {
							// TODO sociocon adding and stuff lol
						}
					}
				}
			}
		}
	}

	@Override
	public String report() {
		// TODO Auto-generated method stub
		return null;
	}

}
