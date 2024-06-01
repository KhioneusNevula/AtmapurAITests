package mind.thought_exp.memory;

import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

import com.google.common.collect.Multimap;
import com.google.common.collect.MultimapBuilder;
import com.google.common.collect.Table;
import com.google.common.collect.TreeBasedTable;
import com.google.common.collect.TreeMultimap;

import actor.ITemplate;
import mind.action.IActionType;
import mind.concepts.CompositeIdentifier;
import mind.concepts.identifiers.IPropertyIdentifier;
import mind.concepts.relations.ConceptRelationType;
import mind.concepts.relations.IConceptRelationType;
import mind.concepts.relations.RelationsGraph;
import mind.concepts.type.IMeme;
import mind.concepts.type.IMeme.IMemeType;
import mind.concepts.type.IMeme.MemeType;
import mind.concepts.type.IProfile;
import mind.concepts.type.ITemplateConcept;
import mind.concepts.type.Property;
import mind.concepts.type.TemplateConcept;
import mind.goals.IGoal;
import mind.goals.IGoal.Type;
import mind.goals.ITaskHint;
import mind.memory.IPropertyData;
import mind.need.INeed;
import mind.need.INeed.INeedType;
import mind.relationships.PartyRelationshipsGraph;
import mind.relationships.RelationType;
import mind.relationships.Relationship;
import sim.relationalclasses.AbstractRelationalGraph.IEdge;

public abstract class UpgradedAbstractKnowledgeBase implements IUpgradedKnowledgeBase {
	protected IProfile selfProfile;
	protected TreeSet<IProfile> knownIds = new TreeSet<>();
	protected TreeMultimap<IMemeType, IMeme> knownConcepts = TreeMultimap.create((a, b) -> a.name().compareTo(b.name()),
			(a, b) -> a.getUniqueName().compareTo(b.getUniqueName()));
	protected TreeMap<IMeme, IPropertyIdentifier> identifiers = new TreeMap<>();
	protected Table<IProfile, Property, IPropertyData> profileProperties = TreeBasedTable.create();
	protected Multimap<INeedType, INeed> needs = MultimapBuilder
			.<INeedType>treeKeys((a, b) -> a.uniqueName().compareTo(b.uniqueName()))
			.<INeed>treeSetValues((a, b) -> a.getUniqueName().compareTo(b.getUniqueName())).build();
	/*
	 * protected Table<IMeme, IConceptRelationType, Collection<IMeme>> relations =
	 * TreeBasedTable.create( (a, b) ->
	 * a.getUniqueName().compareTo(b.getUniqueName()), (a, b) ->
	 * a.idString().compareTo(b.idString())); protected Table<IProfile,
	 * RelationType, Multimap<IProfile, Relationship>> relationships =
	 * TreeBasedTable .create((a, b) ->
	 * a.getUniqueName().compareTo(b.getUniqueName()), RelationType::compareTo);
	 * 
	 * protected Table<IMeme, IMeme, Pair<Map<IConceptRelationType,
	 * Collection<IMeme>>, Multimap<RelationType, Relationship>>>
	 * relationsAndRelationships = TreeBasedTable .create((a, b) ->
	 * a.getUniqueName().compareTo(b.getUniqueName()), (a, b) ->
	 * a.getUniqueName().compareTo(b.getUniqueName()));
	 */
	protected RelationsGraph relations = new RelationsGraph();
	protected PartyRelationshipsGraph relationships = new PartyRelationshipsGraph();
	protected Multimap<IGoal.Type, IGoal> goals = MultimapBuilder.enumKeys(IGoal.Type.class).hashSetValues().build();
	protected Map<IProfile, ITemplateConcept> profileTemplates = new TreeMap<>();
	protected Map<ITemplate, ITemplateConcept> templateConcepts = new TreeMap<>(
			(a, b) -> a.getUniqueName().compareTo(b.getUniqueName()));

