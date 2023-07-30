package mind.memory;

import java.util.Collection;
import java.util.Set;

import com.google.common.collect.Multimap;
import com.google.common.collect.MultimapBuilder;

import main.Pair;
import mind.memory.events.Consequence;
import mind.memory.events.EventDescription;

public class EventAssociationsMemory {

	private Multimap<EventDescription, Pair<Consequence, Float>> map;

	public EventAssociationsMemory() {
	}

	public Collection<Pair<Consequence, Float>> getConsequences(EventDescription event) {

		return map != null ? map.get(event) : Set.of();
	}

	public Collection<EventDescription> getAllEventDescriptions() {
		return map == null ? Set.of() : map.keySet();
	}

	public void rememberConsequence(EventDescription cause, Consequence consequence, float chance) {
		if (map == null) {
			map = MultimapBuilder.treeKeys()
					.<Pair<Consequence, Float>>treeSetValues((p, q) -> (int) (100 * (p.getSecond() - q.getSecond())))
					.build();
		}
		map.put(cause, Pair.of(consequence, chance));
	}

}
