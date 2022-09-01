package psych_first.perception.knowledge;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import main.ImmutableCollection;

public class SocialIdentifier implements Cloneable {

	private Set<String> names = new HashSet<>(); // TODO maybe make these their own object type

	private Set<String> epithets = new HashSet<>();

	// private Set<Title> titles = new HashSet<>(); TODO add titles

	private Identity identity;

	@Override
	protected SocialIdentifier clone() {
		SocialIdentifier n = new SocialIdentifier(identity);
		n.names.addAll(names);
		n.epithets.addAll(epithets);
		return n;
	}

	public SocialIdentifier(Identity identity) {
		this.identity = identity;
	}

	public Identity getIdentity() {
		return identity;
	}

	public SocialIdentifier addNames(String... names) {
		for (String name : names) {
			this.names.add(name);
		}
		return this;
	}

	public SocialIdentifier addEpithets(String... epithets) {
		for (String epi : epithets) {
			this.epithets.add(epi);
		}
		return this;
	}

	public Collection<String> getNames() {
		return new ImmutableCollection<>(this.names);
	}

	public Collection<String> getEpithets() {
		return new ImmutableCollection<>(this.epithets);
	}

	public void removeName(String name) {
		this.names.remove(name);
	}

	public void removeEpithet(String epithet) {
		this.epithets.remove(epithet);
	}

	public boolean hasName(String name) {
		return this.names.contains(name);
	}

	public boolean hasEpithet(String epithet) {
		return this.epithets.contains(epithet);
	}

}
