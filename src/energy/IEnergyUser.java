package energy;

public interface IEnergyUser extends IEnergyHandler {

	/**
	 * Returns how much energy this is using per tick
	 * 
	 * @return
	 */
	public double getPowerUse();

}
