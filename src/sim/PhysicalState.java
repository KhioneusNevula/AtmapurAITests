package sim;

public enum PhysicalState {
	SOLID_WHOLE, SOLID_DAMAGED, SOLID_POWDER, SOLID_FLEXIBLE, SQUISHY_WHOLE, SQUISHY_DAMAGED, SQUISHY_PULPED,
	SQUISHY_PASTE, LIQUID, GAS, PLASMA,
	/** for things with multiple parts, to indicate it has missing parts */
	GONE,
	/**
	 * for things that do not interact with normal matter but exist, like ghosts or
	 * idk
	 */
	METAPHYSICAL;

	/**
	 * Whether this substance is a (hard) solid
	 * 
	 * @return
	 */
	public boolean isSolid() {
		return this == SOLID_WHOLE || this == SOLID_DAMAGED || SOLID_POWDER == this || SOLID_FLEXIBLE == this;
	}

	/**
	 * Whether this substance is a soft pliable sort of solid
	 * 
	 * @return
	 */
	public boolean isSquishy() {
		return this == SQUISHY_WHOLE || this == SQUISHY_DAMAGED || this == SQUISHY_PULPED || this == SQUISHY_PASTE;
	}

	/**
	 * whether this substance is liquid
	 * 
	 * @return
	 */
	public boolean isLiquid() {
		return this == LIQUID;
	}

	/**
	 * whether this substance is gas
	 * 
	 * @return
	 */
	public boolean isGas() {
		return this == GAS;
	}

	/**
	 * If this is plasma
	 * 
	 * @return
	 */
	public boolean isPlasma() {
		return this == PLASMA;
	}

	/**
	 * If this substance is no longer present
	 * 
	 * @return
	 */
	public boolean gone() {
		return this == GONE;
	}

	/**
	 * IF the substance is now metaphysical
	 * 
	 * @return
	 */
	public boolean metaphysical() {
		return this == METAPHYSICAL;
	}

	/**
	 * Whether this substance is damaged (i.e. damaged, pulped, powder, or paste)
	 * 
	 * @return
	 */
	public boolean isDamaged() {
		return this == SOLID_DAMAGED || this == SQUISHY_DAMAGED || this == SQUISHY_PULPED || this == SQUISHY_PASTE
				|| this == SOLID_POWDER;
	}

	/**
	 * Whether this substance is pulped
	 * 
	 * @return
	 */
	public boolean isPulped() {
		return this == SQUISHY_PULPED;
	}

	/**
	 * Whether this substance is paste
	 */
	public boolean isPaste() {
		return this == SQUISHY_PASTE;
	}

	/**
	 * Whether this substance is powder
	 * 
	 * @return
	 */
	public boolean isPowder() {
		return this == SOLID_POWDER;
	}

	/**
	 * Whether this substance is composed of flexible strands
	 * 
	 * @return
	 */
	public boolean isFlexible() {
		return this == SOLID_FLEXIBLE;
	}
}