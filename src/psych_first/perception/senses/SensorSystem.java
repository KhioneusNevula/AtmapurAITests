package psych_first.perception.senses;

import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.TreeMap;

import main.ImmutableCollection;
import psych_first.mind.IMindPart;
import psych_first.mind.Memory.MemoryType;
import psych_first.mind.Mind;
import psych_first.perception.knowledge.IKnowledgeType;
import psych_first.perception.knowledge.Identity;
import psych_first.perception.senses.SensoryAttribute.RecognitionLevel;
import sim.World;
import sociology.Profile;

public class SensorSystem implements IMindPart {

	private Mind owner;
	private World world;
	private Map<Sense, Integer> senses = new TreeMap<>();
	private Map<SensoryInput, Identity> sensed = new HashMap<>();
	private Map<Profile, SensoryInput> profilesensories = new HashMap<>();

	public SensorSystem(Mind mind, Sense... senses) {
		this.owner = mind;
		this.world = mind.getOwner().getWorld();
		for (Sense sense : senses) {
			this.addSense(sense, sense.getStandardLevel());
		}
	}

	public Collection<SensoryInput> getSensedInputs() {
		return new ImmutableCollection<>(sensed.keySet());
	}

	public Collection<Profile> getSensedProfiles() {
		return new ImmutableCollection<>(profilesensories.keySet());
	}

	/**
	 * adds the sensor system to the world's sense handler
	 * 
	 * @return
	 */
	public SensorSystem register() {
		world.getSensoryHandler().register(this);
		return this;
	}

	public void deregister() {
		world.getSensoryHandler().deregister(this);
	}

	/**
	 * a sense level of 0 indicates the sense has been blocked in some way
	 * 
	 * @param sense
	 * @return
	 */
	public int getSenseLevel(Sense sense) {
		return senses.get(sense);
	}

	public void setSenseLevel(Sense sense, int level) {
		this.senses.put(sense, level);
	}

	public void changeSenseLevel(Sense sense, int by) {
		this.senses.put(sense, this.senses.get(sense) + by);
	}

	public boolean hasSense(Sense sense) {
		return senses.containsKey(sense);
	}

	public <T> T getInfo(IKnowledgeType<T> t) {
		return this.getMind().getOwner().getInfo(t, this.getMind().getCulture());
	}

	public SensorSystem addSense(Sense sense, int level) {

		this.senses.put(sense, level);
		return this;
	}

	@Override
	public Mind getMind() {
		return owner;
	}

	/**
	 * process a sensory output in the world
	 * 
	 * @param sensory
	 */
	public void process(SensoryOutput sensory) {
		for (Sense sense : this.senses.keySet()) {
			for (IKnowledgeType<?> t : sense.necessaryProperties()) {
				if (!this.owner.getOwner().hasInfo(t, this.getMind().getCulture())) {
					throw new IllegalStateException(
							owner.getOwner() + " does not have necessary property " + t + "for sense " + sense);
				}
			}
			if (sensory.getOverallSenseLevel(sense) > this.getSenseLevel(sense)) {
				stopSensing(sensory.getIdentity());
				continue;
			}
			if (this.getSenseLevel(sense) <= 0 || !sense.canSense(this, sensory)) {
				stopSensing(sensory.getIdentity());
				continue;
			}

			SensoryInput in = new SensoryInput(sensory);
			for (SensoryAttribute<?> att : sensory.getAttributes()) {
				boolean f = false;
				for (Sense s : this.senses.keySet()) {
					if (att.getSenses().contains(s)) {
						f = true;
						break;
					}
				}
				if (!f) {
					continue;
				}
				if (this.getSenseLevel(sense) >= sensory.getAttribute(att).getSenseLevel()) {
					in.sense(att);
				}
			}
			Collection<SensoryAttribute<?>> atts = sense.senses(this, in);
			if (atts.isEmpty())
				continue;
			in.retainTraits(atts);

			if (!in.isUnsensable()) {
				this.sensed.put(in, Identity.NO_ONE);
				this.profilesensories.put(in.getIdentity(), in);
				this.recognize(in);
			} else {
				stopSensing(sensory.getIdentity());
			}
		}
	}

	public boolean isRecognized(SensoryInput in) {
		return sensed.get(in) != null && sensed.get(in) != Identity.NO_ONE;
	}

	public SensoryInput getSensory(Profile for_) {
		return this.profilesensories.get(for_);
	}

	public boolean isSensed(SensoryInput in) {
		return sensed.containsKey(in);
	}

	private void recognize(SensoryInput s) {
		if (!this.getMind().getMemory().hasMemories(MemoryType.IDENTITIES))
			return;
		for (Identity id : this.getMind().getMemory().getMemories(MemoryType.IDENTITIES)) {
			if (id.getRecognitionPattern().recognizes(s, false, this.getMind())
					.moreRecognizableThan(RecognitionLevel.UNRECOGNIZABLE)) {
				id.getRecognitionPattern().recognizes(s, true, this.getMind()); // marks all traits as accessed
				this.sensed.put(s, id);
				break;
			}
		}
	}

	public SensoryInput stopSensing(Profile sens) {
		Iterator<SensoryInput> it = this.sensed.keySet().iterator();
		while (it.hasNext()) {
			SensoryInput i = it.next();
			if (i.getOutput().getIdentity().equals(sens)) {
				it.remove();
			}
			this.profilesensories.remove(sens);
			return i;
		}
		return null;

	}

	@Override
	public void update(int ticks) {
		// TODO Auto-generated method stub

	}

	@Override
	public String report() {
		return "{sensed:" + this.sensed + "}";
	}

}