	public UpgradedAbstractKnowledgeBase(IProfile self) {
		this.selfProfile = self;
	}

	@Override
	public boolean isKnown(IProfile IProfile) {
		return knownIds.contains(IProfile);
	}

	@Override
	public Collection<IProfile> getKnownProfiles() {
		return knownIds;
	}

	@Override
	public boolean isKnown(IMeme concept) {
		if (concept == Property.ANY)
			return true;
		IMemeType type = concept.getMemeType();
		if (type == MemeType.PROFILE) {
			return isKnown((IProfile) concept);
		}
		return knownConcepts.get(type).contains(concept);
	}

	@Override
	public IPropertyData getPropertyData(IProfile prof, Property prop) {
		if (prop == Property.ANY)
			return IPropertyData.PRESENCE;
		IPropertyData dat = profileProperties.get(prof.getUUID(), prop);
		if (dat == null)
			return IPropertyData.UNKNOWN;
		return dat;
	}

	@Override
	public boolean hasProperty(IProfile prof, Property prop) {
		if (prop == Property.ANY)
			return true;
		return getPropertyData(prof, prop).isPresent();
	}

	@Override
	public ITemplateConcept getOrLearnTemplateConcept(ITemplate forTemplate) {
		return this.templateConcepts.computeIfAbsent(forTemplate, (a) -> new TemplateConcept(forTemplate));
	}

	@Override
	public ITemplateConcept getTemplate(IProfile forProfile) {

		return this.profileTemplates.get(forProfile);
	}

	@Override
	public ITemplateConcept learnTemplateFor(IProfile forProfile, ITemplateConcept value) {
		return this.profileTemplates.put(forProfile, value);
	}

	@Override
	public ITemplateConcept getTemplateConcept(ITemplate forTemplate) {

		return templateConcepts.get(forTemplate);
	}

	@Override
	public void learnTemplateConcept(ITemplateConcept concept) {
		templateConcepts.put(concept.getTemplate(), concept);
	}

	@Override
	public <T extends IMeme> Collection<T> getKnownConceptsOfType(IMemeType type) {
		if (type == MemeType.PROFILE)
			return (Collection<T>) this.knownIds;
		return (Collection<T>) this.knownConcepts.get(type);
	}

	@Override
	public IProfile getSelf() {
		return selfProfile;
	}
	/*
	 * @Override public boolean hasRelation(IMeme one, IMeme other,
	 * IConceptRelationType type) { Pair<Map<IConceptRelationType,
	 * Collection<IMeme>>, Multimap<RelationType, Relationship>> pair =
	 * relationsAndRelationships .get(one, other); if (pair == null) { return
	 * hasDirectionalRelation(other, one, type); } if (pair.getFirst() == null)
	 * return false; return pair.getFirst().get(type) != null; }
	 */

	@Override
	public boolean hasRelation(IMeme one, IMeme other, IConceptRelationType type) {

		return hasDirectionalRelation(one, other, type) || hasDirectionalRelation(other, one, type);
	}
	/*
	 * @Override public boolean hasDirectionalRelation(IMeme one, IMeme other,
	 * IConceptRelationType type) { Pair<Map<IConceptRelationType,
	 * Collection<IMeme>>, Multimap<RelationType, Relationship>> pair =
	 * relationsAndRelationships .get(one, other); if (pair == null) return false;
	 * if (pair.getFirst() == null) return false; return pair.getFirst().get(type)
	 * != null; }
	 */

	@Override
	public boolean hasDirectionalRelation(IMeme one, IMeme other, IConceptRelationType type) {

		return relations.getEdge(one, other, type) != null;
	}

	@Override
	public boolean hasAnyRelationOfType(IMeme one, IConceptRelationType type) {
		return hasDirectionalRelationOfType(one, type) || hasDirectionalRelationOfType(one, type.inverse());
	}

	@Override
	public boolean hasDirectionalRelationOfType(IMeme one, IConceptRelationType type) {

		return !relations.getEdgesOfTypeFrom(one, type).isEmpty();
	}

