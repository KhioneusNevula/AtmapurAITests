package civilization_and_minds.mind.mechanics;

import java.util.EnumMap;
import java.util.Stack;
import java.util.function.Predicate;
import java.util.stream.Stream;

import com.google.common.collect.Table;
import com.google.common.collect.Tables;

import civilization_and_minds.AbstractKnowledgeBase;
import civilization_and_minds.group.ICultureKnowledge;
import civilization_and_minds.mind.memories.IMemoryItem.MemoryType;
import civilization_and_minds.mind.memories.IMemoryItem.Section;
import civilization_and_minds.mind.memories.MemoryWrapper;
import civilization_and_minds.social.concepts.profile.Profile;

public class MemoryKnowledge extends AbstractKnowledgeBase implements IMemoryKnowledge {

	private Table<Section, MemoryType, Stack<MemoryWrapper>> memories;

	public MemoryKnowledge(Profile selfProfile) {
		super(selfProfile);
		memories = Tables.newCustomTable(new EnumMap<>(Section.class),
				() -> new EnumMap<MemoryType, Stack<MemoryWrapper>>(MemoryType.class));
	}

	public void setParent(ICultureKnowledge culture) {
		this.parent = culture;
	}

	@Override
	public boolean hasMemories(Section section, MemoryType type) {
		Stack<MemoryWrapper> memo = memories.get(section, type);
		if (memo == null || memo.isEmpty())
			return false;
		return true;
	}

	@Override
	public MemoryWrapper peekLatestMemory(Section section, MemoryType type) {
		Stack<MemoryWrapper> wrap = memories.get(section, type);
		if (wrap == null || wrap.isEmpty())
			throw new IllegalStateException();
		return wrap.peek();

	}

	@Override
	public MemoryWrapper popLatestMemory(Section section, MemoryType type) {
		Stack<MemoryWrapper> wrap = memories.get(section, type);
		if (wrap == null || wrap.isEmpty())
			throw new IllegalStateException();
		return wrap.pop();
	}

	@Override
	public Stream<MemoryWrapper> getAllMemoriesOfType(Section section, MemoryType type) {
		Stack<MemoryWrapper> wrap = memories.get(section, type);
		if (wrap == null || wrap.isEmpty())
			throw new IllegalStateException();
		return wrap.stream();
	}

	@Override
	public Stream<MemoryWrapper> getAllMemoriesInSection(Section section) {

		return memories.row(section).values().stream().flatMap((a) -> a.stream());
	}

	@Override
	public MemoryWrapper popLatestMemory(Section section, MemoryType type, Predicate<MemoryWrapper> condition) {
		Stack<MemoryWrapper> wrap = memories.get(section, type);
		if (wrap == null || wrap.isEmpty())
			throw new IllegalStateException();
		for (int i = 0; i < wrap.size(); i++) {
			MemoryWrapper wrwr = wrap.get(i);
			if (condition.test(wrwr)) {
				wrap.remove(i);
				return wrwr;
			}
		}
		return null;
	}

	@Override
	public void pushMemory(Section section, MemoryWrapper memory) {
		this.memories.row(section).computeIfAbsent(memory.getMemory().getMemoryType(), (a) -> new Stack<>())
				.push(memory);
	}

}
