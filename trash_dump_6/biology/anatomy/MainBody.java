package biology.anatomy;

import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.TreeMap;
import java.util.UUID;
import java.util.stream.Stream;

import com.google.common.collect.HashBasedTable;

import actor.Actor;
import actor.construction.physical.IPartAbility;
import biology.sensing.ISense;
import civilization_and_minds.social.concepts.profile.Profile;
import civilization_and_minds.social.concepts.profile.ProfileType;
import metaphysical.ISpiritObject;
import metaphysical.soul.ISoul;
import metaphysical.soul.generator.ISoulGenerator;

public class MainBody extends AbstractBody {

	protected ISoul soulReference;

	public MainBody(Actor owner, ISpecies template) {
		super(owner, template);
		this.profile = new Profile(owner.getUUID(), ProfileType.INDIVIDUAL);
		this.senses = new HashSet<>();
	}

	public void buildBody() {
		if (species != null) {
			System.out.println("Creating body from template of species:" + species.name());
			this.addTissueLayers(species.tissueTypes().values());
			this.addBodyPartTypes(species.partTypes().values());

		}
		System.out.print("Starting to build body for " + owner + ".");
		initializeBody();
		System.out.print(".");
		initializeBodyConnections();
		System.out.println(". Body built for " + owner);
		built = true;

	}

	protected void initializeBody() {
		for (Map.Entry<String, IBodyPartType> entry : partTypes.entrySet()) {
			if (bodyParts == null)
				bodyParts = HashBasedTable.<String, UUID, BodyPart>create();
			String key = entry.getKey();
			IBodyPartType partType = entry.getValue();
			int count = partType.count();
			String parentPartStr = partType.getParent();
			if (parentPartStr != null) {
				while (parentPartStr != null) {
					IBodyPartType parentPart = partTypes.get(parentPartStr);
					if (parentPart == null)
						throw new IllegalStateException(
								"no part in body called " + parentPartStr + "; referenced as parent by " + key);
					count *= parentPart.count();
					parentPartStr = parentPart.getParent();
				}
			}
			for (int i = 0; i < count; i++) {
				BodyPart part = new BodyPart(partType, tissueTypes);
				if (!part.type.isHole())
					intactParts++;
				bodyParts.put(partType.getName(), part.id, part);
				for (IPartAbility ab : partType.abilities()) {
					this.partsByAbility.put(ab, part);
					if (ab instanceof ISense is) {
						this.senses.add(is);
					}
				}
				if (partType.isRoot()) {
					if (rootID != null)
						throw new RuntimeException("Existing root " + rootPart.type.getName() + " " + rootID
								+ "; trying to insert new root " + part.type.getName() + " " + part.id);

				}
				if (partType.getParent() == null) {
					if (noParentParts == null)
						noParentParts = new TreeMap<>();
					noParentParts.put(part.id, part);
				}
			}
		}
		if (this.bodyParts.size() == 1) {
			this.onlyPart = this.bodyParts.values().iterator().next();
		}
	}

	protected void initializeBodyConnections() {
		if (bodyParts == null)
			throw new RuntimeException("first step incomplete");
		for (String type : bodyParts.rowKeySet()) {
			Collection<BodyPart> parts = bodyParts.row(type).values();
			IBodyPartType partType = partTypes.get(type);
			String parentPart = partType.getParent();
			String surrounding = partType.getSurroundingPart();
			if (parentPart != null) {
				Collection<BodyPart> parentParts = bodyParts.row(parentPart).values();
				Iterator<BodyPart> parentIter = parentParts.iterator();
				BodyPart parent = parentIter.next();
				int count = 0;
				for (BodyPart part : parts) {
					count++;
					if (count <= partType.count()) {
						parent.addChild(part);
						part.setParent(parent);
					} else {
						count = 0;
						if (parentIter.hasNext()) {
							parent = parentIter.next();
						} else {
							break;
						}
					}
				}
			}
			if (surrounding != null) {/*
										 * System.out.println("surrounded " + type + " by " + surrounding);
										 */
				Collection<BodyPart> surroundParts = bodyParts.row(surrounding).values();
				IBodyPartType surroundingType = partTypes.get(surrounding);
				for (BodyPart part : parts) {
					BodyPart surrounder = null;
					BodyPart currentChecked = part.parent;
					while (currentChecked != null) {
						if (currentChecked.type.equals(surroundingType)) {
							surrounder = currentChecked;
							break;
						} else {
							for (BodyPart child : currentChecked.getSurroundeds().get(surrounding)) {
								surrounder = child;
								break;
							}
						}
						currentChecked = currentChecked.parent;
					}

					if (surrounder == null) {
						surrounder = surroundParts.iterator().next();
					}
					part.setSurrounding(surrounder);
					surrounder.addSurrounded(part);
				}
			}
		}

		for (String type : bodyParts.rowKeySet()) {
			Collection<BodyPart> parts = bodyParts.row(type).values();
			IBodyPartType partType = partTypes.get(type);
			String parentPart = partType.getParent();
			String surrounding = partType.getSurroundingPart();
			if (surrounding == null) {
				if (parentPart != null) {
					for (BodyPart part : parts) {
						BodyPart parent = part.parent;
						boolean surrounded = false;
						while (parent != null) {
							if (parent.type.getSurroundingPart() != null) {
								surrounded = true;/*
													 * System.out.println("surrounded " + type + " at " +
													 * parent.type.getName() + " by " +
													 * parent.type.getSurroundingPart());
													 */
								break;
							}
							parent = parent.parent;
						}
						if (!surrounded) {
							(outermostParts == null ? outermostParts = new TreeMap<>() : outermostParts).put(part.id,
									part);/*
											 * System.out.println("unsurrounded " + type);
											 */
						}

					}
				} else {
					for (BodyPart part : parts) {
						(outermostParts == null ? outermostParts = new TreeMap<>() : outermostParts).put(part.id, part);
						/* System.out.println("unsurrounded " + type); */

					}
				}
			}

		}

	}

	@Override
	public Stream<? extends ISense> getSenses() {
		return senses.stream();
	}

	@Override
	public boolean isDead() {
		return this.soulReference == null;
	}

	/**
	 * Gets the main soul of this being, or null if it lacks a soul
	 * 
	 * @return
	 */
	@Override
	public ISoul getSoulReference() {
		return soulReference;
	}

	@Override
	public void onGiveFirstSoul(ISoul soul, ISoulGenerator soulgen) {
		if (this.soulReference == null) {
			this.soulReference = soul;
			System.out.println(this + " received soul " + this.soulReference);
		} else {
			System.out.println("Clash with existing soul " + this.soulReference);
		}
	}

	@Override
	public boolean hasSoul() {
		return this.soulReference != null;
	}

	@Override
	public boolean containsSpirit(ISpiritObject spir) {
		if (spir == soulReference)
			return true;
		return super.containsSpirit(spir);
	}

	@Override
	public void removeSpirit(ISpiritObject spirit) {
		super.removeSpirit(spirit);

		if (soulReference == spirit)
			soulReference = null;
	}

	@Override
	public String report() {
		return super.report() + "\n\t soul:" + this.soulReference;
	}

}
