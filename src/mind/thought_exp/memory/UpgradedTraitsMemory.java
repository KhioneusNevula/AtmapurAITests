package mind.thought_exp.memory;

import java.util.Map;
import java.util.TreeMap;
import java.util.UUID;

import actor.IComponentPart;
import actor.IComponentType;
import mind.concepts.type.IMeme;
import mind.concepts.type.Profile;
import mind.concepts.type.SenseProperty;
import mind.concepts.type.SenseProperty.IColor;
import mind.concepts.type.SenseProperty.IShape;
import mind.concepts.type.SenseProperty.ISmell;
import mind.concepts.type.SenseProperty.ITexture;
import mind.concepts.type.SenseProperty.UniqueProperty;

/**
 *
 * @author borah
 *
 * @param <T>this is used to indicate the type of the "actor" properties --
 *                whether stored as profiles or as actors or whatever
 */
public class UpgradedTraitsMemory implements IMeme {

	private String name;
	private Profile profile;
	private Traits generalTraits = new Traits();
	private Map<IComponentPart, Traits> specificTraits = Map.of();
	private Map<IComponentType, Traits> traits = Map.of();
	private UUID recognize;
	private boolean manyFaces = false;

	public UpgradedTraitsMemory(Profile of) {
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

	@Override
	public IMemeType getMemeType() {
		return MemeType.TRAITS;
	}

	/**
	 * peruses the given traits memory to update this entity's own memory with new
	 * info
	 * 
	 * @param prof
	 */
	public void acceptProperties(UpgradedTraitsMemory prof) {
		UpgradedTraitsMemory.Traits genT = prof.generalTraits;
		this.generalTraits.learnTraits(genT);
		for (Map.Entry<IComponentType, UpgradedTraitsMemory.Traits> entry : prof.traits.entrySet()) {
			this.getOrInitTraits(entry.getKey()).learnTraits(entry.getValue());
		}
		for (Map.Entry<IComponentPart, UpgradedTraitsMemory.Traits> entry : prof.specificTraits.entrySet()) {
			this.getOrInitTraits(entry.getKey()).learnTraits(entry.getValue());
		}
	}

	public Traits getOrInitTraits(IComponentType c) {
		Traits t;
		if (traits.isEmpty()) {
			traits = new TreeMap<>();
			t = new Traits();
			traits.put(c, t);
		} else {
			t = traits.get(c);
			if (t == null) {
				t = new Traits();
				traits.put(c, t);
			}
		}
		return t;
	}

	public Traits getOrInitTraits(IComponentPart c) {
		Traits t;
		if (specificTraits.isEmpty()) {
			specificTraits = new TreeMap<>();
			t = new Traits();
			specificTraits.put(c, t);
		} else {
			t = specificTraits.get(c);
			if (t == null) {
				t = new Traits();
				specificTraits.put(c, t);
			}
		}
		return t;
	}

	public Traits getGeneralTraits() {
		return generalTraits;
	}

	public Traits getTraits(IComponentPart c) {
		return specificTraits.get(c);
	}

	public Traits getTraits(IComponentType t) {
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
	public class Traits {

		private TreeMap<SenseProperty<?>, Object> senseProperties;

		public void learnTraits(Traits traits) {
			if (traits.senseProperties != null) {
				(this.senseProperties == null ? senseProperties = new TreeMap<>() : senseProperties)
						.putAll(traits.senseProperties);
			}
		}

		public <O> void learnTrait(SenseProperty<O> trait, Object value) {

			if (!trait.getType().isAssignableFrom(value.getClass()))
				throw new RuntimeException();
			if (trait instanceof UniqueProperty)
				UpgradedTraitsMemory.this.recognizeAs((UUID) value);

			(senseProperties == null ? senseProperties = new TreeMap<>() : senseProperties).put(trait, value);
		}

		/**
		 * 
		 * @param <T>
		 * @param trait
		 * @return
		 */
		public <TE> TE getTrait(SenseProperty<TE> trait) {
			return senseProperties == null ? null : (TE) senseProperties.get(trait);
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
			builder.append("}");
			return builder.toString();
		}
	}
}