package energy;

public interface IEnergySource extends IEnergyHandler {

	/**
	 * returns how many energy units are generated per tick from this
	 * 
	 * @return
	 */
	public double getPowerOutput();

}
