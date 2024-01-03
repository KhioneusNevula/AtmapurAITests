package phenomenon;

import java.util.Random;
import java.util.UUID;

import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Table;

import mind.concepts.type.Property;
import mind.thought_exp.memory.IUpgradedKnowledgeBase;
import mind.memory.IPropertyData;
import sim.World;

/**
 * TODO someday do phenomenon
 * 
 * @author borah
 *
 */
public abstract class Phenomenon implements IPhenomenon {

	private World world;
	private UUID id = UUID.randomUUID();
	private IPhenomenonType type;
	private Table<IUpgradedKnowledgeBase, Property, IPropertyData> socialProperties;

	public Phenomenon(IPhenomenonType type) {
		this.type = type;
	}

	@Override
	public void assignProperty(IUpgradedKnowledgeBase culture, Property property, IPropertyData data) {
		(socialProperties == null ? socialProperties = HashBasedTable.create() : socialProperties).put(culture,
				property, data);
	}

	@Override
	public IPropertyData getPropertyData(IUpgradedKnowledgeBase culture, Property property) {
		return socialProperties == null ? IPropertyData.UNKNOWN : socialProperties.get(culture, property);
	}

	@Override
	public World getWorld() {
		return world;
	}

	@Override
	public UUID getUUID() {
		return id;
	}

	@Override
	public IPhenomenonType type() {
		return type;
	}

	@Override
	public IPhenomenonType getSpecies() {
		return type;
	}

	@Override
	public Random rand() {
		return this.world.rand();
	}
}
