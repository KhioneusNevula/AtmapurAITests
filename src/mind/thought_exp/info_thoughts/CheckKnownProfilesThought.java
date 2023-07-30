package mind.thought_exp.info_thoughts;

import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.function.BiPredicate;

import main.Pair;
import mind.concepts.type.ILocationMeme;
import mind.concepts.type.Profile;
import mind.concepts.type.Property;
import mind.goals.IGoal.Priority;
import mind.memory.IPropertyData;
import mind.thought_exp.ICanThink;
import mind.thought_exp.IThought;
import mind.thought_exp.ThoughtType;
import mind.thought_exp.type.AbstractInformationThought;

/**
 * Gets the location of a set of profiles matching the given property from
 * memory, not senses. May lose focus
 */
public class CheckKnownProfilesThought extends AbstractInformationThought<Set<Pair<Profile, ILocationMeme>>> {
	private Property property;
	private String failure;
	private boolean done;
	private BiPredicate<Profile, IPropertyData> predicate;
	private Iterator<Profile> profiles;
	private Set<Profile> tested = new HashSet<>();
	private boolean onlyIfLocationKnown;

	/**
	 * 
	 * @param profile
	 * @param onlyIfLocationKnown only select profiles if their location is knonw
	 */
	public CheckKnownProfilesThought(Property profile, BiPredicate<Profile, IPropertyData> predicate,
			boolean onlyIfLocationKnown) {
		this.property = profile;
		this.predicate = predicate;
		this.onlyIfLocationKnown = onlyIfLocationKnown;
	}

	/**
	 * 
	 * @param property
	 * @param onlyIfLocationKnown only select profiles if their location is known
	 */
	public CheckKnownProfilesThought(Property property, boolean onlyIfLocationKnown) {
		this(property, (a, b) -> true, onlyIfLocationKnown);
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
		information = new LinkedHashSet<>();
		profiles = mind.getMindMemory().getKnownProfiles().iterator();
	}

	@Override
	public boolean isFinished(ICanThink memory, int ticks, long worldTick) {
		return done;
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
				Profile profile = profiles.next();

				IPropertyData dat = memory.getKnowledgeBase().getProperties(profile, property);
				ILocationMeme location = memory.getKnowledgeBase().getLocation(profile);
				if (dat.isPresent() && this.predicate.test(profile, dat) && !tested.contains(profile)
						&& (onlyIfLocationKnown ? location != null : true)) {
					this.information.add(Pair.of(profile, location));
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
	public void getInfoFromChild(IThought childThought, boolean interrupted, int ticks) {

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
