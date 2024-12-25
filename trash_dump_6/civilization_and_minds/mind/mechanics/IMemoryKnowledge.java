package civilization_and_minds.mind.mechanics;

import java.util.function.Predicate;
import java.util.stream.Stream;

import civilization_and_minds.IKnowledgeBase;
import civilization_and_minds.group.ICultureKnowledge;
import civilization_and_minds.mind.memories.IMemoryItem.MemoryType;
import civilization_and_minds.mind.memories.IMemoryItem.Section;
import civilization_and_minds.mind.memories.MemoryWrapper;

/**
 * Memory of an individual person. Mainly based on the dominant culture of the
 * being
 * 
 * @author borah
 *
 */
public interface IMemoryKnowledge extends IKnowledgeBase {

	@Override
	public ICultureKnowledge getParent();

	/**
	 * get the main culture that this mind uses knowledge from. Equivalent to
	 * {@link #getParent()}
	 * 
	 * @return
	 */
	default ICultureKnowledge getCulture() {
		return getParent();
	}

	/**
	 * Whether this mind has any memories in the given section and type
	 * 
	 * @param section
	 * @param type
	 * @return
	 */
	public boolean hasMemories(Section section, MemoryType type);

	/**
	 * Peek the latest memory stored of the given type
	 * 
	 * @param section
	 * @param type
	 * @return
	 */
	public MemoryWrapper peekLatestMemory(Section section, MemoryType type);

	/**
	 * Pops latest memory of the given type
	 * 
	 * @param section
	 * @param type
	 * @return
	 */
	public MemoryWrapper popLatestMemory(Section section, MemoryType type);

	/**
	 * Stream all memories of the given type in the section.
	 * 
	 * @param type
	 * @return
	 */
	public Stream<MemoryWrapper> getAllMemoriesOfType(Section section, MemoryType type);

	/**
	 * Stream all memories in the given section.
	 * 
	 * @param section
	 * @return
	 */
	public Stream<MemoryWrapper> getAllMemoriesInSection(Section section);

	/**
	 * Pop the first memory that fits the given condition
	 * 
	 * @param section
	 * @param type
	 * @param condition
	 * @return
	 */
	public MemoryWrapper popLatestMemory(Section section, MemoryType type, Predicate<MemoryWrapper> condition);

	/**
	 * Add a new memory in the given section
	 * 
	 * @param section
	 * @param memory
	 */
	public void pushMemory(Section section, MemoryWrapper memory);

}