	/*
	 * @Override public <T extends IMeme> Collection<T> relationArguments(IMeme one,
	 * IMeme other, IConceptRelationType type) { Pair<Map<IConceptRelationType,
	 * Collection<IMeme>>, Multimap<RelationType, Relationship>> pair =
	 * relationsAndRelationships .get(one, other); if (pair == null) return
	 * Collections.emptySet(); if (pair.getFirst() == null) return
	 * Collections.emptySet(); Collection<IMeme> mems = pair.getFirst().get(type);
	 * if (mems == null) return Collections.emptySet(); return (Collection<T>) mems;
	 * }
	 */
	@Override
	public <T extends IMeme> Collection<T> relationArguments(IMeme one, IMeme other, IConceptRelationType type) {
		IEdge<IConceptRelationType, Collection<IMeme>, IMeme> edge = relations.getEdge(one, other, type);
		if (edge == null)
			return null;
		return (Collection<T>) edge.getArgs();
	}

	/*
	 * @Override public Collection<Relationship> getKnownRelationships(IProfile one,
	 * IProfile other, RelationType type) {
	 * 
	 * Pair<Map<IConceptRelationType, Collection<IMeme>>, Multimap<RelationType,
	 * Relationship>> pair = relationsAndRelationships .get(one, other); if (pair ==
	 * null) return Collections.emptySet(); if (pair.getSecond() == null) return
	 * Collections.emptySet(); Collection<Relationship> mems =
	 * pair.getSecond().get(type); if (mems == null) return Collections.emptySet();
	 * return mems; }
	 */
	@Override
	public Collection<Relationship> getKnownRelationships(IProfile one, IProfile other, RelationType type) {
		IEdge<RelationType, Collection<Relationship>, IProfile> edge = relationships.getEdge(one, other, type);
		if (edge == null)
			return Collections.emptySet();
		return edge.getArgs();
	}

	@Override
	public Iterator<IProfile> getProfilesWithProperty(Property prop) {
		if (prop == Property.ANY)
			return this.profileProperties.cellSet().stream().map((e) -> e.getRowKey()).iterator();
		return this.profileProperties.column(prop).entrySet().stream().filter((e) -> e.getValue().isPresent())
				.map((e) -> e.getKey()).iterator();
	}

	/*
	 * @Override public Collection<Relationship> getAllKnownRelationships(IProfile
	 * one, IProfile other) { Pair<Map<IConceptRelationType, Collection<IMeme>>,
	 * Multimap<RelationType, Relationship>> pair = relationsAndRelationships
	 * .get(one, other); if (pair == null) return Collections.emptySet(); if
	 * (pair.getSecond() == null) return Collections.emptySet();
	 * Collection<Relationship> mems = pair.getSecond().values(); return mems; }
	 */

	@Override
	public Collection<Relationship> getAllKnownRelationships(IProfile one, IProfile other) {
		return relationships.getAllRelationships(one, other);
	}

	/*
	 * @Override public <T extends IMeme> Collection<T>
	 * getConceptsWithRelation(IMeme fromWhat, IConceptRelationType type) {
	 * Collection<IMeme> col = relations.get(fromWhat, type); return col == null ?
	 * Collections.emptyList() : (Collection<T>) col; }
	 */
	@Override
	public <T extends IMeme> Collection<T> getConceptsWithRelation(IMeme fromWhat, IConceptRelationType type) {
		return (Collection<T>) relations.getEdgesOfTypeFrom(fromWhat, type).keySet();
	}

	/**
	 * @Override public <T extends IProfile> Multimap<T, Relationship>
	 *           getProfilesWithRelationship(IProfile fromWhat, RelationType type) {
	 *           Multimap<IProfile, Relationship> col = relationships.get(fromWhat,
	 *           type); return col == null ? ImmutableMultimap.of() : (Multimap<T,
	 *           Relationship>) col; }
	 */

