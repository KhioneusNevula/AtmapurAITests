package _nonsense;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.BiPredicate;
import java.util.function.Function;
import java.util.stream.Collectors;

import entity.Actor;

public interface ConditionType<T extends ConditionType<T>> {

	public Set<T> getSubtypeSet();

	public List<T> valueList();

	public default boolean isSubtype(T other) {
		return getSubtypeSet().contains(other);
	}

	public default boolean isSupertype(T other) {
		return other.getSubtypeSet().contains(this);
	}

	public default Set<T> getSupertypeSet() {
		return valueList().stream().filter((a) -> this.isSupertype(a)).collect(Collectors.toSet());
	}

	public static enum ProfileType implements ConditionType<ProfileType> {
		PROFILE;

		@Override
		public List<ProfileType> valueList() {
			return Arrays.asList(values());
		}

		@Override
		public Set<ProfileType> getSubtypeSet() {
			return Set.of();
		}
	}

	public static enum RelationalType implements ConditionType<RelationalType> {
		HELD(actorPropertyCheck(Actor::getHeld), actorPropertyCheckCompleted(Actor::getHeld)) {
			@Override
			public Set<RelationalType> getSubtypeSet() {
				return Set.of(AT);
			}
		},
		WORN(actorPropertyCheck(Actor::getClothing), actorPropertyCheckCompleted(Actor::getClothing)) {
			@Override
			public Set<RelationalType> getSubtypeSet() {
				return Set.of(AT);
			}
		},
		AT((a, p) -> {

			return a.getWorld().getAt(a.getX(), a.getY()).stream().filter((b) -> p.satisfies(a, b.getProfile()))
					.findAny().isPresent();
		}, (a, p) -> {
			if (p.getType() == Profile.Type.ACTOR) {
				return p.getOriginalActor().distance(a) <= 0;
			} else {
				return false;
			}
		}) {
			@Override
			public Set<RelationalType> getSubtypeSet() {
				return Set.of(REACHABLE);
			}
		},

		REACHABLE((a, p) -> a.getWorld().getActors().stream().filter((m) -> a.reachable(m)).findAny().isPresent(),
				(a, p) -> p.getType() == Profile.Type.ACTOR ? a.reachable(p.getOriginalActor()) : false);

		private BiPredicate<Actor, ProfileCondition<?>> checker;
		private BiPredicate<Actor, Profile> completedChecker;

		public static BiPredicate<Actor, ProfileCondition<?>> actorPropertyCheck(Function<Actor, Actor> getter) {
			return (a, p) -> {
				Actor c = getter.apply(a);
				return c == null ? false : p.satisfies(a, c.getProfile());
			};
		}

		public static BiPredicate<Actor, Profile> actorPropertyCheckCompleted(Function<Actor, Actor> getter) {
			return (a, p) -> {
				Actor c = getter.apply(a);
				return c == null ? false : c.getProfile() == p;
			};
		}

		private RelationalType(BiPredicate<Actor, ProfileCondition<?>> check,
				BiPredicate<Actor, Profile> completedChecker) {
			this.checker = check;
			this.completedChecker = completedChecker;
		}

		public boolean check(Actor a, ProfileCondition<?> p) {
			return checker.test(a, p);
		}

		public boolean check(Actor a, Profile p) {
			return completedChecker.test(a, p);
		}

		public List<RelationalType> valueList() {
			return Arrays.asList(values());
		}

		@Override
		public Set<RelationalType> getSubtypeSet() {
			return Set.of();
		}
	}

	public static final ConditionType<ProfileType> ALL_PROFILE = new ConditionType<>() {

		private Set<ProfileType> subtypeSet = new HashSet<>(Arrays.asList(ProfileType.values()));

		@Override
		public Set<ProfileType> getSubtypeSet() {

			return subtypeSet;
		}

		@Override
		public List<ProfileType> valueList() {
			return List.of(ProfileType.values());
		}
	};
	public static final ConditionType<RelationalType> ALL_RELATIONAL = new ConditionType<>() {

		private Set<RelationalType> subtypeSet = new HashSet<>(Arrays.asList(RelationalType.values()));

		@Override
		public Set<RelationalType> getSubtypeSet() {
			return subtypeSet;
		}

		@Override
		public List<RelationalType> valueList() {
			return List.of(RelationalType.values());
		}
	};
}
