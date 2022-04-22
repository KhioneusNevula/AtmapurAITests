package psych.conditions;

import java.util.UUID;

import entity.Actor;
import psych.conditions.ConditionType.ProfileType;
import sociology.Profile;
import sociology.Sociocontype.SocioconArgument;

public class ProfileCondition<ArgType> extends ActionCondition implements Comparable<ProfileCondition<?>> {

	private CheckCondition<ArgType> checker;
	private SocioconArgument<ArgType> argument;
	public static final ProfileCondition<?> ALL = new ProfileCondition<Object>();
	private UUID id = UUID.randomUUID();

	public static <T> CheckCondition<T> initCheck(SocioconArgument<T> arg) {
		return new CheckCondition<>(arg.getParent());
	}

	public ProfileCondition(SocioconArgument<ArgType> argument, CheckCondition<ArgType> checker) {
		super(ProfileType.PROFILE);
		this.checker = checker;
	}

	private ProfileCondition() {
		super(ProfileType.ALL_PROFILE);
	}

	public SocioconArgument<ArgType> getArgument() {
		return argument;
	}

	public CheckCondition<ArgType> getChecker() {
		return checker;
	}

	@Override
	public ConditionType.ProfileType getType() {
		return (ProfileType) super.getType();
	}

	@Override
	public boolean matches(ActionCondition other) {
		if (this.isAll() || other.isAll()) {
			return true;
		}
		return super.matches(other)
				&& this.checker.getCheckType() == ((ProfileCondition<?>) other).checker.getCheckType()
				&& this.argument == ((ProfileCondition<?>) other).argument;
	}

	public boolean satisfies(Actor for_, Profile profile) {
		if (this.isAll()) {
			return true;
		}
		return this.checker.check(argument, profile.getSociocon(argument.getParent()).getValue(argument));
	}

	@Override
	public int compareTo(ProfileCondition<?> o) {
		return id.compareTo(o.id);
	}

}
