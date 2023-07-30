package mind.thought_exp.memory;

import java.util.Collection;
import java.util.Map;
import java.util.TreeMap;
import java.util.UUID;

import com.google.common.collect.Multimap;
import com.google.common.collect.MultimapBuilder;

import mind.Culture;
import mind.action.IActionType;
import mind.concepts.PropertyController;
import mind.concepts.type.ILocationMeme;
import mind.concepts.type.IMeme;
import mind.concepts.type.IProfile;
import mind.concepts.type.Profile;
import mind.concepts.type.Property;
import mind.feeling.IFeeling;
import mind.goals.ITaskHint;
import mind.memory.AbstractKnowledgeEntity;
import mind.memory.IEmotions;
import mind.memory.IMindMemory;
import mind.memory.IPropertyData;
import mind.memory.MemoryEmotions;
import mind.memory.SenseMemory;

public class UpgradedBrain extends AbstractKnowledgeEntity implements IMindMemory {

	private boolean feelingCurious;
	private boolean socializedRecently;
	private Map<IProfile, Culture> cultures = new TreeMap<>();
	private Map<IMeme, IFeeling> feelings;
	private MemoryEmotions emotions = new MemoryEmotions();

	public UpgradedBrain(UUID selfID, String type) {
		super(selfID, type);
	}

	@Override
	public IFeeling getAssociatedFeeling(IMeme concept) {
		return feelings == null ? null : feelings.get(concept);
	}

	@Override
	public void prune(int passes) {
		// TODO prune memory

	}

	@Override
	public IEmotions getEmotions() {
		return this.emotions;
	}

	@Override
	public Map<Culture, IPropertyData> getPropertiesFromCulture(Profile prof, Property cat) {
		Map<Culture, IPropertyData> props = new TreeMap<>();
		for (Culture culture : this.cultures.values()) {
			props.put(culture, culture.getProperties(prof, cat));
		}
		return props;
	}

	@Override
	public Map<Culture, PropertyController> getPropertyAssociationsFromCulture(Property prop) {
		Map<Culture, PropertyController> props = new TreeMap<>();
		for (Culture culture : this.cultures.values()) {
			PropertyController con = culture.getPropertyAssociations(prop);
			if (con != null) {
				props.put(culture, con);
			}
		}
		return props;
	}

	@Override
	public Multimap<Culture, IActionType<?>> getPossibleActionsFromCulture(ITaskHint hint) {
		Multimap<Culture, IActionType<?>> acs = MultimapBuilder.treeKeys()
				.<IActionType<?>>treeSetValues((a, b) -> a.getUniqueName().compareTo(b.getUniqueName())).build();
		for (Culture culture : this.cultures.values()) {
			acs.putAll(culture, culture.getPossibleActions(hint));
		}
		return acs;
	}

	@Override
	public Map<Culture, ILocationMeme> getLocationsFromCulture(Profile prof) {
		Map<Culture, ILocationMeme> locs = new TreeMap<>();
		for (Culture culture : this.cultures.values()) {
			ILocationMeme lo = culture.getLocation(prof);
			locs.put(culture, lo);
		}
		return locs;
	}

	@Override
	public Multimap<Culture, Profile> getProfilesWithPropertyFromCulture(Property prop) {
		Multimap<Culture, Profile> profs = MultimapBuilder.treeKeys()
				.<Profile>treeSetValues((a, b) -> a.getUniqueName().compareTo(b.getUniqueName())).build();
		for (Culture culture : this.cultures.values()) {
			profs.putAll(culture, culture.getProfilesWithProperty(prop));
		}
		return profs;
	}

	@Override
	public Collection<Culture> cultures() {
		return cultures.values();
	}

	@Override
	public SenseMemory getSenses() {
		throw new UnsupportedOperationException();
	}

	@Override
	public void setFeelingCurious(boolean b) {
		this.feelingCurious = b;
	}

	@Override
	public boolean isFeelingCurious() {
		return feelingCurious;
	}

	@Override
	public boolean socializedRecently() {
		return socializedRecently;
	}

	@Override
	public IMindMemory addCulture(Culture a) {
		this.cultures.put(a.getSelfProfile(), a);
		return this;
	}

}