	@Override
	public <T extends IProfile> Multimap<T, Relationship> getProfilesWithRelationship(IProfile fromWhat,
			RelationType type) {
		Map<IProfile, IEdge<RelationType, Collection<Relationship>, IProfile>> col = relationships
				.getEdgesOfTypeFrom(fromWhat, type);
		Multimap<T, Relationship> multi = MultimapBuilder.treeKeys().treeSetValues().build();
		for (Map.Entry<IProfile, IEdge<RelationType, Collection<Relationship>, IProfile>> entry : col.entrySet()) {
			multi.putAll((T) entry.getKey(), entry.getValue().getArgs());
		}
		return multi;
	}

	@Override
	public <T extends IProfile> Collection<T> getProfilesWithRelationshipFrom(IProfile fromWhat, RelationType type) {
		return (Collection<T>) relationships.getEdgesOfTypeFrom(fromWhat, type).keySet();
	}

	/*
	 * private Pair<Map<IConceptRelationType, Collection<IMeme>>,
	 * Multimap<RelationType, Relationship>> makeRelMapEntry( IMeme one, IMeme
	 * other) { if (relationsAndRelationships.contains(one, other)) return
	 * relationsAndRelationships.get(one, other); Pair<Map<IConceptRelationType,
	 * Collection<IMeme>>, Multimap<RelationType, Relationship>> pair = Pair.of( new
	 * TreeMap<>((a, b) -> a.idString().compareTo(b.idString())),
	 * MultimapBuilder.enumKeys(RelationType.class).treeSetValues().build());
	 * this.relationsAndRelationships.put(one, other, pair); return pair; }
	 * 
	 * private Multimap<IProfile, Relationship> makeRelationshipsMapEntry(IProfile
	 * one, RelationType type) { Multimap<IProfile, Relationship> mm =
	 * MultimapBuilder.treeKeys().linkedListValues().build(); relationships.put(one,
	 * type, mm); return mm; }
	 * 
	 * private Collection<IMeme> makeRelationsMapEntry(IMeme one,
	 * IConceptRelationType type) { LinkedList<IMeme> memes = new LinkedList<>();
	 * relations.put(one, type, memes); return memes; }
	 */
	/*
	 * @Override public void learnOfRelationship(IProfile one, IProfile other,
	 * Relationship relationship) { if (one.equals(selfProfile) ||
	 * other.equals(selfProfile)) throw new UnsupportedOperationException();
	 * makeRelMapEntry(one, other).getSecond().put(relationship.getType(),
	 * relationship); makeRelationshipsMapEntry(one,
	 * relationship.getType()).put(other, relationship); }
	 */

	@Override
	public void learnOfRelationship(IProfile one, IProfile other, Relationship relationship) {
		IEdge<RelationType, Collection<Relationship>, IProfile> edge = this.relationships.getEdge(one, other,
				relationship.getType());
		if (edge == null) {
			edge = relationships.createEdge(one, other, relationship.getType(), new LinkedList<>());
		}
		edge.getArgs().add(relationship);
	}

	@Override
	public boolean learnConcept(IMeme concept) {
		if (concept instanceof IProfile)
			return this.learnProfile((IProfile) concept);
		boolean done = this.knownConcepts.put(concept.getMemeType(), concept);
		// TODO make a better way to learn what actions do, e.g. by just doing them
		if (concept instanceof IActionType<?>iat) {
			Set<ITaskHint> hints = iat.getUsage();
			for (ITaskHint hint : hints) {
				this.learnConcept(hint);
				this.learnRelation(iat, hint, ConceptRelationType.USED_FOR, Collections.emptyList());
			}
		}
		return done;
	}

	@Override
	public boolean learnProfile(IProfile IProfile) {

		return this.knownIds.add(IProfile);
	}

