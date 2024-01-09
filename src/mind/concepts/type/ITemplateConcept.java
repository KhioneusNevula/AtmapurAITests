package mind.concepts.type;

import actor.ITemplate;

public interface ITemplateConcept extends IMeme, Comparable<ITemplateConcept> {

	@Override
	default IMemeType getMemeType() {
		return MemeType.TEMPLATE;
	}

	ITemplate getTemplate();

	/**
	 * how unique each member of this template should be considered; 1.0f means no
	 * assumptions will ever get made about any member of this template, whereas
	 * 0.0f means that every member of this template experiences the same
	 * assumptions at full strength. Similarly, 1.0f means that any trait that one
	 * member of the template possesses will never be applied to the entire
	 * template, whereas 0.0f means any trait a single member of the template
	 * possesses will always be assumed for the entirety of the template. 1.0f and
	 * 0.0f should not be values used for any template, but the values can get very
	 * close to either number
	 * 
	 * @return
	 */
	float uniquenessOfMembers();
}
