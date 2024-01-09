package phenomenon.type;

import java.awt.Color;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.TreeMap;

import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import com.google.common.collect.MultimapBuilder;

import actor.Actor;
import actor.IUniqueExistence;
import actor.IVisage;
import actor.SimpleVisage;
import biology.systems.SystemType;
import biology.systems.types.ISensor;
import mind.concepts.relations.ConceptRelationType;
import mind.concepts.relations.IConceptRelationType;
import mind.concepts.type.IMeme;
import phenomenon.Phenomenon;
import phenomenon.PhenomenonType;
import processing.core.PApplet;
import sim.WorldGraphics;

public class DamagePhenomenon extends Phenomenon {

	private float amount;
	private Actor cause;
	private Actor object;
	private IVisage visage = new SimpleVisage(this);
	private Multimap<IUniqueExistence, ISensor> senseMap;
	private Map<IUniqueExistence, Map<IConceptRelationType, Collection<IMeme>>> relationMap;

	/**
	 * Cause can be null
	 * 
	 * @param amount
	 * @param cause
	 * @param object
	 */
	public DamagePhenomenon(float amount, Actor cause, Actor object) {
		this(amount, cause, object, ISensor.SIGHT);
	}

	/**
	 * Cause can be null
	 * 
	 * @param amount
	 * @param cause
	 * @param object
	 * @param visibleTo
	 */
	public DamagePhenomenon(float amount, Actor cause, Actor object, ISensor... visibleTo) {
		super(PhenomenonType.DAMAGE);
		this.amount = amount;
		this.cause = cause;
		this.object = object;
		relationMap = new TreeMap<>();
		if (cause != null) {
			senseMap = MultimapBuilder.treeKeys().arrayListValues().build();
			senseMap.putAll(cause, Arrays.asList(visibleTo));

			Map<IConceptRelationType, Collection<IMeme>> innerMap = new TreeMap<>();
			innerMap.put(ConceptRelationType.DANGER_TO, Collections.emptySet());
			relationMap.put(cause, innerMap);
		}
		this.setLifeTimer(5);
	}

	@Override
	public Multimap<IUniqueExistence, ISensor> cause() {
		if (cause == null)
			return ImmutableMultimap.of();
		return senseMap;
	}

	public float getAmount() {
		return amount;
	}

	@Override
	public Map<IUniqueExistence, Map<IConceptRelationType, Collection<IMeme>>> causeToObjectRelations() {
		return relationMap;
	}

	@Override
	public String getSimpleName() {
		return "damage(" + this.object.getSimpleName() + ")";
	}

	@Override
	public Collection<IUniqueExistence> source() {
		if (cause == null)
			return Collections.emptySet();
		return Collections.singleton(cause);
	}

	@Override
	public Actor object() {
		return object;
	}

	@Override
	public Collection<IUniqueExistence> products() {
		return Collections.emptySet();
	}

	@Override
	public void tick() {
		super.tick();
		if (isComplete())
			object.getSystem(SystemType.LIFE).drainEnergy(amount);
	}

	@Override
	public IVisage getVisage() {
		return visage;
	}

	@Override
	public String toString() {
		return "Damage: " + amount + " on " + this.object + (cause == null ? "" : " from " + cause);
	}

	@Override
	public boolean canRender() {
		return true;
	}

	@Override
	public boolean isRelational() {
		return this.cause != null;
	}

	@Override
	public void draw(WorldGraphics g) {
		g.ellipseMode(PApplet.RADIUS);
		g.fill(Color.orange.getRGB(), 100);
		g.noStroke();
		g.circle(object.getX(), object.getY(), 20);

	}

}
