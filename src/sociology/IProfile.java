package sociology;

import java.util.Collection;

import culture.CulturalContext;
import culture.Culture;
import psych_first.perception.knowledge.IKnowledgeType;
import psych_first.perception.knowledge.events.IEvent;
import sim.IHasProfile;
import sociology.sociocon.Sociocat;
import sociology.sociocon.Sociocon;
import sociology.sociocon.Socioprop;

public interface IProfile {

	public String getName();

	public IHasProfile getOwner();

	public void setOwner(IHasProfile owner);

	public boolean hasSociocon(Sociocon con);

	public boolean hasSociocat(Sociocat cat, CulturalContext context);

	/**
	 * Changes the value of the given property and returns the old one
	 * 
	 * @param <T>
	 * @param prop
	 * @return
	 */
	public <T> T setValue(Socioprop<T> prop, T val);

	/**
	 * Returns the value of the given property as stored in the profile
	 * 
	 * @param <T>
	 * @param prop
	 * @return
	 */
	public <T> T getValue(Socioprop<T> prop, CulturalContext context);

	public Sociocon getSociocon(Sociocat cat, String name);

	public void addSociocon(Sociocon con);

	public void removeSociocon(Sociocon con);

	public String profileReport();

	/**
	 * Can be null; used so that resolvedprofiles can be accessed easily
	 * 
	 * @return
	 */
	public Profile getActualProfile();

	public boolean isTypeProfile();

	public TypeProfile getTypeProfile();

	public boolean hasValue(Socioprop<?> checker, CulturalContext context);

	/**
	 * returns all sociocons for the culture
	 * 
	 * @param cul
	 * @return
	 */
	public Collection<Sociocon> getSocioconsFor(Culture cul);

	public boolean hasInfo(IKnowledgeType<?> info, CulturalContext ctxt);

	public <T> T getInfo(IKnowledgeType<T> info, CulturalContext ctxt);

	public default boolean isEvent() {
		return this.getOwner() instanceof IEvent;
	}
}
