package psych_first.perception.knowledge;

import java.util.Collection;

public interface IKnowledgeCategory<T> extends IKnowledgeType<Collection> {

	public Class<T> getElementClass();

	@Override
	default Class<Collection> getValueClass() {
		return Collection.class;
	}
}
