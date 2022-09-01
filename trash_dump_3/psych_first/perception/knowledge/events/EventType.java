package psych_first.perception.knowledge.events;

import psych_first.perception.knowledge.IInformation;
import psych_first.perception.knowledge.events.types.AnimateEvent;
import psych_first.perception.knowledge.events.types.AppearEvent;
import psych_first.perception.knowledge.events.types.AttackEvent;
import psych_first.perception.knowledge.events.types.BirthEvent;
import psych_first.perception.knowledge.events.types.ConstructingEvent;
import psych_first.perception.knowledge.events.types.DamageEvent;
import psych_first.perception.knowledge.events.types.DeathEvent;
import psych_first.perception.knowledge.events.types.DestroyEvent;
import psych_first.perception.knowledge.events.types.DisappearEvent;
import psych_first.perception.knowledge.events.types.DropEvent;
import psych_first.perception.knowledge.events.types.GiveEvent;
import psych_first.perception.knowledge.events.types.PickupEvent;
import psych_first.perception.knowledge.events.types.SpawnEvent;
import psych_first.perception.knowledge.events.types.SproutEvent;
import psych_first.perception.knowledge.events.types.TakeEvent;
import psych_first.perception.knowledge.events.types.TalkingEvent;
import psych_first.perception.knowledge.events.types.TradeEvent;

public class EventType<T extends IEvent> implements IInformation {

	public static final EventType<DestroyEvent> DESTROY = new EventType<>(DestroyEvent.class, "destroy");
	public static final EventType<DamageEvent> DAMAGE = new EventType<>(DamageEvent.class, "damage");
	public static final EventType<DeathEvent> DEATH = new EventType<>(DeathEvent.class, "death");
	public static final EventType<AttackEvent> ATTACK = new EventType<>(AttackEvent.class, "attack");
	public static final EventType<TalkingEvent> TALK = new EventType<>(TalkingEvent.class, "talk");
	public static final EventType<TakeEvent> TAKE = new EventType<>(TakeEvent.class, "take");
	public static final EventType<GiveEvent> GIVE = new EventType<>(GiveEvent.class, "give");
	public static final EventType<PickupEvent> PICKUP = new EventType<>(PickupEvent.class, "pickup");
	public static final EventType<DropEvent> DROP = new EventType<>(DropEvent.class, "drop");
	public static final EventType<TradeEvent> TRADE = new EventType<>(TradeEvent.class, "trade");
	public static final EventType<ConstructingEvent> CONSTRUCT = new EventType<>(ConstructingEvent.class, "construct");
	public static final EventType<SpawnEvent> SPAWN = new EventType<>(SpawnEvent.class, "spawn");
	public static final EventType<BirthEvent> BIRTH = new EventType<>(BirthEvent.class, "birth");
	public static final EventType<SproutEvent> SPROUT = new EventType<>(SproutEvent.class, "sprout");
	public static final EventType<AppearEvent> APPEAR = new EventType<>(AppearEvent.class, "appear");
	public static final EventType<DisappearEvent> DISAPPEAR = new EventType<>(DisappearEvent.class, "disappear");
	public static final EventType<AnimateEvent> ANIMATE = new EventType<>(AnimateEvent.class, "animate");

	private Class<T> eventClass;
	String eventID;

	public EventType(Class<T> eventClass, String eventID) {
		this.eventClass = eventClass;
		this.eventID = eventID.startsWith("event_") ? eventID : "event_" + eventID;
	}

	public Class<T> getEventClass() {
		return eventClass;
	}

	public String getEventID() {
		return eventID;
	}

	@Override
	public String toString() {
		return "type;" + eventID;
	}

	@Override
	public EventType<T> clone() {
		return this;
	}
}