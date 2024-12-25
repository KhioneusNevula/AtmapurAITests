package civilization_and_minds.group.purpose;

public class SocietyPurpose implements IGroupPurpose {

	private GroupType type;
	private String name;

	public SocietyPurpose(String name, GroupType type) {
		this.type = type;
		this.name = name;
	}

	/**
	 * Return this cast to SettlementPurpose
	 * 
	 * @return
	 */
	public SettlementPurpose asSettlement() {
		return (SettlementPurpose) this;
	}

	@Override
	public String getUniqueName() {
		return type.toString().toLowerCase() + "_" + name;
	}

	@Override
	public GroupType getGroupType() {
		return type;
	}

	@Override
	public String getName() {
		return name;
	}

}
