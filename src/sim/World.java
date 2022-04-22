package sim;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import entity.Actor;
import psych.conditions.ProfileCondition;
import sociology.Sociocon;
import sociology.Sociocontype;

public class World {

	private List<Actor> actors = new ArrayList<>();

	private TreeMap<Sociocontype, Map<String, Sociocon>> sociocons = new TreeMap<>();

	public World() {

	}

	public void spawnActor(Actor a) {
		this.actors.add(a);
		this.actors.forEach((e) -> e.notifyOfSpawn(a));
	}

	public List<Actor> getActors() {
		return new ArrayList<>(actors);
	}

	public Map<String, Sociocon> getSocioconMap(Sociocontype type) {
		return sociocons.computeIfAbsent(type, (a) -> new TreeMap<>());
	}

	public void tick() {
		for (Actor e : actors) {
			e.baseTick();
		}
	}

	public Set<Actor> getAt(int x, int y) {
		return actors.stream().filter((a) -> a.getX() == x && a.getY() == y).collect(Collectors.toSet());
	}

	public Set<Actor> getFor(ProfileCondition<?> condition, Actor ar) {
		return actors.stream().filter((a) -> condition.satisfies(ar, a.getProfile())).collect(Collectors.toSet());
	}

	public Set<Actor> getFor(Predicate<Actor> predicate) {
		return actors.stream().filter(predicate).collect(Collectors.toSet());
	}
}
