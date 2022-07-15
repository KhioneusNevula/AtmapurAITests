package sociology;

import java.util.UUID;

import culture.CulturalContext;
import psych_first.perception.knowledge.IKnowledgeType;
import sim.IHasProfile;

public class Placeholder implements IHasProfile {

	private String name;

	private Profile profile;

	public Placeholder(UUID name) {
		this.name = name.toString();
		this.profile = new Profile(this.name) {

			@Override
			public void setOwner(IHasProfile owner) {
				throw new UnsupportedOperationException();
			}

			@Override
			public boolean isTypeProfile() {
				return false;
			}

			@Override
			public TypeProfile getTypeProfile() {
				return null;
			}

			@Override
			public IHasProfile getOwner() {
				return Placeholder.this;
			}
		};
	}

	@Override
	public Profile getProfile() {

		return profile;
	}

	@Override
	public TypeProfile getType() {
		return null;
	}

	@Override
	public boolean hasInfo(IKnowledgeType<?> type, CulturalContext ctxt) {
		return profile.hasInfo(type, ctxt);
	}

	@Override
	public <T> T getInfo(IKnowledgeType<T> type, CulturalContext ctxt) {
		return profile.getInfo(type, ctxt);
	}

	@Override
	public UUID getUuid() {
		return UUID.fromString(name);
	}

}
