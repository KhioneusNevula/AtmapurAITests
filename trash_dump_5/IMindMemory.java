package mind.memory;

import java.util.Collection;
import java.util.Map;

import com.google.common.collect.Multimap;

import mind.Culture;
import mind.action.IActionType;
import mind.concepts.PropertyController;
import mind.concepts.type.ILocationMeme;
import mind.concepts.type.Profile;
import mind.concepts.type.Property;
import mind.goals.IGoal;
import mind.goals.ITaskHint;
import mind.need.INeed;

public interface IMindMemory extends IKnowledgeBase {

	public IEmotions getEmotions();

	/**
	 * Returns the knowledge of the properties from the culture, since an individual
	 * may have conflicting cultural AND individual info
	 */
	Map<Culture, IPropertyData> getPropertiesFromCulture(Profile prof, Property cat);

	Map<Culture, PropertyController> getPropertyAssociationsFromCulture(Property prop);

	Multimap<Culture, IActionType<?>> getPossibleActionsFromCulture(ITaskHint hint);

	Map<Culture, ILocationMeme> getLocationsFromCulture(Profile prof);

	Multimap<Culture, Profile> getProfilesWithPropertyFromCulture(Property prop);

	Collection<Culture> cultures();

	public SenseMemory getSenses();

	public void setFeelingCurious(boolean b);

	public boolean isFeelingCurious();

	public boolean socializedRecently();

	/**
	 * Rather than just getting the associations, it creates them if they're absent
	 * 
	 * @param prop
	 * @return
	 */
	public PropertyController findPropertyAssociations(Property prop);

	public void learnLocation(Profile self, ILocationMeme location);

	public IMindMemory addCulture(Culture a);

	public void forgetGoal(IGoal iGoal);

	public void forgetNeed(INeed last);

	public void addGoal(IGoal genIndividualGoal);

}
