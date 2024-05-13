package mind.thought_exp.type;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.google.common.collect.Multimap;

import actor.IUniqueExistence;
import biology.systems.types.ISensor;
import mind.concepts.relations.IConceptRelationType;
import mind.concepts.type.IMeme;
import mind.concepts.type.IProfile;
import mind.concepts.type.ITemplateConcept;
import mind.concepts.type.Profile;
import mind.goals.IGoal.Priority;
import mind.personality.Personality.BasicPersonalityTrait;
import mind.thought_exp.ICanThink;
import mind.thought_exp.IThought;
import mind.thought_exp.IThoughtMemory;
import mind.thought_exp.IThoughtMemory.Interest;
import mind.thought_exp.ThoughtType;
import mind.thought_exp.memory.type.LearnProfileMemory;
import mind.thought_exp.memory.type.RelationMemory;
import phenomenon.IPhenomenon;

public class LearnRelationThought extends AbstractThought {

	private IPhenomenon phenomenon;
	private List<RelationMemory> memories = new LinkedList<>();
	private Set<LearnProfileMemory> pMemories = new HashSet<>();
	private Map<IThoughtMemory, Interest> memoryResult = new HashMap<>();

	/**
	 * learn a relation from a phenomenon which has both a cause/source and an
	 * object
	 * 
	 * @param relationalPhenomenon
	 */
	public LearnRelationThought(IPhenomenon relationalPhenomenon) {
		if (!relationalPhenomenon.isRelational()) {
			throw new IllegalArgumentException();
		}
		phenomenon = relationalPhenomenon;
	}

	@Override
	public IThoughtType getThoughtType() {
		return ThoughtType.REFINE_BELIEFS;
	}

	@Override
	public boolean isLightweight() {
		return false;
	}

	@Override
	public Interest shouldProduceRecentThoughtMemory(ICanThink mind, int finishingTicks, long worldTicks) {
		return Interest.FORGET;
	}

	@Override
	public void startThinking(ICanThink memory, long worldTick) {
		Multimap<? extends IUniqueExistence, ISensor> causes = phenomenon.cause();
		// TODO figure out how to connect concepts and designate examples as
		// particularly unique, allowing for example assumptions to be made about, say,
		// a culture
		ITemplateConcept objectT = memory.getKnowledgeBase()
				.getOrLearnTemplateConcept(phenomenon.object().getVisage().getSpecies());
		/** whether to treat this object as unique */
		boolean useObjectUniqueness = memory.rand().nextFloat()
				* memory.personality().getTrait(BasicPersonalityTrait.PREJUDGEMENT)
				* 1.5 < objectT.uniquenessOfMembers();
		IMeme object = useObjectUniqueness ? new Profile(phenomenon.object()) : objectT;
		if (useObjectUniqueness) {
			pMemories.add(new LearnProfileMemory((IProfile) object, phenomenon.object().uniqueness()));
		}
		Map<IUniqueExistence, Map<IConceptRelationType, Collection<IMeme>>> relations = phenomenon
				.causeToObjectRelations();
		Map<IUniqueExistence, Map<IConceptRelationType, Collection<IMeme>>> leftRelations = phenomenon
				.objectToCauseRelations();

		for (IUniqueExistence ex : causes.keySet()) {
			Map<IConceptRelationType, Collection<IMeme>> relationsForEx = relations.get(ex);
			Map<IConceptRelationType, Collection<IMeme>> leftRelationsForEx = leftRelations.get(ex);
			if ((relationsForEx == null || relationsForEx.isEmpty())
					&& (leftRelationsForEx == null || leftRelationsForEx.isEmpty()))
				continue;
			Collection<ISensor> sens = phenomenon.cause().get(ex);
			boolean allowed = false;
			for (ISensor sen : sens)
				if (memory.getSenses().contains(sen)) {
					allowed = true;
					break;
				}
			if (!allowed)
				continue;
			// TODO add a system to designate things as particularly unique so relations
			// about them don't apply to their whole kind, as well as systems to designate
			// that a person makes less assumptions and won't apply an assumption to a whole
			// kind either and all that
			ITemplateConcept causeT = memory.getKnowledgeBase().getOrLearnTemplateConcept(ex.getVisage().getSpecies());
			/** whether to treat the cause as unique */
			boolean useCauseUniqueness = memory.rand().nextFloat()
					* memory.personality().getTrait(BasicPersonalityTrait.PREJUDGEMENT)
					* 1.5 < causeT.uniquenessOfMembers();
			IMeme cause = useCauseUniqueness ? new Profile(ex) : causeT;
			if (useCauseUniqueness) {
				pMemories.add(new LearnProfileMemory((IProfile) cause, ex.uniqueness()));
			}
			if (leftRelationsForEx == null || leftRelationsForEx.isEmpty()) {
				this.memories.add(new RelationMemory(cause, object, relationsForEx));
			} else if (relationsForEx == null || relationsForEx.isEmpty()) {

				this.memories.add(new RelationMemory(cause, object, Map.of(), leftRelationsForEx));
			} else {

				this.memories.add(new RelationMemory(cause, object, relationsForEx, leftRelationsForEx));
			}
		}
		memories.forEach((a) -> memoryResult.put(a, Interest.FORGET));
		pMemories.forEach((a) -> memoryResult.put(a, Interest.SHORT_TERM));
	}

	@Override
	public Map<IThoughtMemory, Interest> produceMemories(ICanThink mind, int finishingTicks, long worldTicks) {

		return memoryResult;
	}

	@Override
	public boolean isFinished(ICanThink memory, int ticks, long worldTick) {

		return true;
	}

	@Override
	public Priority getPriority() {
		return Priority.NORMAL;
	}

	@Override
	public void thinkTick(ICanThink memory, int ticks, long worldTick) {

	}

	@Override
	public void getInfoFromChild(ICanThink mind, IThought childThought, boolean interrupted, int ticks) {

	}

	@Override
	public String displayText() {
		return "learning relations from " + this.phenomenon;
	}

	@Override
	public boolean equivalent(IThought other) {

		return other instanceof LearnRelationThought
				&& ((LearnRelationThought) other).phenomenon.getUUID().equals(this.phenomenon.getUUID());
	}

}