	@Override
	public IPropertyData learnPropertyData(IProfile prof, Property prop, IPropertyData dat) {
		// TODO learnPropertyData. Think about whether properties should be stored
		// per-IProfile or in the web of relationships. I think that makes more sense.
		return this.profileProperties.put(prof, prop, dat);
	}

	/*
	 * @Override public <T extends IMeme> void learnRelation(IMeme one, IMeme other,
	 * IConceptRelationType type, Iterable<T> args) { makeRelMapEntry(one,
	 * other).getFirst().put(type, args == null ? new LinkedList<>() :
	 * ((Supplier<LinkedList<IMeme>>) () -> { LinkedList<IMeme> mem = new
	 * LinkedList<>(); for (T arg : args) mem.add(arg); return mem; }).get());
	 * makeRelMapEntry(other, one).getFirst().put(type.inverse(), new
	 * LinkedList<>()); makeRelationsMapEntry(one, type).add(other);
	 * makeRelationsMapEntry(other, type.inverse()).add(one); }
	 */
	@Override
	public <T extends IMeme> void learnRelation(IMeme one, IMeme other, IConceptRelationType type, Iterable<T> args) {
		IEdge<IConceptRelationType, Collection<IMeme>, IMeme> edge = this.relations.createEdge(one, other, type,
				new TreeSet<>((a, b) -> a.getUniqueName().compareTo(b.getUniqueName())));
		for (T arg : args) {
			edge.getArgs().add(arg);
		}
	}

	@Override
	public <T extends IMeme> boolean learnRelationArguments(IMeme one, IMeme other, IConceptRelationType type,
			Iterable<T> arguments) {
		IEdge<IConceptRelationType, Collection<IMeme>, IMeme> edge = this.relations.getEdge(one, other, type);
		if (edge == null)
			return false;
		for (T arg : arguments) {
			edge.getArgs().add(arg);
		}

		return true;
	}

	/*
	 * @Override public <T extends IMeme> boolean learnRelationArguments(IMeme one,
	 * IMeme other, IConceptRelationType type, Iterable<T> arguments) {
	 * Pair<Map<IConceptRelationType, Collection<IMeme>>, Multimap<RelationType,
	 * Relationship>> pair = this.relationsAndRelationships .get(one, other); if
	 * (pair == null) return false; Collection<IMeme> col =
	 * pair.getFirst().get(type); if (col == null) return false; for (T e :
	 * arguments) { col.add(e); } return true;
	 * 
	 * }
	 */

	/**
	 * @Override public boolean forgetAboutAllRelationships(IProfile one, IProfile
	 *           other) { if (one.equals(selfProfile) || other.equals(selfProfile))
	 *           throw new UnsupportedOperationException(); boolean change = false;
	 *           Map<RelationType, Multimap<IProfile, Relationship>> rshipsm =
	 *           this.relationships.row(one); Iterator<Map.Entry<RelationType,
	 *           Multimap<IProfile, Relationship>>> iter =
	 *           rshipsm.entrySet().iterator(); while (iter.hasNext()) {
	 *           Map.Entry<RelationType, Multimap<IProfile, Relationship>> entry =
	 *           iter.next(); if (entry.getValue() == null) continue;
	 *           Collection<Relationship> rels = entry.getValue().get(other); if
	 *           (!rels.isEmpty()) { rels.clear(); change = true; } if
	 *           (entry.getValue().isEmpty()) iter.remove(); }
	 *           Pair<Map<IConceptRelationType, Collection<IMeme>>,
	 *           Multimap<RelationType, Relationship>> pair =
	 *           this.relationsAndRelationships .get(one, other); if (pair == null)
	 *           return change; if (pair.getSecond().size() != 0) {
	 *           pair.getSecond().clear(); change = true; } return change; }
	 **/
	@Override
	public boolean forgetAboutAllRelationships(IProfile one, IProfile other) {
		return !this.relationships.removeAllEdgesBetween(one, other).isEmpty();
	}

