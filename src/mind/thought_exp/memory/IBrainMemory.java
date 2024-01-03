package mind.thought_exp.memory;

import java.util.Collection;

import mind.concepts.type.IMeme;
import mind.concepts.type.IProfile;
import mind.feeling.IFeeling;
import mind.thought_exp.IThoughtMemory;
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
	public Collection<IThoughtMemory> getShortTermMemoriesOfType(IThoughtMemory.Type type);

	/**
	 * Keeps this thought in short term memory; return false if there isn't space
	 * 
	 * @param memory
	 */
	public boolean rememberShortTerm(IThoughtMemory memory);

	/**
	 * Forget this memory from short term, return false if it isn't there
	 * 
	 * @param memory
	 * @return
	 */
	public boolean forgetShortTerm(IThoughtMemory memory);

	/**
	 * Associate feelings with concepts
	 * 
	 * @param concept
	 * @return
	 */
	IFeeling getAssociatedFeeling(IMeme concept);

}
