package mind.concepts.type;

import java.util.UUID;

import sim.interfaces.IUnique;

public interface IProfile extends IMeme, IUnique {

	/**
	 * indicates the haver of a goal
	 */
	public static final IProfile SELF = new IProfile() {
		private UUID selfID = UUID.fromString("c90fb95e-2b46-4bef-9de9-034ef0627dcf");

		@Override
		public String getUniqueName() {
			return "__self";
		}

		@Override
		public UUID getUUID() {
			return selfID;
		}
	};

	/**
	 * Indicates the profile that is the other end of a relaitonship
	 */
	public static final IProfile RELATIONSHIP_PARTY = new IProfile() {
		private UUID atID = UUID.fromString("c35f3e35-2992-4f02-bc55-103dde4b857b");

		public String getUniqueName() {
			return "__relationship_party";
		}

		public UUID getUUID() {
			return atID;
		}
	};

}
