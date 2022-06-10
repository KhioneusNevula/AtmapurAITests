package _nonsense;

import java.util.HashMap;
import java.util.Map;

import entity.Actor;

public class Profile {

	public static enum Type {
		ACTOR, SOCIOCON;

		public boolean isSociocon() {
			return this == SOCIOCON;
		}

		public boolean isActor() {
			return this == ACTOR;
		}
	}

	private Object original;

	private Map<Sociocontype, Sociocon> sociocons = new HashMap<>();

	private Profile(Object owner) {
		this.original = owner;
	}

	public Profile(Actor owner) {
		this((Object) owner);
	}

	public Profile(Sociocon owner) {
		this((Object) owner);
	}

	public Actor getOriginalActor() {
		return (Actor) original;
	}

	public Sociocon getOriginalSociocon() {
		return (Sociocon) original;
	}

	public Profile.Type getType() {
		return original.getClass() == Actor.class ? Type.ACTOR : Type.SOCIOCON;
	}

	public void addSociocon(Sociocon con) {
		if (!sociocons.containsValue(con)) {
			sociocons.put(con.getType(), con);
		}
		con.members.add(this);
	}

	public boolean hasSociocon(Sociocon con) {
		return sociocons.containsValue(con);
	}

	public boolean hasType(Sociocontype type) {

		return sociocons.containsKey(type);
	}

	public void removeSociocon(Sociocon con) {
		sociocons.remove(con.getType(), con);
		con.members.remove(this);
	}

	public Sociocon getSociocon(Sociocontype type) {
		return sociocons.get(type);
	}

}
