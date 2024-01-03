package mind.memory;

import java.lang.ref.WeakReference;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.UUID;

import com.google.common.collect.Multimap;
import com.google.common.collect.MultimapBuilder;

import actor.Actor;
import mind.concepts.type.Profile;
import mind.concepts.type.SenseProperty;
import sim.Location;

/**
 * This is the memory that stores what is sensed; can double as both a profile
 * recognition memory and a general memory for senses
 * 
 * @author borah
 * @param <T> should be type Actor if this sensememory remembers actors, or type
 *            profile if it remembers profiles
 *
 */
public class SenseMemory implements IRecordable {

	private Set<Profile> sensedProfiles;
	private Map<Profile, WeakReference<Actor>> profilesToActors;
	private Map<Profile, Location> sensedLocations = new TreeMap<>();
	/**
	 * Sensed traits for actors
	 */
	private Map<Profile, TraitsMemory> sensedTraits;

	/**
	 * Things that are randomly sensed -- random sounds, etc
	 */
	private Multimap<SenseProperty<?>, Object> randomlySensed;

	/**
	 * Max amount of profiles that are to be remembered <br>
	 * TODO implement an Interest system to indicate what is and isn't remembered.
	 * <br>
	 * TODO also figure out how to remmeber things en masse (? Constructs?) e.g. a
	 * copse of flowers rather than individual ones
	 */
	private int max;

	/**
	 * 
	 * @param max
	 * @param storeActors if this memory stores the actual actor along with its
	 *                    traits, or just profiles
	 */
	public SenseMemory(int max, boolean storeActors) {
		this.max = max;
		if (storeActors)
			this.profilesToActors = new TreeMap<>();
	}

	/**
	 * clears everything in this memory
	 */
	public void clearAll() {
		sensedProfiles = null;
		sensedLocations.clear();
		sensedTraits = null;
		randomlySensed = null;
		this.profilesToActors.clear();
	}

	/**
	 * If this memory is remembering a maximum amount of recently seen
	 * beings/creatures/etc
	 * 
	 * @return
	 */
	public boolean maxedOut() {
		return this.sensedProfiles == null ? false : sensedProfiles.size() > max;
	}

	public boolean isSensed(UUID id) {
		return sensedProfiles == null ? false : sensedProfiles.contains(new Profile(id));
	}

	public Profile senseActor(Actor actor, boolean overrideMax) {
		if (profilesToActors == null)
			throw new UnsupportedOperationException();
		Profile prof = this.addProfile(actor.getUUID(), actor.getName().toLowerCase(), overrideMax);
		this.profilesToActors.put(prof, new WeakReference<>(actor));
		return prof;
	}

	public Profile addProfile(UUID forID, String type, boolean overrideMax) {
		if (!overrideMax && maxedOut())
			throw new RuntimeException("" + forID + type);
		Profile p = new Profile(forID, type);
		findProfilesSet().add(p);
		return p;
	}

	public TraitsMemory obtainTraitsForProfile(Profile prof, boolean overrideMax) {

		TraitsMemory a = this.getTraits(prof);
		if (a == null) {
			if (!overrideMax && maxedOut())
				return null;
			a = new TraitsMemory(prof);
			findProfilesSet().add(prof);
			(this.sensedTraits == null ? sensedTraits = new TreeMap<>() : sensedTraits).put(prof, a);
		}
		return a;
	}

	private Set<Profile> findProfilesSet() {
		return (this.sensedProfiles == null ? sensedProfiles = new TreeSet<>() : sensedProfiles);
	}

	/**
	 * Gets the actor for the given profile
	 * 
	 * @param prof
	 * @return
	 */
	public Actor getActorFor(Profile prof) {
		if (this.profilesToActors == null)
			throw new UnsupportedOperationException();
		WeakReference<Actor> ref = this.profilesToActors.get(prof);
		return ref == null ? null : ref.get();
	}

	public Collection<Profile> getAllSensedProfiles() {
		return this.sensedProfiles == null ? Set.of() : this.sensedProfiles;
	}

