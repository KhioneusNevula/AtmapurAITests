package sim;

import java.util.UUID;

import abilities.ISystemHolder;
import abilities.types.SystemType;
import culture.CulturalContext;
import entity.ISensable;
import psych_first.perception.knowledge.IKnowledgeType;
import sociology.Profile;
import sociology.TypeProfile;

public interface IHasProfile {

	public Profile getProfile();

	public default boolean hasProfile() {
		return getProfile() == null;
	}

	public default String getName() {
		return getProfile().getName();
	}

	public TypeProfile getType();

	public boolean hasInfo(IKnowledgeType<?> type, CulturalContext ctxt);

	public <T> T getInfo(IKnowledgeType<T> type, CulturalContext ctxt);

	public UUID getUuid();

	public default boolean canBeSensed() {
		return this instanceof ISensable p && p.getSensory() != null;
	}

	public default boolean canThink() {
		return this instanceof ICanHaveMind m && m.hasMind();
	}

	public default boolean canBeAlive() {
		return this instanceof ISystemHolder sh && sh.hasSystem(SystemType.LIFE);
	}

}
