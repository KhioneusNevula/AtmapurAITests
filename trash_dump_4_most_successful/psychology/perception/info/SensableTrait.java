package psychology.perception.info;

import com.google.common.collect.ImmutableSet;

import psychology.perception.sense.Sense;

public class SensableTrait<T> extends BruteTrait<T> {

	private ImmutableSet<Sense> senses;

	public SensableTrait(String name, KDataType<T> dataType, Sense... senses) {
		super(name, dataType);
		this.senses = ImmutableSet.copyOf(senses);
	}

	public ImmutableSet<Sense> getSenses() {
		return senses;
	}

}
