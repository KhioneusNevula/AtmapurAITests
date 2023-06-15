package mind.memory;

import java.util.Collection;
import java.util.Iterator;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.TreeMap;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.google.common.collect.Multimap;
import com.google.common.collect.MultimapBuilder;

import actor.Actor;
import actor.IComponentPart;
import actor.IComponentType;
import mind.concepts.type.IConcept;
import mind.concepts.type.Profile;
import mind.concepts.type.SenseProperty;
import mind.concepts.type.SenseProperty.ActorReferentProperty;
import mind.concepts.type.SenseProperty.IColor;
import mind.concepts.type.SenseProperty.IShape;
import mind.concepts.type.SenseProperty.ISmell;
import mind.concepts.type.SenseProperty.ITexture;
import mind.concepts.type.SenseProperty.UniqueProperty;
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
public class SenseMemory<T> {

	private Map<UUID, Profile> sensedProfiles;
	private Map<Profile, Actor> profilesToActors;
	private Map<Profile, Location> sensedLocations;
	/**
	 * Sensed traits for actors
	 */
	private Map<Profile, TraitsMemory<T>> sensedTraits;

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
	 *                    traits
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
		sensedLocations = null;
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
		return sensedProfiles == null ? false : sensedProfiles.containsKey(id);
	}

	public Profile senseActor(Actor actor, boolean overrideMax) {
		if (profilesToActors == null)
			throw new UnsupportedOperationException();
		Profile prof = this.addProfile(actor.getUUID(), "unit", overrideMax);
		this.profilesToActors.put(prof, actor);
		return prof;
	}

	public Profile addProfile(UUID forID, String type, boolean overrideMax) {
		if (!overrideMax && maxedOut())
			throw new RuntimeException("" + forID + type);
		return (this.sensedProfiles == null ? sensedProfiles = new TreeMap<>() : sensedProfiles).computeIfAbsent(forID,
				(u) -> new Profile(u, type));
	}

	public TraitsMemory<T> obtainTraitsForProfile(Profile prof, boolean overrideMax) {

		TraitsMemory<T> a = this.getTraits(prof);
		if (a == null) {
			if (!overrideMax && maxedOut())
				return null;
			a = new TraitsMemory<>(prof);
			(this.sensedProfiles == null ? sensedProfiles = new TreeMap<>() : sensedProfiles).put(prof.getUUID(), prof);
			(this.sensedTraits == null ? sensedTraits = new TreeMap<>() : sensedTraits).put(prof, a);
		}
		return a;
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
		return this.profilesToActors.get(prof);
	}

	public Collection<Profile> getAllSensedProfiles() {
		return this.sensedProfiles == null ? Set.of() : this.sensedProfiles.values();
	}

	public void forget(Profile prof) {
		if (this.sensedProfiles == null)
			return;
		if (this.sensedProfiles.remove(prof.getUUID()) != null) {
			this.sensedTraits.remove(prof);
			this.sensedLocations.remove(prof);
			if (sensedProfiles.isEmpty())
				sensedProfiles = null;
			if (sensedTraits.isEmpty())
				sensedTraits = null;
			if (sensedLocations.isEmpty())
				sensedLocations = null;
			this.profilesToActors.remove(prof);
		}
	}

	/**
	 * Call this frequently on sense memory to clean out actors that no longer exist
	 */
	public void cleanDeadActors() {
		if (this.profilesToActors == null)
			throw new UnsupportedOperationException();
		Iterator<Actor> actors = this.profilesToActors.values().iterator();
		while (actors.hasNext()) {
			if (actors.next().isRemoved())
				actors.remove();
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
	public boolean processMemory(Memory owner, SenseMemory<Actor> otherMemory, int passes) {
		if (passes <= 0)
			throw new IllegalArgumentException("" + passes);
		for (int i = 0; i < passes; i++) {
			if (otherMemory.sensedProfiles == null)
				return false;
			Optional<Map.Entry<UUID, Profile>> op = otherMemory.sensedProfiles.entrySet().stream()
					.filter((a) -> owner.isSignificant(a.getValue())).findAny();
			if (!op.isPresent()) {
				return false;
			}
			(this.sensedProfiles == null ? sensedProfiles = new TreeMap<>() : sensedProfiles)
					.computeIfAbsent(op.get().getKey(), (a) -> op.get().getValue());
			TraitsMemory<?> otherTM = otherMemory.getTraits(op.get().getValue());
			if (otherTM != null) {
				TraitsMemory<Profile> otherTMP = otherTM.convertActorProperties();
				TraitsMemory<Profile> mem = (TraitsMemory<Profile>) this.obtainTraitsForProfile(op.get().getValue(),
						true);
				mem.acceptProperties(otherTMP);
			}
			Location loc = otherMemory.getSensedLocation(op.get().getValue());
			this.learnLocation(op.get().getValue(), loc);
			owner.recognizeProfile(op.get().getValue());
			otherMemory.forget(op.get().getValue());
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
		if (!this.isSensed(forP.getUUID()))
			throw new IllegalArgumentException("Please recognize this profile first " + forP);
		(sensedLocations == null ? sensedLocations = new TreeMap<>() : sensedLocations).put(forP, at);
	}

	public Location getSensedLocation(Profile location) {
		return sensedLocations == null ? null : sensedLocations.get(location);
	}

	public TraitsMemory<T> getTraits(Profile forP) {
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

	/**
	 *
	 * @author borah
	 *
	 * @param <T>this is used to indicate the type of the "actor" properties --
	 *                whether stored as profiles or as actors or whatever
	 */
	public static class TraitsMemory<AP> implements IConcept {

		private String name;
		private Profile profile;
		private Traits<AP> generalTraits = new Traits<>();
		private Map<IComponentPart, Traits<AP>> specificTraits = Map.of();
		private Map<IComponentType, Traits<AP>> traits = Map.of();
		private UUID recognize;
		private boolean manyFaces = false;

		public TraitsMemory(Profile of) {
			name = "traits_" + of.getUniqueName();
			this.profile = of;
		}

		@Override
		public String getUniqueName() {
			return name;
		}

		public Profile getProfile() {
			return profile;
		}

		public TraitsMemory<Profile> convertActorProperties() {
			TraitsMemory<Profile> newMem = new TraitsMemory<>(this.profile);
			newMem.manyFaces = this.manyFaces;
			newMem.recognize = this.recognize;
			newMem.name = this.name;
			newMem.generalTraits = this.generalTraits.convertActorProperties(newMem);
			if (!specificTraits.isEmpty()) {
				newMem.specificTraits = new TreeMap<>();
				newMem.specificTraits.putAll(this.specificTraits.entrySet().stream()
						.map((e) -> Map.entry(e.getKey(), e.getValue().convertActorProperties(newMem)))
						.collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue)));
			}
			if (!traits.isEmpty()) {
				newMem.traits = new TreeMap<>();
				newMem.traits.putAll(this.traits.entrySet().stream()
						.map((e) -> Map.entry(e.getKey(), e.getValue().convertActorProperties(newMem)))
						.collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue)));

			}
			return newMem;
		}

		/**
		 * peruses the given traits memory to update this entity's own memory with new
		 * info
		 * 
		 * @param prof
		 */
		public void acceptProperties(TraitsMemory<AP> prof) {
			TraitsMemory<AP>.Traits<AP> genT = prof.generalTraits;
			this.generalTraits.learnTraits(genT);
			for (Map.Entry<IComponentType, TraitsMemory<AP>.Traits<AP>> entry : prof.traits.entrySet()) {
				this.getOrInitTraits(entry.getKey()).learnTraits(entry.getValue());
			}
			for (Map.Entry<IComponentPart, TraitsMemory<AP>.Traits<AP>> entry : prof.specificTraits.entrySet()) {
				this.getOrInitTraits(entry.getKey()).learnTraits(entry.getValue());
			}
		}

		public Traits<AP> getOrInitTraits(IComponentType c) {
			Traits<AP> t;
			if (traits.isEmpty()) {
				traits = new TreeMap<>();
				t = new Traits<>();
				traits.put(c, t);
			} else {
				t = traits.get(c);
				if (t == null) {
					t = new Traits<>();
					traits.put(c, t);
				}
			}
			return t;
		}

		public Traits<AP> getOrInitTraits(IComponentPart c) {
			Traits<AP> t;
			if (specificTraits.isEmpty()) {
				specificTraits = new TreeMap<>();
				t = new Traits<>();
				specificTraits.put(c, t);
			} else {
				t = specificTraits.get(c);
				if (t == null) {
					t = new Traits<>();
					specificTraits.put(c, t);
				}
			}
			return t;
		}

		public Traits<AP> getGeneralTraits() {
			return generalTraits;
		}

		public Traits<AP> getTraits(IComponentPart c) {
			return specificTraits.get(c);
		}

		public Traits<AP> getTraits(IComponentType t) {
			return traits.get(t);
		}

		private void recognizeAs(UUID id) {
			if (this.recognize == null) {
				this.recognize = id;
			} else {
				if (!id.equals(this.recognize)) {
					manyFaces = true;
					recognize = null;
				}
			}
		}

		/**
		 * If this entity, for whatever reason, presents as multiple recognizable
		 * entities and is thusly unrecognizable
		 */
		public boolean ofManyFaces() {
			return manyFaces;
		}

		/**
		 * If this entity is recognizable as a singel entity
		 * 
		 * @return
		 */
		public boolean isRecognizable() {
			return recognize != null;
		}

		/**
		 * what id this entity is recognized as
		 * 
		 * @return
		 */
		public UUID recognizedAs() {
			return recognize;
		}

		@Override
		public String toString() {
			StringBuilder str = new StringBuilder("{");
			if (!this.generalTraits.toString().equals("{}")) {
				str.append("-general:[");
				str.append(generalTraits).append("];");
			}
			if (this.traits != null && !this.traits.isEmpty()) {
				str.append(" -typed:[");
				str.append(traits).append("];");
			}
			if (this.specificTraits != null && this.specificTraits.isEmpty()) {
				str.append("\n\t -specific:[");
				str.append(specificTraits).append("]");
			}
			if (this.isRecognizable()) {
				str.append(",+recognizable");
			}
			if (this.manyFaces) {
				str.append(",+manyFaces");
			}
			return str.append("}").toString();
		}

		/**
		 * 
		 * @author borah
		 *
		 * @param <T> this is used to indicate the type of the "actor" properties --
		 *            whether stored as profiles or as actors or whatever
		 */
		public class Traits<T> {

			private TreeMap<SenseProperty<?>, Object> senseProperties;
			private TreeMap<ActorReferentProperty, T> actorRefSenseProperties;

			public void learnTraits(Traits<T> traits) {
				if (traits.senseProperties != null) {
					(this.senseProperties == null ? senseProperties = new TreeMap<>() : senseProperties)
							.putAll(traits.senseProperties);
				}
				if (traits.actorRefSenseProperties != null) {
					(this.actorRefSenseProperties == null ? actorRefSenseProperties = new TreeMap<>()
							: actorRefSenseProperties).putAll(traits.actorRefSenseProperties);
				}
			}

			public void learnTrait(ActorReferentProperty trait, T value) {
				(actorRefSenseProperties == null ? actorRefSenseProperties = new TreeMap<>() : actorRefSenseProperties)
						.put(trait, value);
			}

			public <O> void learnTrait(SenseProperty<O> trait, Object value) {
				if (trait instanceof ActorReferentProperty) {
					learnTrait((ActorReferentProperty) trait, (T) value);
					return;
				}
				if (!trait.getType().isAssignableFrom(value.getClass()))
					throw new RuntimeException();
				if (trait instanceof UniqueProperty)
					TraitsMemory.this.recognizeAs((UUID) value);

				(senseProperties == null ? senseProperties = new TreeMap<>() : senseProperties).put(trait, value);
			}

			/**
			 * Returns a new Traits containing everything in this Traits but with all Actor
			 * properties converted to profile ones; clears all contents of this Traits as
			 * well
			 * 
			 * @return
			 */
			public TraitsMemory<Profile>.Traits<Profile> convertActorProperties(TraitsMemory<Profile> oth) {
				TraitsMemory<Profile>.Traits<Profile> newTraits = oth.new Traits<Profile>();
				newTraits.senseProperties = this.senseProperties;
				this.senseProperties = null;
				if (actorRefSenseProperties != null && !actorRefSenseProperties.isEmpty()) {
					newTraits.actorRefSenseProperties = new TreeMap<>();
					Stream<Map.Entry<ActorReferentProperty, T>> stream = actorRefSenseProperties.entrySet().stream();
					if (!(stream.findAny().get().getValue() instanceof Actor))
						throw new UnsupportedOperationException();
					newTraits.actorRefSenseProperties.putAll(stream
							.map((a) -> Map.entry(a.getKey(), new Profile(((Actor) a.getValue()).getUUID(), "unit")))
							.collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue)));
					this.actorRefSenseProperties = null;
				}
				return newTraits;
			}

			public T getTrait(ActorReferentProperty trait) {
				return actorRefSenseProperties == null ? null : actorRefSenseProperties.get(trait);
			}

			/**
			 * Do not use an actor-referent-property with this
			 * 
			 * @param <T>
			 * @param trait
			 * @return
			 */
			public <TE> TE getTrait(SenseProperty<TE> trait) {
				if (trait instanceof ActorReferentProperty)
					throw new IllegalArgumentException("use non-actor referent property");
				return senseProperties == null ? null : (TE) senseProperties.get(trait);
			}

			public T getHolding() {
				return getTrait(SenseProperty.HOLDING);
			}

			public IColor getColor() {
				return getTrait(SenseProperty.COLOR);
			}

			public IShape getShape() {
				return getTrait(SenseProperty.SHAPE);
			}

			public ITexture getTexture() {
				return getTrait(SenseProperty.TEXTURE);
			}

			public ISmell getSmell() {
				return getTrait(SenseProperty.SMELL);
			}

			public Boolean isTranslucent() {
				return getTrait(SenseProperty.TRANSLUCENCE);
			}

			@Override
			public String toString() {
				StringBuilder builder = new StringBuilder("{");
				if (senseProperties != null) {
					builder.append("non-actor:");
					builder.append(senseProperties).append(";");
				}
				if (actorRefSenseProperties != null) {
					builder.append(" actor-referent:");
					builder.append(actorRefSenseProperties);
				}
				builder.append("}");
				return builder.toString();
			}
		}
	}

}
