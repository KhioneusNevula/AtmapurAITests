package energy;

public interface IEnergyUnit {

	public static enum EnergyUnit implements IEnergyUnit {
		/** energy as a simple value */
		RAW(1, "ergs"),
		/** energy of temperature and fire */
		HEAT(1, "heats"),
		/** energy units of light TODO make this more accurate */
		LIGHT(0.0001, "lumes"),
		/** energy units of life */
		LIFE(10000, "anims"),
		/**
		 * energy units of the type used in mechanisms and in lightning, a
		 * simplification of electricity
		 */
		ELECTRO(10, "zaps");

		private double inRaws;
		private String unitName;

		/**
		 * @param oneInRaw = one unit of this energy in raw units
		 */
		private EnergyUnit(double oneInRaw, String unitName) {
			this.inRaws = oneInRaw;
			this.unitName = unitName;
		}

		public String getUnitName() {
			return unitName;
		}

		public double getOneInRaw() {
			return inRaws;
		}

		@Override
		public double convertToRaw(double energyValue) {
			return energyValue * inRaws;
		}

		@Override
		public double convertFromRaw(double rawValue) {
			return rawValue / inRaws;
		}
	}

	public double convertToRaw(double energyValue);

	public double convertFromRaw(double rawValue);

	/**
	 * returns the name of this unit as displayed
	 * 
	 * @return
	 */
	public String getUnitName();

	/**
	 * returns string showing the amount as number units
	 */
	public default String disp(double amount) {

		return amount + " " + this.getUnitName();
	}

	/**
	 * does a direct conversion to the other energy type
	 * 
	 * @param other
	 * @param value
	 * @return
	 */
	public default double convertTo(IEnergyUnit to, double value) {
		return to.convertFromRaw(this.convertToRaw(value));
	}

}
