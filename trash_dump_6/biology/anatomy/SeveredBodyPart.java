package biology.anatomy;

import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

import com.google.common.collect.HashBasedTable;

import actor.Actor;
import actor.construction.physical.IPartAbility;
import actor.types.SeveredBodyPartActor;
import biology.sensing.ISense;
import civilization_and_minds.social.concepts.profile.Profile;
import civilization_and_minds.social.concepts.profile.ProfileType;

/**
 * A severed body part.
 * 
 * @author borah
 *
 */
public class SeveredBodyPart extends AbstractBody {

	/**
	 * Constructor to make a body representation of a severed body part. This class
	 * will prune connections to the old body on its own
	 * 
	 * @param owner
	 * @param radius
	 * @param mass
	 */
	public SeveredBodyPart(BodyPart rootPart, AbstractBody frombody, int radius, float mass) {
		super(null, radius, mass);
		this.rootPart = rootPart;
		this.rootID = rootPart.id;
		this.species = SeveredBodyPartActor.SEVERED_TYPE;
		this.profile = new Profile(rootID, ProfileType.ITEM);
		this.senses = new HashSet<>();
		this.buildBody(frombody);
	}

	@Override
	public Stream<? extends ISense> getSenses() {
		return this.senses.stream();
	}

	public BodyPart getRootPart() {
		return this.rootPart;
	}

	public SeveredBodyPart setOwner(Actor owner) {
		this.owner = owner;
		return this;
	}

	@Override
	public boolean isDead() {
		return true;
	}

	@Override
	public boolean hasSoul() {
		return false;
	}

	@Override
	public boolean completelyDestroyed() {
		return super.completelyDestroyed();
	}

	public void buildBody(AbstractBody fromBody) {
		this.bodyParts = HashBasedTable.create();
		this.noParentParts = new HashMap<>(Map.of(rootPart.id, rootPart));
		this.bloodTypes = new HashMap<>();
		this.tissueTypes = new HashMap<>();
		this.outermostParts = new HashMap<>();
		this.built = true;
		if (this.rootPart.parent != null) {
			this.rootPart.parent.removeChild(rootPart);
			rootPart.parent = null;
		}
		if (rootPart.getChildParts().isEmpty()) {
			System.out.println(rootPart + " has no children");
			this.onlyPart = rootPart;
		} else {
			System.out.println(rootPart + " has children:" + rootPart.getChildParts().values());
		}
		this.recursivelyRegisterParts(rootPart);
		this.recursivelyPruneConnections(rootPart, fromBody);
	}

	private void recursivelyRegisterParts(BodyPart root) {
		root.getMaterials().keySet().forEach((type) -> {
			this.tissueTypes.put(type.getName(), type);
			if (type.isLifeEssence())
				this.bloodTypes.put(type.getName(), type);
		});
		this.bodyParts.put(root.type.toString(), root.id, root);
		this.senses.addAll(root.getSenses());
		if (!root.isGone() && !root.type.isHole())
			this.intactParts++;

		for (BodyPart child : root.getChildParts().values()) {
			this.recursivelyRegisterParts(child);
		}
	}

	private void recursivelyPruneConnections(BodyPart root, AbstractBody ogbod) {
		if (ogbod.bodyParts.remove(root.type.getName(), root.id) != null) {
			if (!root.type.isHole()) {
				ogbod.intactParts -= 1;
			}
		}
		if (ogbod.outermostParts != null)
			ogbod.outermostParts.remove(root.id);
		if (ogbod.noParentParts != null)
			ogbod.noParentParts.remove(root.id);
		if (ogbod.partsByAbility != null) {
			for (IPartAbility abi : root.type.abilities()) {
				ogbod.partsByAbility.remove(abi, root);
			}
		}
		List<BodyPart> rems = new LinkedList<>();
		for (BodyPart surro : root.getSurroundeds().values()) {
			if (this.bodyParts.get(surro.type.getName(), surro.id) != surro) {
				rems.add(surro);
			}
		}
		for (BodyPart part : rems) {
			root.removeSurrounded(part);
			part.setSurrounding(root.getSurrounding()); // set surrounding part of surrounded part to the surrounding
														// part of this part, since it is removed
		}
		while (root.getSurrounding() != null && this.bodyParts.get(root.getSurrounding().type.getName(),
				root.getSurrounding().getId()) != root.getSurrounding()) {
			root.getSurrounding().removeSurrounded(root);
			root.setSurrounding(root.getSurrounding().getSurrounding());

		}
		if (root.getSurrounding() == null) {
			this.outermostParts.put(root.id, root);
		}

		for (BodyPart child : root.getChildParts().values()) {
			this.recursivelyPruneConnections(child, ogbod);
		}
	}

}