	/**
	 * @Override public boolean forgetAboutAllRelationshipsOfType(IProfile one,
	 *           IProfile other, RelationType type) { if (one.equals(selfProfile) ||
	 *           other.equals(selfProfile)) throw new
	 *           UnsupportedOperationException(); boolean change = false;
	 *           Multimap<IProfile, Relationship> rels1 =
	 *           this.relationships.get(one, type); if (rels1 != null &&
	 *           !rels1.removeAll(other).isEmpty()) change = true;
	 *           Pair<Map<IConceptRelationType, Collection<IMeme>>,
	 *           Multimap<RelationType, Relationship>> pair =
	 *           this.relationsAndRelationships .get(one, other); if (pair == null)
	 *           return change; Collection<Relationship> rels =
	 *           pair.getSecond().get(type); if (rels.size() != 0) { rels.clear();
	 *           change = true; } return change; }
	 */
	@Override
	public boolean forgetAboutAllRelationshipsOfType(IProfile one, IProfile other, RelationType type) {
		return relationships.removeEdge(one, other, type) != null;
	}

	/*
	 * @Override public boolean forgetAboutRelationship(IProfile one, IProfile
	 * other, Relationship relationship) { if (one.equals(selfProfile) ||
	 * other.equals(selfProfile)) throw new UnsupportedOperationException(); boolean
	 * change = false; Multimap<IProfile, Relationship> rels1 =
	 * this.relationships.get(one, relationship.getType()); if (rels1 != null &&
	 * rels1.remove(other, relationship)) change = true;
	 * Pair<Map<IConceptRelationType, Collection<IMeme>>, Multimap<RelationType,
	 * Relationship>> pair = this.relationsAndRelationships .get(one, other); if
	 * (pair == null) return change; Collection<Relationship> rels =
	 * pair.getSecond().get(relationship.getType()); if (rels.remove(relationship))
	 * change = true; return change; }
	 */
	@Override
	public boolean forgetAboutRelationship(IProfile one, IProfile other, Relationship relationship) {
		return this.relationships.removeRelationship(one, other, relationship);
	}

	@Override
	public boolean forgetAllPropertyData(Property property) {
		Map<IProfile, IPropertyData> p = profileProperties.column(property);
		if (p.size() > 0) {
			p.clear();
			return true;
		}
		return false;
	}
	/*
	 * private boolean directionalForgetAllRelations(IMeme one, IMeme other) {
	 * boolean change = false; Map<IConceptRelationType, Collection<IMeme>> rshipsm
	 * = this.relations.row(one); Iterator<Collection<IMeme>> iter =
	 * rshipsm.values().iterator(); while (iter.hasNext()) { Collection<IMeme> rels
	 * = iter.next(); if (rels == null) continue; if (!rels.isEmpty()) {
	 * rels.clear(); change = true; } if (rels.isEmpty()) iter.remove(); }
	 * 
	 * Pair<Map<IConceptRelationType, Collection<IMeme>>, Multimap<RelationType,
	 * Relationship>> pair = this.relationsAndRelationships .get(one, other); if
	 * (pair == null) return change; if (pair.getFirst().size() != 0) {
	 * pair.getFirst().clear(); change = true; } return change; }
	 */

	/*
	 * @Override public boolean forgetAllRelations(IMeme one, IMeme other) {
	 * 
	 * boolean a = directionalForgetAllRelations(one, other); boolean b =
	 * directionalForgetAllRelations(other, one);
	 * 
	 * return a || b; }
	 */
	@Override
	public boolean forgetAllRelations(IMeme one, IMeme other) {

		return !this.relations.removeAllEdgesBetween(one, other).isEmpty();
	}

	@Override
	public boolean forgetAllRelationsOfType(IMeme fromOne, IConceptRelationType type) {

		return !relations.removeAllEdgesFrom(fromOne, type).isEmpty();
	}

