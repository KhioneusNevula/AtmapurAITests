package mind.concepts.identifiers;

import java.util.function.Function;

import actor.ITemplate;
import actor.IUniqueExistence;
import actor.IVisage;
import mind.concepts.type.Property;
import mind.memory.IPropertyData;

/**
 * Returns Presence if the predicate returns true
 * 
 * @author borah
 *
 */
public class TemplateBasedIdentifier implements IPropertyIdentifier {

	private ITemplate template;
	private Function<IUniqueExistence, IPropertyData> func;
	private double chance;

	public TemplateBasedIdentifier(ITemplate template, Function<IUniqueExistence, IPropertyData> func, double chance) {
		this.template = template;
		this.func = func;
		this.chance = chance;
	}

	@Override
	public IPropertyData identifyInfo(Property prop, IUniqueExistence forExistence, IVisage visage) {

		return (forExistence.getSpecies().equals(template) && forExistence.rand().nextDouble() <= chance
				? func.apply(forExistence)
				: IPropertyData.UNKNOWN);
	}

	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof TemplateBasedIdentifier))
			return false;
		TemplateBasedIdentifier tbi = (TemplateBasedIdentifier) obj;
		return tbi.template.equals(this.template);
	}

}
