package mind;

import java.util.Map;
import java.util.Set;
import java.util.UUID;

import mind.action.ActionType;
import mind.action.IActionType;
import mind.concepts.type.BasicProperties;
import mind.concepts.type.Profile;
import mind.concepts.type.Property;
import mind.memory.AbstractKnowledgeEntity;
import sim.Location;

/**
 * mainly just a storage object for the cultural properties of a group
 * 
 * @author borah
 *
 */
public class Culture extends AbstractKnowledgeEntity implements Comparable<Culture> {

	private Map<Profile, Location> locationKnowledge;
	private boolean isStatic;
	private String groupName;

	/**
	 * TODO will delete later when better systems in place
	 */
	public static final Set<IActionType<?>> USUAL_ACTIONS = Set.of(ActionType.EAT, ActionType.WALK, ActionType.SLEEP,
			ActionType.PICK_UP, ActionType.WANDER);

	public Culture(UUID id, String type, String groupName) {
		super(id, type);
		this.groupName = groupName;
	}

	/**
	 * Initializes associations of properties common to every civilization,
	 * 
	 * @return
	 */
	public Culture usualInit() {
		for (Property property : BasicProperties.getAll()) {
			this.learnProperty(property, BasicProperties.genAssociations(property));
		}
		for (IActionType<?> atype : USUAL_ACTIONS) {
			this.addDoableAction(atype);
		}
		return this;
	}

	/**
	 * makes the culture unchanging
	 * 
	 * @return
	 */
	public Culture makeStatic() {
		this.isStatic = true;
		return this;
	}

	/**
	 * Makes the culture no longer unchanging
	 * 
	 * @return
	 */
	public Culture makeDynamic() {
		this.isStatic = false;
		return this;
	}

	/**
	 * If this culture undergoes no changes over time
	 * 
	 * @return
	 */
	public boolean isStatic() {
		return isStatic;
	}

	@Override
	public Location getLocation(Profile prof) {
		if (this.locationKnowledge == null)
			return null;
		return locationKnowledge.get(prof);
	}

	@Override
	public int compareTo(Culture o) {
		return this.self.compareTo(o.self);
	}

	public String report() {
		StringBuilder builder = new StringBuilder("Culture-" + this.groupName + "{");
		if (this.locationKnowledge != null)
			builder.append("\n\tlocations:" + this.locationKnowledge);

		if (this.propertyConcepts != null)
			builder.append("\n\tconcepts:" + this.propertyConcepts);
		if (this.needs != null)
			builder.append("\n\tneeds:" + this.needs.values());
		if (this.goals != null)
			builder.append("\n\tgoals:" + this.goals);
		if (this.doableActions != null)
			builder.append("\n\tdoableactions:" + this.doableActions);
		if (this.infoTable != null)
			builder.append("\n\tinfo:" + this.infoTable);
		// TODO append profile table info
		return builder.append("\n  }").toString();
	}

	@Override
	public String toString() {
		return "culture_" + this.groupName + "_" + this.getSelfProfile().getUUID().toString().substring(0, 4);
	}

	@Override
	public void prune(int passes) {
		// TODO pruning
	}

}
