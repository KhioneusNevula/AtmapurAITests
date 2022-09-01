package sociology.sociocon;

public interface IPurposeElement {
	public IPurposeSource getOrigin();

	public String getName();

	/**
	 * Set origin and return self
	 * 
	 * @param source
	 * @return
	 */
	public IPurposeElement setOrigin(IPurposeSource source);
}