	/*
	 * @Override public boolean deepForgetProfile(IProfile IProfile) {
	 * this.profileProperties.row(IProfile).clear();
	 * this.relationsAndRelationships.row(IProfile).clear();
	 * this.relationsAndRelationships.column(IProfile).clear();
	 * relations.row(IProfile).clear(); relationships.row(IProfile).clear();
	 * Iterator<Collection<IMeme>> iter = relations.values().iterator(); while
	 * (iter.hasNext()) { Collection<IMeme> rels = iter.next(); if (rels == null)
	 * continue; rels.remove(IProfile); if (rels.isEmpty()) iter.remove(); }
	 * Iterator<Multimap<mind.concepts.type.IProfile, Relationship>> iter2 =
	 * relationships.values().iterator(); while (iter2.hasNext()) {
	 * Multimap<mind.concepts.type.IProfile, Relationship> rels = iter2.next(); if
	 * (rels == null) continue; rels.removeAll(IProfile); if (rels.isEmpty())
	 * iter2.remove(); } return this.forgetProfile(IProfile); }
	 */
	@Override
	public boolean deepForgetProfile(IProfile IProfile) {

		relations.removeNode(IProfile);
		relationships.removeNode(IProfile);
		return this.knownIds.remove(IProfile);
	}

	@Override
	public boolean forgetConcept(IMeme concept) {
		return this.knownConcepts.get(concept.getMemeType()).remove(concept);
	}

	/**
	 * @Override public boolean forgetConceptRelations(IMeme concept) { boolean
	 *           change = false; Map<IMeme, Pair<Map<IConceptRelationType,
	 *           Collection<IMeme>>, Multimap<RelationType, Relationship>>> r1 =
	 *           this.relationsAndRelationships .row(concept); if (r1.size() > 0) {
	 *           change = true; r1.clear(); } r1 =
	 *           this.relationsAndRelationships.column(concept); if (r1.size() > 0)
	 *           { change = true; r1.clear(); } Map<IConceptRelationType,
	 *           Collection<IMeme>> ma = relations.row(concept); if (!ma.isEmpty())
	 *           { change = true; ma.clear(); }
	 * 
	 *           Iterator<Collection<IMeme>> iter = relations.values().iterator();
	 *           while (iter.hasNext()) { Collection<IMeme> rels = iter.next(); if
	 *           (rels == null) continue; change = change || rels.remove(concept);
	 *           if (rels.isEmpty()) iter.remove(); }
	 * 
	 *           return change; }
	 */
	@Override
	public boolean forgetConceptRelations(IMeme concept) {
		boolean a = relations.removeNode(concept) != null;
		boolean b = concept instanceof IProfile ? relationships.removeNode((IProfile) concept) != null : false;
		return a || b;
	}

	@Override
	public boolean forgetProfile(IProfile IProfile) {

		return this.knownIds.remove(IProfile);
	}

	@Override
	public IPropertyData forgetPropertyData(IProfile prof, Property prop) {

		return this.profileProperties.remove(prof, prop);
	}

	/*
	 * @Override public <T extends IMeme> Collection<T> forgetRelation(IMeme one,
	 * IMeme other, IConceptRelationType type) { Pair<Map<IConceptRelationType,
	 * Collection<IMeme>>, Multimap<RelationType, Relationship>> pair =
	 * this.relationsAndRelationships .get(one, other); Collection<IMeme> col =
	 * relations.get(one, type); if (col != null) { col.remove(other); if
	 * (col.isEmpty()) relations.remove(one, type); } if (pair != null) { return
	 * (Collection<T>) pair.getFirst().remove(type); } return
	 * Collections.emptyList(); }
	 */
	@Override
	public <T extends IMeme> Collection<T> forgetRelation(IMeme one, IMeme other, IConceptRelationType type) {
		IEdge<IConceptRelationType, Collection<IMeme>, IMeme> edge = relations.removeEdge(one, other, type);
		if (edge == null)
			return null;
		return (Collection<T>) edge.getArgs();
	}

