package mind.thought_exp.memory;

import java.util.Collection;

import mind.concepts.relations.RelationsGraph;
import mind.concepts.type.IMeme;
import mind.concepts.type.IProfile;
import mind.feeling.IFeeling;
import mind.thought_exp.IThoughtMemory;
import mind.thought_exp.IThoughtMemory.Interest;
import mind.thought_exp.IThoughtMemory.MemoryCategory;
import mind.thought_exp.culture.UpgradedCulture;

public interface IBrainMemory extends IUpgradedKnowledgeBase {

	// public IEmotions getEmotions();

	Collection<UpgradedCulture> cultures();

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
	 * gets short term memories
	 * 
	 * @return
	 */
	public Collection<IThoughtMemory> getShortTermMemories();

	/**
	 * gets short term memories of this type
	 * 
	 * @param type
	 * @return
	 */
	public Collection<IThoughtMemory> getShortTermMemoriesOfType(IThoughtMemory.MemoryCategory type);

	/**
	 * Keeps this thought in short term memory; return false if there isn't space
	 * 
	 * @param memory
	 */
	public boolean rememberShortTerm(IThoughtMemory memory);

	/**
	 * Remember this memory in the given section of interest; return false if there
	 * isn't space or whatever
	 * 
	 * @param memoryType
	 * @param memory
	 * @return
	 */
	public boolean remember(Interest memoryType, IThoughtMemory memory);

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
	public Collection<IThoughtMemory> getMemories(Interest section);

	/**
	 * Get memories of this type from the given section
	 * 
	 * @param section
	 * @param type
	 * @return
	 */
	public Collection<IThoughtMemory> getMemoriesOfType(Interest section, MemoryCategory type);

	/**
	 * Associate feelings with concepts
	 * 
	 * @param concept
	 * @return
	 */
	IFeeling getAssociatedFeeling(IMeme concept);

	/**
	 * return null if you store data another way. This is just for Mind Display
	 * 
	 * @return
	 */
	RelationsGraph getRelationsGraph();

}
