package mind.thought_exp.culture;

import java.util.Collection;
import java.util.Collections;
import java.util.EnumMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.TreeMap;
import java.util.UUID;

import mind.action.ActionType;
import mind.action.IActionType;
import mind.concepts.relations.IConceptRelationType;
import mind.concepts.type.BasicProperties;
import mind.concepts.type.IMeme;
import mind.concepts.type.IProfile;
import mind.concepts.type.Profile;
import mind.concepts.type.Property;
import mind.goals.IGoal;
import mind.goals.IGoal.Type;
import mind.memory.IPropertyData;
import mind.memory.trend.ITrend;
import mind.memory.trend.ITrend.TrendType;
import mind.relationships.RelationType;
import mind.relationships.Relationship;
import mind.thought_exp.memory.IUpgradedKnowledgeBase;
import mind.thought_exp.memory.UpgradedAbstractKnowledgeBase;

public class UpgradedCulture extends UpgradedAbstractKnowledgeBase {

	private boolean isStatic;
	private String groupName;

	/**
	 * I assume trends probably periodically update npc's in a region, and when they
	 * do update npc's in the area, you roll a probability check. And when you load
	 * an area, also update trends. The commonality of trends will be Another
	 * algorithm that I'll figure out later.
	 */
	private Map<TrendType, Map<ITrend, Float>> trends = new EnumMap<>(TrendType.class);

	public static final Set<IActionType<?>> USUAL_ACTIONS = Set.of(ActionType.EAT, ActionType.WALK, ActionType.SLEEP,
			ActionType.PICK_UP, ActionType.WANDER, ActionType.SEARCH);

	private Random rand;

	public UpgradedCulture(Profile self, String name, Random rand) {
		super(self);
		this.groupName = name;
		this.rand = rand;
	}

	public String getGroupName() {
		return groupName;
	}

	/**
	 * TODO replace with a better system, this is just for testing
	 */
	public void usualInit() {
		for (Property property : BasicProperties.getAll()) {
			this.learnConcept(property, BasicProperties.genAssociations(property));
		}
	}

	public UUID getUUID() { // TODO Auto-generated method stub
		return this.selfProfile.getUUID();
	}

	/**
	 * Adds a new trend
	 * 
	 * @param newTrend
	 * @param percentage
	 */
	public void startTrend(ITrend newTrend, float percentage) {
		if (percentage == 0)
			throw new IllegalArgumentException();
		trends.computeIfAbsent(newTrend.getType(),
				(k) -> new TreeMap<>((a, b) -> a.getUniqueName().compareTo(b.getUniqueName())))
				.put(newTrend, percentage);
	}

	public float forgetTrend(ITrend forTrend) {
		Map<ITrend, Float> map = trends.get(forTrend.getType());
		if (map != null) {
			Float f = map.remove(forTrend);
			return f == null ? 0f : f;
		}
		return 0f;
	}

	public float getProbability(ITrend forTrend) {
		Map<ITrend, Float> map = trends.get(forTrend.getType());
		if (map != null) {
			return map.getOrDefault(forTrend, 0f);
		}
		return 0f;
	}

	public Collection<ITrend> getTrends(TrendType type) {
		Map<ITrend, Float> map = trends.get(type);
		if (map == null)
			return Collections.emptySet();
		return map.keySet();
	}

	/**
	 * Apply the effects of trends which role an appropriate probability check on an
	 * individual (assumed to be) in this Culture Return successful trends. TODO
	 * people can obviously resist trends, so figure out how to account for that
	 * 
	 * @return
	 */
	public Collection<ITrend> rollTrendProbabilities(IUpgradedKnowledgeBase uke) {
		LinkedList<ITrend> sTrends = new LinkedList<>();
		for (TrendType type : TrendType.values()) {
			for (Map.Entry<ITrend, Float> entry : this.trends.get(type).entrySet()) {
				if (rand.nextFloat() <= entry.getValue()) {
					entry.getKey().integrate(uke);
				}
			}
		}
		return sTrends;
	}

	public String report() {

		return "";
	}

	@Override
	public String toString() {
		return "Culture_" + this.groupName + "{id:" + this.getUUID().toString().substring(0, 6) + "...}";
	}

	// deal with staticness

	public boolean isStatic() {
		return isStatic;
	}

	public void makeStatic() {
		this.isStatic = true;
	}

	public void makeDynamic() {
		this.isStatic = false;
	}

	@Override
	public boolean learnConcept(IMeme concept) {
		if (isStatic) {
			return false;
		}
		return super.learnConcept(concept);
	}

	@Override
	public void learnGoal(IGoal goal) {
		if (isStatic) {
			throw new CultureIsStaticException(this);
		}
		super.learnGoal(goal);
	}

