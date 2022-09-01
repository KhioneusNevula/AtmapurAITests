package psychology.perception.info;

import psychology.perception.Profile;
import psychology.social.culture.Group;

public interface InfoUnit<T> extends InfoKey {

	public KDataType<T> getDataType();

	public boolean hasValue(Profile toCheck);

	/**
	 * returns the group associated with this info. Usually null
	 * 
	 * @return
	 */
	public default Group getGroup() {
		return null;
	}

	/**
	 * if this fact is observable in something directly or only observable by proxy
	 * of a cultural viewpoint
	 * 
	 * @return
	 */
	public boolean isSocial();

	public interface InfoUnitOfPresence extends InfoUnit<Boolean> {
		public default KDataType<Boolean> getDataType() {
			return KDataType.BOOLEAN;
		}

	}

}
