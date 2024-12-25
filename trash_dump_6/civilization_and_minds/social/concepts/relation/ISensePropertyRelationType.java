package civilization_and_minds.social.concepts.relation;

import actor.construction.properties.ISensableTrait;
import actor.construction.properties.SenseProperty;

/**
 * Relation type expressing that the passive element is a sensable trait of the
 * active element, as per the given sense-property.
 * 
 * @author borah
 *
 * @param <T>
 */
public interface ISensePropertyRelationType<T extends ISensableTrait, B extends ISensePropertyRelationType<?, ?>>
		extends IConceptRelationType<B> {

	/**
	 * Get the property that this relation encodes
	 * 
	 * @return
	 */
	public SenseProperty<T> getProperty();

	public class HasSensePropertyRelationType<T extends ISensableTrait>
			implements ISensePropertyRelationType<T, HasSensePropertyRelationType<T>.IsSensePropertyOfRelationType> {

		private SenseProperty<T> property;
		private IsSensePropertyOfRelationType inverse;

		public static <T extends ISensableTrait> HasSensePropertyRelationType<T> of(SenseProperty<T> property) {
			return new HasSensePropertyRelationType<>(property);
		}

		private HasSensePropertyRelationType(SenseProperty<T> property) {
			this.property = property;
			this.inverse = new IsSensePropertyOfRelationType();
		}

		/**
		 * Get the property that this relation encodes
		 * 
		 * @return
		 */
		@Override
		public SenseProperty<T> getProperty() {
			return property;
		}

		@Override
		public String getUniqueName() {
			return "relation_" + this.property.toString();
		}

		@Override
		public IsSensePropertyOfRelationType inverse() {
			return inverse;
		}

		@Override
		public boolean bidirectional() {
			return false;
		}

		@Override
		public boolean leftIsAgent() {
			return true;
		}

		@Override
		public boolean leftIsObject() {
			return false;
		}

		@Override
		public boolean creates() {
			return false;
		}

		@Override
		public boolean transfers() {
			return false;
		}

		@Override
		public boolean transforms() {
			return false;
		}

		@Override
		public boolean requiresArgument() {
			return false;
		}

		@Override
		public boolean requiresAction() {
			return false;
		}

		@Override
		public boolean consumes() {
			return false;
		}

		@Override
		public boolean atLocation() {
			return false;
		}

		@Override
		public boolean madeOf() {
			return false;
		}

		@Override
		public boolean tradeWorth() {
			return false;
		}

		@Override
		public boolean dominates() {
			return false;
		}

		@Override
		public boolean isProperty() {
			return true;
		}

		@Override
		public boolean linguistic() {
			return false;
		}

		@Override
		public boolean damages() {
			return false;
		}

		@Override
		public boolean social() {
			return false;
		}

		@Override
		public boolean copular() {
			return true;
		}

		@Override
		public boolean has() {
			return false;
		}

		@Override
		public boolean equals(Object obj) {
			if (obj instanceof HasSensePropertyRelationType po) {
				return this.property.equals(po.property);
			}
			return super.equals(obj);
		}

		@Override
		public int hashCode() {
			return this.property.hashCode() * 2;
		}

		public class IsSensePropertyOfRelationType
				implements ISensePropertyRelationType<T, HasSensePropertyRelationType<T>> {

			@Override
			public String getUniqueName() {
				return "inverserelation_" + HasSensePropertyRelationType.this.property.toString();
			}

			@Override
			public HasSensePropertyRelationType<T> inverse() {
				return HasSensePropertyRelationType.this;
			}

			@Override
			public boolean bidirectional() {
				return false;
			}

			@Override
			public boolean leftIsAgent() {
				return false;
			}

			@Override
			public boolean leftIsObject() {
				return true;
			}

			@Override
			public boolean creates() {
				return false;
			}

			@Override
			public boolean transfers() {
				return false;
			}

			@Override
			public boolean transforms() {
				return false;
			}

			@Override
			public boolean requiresArgument() {
				return false;
			}

			@Override
			public boolean requiresAction() {
				return false;
			}

			@Override
			public boolean consumes() {
				return false;
			}

			@Override
			public boolean atLocation() {
				return false;
			}

			@Override
			public boolean madeOf() {
				return false;
			}

			@Override
			public boolean tradeWorth() {
				return false;
			}

			@Override
			public boolean dominates() {
				return false;
			}

			@Override
			public boolean isProperty() {
				return true;
			}

			@Override
			public boolean linguistic() {
				return false;
			}

			@Override
			public boolean damages() {
				return false;
			}

			@Override
			public boolean social() {
				return false;
			}

			@Override
			public boolean copular() {
				return true;
			}

			@Override
			public boolean has() {
				return false;
			}

			@Override
			public boolean equals(Object obj) {
				if (obj instanceof HasSensePropertyRelationType<?>.IsSensePropertyOfRelationType isport) {
					return HasSensePropertyRelationType.this.equals(isport.inverse());
				}
				return HasSensePropertyRelationType.this.equals(obj);
			}

			@Override
			public int hashCode() {
				return HasSensePropertyRelationType.this.hashCode() * -1;
			}

			/**
			 * Get the property that this relation encodes
			 * 
			 * @return
			 */
			@Override
			public SenseProperty<T> getProperty() {
				return HasSensePropertyRelationType.this.getProperty();
			}

		}

	}
}
