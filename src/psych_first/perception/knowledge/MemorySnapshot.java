package psych_first.perception.knowledge;

import java.util.HashMap;
import java.util.Map;

import psych_first.perception.emotions.Emotion;
import psych_first.perception.emotions.EmotionType;
import psych_first.perception.emotions.EmotionalState;
import psych_first.perception.knowledge.facts.PieceOfInformation;

public class MemorySnapshot implements IInformation {

	private PieceOfInformation information;

	private long time;

	private Map<EmotionType, Integer> emotions = new HashMap<>(0);

	public MemorySnapshot(PieceOfInformation fact, long time, EmotionalState emotions) {
		this.information = fact;
		this.time = time;
		for (Emotion e : emotions) {
			this.emotions.put(e.getEmotion(), e.getLevel());
		}
	}

	public int getEmotion(EmotionType e) {
		return emotions.getOrDefault(e, 0);
	}

	public long getTime() {
		return time;
	}

	public PieceOfInformation getInformation() {
		return information;
	}

	@Override
	public boolean equals(Object obj) {
		return obj instanceof MemorySnapshot ms && ms.information.equals(this.information) && ms.time == this.time;
	}

}
