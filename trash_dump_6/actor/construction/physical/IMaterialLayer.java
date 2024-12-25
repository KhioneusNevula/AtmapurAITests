package actor.construction.physical;

import java.util.Collection;
import java.util.Map;

import actor.construction.properties.ISensableTrait;
import actor.construction.properties.SenseProperty;
import sim.physicality.PhysicalState;

public interface IMaterialLayer {

	public IMaterialLayerType getType();

	public PhysicalState getState();

	/**
	 * Throw an illegal argument exception if the new state is the same.<br>
	 * REMEMBER TO CALL {@link IPhysicalActorObject#updatePart(IComponentPart)}
	 * 
	 * @param state
	 */
	public void changeState(PhysicalState state);

	public Map<? extends IMaterialLayerType, ? extends IMaterialLayer> getSubLayers();

	/**
	 * 
	 * @param <A>
	 * @param property
	 * @param ignoreType whether to only get traits specific to this specific
	 *                   material layer and not the type as a whole
	 * @return
	 */
	public <A extends ISensableTrait> A getProperty(SenseProperty<A> property, boolean ignoreType);

	public Collection<SenseProperty<? extends ISensableTrait>> getSensableProperties();

	/**
	 * Whether this particular layer of material is "usual," i.e. undamaged, etc
	 * 
	 * @return
	 */
	public boolean isUsual();

}
