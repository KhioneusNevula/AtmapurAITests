package mind.concepts.type;

public class Property implements IConcept, Comparable<Property> {

	private String uniqueName;
	private boolean hasIntegerProperty;
	private boolean hasProfileProperty;
	private boolean hasConceptProperty;
	private boolean hasConceptListProperty;
	private boolean hasProfileListProperty;

	public static Builder builder(String nam) {
		return Builder.start(nam);
	}

	public static class Builder {
		public static Builder start(String nam) {
			return new Builder(nam);
		}

		private Property cat;

		private Builder(String nam) {
			this.cat = new Property(nam);
		}

		public Property build() {
			return cat;
		}

		public Builder addConceptListProp() {
			cat.hasConceptListProperty = true;
			return this;
		}

		public Builder addConceptProp() {
			cat.hasConceptProperty = true;
			return this;
		}

		public Builder addProfileListProp() {
			cat.hasProfileListProperty = true;
			return this;
		}

		public Builder addProfileProp() {
			cat.hasProfileProperty = true;
			return this;
		}

		public Builder addIntProp() {
			cat.hasIntegerProperty = true;
			return this;
		}
	}

	private Property(String uniqueName) {
		this.uniqueName = "property_" + uniqueName;
	}

	public static Property create(String name) {
		return new Property(name);
	}

	@Override
	public String getUniqueName() {
		return uniqueName;
	}

	/**
	 * If this concept only is checked for presence, and no other values
	 * 
	 * @return
	 */
	public boolean isOnlyPresence() {
		return !(hasConceptProperty || hasIntegerProperty || hasProfileProperty || hasConceptListProperty
				|| hasProfileListProperty);
	}

	/**
	 * Whether this category has a property pointing toward another concept. For
	 * example, a category pertaining to a Name has a property pointing toward a
	 * concept describing that Name
	 * 
	 * @return
	 */
	public boolean hasConceptProperty() {
		return hasConceptProperty;
	}

	/**
	 * Whether this category has a property pointing toward an integer. For example,
	 * a category pertaining to being able to move has a numeric property of Speed
	 * 
	 * @return
	 */
	public boolean hasIntegerProperty() {
		return hasIntegerProperty;
	}

	/**
	 * Whether this category has a property pointing toward a profile. For example,
	 * a category pertaining to be able to hold things has a profile pointing toward
	 * holding things.
	 * 
	 * @return
	 */
	public boolean hasProfileProperty() {
		return hasProfileProperty;
	}

	/**
	 * Whether this category has a property pointing toward a list of other concepts
	 * 
	 * @return
	 */
	public boolean hasConceptListProperty() {
		return hasConceptListProperty;
	}

	/**
	 * Whether this category has a property pointing toward a list of other profiles
	 * (e.g. a storage stores multiple items)
	 * 
	 * @return
	 */
	public boolean hasProfileListProperty() {
		return hasProfileListProperty;
	}

	@Override
	public int compareTo(Property o) {
		return this.uniqueName.compareTo(o.uniqueName);
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder(this.uniqueName);
		/*
		 * if (!this.isOnlyPresence()) builder.append("{"); if (this.hasIntegerProperty)
		 * builder.append("int;"); if (this.hasProfileProperty)
		 * builder.append("profile;"); if (this.hasConceptProperty)
		 * builder.append("concept;"); if (this.hasProfileListProperty)
		 * builder.append("profile[];"); if (this.hasConceptListProperty)
		 * builder.append("concept[];"); if (!this.isOnlyPresence())
		 * builder.append("}");
		 */
		return builder.toString();
	}

}
