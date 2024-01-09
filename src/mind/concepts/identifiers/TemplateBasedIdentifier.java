package mind.concepts.identifiers;

import actor.ITemplate;
import actor.IUniqueExistence;
import actor.IVisage;
import mind.concepts.type.Property;
import mind.memory.IPropertyData;
import mind.thought_exp.IUpgradedHasKnowledge;

/**
 * Returns Presence if the predicate returns true
 * 
 * @author borah
 *
 */
public class TemplateBasedIdentifier implements IPropertyIdentifier {

	private ITemplate template;
	private double chance;

	public TemplateBasedIdentifier(ITemplate template, double chance) {
		this.template = template;
		this.chance = chance;
	}

	@Override
	public IPropertyData identifyInfo(Property prop, IUniqueExistence forExistence, IVisage visage,
			IUpgradedHasKnowledge ihk) {

		return (forExistence.getVisage().getSpecies().equals(template) && forExistence.rand().nextDouble() <= chance
				? forExistence.getVisage().getSpecies().getPropertyHint(prop)
				: IPropertyData.UNKNOWN);
	}

	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof TemplateBasedIdentifier))
			return false;
		TemplateBasedIdentifier tbi = (TemplateBasedIdentifier) obj;
		return tbi.template.equals(this.template);
	}

	@Override
	public String toString() {
		return "TemplateID:" + this.template;
	}

	@Override
	public int hashCode() {
		return this.template.hashCode();
	}

}
