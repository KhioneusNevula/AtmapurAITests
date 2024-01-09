package mind.concepts.type;

import actor.ITemplate;

public class TemplateConcept implements ITemplateConcept {

	private ITemplate template;
	private float uniqueness = 0.3f;

	public TemplateConcept(ITemplate forTemplate) {
		this.template = forTemplate;
		uniqueness = forTemplate.averageUniqueness();
	}

	public TemplateConcept setUniqueness(float uniqueness) {
		this.uniqueness = uniqueness;
		return this;
	}

	@Override
	public float uniquenessOfMembers() {
		return uniqueness;
	}

	@Override
	public String getUniqueName() {
		return "TC(" + template.toString() + ")";
	}

	@Override
	public ITemplate getTemplate() {
		return template;
	}

	@Override
	public boolean equals(Object obj) {

		return obj instanceof ITemplateConcept && ((ITemplateConcept) obj).getTemplate().equals(this.template)
				|| obj instanceof ITemplate && this.getTemplate().equals(obj);

	}

	@Override
	public int compareTo(ITemplateConcept o) {
		return (this.template.getUniqueName() + this.uniqueness)
				.compareTo(o.getTemplate().getUniqueName() + o.uniquenessOfMembers());
	}

	@Override
	public int hashCode() {
		return this.template.hashCode() + Float.hashCode(this.uniqueness);
	}

	@Override
	public String toString() {
		return this.getUniqueName();
	}

}
