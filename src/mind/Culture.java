package mind;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import main.Pair;
import mind.action.ActionType;
import mind.action.IActionType;
import mind.concepts.type.BasicProperties;
import mind.concepts.type.IMeme;
import mind.concepts.type.Property;
import mind.feeling.IFeeling;
import mind.linguistics.Language;
import mind.linguistics.NameWord;
import mind.memory.AbstractKnowledgeEntity;
import mind.memory.IPropertyData;
import mind.memory.trend.ActionKnowledgeTrend;
import mind.memory.trend.GoalTrend;
import mind.memory.trend.ITrend;
import mind.memory.trend.ITrend.TrendType;
import mind.memory.trend.LanguageKnowledgeTrend;
import mind.memory.trend.LocationTrend;
import mind.memory.trend.NameTrend;
import mind.memory.trend.ProfileAssociationsTrend;
import mind.memory.trend.PropertyTrend;

/**
 * mainly just a storage object for the cultural properties of a group
 * 
 * @author borah
 *
 */
public class Culture extends AbstractKnowledgeEntity implements Comparable<Culture> {

	private Map<IMeme, IFeeling> feelings;
	private boolean isStatic;
	private String groupName;
	private NameWord nameWord;
	private Group group;
	/**
	 * Key - trend. Value - <amount of people who know the trend, chance a random
	 * individual knows the trend>
	 */
	private Map<ITrend, Pair<Integer, Float>> trends;

	/**
	 * TODO will delete later when better systems in place
	 */
	public static final Set<IActionType<?>> USUAL_ACTIONS = Set.of(ActionType.EAT, ActionType.WALK, ActionType.SLEEP,
			ActionType.PICK_UP, ActionType.WANDER, ActionType.SEARCH);

	public Culture(UUID id, String type, String ident) {
		super(id, type);
		this.groupName = ident;
	}

	public Culture(Group group, String type) {
		super(group.getUUID(), type);
		this.groupName = group.getIdentifier();
	}

