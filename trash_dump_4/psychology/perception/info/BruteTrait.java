package psychology.perception.info;

/**
 * a trait that is not socially constructed in nature
 * 
 * @author borah
 *
 */
public class BruteTrait<T> extends Trait<T> {

	public BruteTrait(String name, KDataType<T> dataType) {
		super(name, dataType);
	}

	@Override
	public boolean isSocial() {
		return false;
	}
}
