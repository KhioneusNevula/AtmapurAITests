package psych_first.mind;

import java.util.Collection;
import java.util.Map;
import java.util.TreeMap;

import main.ImmutableCollection;

public interface ILevel {
	public static enum Fundamental implements ILevel {
		REWARD(), PUNISHMENT();

		private Fundamental() {
		}

		public boolean isDesired() {
			return this == REWARD;
		}

		public boolean isUndesired() {
			return this == PUNISHMENT;
		}

		public String getId() {
			return this.name().toLowerCase();
		}

	}

	public static class ELevel implements ILevel {

		private static final Map<String, ELevel> map = new TreeMap<>();

		public static final ELevel LETHARGY = new ELevel("lethargy", -1, 0);
		public static final ELevel AGGRESSION = new ELevel("aggression", 1, 1);
		public static final ELevel STRESS = new ELevel("stress", 2, 0);
		public static final ELevel FOCUS = new ELevel("focus", 0, 0);
		public static final ELevel MORALITY = new ELevel("morality", 1, 1);
		public static final ELevel DUTIFULNESS = new ELevel("dutifulness", 1, 1);
		public static final ELevel STRANGE = new ELevel("strange", 0, 0);

		private String id;
		private int reward;
		private int punishment;

		/**
		 * 
		 * @param id
		 * @param reward     1 if this increases reward, 0 if it doesn't affect reward,
		 *                   -1 if this decreases reward, 2 if it can do both
		 * @param punishment same as reward but for punishment
		 */
		public ELevel(String id, int reward, int punishment) {
			this.id = id;
			this.reward = reward;
			this.punishment = punishment;
			map.put(id, this);
		}

		public boolean increasesReward() {
			return reward > 0;
		}

		public boolean decreasesReward() {
			return reward < 0 || reward == 2;
		}

		public boolean increasesPunishment() {
			return punishment > 0;
		}

		public boolean decreasesPunishment() {
			return punishment < 0 || punishment == 2;
		}

		public boolean increases(Fundamental fund) {
			switch (fund) {
			case REWARD:
				return increasesReward();
			case PUNISHMENT:
				return increasesPunishment();
			}
			return false;
		}

		public boolean decreases(Fundamental fund) {
			switch (fund) {
			case REWARD:
				return decreasesReward();
			case PUNISHMENT:
				return decreasesPunishment();
			}
			return false;
		}

		/**
		 * 1 = increases reward, -1 = decreases, 0 = no effect, 2 if it can do both
		 * 
		 * @return
		 */
		public int getReward() {
			return reward;
		}

		/**
		 * 1 = increases punishment, -1 = decreases, 0 = no effect, 2 if it can do both
		 * 
		 * @return
		 */
		public int getPunishment() {
			return punishment;
		}

		public String getId() {
			return id;
		}

		public static ELevel get(String id) {
			return map.get(id);
		}

		public static Collection<ELevel> getAll() {
			return new ImmutableCollection<>(map.values());
		}
	}

	public String getId();

}
