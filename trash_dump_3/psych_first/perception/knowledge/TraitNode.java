package psych_first.perception.knowledge;

public class TraitNode<T> implements ICircumstance, IInformation {

	private IKnowledgeType<T> type;
	private T data;

	public TraitNode(IKnowledgeType<T> type, Object data) {
		this.data = (T) data;
		this.type = type;
	}

	public T getData() {
		return data;
	}

	public IKnowledgeType<T> getType() {
		return type;
	}

	@Override
	public TraitNode<T> clone() throws CloneNotSupportedException {
		return (TraitNode<T>) super.clone();
	}

	@Override
	public int hashCode() {
		return this.data.hashCode() + this.type.hashCode();
	}

	@Override
	public boolean equals(Object obj) {
		return obj instanceof TraitNode<?>nod ? this.type.equals(nod.type) && this.data.equals(nod.data) : false;
	}

}