	/*
	 * @Override public <T extends IMeme> void forgetRelationArguments(IMeme one,
	 * IMeme other, IConceptRelationType type, Iterable<T> arguments) {
	 * Pair<Map<IConceptRelationType, Collection<IMeme>>, Multimap<RelationType,
	 * Relationship>> pair = this.relationsAndRelationships .get(one, other); if
	 * (pair == null) return; Collection<IMeme> col = pair.getFirst().get(type); if
	 * (col == null) return; for (T ar : arguments) { col.remove(ar); } }
	 */
	@Override
	public <T extends IMeme> void forgetRelationArguments(IMeme one, IMeme other, IConceptRelationType type,
			Iterable<T> arguments) {
		IEdge<IConceptRelationType, Collection<IMeme>, IMeme> edge = relations.removeEdge(one, other, type);
		if (edge == null)
			return;
		for (T arg : arguments) {
			edge.getArgs().remove(arg);
		}

	}

	@Override
	public boolean forgetGoal(IGoal goal) {
		return goals.get(goal.getGoalType()).remove(goal);
	}

	@Override
	public boolean forgetGoals(Type type) {
		if (goals.get(type).isEmpty()) {
			return false;
		}
		goals.get(type).clear();
		return true;
	}

	@Override
	public Collection<IGoal> getGoals(Type type) {

		return goals.get(type);
	}

	@Override
	public Collection<IGoal> getGoals() {
		return goals.values();
	}

	@Override
	public void learnGoal(IGoal goal) {
		Iterator<IGoal> goalI = this.goals.values().iterator();
		boolean add = true;
		while (goalI.hasNext()) {
			IGoal next = goalI.next();
			if (next.equivalent(goal)) {
				add = false;
				break;
			} else {
				goalI.remove();
				break;
			}
		}
		if (add)
			goals.get(goal.getGoalType()).add(goal);
	}

	@Override
	public void forgetNeed(INeed last) {
		needs.remove(last.getType(), last);
	}

	@Override
	public void addNeed(INeed need) {
		needs.put(need.getType(), need);
	}

	@Override
	public Collection<INeed> getNeeds() {
		return needs.values();
	}

	@Override
	public Collection<INeed> getNeeds(INeedType type) {
		return needs.get(type);
	}

	@Override
	public void learnPropertyIdentifier(Property prop, IPropertyIdentifier identifier) {
		IPropertyIdentifier iden = this.identifiers.computeIfAbsent(prop, (a) -> new CompositeIdentifier());
		if (!(iden instanceof CompositeIdentifier)) {
			iden = new CompositeIdentifier().addIdentifier(iden);
			identifiers.put(prop, iden);
		}
		((CompositeIdentifier) iden).addIdentifier(identifier);
	}

	@Override
	public IPropertyIdentifier getPropertyIdentifier(Property prop) {
		return this.identifiers.getOrDefault(prop, IPropertyIdentifier.UNKNOWN);
	}

	@Override
	public IPropertyIdentifier forgetIdentifiers(Property prop) {

		IPropertyIdentifier id = this.identifiers.remove(prop);
		if (id == null)
			return IPropertyIdentifier.UNKNOWN;
		return id;
	}

	@Override
	public boolean forgetIdentifier(Property prop, IPropertyIdentifier identifier) {
		IPropertyIdentifier id = this.getPropertyIdentifier(prop);
		if (id.isUnknown())
			return false;
		if (id instanceof CompositeIdentifier) {
			CompositeIdentifier ci = (CompositeIdentifier) id;
			return ci.removeIdentifier(identifier);
		} else {
			if (id.equals(identifier))
				return this.identifiers.remove(prop, id);
		}
		return false;
	}

	@Override
	public RelationsGraph getRelationsGraph() {
		return this.relations;
	}

	@Override
	public boolean isConceptSubtype(IMeme concept, IMeme superclass) {

		return this.relations.isSubtypeOf(concept, superclass);
	}

}
