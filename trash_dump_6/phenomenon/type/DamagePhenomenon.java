package phenomenon.type;

import java.awt.Color;
import java.util.Collection;
import java.util.Collections;
import java.util.Map;

import com.google.common.collect.ImmutableSet;

import actor.Actor;
import actor.IUniqueEntity;
import actor.construction.physical.IVisage;
import biology.systems.SystemType;
import main.WorldGraphics;
import phenomenon.Phenomenon;
import phenomenon.PhenomenonType;
import phenomenon.SimplePhenomenonVisage;
import processing.core.PApplet;

public class DamagePhenomenon extends Phenomenon {

	private float amount;
	private Actor cause;
	private Actor object;
	private IVisage visage = new SimplePhenomenonVisage(this, Map.of());

	/**
	 * Cause can be null
	 * 
	 * @param amount
	 * @param cause
	 * @param object
	 * @param visibleTo
	 */
	public DamagePhenomenon(float amount, Actor cause, Actor object) {
		super(PhenomenonType.DAMAGE);
		this.amount = amount;
		this.cause = cause;
		this.object = object;
		this.setLifeTimer(5);
	}

	@Override
	public Collection<IUniqueEntity> cause() {
		if (cause == null)
			return ImmutableSet.of();
		return Collections.singleton(cause);
	}

	public float getAmount() {
		return amount;
	}

	@Override
	public String getSimpleName() {
		return "damage(" + this.object.getSimpleName() + ")";
	}

	@Override
	public Collection<IUniqueEntity> source() {
		if (cause == null)
			return Collections.emptySet();
		return Collections.singleton(cause);
	}

	@Override
	public Actor object() {
		return object;
	}

	@Override
	public Collection<IUniqueEntity> products() {
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
