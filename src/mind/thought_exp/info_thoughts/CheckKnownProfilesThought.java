package mind.thought_exp.info_thoughts;

import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.function.BiPredicate;

import mind.concepts.relations.ConceptRelationType;
import mind.concepts.type.ILocationMeme;
import mind.concepts.type.IProfile;
import mind.concepts.type.Property;
import mind.goals.IGoal.Priority;
import mind.goals.ITaskGoal;
import mind.memory.IPropertyData;
import mind.thought_exp.ICanThink;
import mind.thought_exp.IThought;
import mind.thought_exp.IThoughtMemory;
import mind.thought_exp.IThoughtMemory.Interest;
import mind.thought_exp.ThoughtType;
import mind.thought_exp.memory.type.ImportantProfilesMemory;
import mind.thought_exp.type.AbstractInformationThought;

/**
 * Gets the location of a set of profiles matching the given property from
 * memory, not senses. May lose focus
 */
public class CheckKnownProfilesThought extends AbstractInformationThought<Set<IProfile>> {
	private Property property;
	private String failure;
	private boolean done;
	private BiPredicate<IProfile, IPropertyData> predicate;
	private Iterator<IProfile> profiles;
	private Set<IProfile> tested = new HashSet<>();
	private boolean onlyIfLocationKnown;
	private ITaskGoal rememberFor;

	/**
	 * 
	 * @param profile
	 * @param onlyIfLocationKnown only select profiles if their location is knonw
	 * @param rememberFor         if this is non-null, the profiles will be stored
	 *                            in a memory
	 */
	public CheckKnownProfilesThought(Property profile, BiPredicate<IProfile, IPropertyData> predicate,
			boolean onlyIfLocationKnown, ITaskGoal rememberFor) {
		this.property = profile;
		this.predicate = predicate;
		this.onlyIfLocationKnown = onlyIfLocationKnown;
	}

	/**
	 * 
	 * @param property
	 * @param onlyIfLocationKnown only select profiles if their location is known
	 */
	public CheckKnownProfilesThought(Property property, boolean onlyIfLocationKnown, ITaskGoal rememberFor) {
		this(property, (a, b) -> true, onlyIfLocationKnown, rememberFor);
	}

	@Override
	public IThoughtType getThoughtType() {
		return ThoughtType.FIND_MEMORY_INFO;
	}

	@Override
	public boolean isLightweight() {
		return true;
	}

	@Override
	public void startThinking(ICanThink mind, long worldTick) {
		information = new HashSet<>();
		profiles = mind.getMindMemory().getKnownProfiles().iterator();
	}

	@Override
	public boolean isFinished(ICanThink memory, int ticks, long worldTick) {
		return done;
	}

	@Override
	public Map<IThoughtMemory, Interest> produceMemories(ICanThink mind, int finishingTicks, long worldTicks) {
		if (rememberFor != null) {
			if (!information.isEmpty()) {
				return Map.of(new ImportantProfilesMemory(information, rememberFor), Interest.SHORT_TERM);
			}
		}
		return super.produceMemories(mind, finishingTicks, worldTicks);
	}

	@Override
	public Priority getPriority() {
		return Priority.TRIVIAL;
	}

	@Override
	public void thinkTick(ICanThink memory, int ticks, long worldTick) {
		if (profiles.hasNext()) {
			if (memory.rand().nextDouble() <= memory.loseFocusChance()) {
				if (information.isEmpty()) {
					failure = "lost focus";
				}
				done = true;
				return;
			}
			for (int i = 0; i < memory.getMaxFocusObjects() && profiles.hasNext(); i++) {
				IProfile profile = profiles.next();

				IPropertyData dat = memory.getKnowledgeBase().getPropertyData(profile, property);
				Collection<ILocationMeme> locations = memory.getKnowledgeBase().getConceptsWithRelation(profile,
						ConceptRelationType.FOUND_AT);
				ILocationMeme location = null;
				for (ILocationMeme locationa : locations) {
					location = locationa;
					break;

				}
				if (dat.isPresent() && this.predicate.test(profile, dat) && !tested.contains(profile)
						&& (onlyIfLocationKnown ? location != null : true)) {
					this.information.add(profile);
				}
			}
		} else {
			done = true;
			if (information.isEmpty()) {
				this.failure = "nothing seen or remembered";
			}
		}
	}

	@Override
	public void getInfoFromChild(ICanThink mind, IThought childThought, boolean interrupted, int ticks) {

	}

	public boolean failed() {
		return failure != null;
	}

	public String getFailure() {
		return failure == null ? "n/a" : failure;
	}

	@Override
	public String displayText() {
		return "checking known actors for " + property;
	}

	@Override
	public boolean equivalent(IThought other) {
		if (other instanceof CheckKnownProfilesThought ckat) {
			return this.property.equals(ckat.property) && this.onlyIfLocationKnown == ckat.onlyIfLocationKnown;
		}
		return this.equals(other);
	}

}
