package mind.thought_exp.memory;

import java.util.Collection;
import java.util.Random;
import java.util.function.Function;

import mind.concepts.relations.IConceptRelationType;
import mind.concepts.type.IMeme;
import mind.concepts.type.IProfile;
import mind.feeling.IFeeling;
import mind.thought_exp.IThoughtMemory;
import mind.thought_exp.IThoughtMemory.Interest;
import mind.thought_exp.IThoughtMemory.MemoryCategory;
import mind.thought_exp.culture.UpgradedCulture;
import mind.thought_exp.memory.type.MemoryWrapper;

public interface IBrainMemory extends IUpgradedKnowledgeBase {

	// public IEmotions getEmotions();

	Collection<UpgradedCulture> cultures();

	/**
	 * returns an iterable consisting of this mind and all its cultures
	 * 
	 * @return
	 */
	Collection<IUpgradedKnowledgeBase> mindAndCultures();

	/**
	 * Returns a conglomerate collection for the cultures of this mind, made of
	 * collections derived from each culture using the given function; i.e. getting
	 * an iterator of all of a certain concept across cultures
	 * 
	 * @param <T>
	 * @param function
	 * @return
	 */
	<T> Collection<T> getCollectionFromCultures(
			Function<IUpgradedKnowledgeBase, ? extends Collection<? extends T>> function);

	/**
	 * Returns a conglomerate collection for this mind AND the cultures of this
	 * mind, made of collections derived from each culture using the given function
	 * 
	 * @param <T>
	 * @param function
	 * @return
	 */
	<T> Collection<T> getCollectionFromMindAndCultures(
			Function<IUpgradedKnowledgeBase, ? extends Collection<? extends T>> function);

	/**
	 * {@link #getCollectionFromMindAndCultures(Function)} but with some
	 * randomization
	 * 
	 * @param <T>
	 * @param function
	 * @param rand
	 * @return
	 */
	<T> Collection<T> getCollectionFromMindAndCultures(
			Function<IUpgradedKnowledgeBase, ? extends Collection<? extends T>> function, Random rand);

	/*
	 * public SenseMemory getSenses();
	 *
	 * public void setFeelingCurious(boolean b);
	 * 
	 * public boolean isFeelingCurious();
	 * 
	 * public boolean socializedRecently();
	 */

	public void addCulture(UpgradedCulture a);

	public void forgetCulture(UpgradedCulture toForget);

	public void forgetCulture(IProfile toForget);

	/**
	 * gets short term memories and the time they were remembered
	 * 
	 * @return
	 */
	public Collection<MemoryWrapper> getShortTermMemories();

	/**
	 * gets short term memories of this type with the times they were remembered
	 * 
	 * @param type
	 * @return
	 */
	public Collection<MemoryWrapper> getShortTermMemoriesOfType(IThoughtMemory.MemoryCategory type);

	/**
	 * Keeps this thought in short term memory; return false if there isn't space.
	 * second argument = time of remembrance, or -1 if the memory's time is
	 * irrelevant
	 * 
	 * @param memory
	 */
	public boolean rememberShortTerm(IThoughtMemory memory, long ticks);

	/**
	 * Remember this memory in the given section of interest; return false if there
	 * isn't space or whatever
	 * 
	 * @param memoryType
	 * @param memory
	 * @return
	 */
	public boolean remember(Interest memoryType, IThoughtMemory memory, long ticks);

	/**
	 * Forget this memory, return false if it isn't there
	 * 
	 * @param memory
	 * @return
	 */
	public boolean forgetMemory(Interest fromSection, IThoughtMemory memory);

	/**
	 * Gets memories of this section
	 * 
	 * @param section
	 * @return
	 */
	public Collection<MemoryWrapper> getMemories(Interest section);

	/**
	 * Get memories of this type from the given section
	 * 
	 * @param section
	 * @param type
	 * @return
	 */
	public Collection<MemoryWrapper> getMemoriesOfType(Interest section, MemoryCategory type);

	/**
	 * Associate feelings with concepts
	 * 
	 * @param concept
	 * @return
	 */
	IFeeling getAssociatedFeeling(IMeme concept);

	/**
	 * Learn a relation or add a source to an existing relation
	 * 
	 * @param <T>
	 * @param one
	 * @param other
	 * @param type
	 * @param source
	 */
	<T extends IMeme> void learnRelationAndAddSource(IMeme one, IMeme other, IConceptRelationType type,
			IThoughtMemory source);

	/**
	 * Attempt to remove a relation using one of its sources, or fail to if the
	 * source is not really a source or the relation was not deleted
	 */
	<T extends IMeme> Collection<T> tryForgetRelationUsingSource(IMeme one, IMeme other, IConceptRelationType type,
			IThoughtMemory source);

}
