package psych.conditions;

import entity.Actor;
import sociology.Profile;
import sociology.Sociocontype;
import sociology.Sociocontype.SocioconArgument;

public class CheckCondition<ValueType> {

	ValueType equalComparison = null;
	private Sociocontype sociocontype;

	public CheckCondition(Sociocontype type) {
		this.sociocontype = type;
	}

	public CheckCondition<ValueType> addEqualityCheck(ValueType equal) {
		equalComparison = equal;
		return this;
	}

	public ValueType getEqualComparison() {
		return equalComparison;
	}

	public Sociocontype getSociocontype() {
		return sociocontype;
	}

	public CheckCondition.CheckType getCheckType() {
		if (equalComparison != null)
			return CheckType.EQUALITY;
		return CheckType.SOCIOCONTYPE;
	}

	public boolean check(SocioconArgument<ValueType> arg, ValueType value) {
		return arg.getParent() == this.sociocontype && (equalComparison != null ? equalComparison.equals(value) : true);
	}

	public static enum CheckType {
		/**
		 * This is returned if no other checks are performed
		 */
		SOCIOCONTYPE, INTERVAL, LESS_THAN, GREATER_THAN, EQUALITY, PROFILE_EQUALITY, PROFILE_CONDITION
	}

	public static class ProfilePropertyCheck extends CheckCondition<Profile> {

		private ProfileCondition<?> con = null;

		public ProfilePropertyCheck(Sociocontype type) {
			super(type);
		}

		public ProfilePropertyCheck addPCCheck(ProfileCondition<?> con) {
			this.con = con;
			this.equalComparison = null;
			return this;
		}

		@Override
		public CheckType getCheckType() {
			if (con != null)
				return CheckType.PROFILE_CONDITION;
			if (this.getEqualComparison() != null)
				return CheckType.PROFILE_EQUALITY;
			return super.getCheckType();
		}

		public boolean check(Sociocontype.SocioconArgument<Profile> arg, Actor for_, Profile value) {
			return this.getSociocontype() == arg.getParent() && (con != null ? con.satisfies(for_, value) : true);
		}

	}

	public static class NumberPropertyCheck<NumberType extends Number> extends CheckCondition<NumberType> {

		private NumberType lowerBound = null;
		private boolean lInclusive = false;
		private NumberType upperBound = null;
		private boolean uInclusive = false;

		public NumberPropertyCheck(Sociocontype type) {
			super(type);
		}

		public NumberPropertyCheck<NumberType> addLowerBound(NumberType lower, boolean inclusive) {
			lowerBound = lower;
			this.lInclusive = inclusive;
			this.equalComparison = null;
			return this;
		}

		public NumberPropertyCheck<NumberType> addUpperBound(NumberType upper, boolean inclusive) {
			upperBound = upper;
			this.uInclusive = inclusive;
			this.equalComparison = null;
			return this;
		}

		public NumberType getLowerBound() {
			return lowerBound;
		}

		public NumberType getUpperBound() {
			return upperBound;
		}

		@Override
		public CheckType getCheckType() {
			if (this.upperBound != null && this.lowerBound != null)
				return CheckType.INTERVAL;
			if (this.upperBound != null)
				return CheckType.LESS_THAN;
			if (this.lowerBound != null)
				return CheckType.GREATER_THAN;
			if (this.equalComparison != null)
				return CheckType.EQUALITY;
			throw new IllegalStateException("Numeric check condition uninitialized");
		}

		@Override
		public boolean check(SocioconArgument<NumberType> arg, NumberType value) {
			double d = value.doubleValue();
			return arg.getParent() == this.getSociocontype() && (numericCheck(d));
		}

		public boolean numericCheck(double value) {
			boolean tot = true;
			if (equalComparison != null) {
				return Math.abs(value - equalComparison.doubleValue()) < 0.000000001;
			}
			if (lowerBound != null) {
				tot &= (lInclusive ? value >= lowerBound.doubleValue() : value > lowerBound.doubleValue());
			}
			if (upperBound != null) {
				tot &= (uInclusive ? value <= upperBound.doubleValue() : value < upperBound.doubleValue());
			}
			return tot;
		}
	}

}