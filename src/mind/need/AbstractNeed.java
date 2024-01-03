package mind.need;

public abstract class AbstractNeed implements INeed {

	private INeedType type;
	private INeed.Degree degree;

	public AbstractNeed(INeedType type, INeed.Degree degree) {
		this.type = type;
		this.degree = degree;
	}

	public INeed.Degree getDegree() {
		return degree;
	}

	public INeedType getType() {
		return type;
	}

	@Override
	public String toString() {
		return this.getClass().getSimpleName() + "{" + this.type.toString() + "," + degree + "}";
	}

	@Override
	public IMemeType getMemeType() {
		return MemeType.NEED;
	}

	@Override
	public String getUniqueName() {
		return toString();
	}

}