	@Override
	public void learnOfRelationship(IProfile one, IProfile other, Relationship relationship) {
		if (isStatic) {
			throw new CultureIsStaticException(this);
		}
		super.learnOfRelationship(one, other, relationship);
	}

	@Override
	public boolean learnProfile(IProfile IProfile) {
		if (isStatic) {
			throw new CultureIsStaticException(this);
		}
		return super.learnProfile(IProfile);
	}

	@Override
	public IPropertyData learnPropertyData(IProfile prof, Property prop, IPropertyData dat) {
		if (isStatic) {
			throw new CultureIsStaticException(this);
		}
		return super.learnPropertyData(prof, prop, dat);
	}

	@Override
	public <T extends IMeme> void learnRelation(IMeme one, IMeme other, IConceptRelationType type, Iterable<T> args) {
		if (isStatic) {
			throw new CultureIsStaticException(this);
		}
		super.learnRelation(one, other, type, args);
	}

	@Override
	public <T extends IMeme> boolean learnRelationArguments(IMeme one, IMeme other, IConceptRelationType type,
			Iterable<T> arguments) {
		if (isStatic) {
			throw new CultureIsStaticException(this);
		}
		return super.learnRelationArguments(one, other, type, arguments);
	}

	@Override
	public boolean forgetAboutAllRelationships(IProfile one, IProfile other) {
		if (isStatic) {
			throw new CultureIsStaticException(this);
		}
		return super.forgetAboutAllRelationships(one, other);
	}

	@Override
	public boolean forgetAboutAllRelationshipsOfType(IProfile one, IProfile other, RelationType type) {
		if (isStatic) {
			throw new CultureIsStaticException(this);
		}
		return super.forgetAboutAllRelationshipsOfType(one, other, type);
	}

	@Override
	public boolean forgetAboutRelationship(IProfile one, IProfile other, Relationship relationship) {
		if (isStatic) {
			throw new CultureIsStaticException(this);
		}
		return super.forgetAboutRelationship(one, other, relationship);
	}

	@Override
	public boolean forgetAllPropertyData(Property property) {
		if (isStatic) {
			throw new CultureIsStaticException(this);
		}
		return super.forgetAllPropertyData(property);
	}

	@Override
	public boolean forgetAllRelations(IMeme one, IMeme other) {
		if (isStatic) {
			throw new CultureIsStaticException(this);
		}
		return super.forgetAllRelations(one, other);
	}

	@Override
	public boolean forgetConcept(IMeme concept) {
		if (isStatic) {
			throw new CultureIsStaticException(this);
		}
		return super.forgetConcept(concept);
	}

	@Override
	public boolean forgetConceptRelations(IMeme concept) {
		if (isStatic) {
			throw new CultureIsStaticException(this);
		}
		return super.forgetConceptRelations(concept);
	}

	@Override
	public boolean forgetGoal(IGoal goal) {
		if (isStatic) {
			throw new CultureIsStaticException(this);
		}
		return super.forgetGoal(goal);
	}

	@Override
	public boolean forgetGoals(Type type) {
		if (isStatic) {
			throw new CultureIsStaticException(this);
		}
		return super.forgetGoals(type);
	}

	@Override
	public boolean forgetProfile(IProfile IProfile) {
		if (isStatic) {
			throw new CultureIsStaticException(this);
		}
		return super.forgetProfile(IProfile);
	}

	@Override
	public IPropertyData forgetPropertyData(IProfile prof, Property prop) {
		if (isStatic) {
			throw new CultureIsStaticException(this);
		}
		return super.forgetPropertyData(prof, prop);
	}

	@Override
	public <T extends IMeme> Collection<T> forgetRelation(IMeme one, IMeme other, IConceptRelationType type) {
		if (isStatic) {
			throw new CultureIsStaticException(this);
		}
		return super.forgetRelation(one, other, type);
	}

	@Override
	public <T extends IMeme> void forgetRelationArguments(IMeme one, IMeme other, IConceptRelationType type,
			Iterable<T> arguments) {
		if (isStatic) {
			throw new CultureIsStaticException(this);
		}
		super.forgetRelationArguments(one, other, type, arguments);
	}

	@Override
	public boolean deepForgetProfile(IProfile IProfile) {
		if (isStatic) {
			throw new CultureIsStaticException(this);
		}
		return super.deepForgetProfile(IProfile);
	}

	public static class CultureIsStaticException extends IllegalStateException {

		/**
		 * 
		 */
		private static final long serialVersionUID = 1990801076293215311L;
		private UpgradedCulture culture;

		public CultureIsStaticException(UpgradedCulture culture) {
			super("Culture " + culture.groupName + ":" + culture.getUUID().toString().substring(0, 6)
					+ "... is static");
			this.culture = culture;
		}

		public UpgradedCulture getCulture() {
			return culture;
		}
	}

}