	public void forget(Profile prof) {
		if (this.sensedProfiles == null)
			return;
		if (this.sensedProfiles.remove(prof)) {
			if (sensedTraits != null)
				this.sensedTraits.remove(prof);
			this.sensedLocations.remove(prof);
			if (sensedProfiles != null && sensedProfiles.isEmpty())
				sensedProfiles = null;
			if (sensedTraits != null && sensedTraits.isEmpty())
				sensedTraits = null;
			if (profilesToActors != null) {
				this.profilesToActors.remove(prof);
			}
		}
	}

	/**
	 * Call this frequently on sense memory to clean out actors that no longer exist
	 * <br>
	 * for now, just deletes them all
	 */
	public void cleanDeadActors() {
		if (this.profilesToActors == null)
			throw new UnsupportedOperationException();
		Iterator<Entry<Profile, WeakReference<Actor>>> actors = Set.copyOf(this.profilesToActors.entrySet()).iterator();
		while (actors.hasNext()) {
			Entry<Profile, WeakReference<Actor>> next = actors.next();
			if (next.getValue().get() == null || next.getValue().get().isRemoved()) {
				this.forget(next.getKey());
			}
		}
	}

	/**
	 * This procedure begins transferring the contents of the other memory to this
	 * one, deleting them from the other memory and deciding whether to forget them
	 * or remember them. Passes indicates how many memory items will be transferred
	 * per "pass". occurs between "Sense" memory and "Recognition" memory.
	 * Non-transferred items are eventually cleared alongwith the other memory
	 * (during sleep or whatever process is used to perform clearing)
	 * 
	 * @param otherMemory
	 * @return false if there was nothing to transfer
	 */
	public boolean processMemory(Memory owner, SenseMemory otherMemory, int passes) {
		if (passes <= 0)
			throw new IllegalArgumentException("" + passes);
		for (int i = 0; i < passes; i++) {
			if (otherMemory.sensedProfiles == null)
				return false;
			Optional<Profile> op = otherMemory.sensedProfiles.stream().filter((a) -> owner.isSignificant(a)).findAny();
			if (!op.isPresent()) {
				return false;
			}
			this.findProfilesSet().add(op.get());
			TraitsMemory otherTM = otherMemory.getTraits(op.get());
			if (otherTM != null) {
				TraitsMemory mem = (TraitsMemory) this.obtainTraitsForProfile(op.get(), true);
				mem.acceptProperties(otherTM);
			}
			Location loc = otherMemory.getSensedLocation(op.get());
			if (loc != null)
				owner.learnLocation(op.get(), loc);
			owner.recognizeProfile(op.get());
			otherMemory.forget(op.get());
		}
		return true;
	}

	/**
	 * Get things that were sensed randomly in the environment
	 * 
	 * @param <T>
	 * @param p
	 * @return
	 */
	public <TE> Collection<TE> getSensed(SenseProperty<TE> p) {
		return (Collection<TE>) (randomlySensed == null ? Set.of() : randomlySensed.get(p));
	}

	/**
	 * randomly sense this thing in the environment
	 * 
	 * @param <T>
	 * @param s
	 * @param v
	 */
	public void sense(SenseProperty<?> s, Object v) {
		if (!s.getType().isAssignableFrom(v.getClass()))
			throw new IllegalArgumentException(s + " vs " + v.getClass());
		(randomlySensed == null ? randomlySensed = MultimapBuilder.treeKeys().arrayListValues().build()
				: randomlySensed).put(s, v);
	}

	public void learnLocation(Profile forP, Location at) {
		if (this.profilesToActors == null || !this.isSensed(forP.getUUID()))
			throw new IllegalArgumentException("Please recognize this profile first " + forP);
		sensedLocations.put(forP, at);
	}

	public Location getSensedLocation(Profile location) {
		return sensedLocations.get(location);
	}

	public TraitsMemory getTraits(Profile forP) {
		return sensedTraits == null ? null : sensedTraits.get(forP);
	}

	public String report() {
		StringBuilder str = new StringBuilder();
		str.append("SenseMemory::{\n\t-traits:");
		str.append(("" + sensedTraits).toString().length() > 50 ? ("" + sensedTraits).substring(0, 50) + "..."
				: ("" + sensedTraits));
		str.append(";;\n\t-locations:");
		str.append(this.sensedLocations);
		str.append(";;\n\t-actors:");
		str.append(this.profilesToActors);
		str.append("}");
		return str.toString();
	}

}