	public Culture languageInit(Random rand) {
		this.mainLanguage = new Language(this.groupName); // TODO idk allow using existing languages? idfk
		this.mainLanguage.generate(rand);
		this.languages.add(mainLanguage);
		nameWord = mainLanguage.name(getSelfProfile(), new Random(), Set.of(this));
		return this;
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
	public IFeeling getAssociatedFeeling(IMeme concept) {
		return this.feelings == null ? null : feelings.get(concept);
	}

	public NameWord getNameWord() {
		return nameWord;
	}

	@Override
	public int compareTo(Culture o) {
		return this.self.compareTo(o.self);
	}

	public String report() {
		StringBuilder builder = new StringBuilder("Culture-" + this.groupName
				+ (this.nameWord != null ? " \"" + this.nameWord.getDisplay() + "\"" : this.nameWord) + "{");
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
		return "culture_" + this.groupName + (this.nameWord != null ? "_" + nameWord.getDisplay() : "");
	}

	/**
	 * Get percent of individuals who would know this trend
	 * 
	 * @param forTrend
	 * @return
	 */
	public float getKnownPercentage(ITrend forTrend) {
		if (trends != null) {
			Pair<Integer, Float> pair = trends.get(forTrend);
			if (pair != null) {
				return pair.getSecond();
			}
			return 0f;
		}
		return 0f;
	}

	/**
	 * Amount of individuals who know the info of this trend
	 * 
	 * @param forTrend
	 * @return
	 */
	public int getKnownCount(ITrend forTrend) {
		if (trends != null) {
			Pair<Integer, Float> pair = trends.get(forTrend);
			if (pair != null) {
				return pair.getFirst();
			}
			return 0;
		}
		return 0;
	}

	public Pair<Integer, Float> forgetTrend(ITrend trend) {
		return trends == null ? Pair.of(0, 0f) : trends.getOrDefault(trend, Pair.of(0, 0f));
	}

	public void incrementKnownCount(ITrend forTrend) {
		changeKnownCount(forTrend, 1);
	}

	/**
	 * throws exception if the trend is not already known
	 * 
	 * @param forTrend
	 */
	public void changeKnownCount(ITrend forTrend, int by) {
		if (trends == null || !trends.containsKey(forTrend))
			throw new IllegalStateException();
		Pair<Integer, Float> pair = trends.get(forTrend);
		pair.setFirst(pair.getFirst() + by);
	}

	/**
	 * Post a trend to this culture to allow it to change. Does not accept trends
	 * with 0 or 100% startingAcceptance. Note that this does NOT automatically add
	 * 1 to the number of people who know this trend
	 *
	 * 
	 * @param newTrend
	 * @param startingAcceptance
	 * @param override           whether to push the trend anyway, even if the
	 *                           culture is static
	 * @throws IllegalArgumentException if the startingAcceptance is not between 0
	 *                                  and 1 (exclusive)
	 * @throws IllegalStateException    if the culture is static and override is
	 *                                  false
	 */
	public void postTrend(ITrend newTrend, float startingAcceptance, boolean override) {
		if (startingAcceptance >= 1 || startingAcceptance <= 0)
			throw new IllegalArgumentException();
		if (!override && this.isStatic())
			throw new IllegalStateException();
		if (this.trends == null) {
			trends = new HashMap<>();
		}
		trends.put(newTrend, Pair.of(0, startingAcceptance));
	}

	public Collection<ITrend> getTrendsByType(TrendType type) {
		return trends == null ? Set.of()
				: trends.keySet().stream().filter((a) -> a.getType() == type).collect(Collectors.toSet());
	}

	public Collection<ITrend> getTrends() {
		return trends == null ? Set.of() : trends.keySet();
	}

	/**
	 * Note that this does NOT delete the trend, nor does it mark the trend as
	 * Integrated
	 * 
	 * @param usingTrend
	 */
	public void changeCulture(ITrend usingTrend) {
		switch (usingTrend.getType()) {
		case ACTION_KNOWLEDGE: {
			ActionKnowledgeTrend trend = (ActionKnowledgeTrend) usingTrend;
			if (trend.isDeletion())
				this.forgetDoableAction(trend.getConcept());
			else
				this.addDoableAction(trend.getConcept());

			break;
		}
		case GOAL: {
			GoalTrend trend = (GoalTrend) usingTrend;
			if (trend.isDeletion())
				this.forgetGoal(trend.getConcept());
			else
				this.addGoal(trend.getConcept());
			break;
		}
		case LOCATION_INFO: {
			LocationTrend trend = (LocationTrend) usingTrend;
			if (trend.isDeletion()) {
				if (this.locationKnowledge != null)
					this.locationKnowledge.remove(trend.getConcept(), trend.getData());
			} else
				this.learnLocation(trend.getConcept(), trend.getData());
			break;
		}
		case PROFILE_ASSOCIATIONS: {
			ProfileAssociationsTrend trend = (ProfileAssociationsTrend) usingTrend;
			if (trend.getData().isEmpty()) {
				if (trend.isDeletion()) {
					if (this.profiles != null)
						this.profiles.remove(trend.getConcept().getUUID());
				} else {
					this.recognizeProfile(trend.getConcept());
				}
			}
			for (Map.Entry<Property, IPropertyData> entry : trend.getData().entrySet()) {
				if (trend.isDeletion()) {
					this.removeProperty(trend.getConcept(), entry.getKey());
				} else
					this.setProperty(trend.getConcept(), entry.getKey(), entry.getValue());
			}
			break;
		}
		case PROPERTY_KNOWLEDGE: {
			PropertyTrend trend = (PropertyTrend) usingTrend;
			if (trend.isDeletion()) {
				this.forgetAssociations(trend.getConcept());
			} else
				this.learnProperty(trend.getConcept(), trend.getData());
			break;
		}
		case LANGUAGE_KNOWLEDGE: {
			LanguageKnowledgeTrend trend = (LanguageKnowledgeTrend) usingTrend;
			if (trend.isDeletion()) {
				languages.remove(trend.getConcept());
				if (mainLanguage == trend.getConcept()) {
					mainLanguage = languages.stream().findFirst().orElse(null);
				}
			} else {
				if (trend.isMainLanguage()) {
					this.mainLanguage = trend.getConcept();
				}
				this.languages.add(trend.getConcept());
			}
			break;
		}
		case NAME: {
			NameTrend trend = (NameTrend) usingTrend;
			if (trend.isDeletion()) {
				this.languages.add(trend.getData().getFirst());
				Set<Culture> culset = trend.getData().getFirst().getWord(trend.getConcept()) == null ? Set.of()
						: Set.of(this);
				trend.getData().getFirst().name(trend.getConcept(), trend.getData().getSecond(), culset);
			}
			break;
		}
		}
	}

	@Override
	public void prune(int passes) {
		// TODO pruning
		if (group != null && !isStatic()) {

		}

	}

	public Group getGroup() {
		return group;
	}

}
