package biology.sensing.senses;

import java.util.Collection;
import java.util.Map;
import java.util.stream.Stream;

import com.google.common.collect.Multimap;
import com.google.common.collect.MultimapBuilder;

import actor.Actor;
import actor.IUniqueEntity;
import actor.construction.physical.IComponentPart;
import actor.construction.physical.IMaterialLayer;
import actor.construction.physical.IVisage;
import actor.construction.properties.SenseProperty;
import biology.sensing.AbstractSense;
import biology.sensing.stats.BasicSenseStats;
import phenomenon.IPhenomenon;
import sim.interfaces.IPhysicalEntity;
import sim.physicality.IInteractability;

/**
 * Sight sense checks
 * 
 * @author borah
 *
 */
public class SightSense extends AbstractSense {

	public SightSense(String name) {
		super(name, BasicSenseStats.DISTANCE, BasicSenseStats.EXISTENCE_PLANES, BasicSenseStats.DARKVISION,
				BasicSenseStats.CLOUD_PENETRATION);
	}

	@Override
	public Stream<Actor> getActorVisages(Actor sensingActor, IComponentPart usingPart) {
		int distance = usingPart.getAbilityStat(this, BasicSenseStats.DISTANCE);
		Collection<IInteractability> planes = usingPart.getAbilityStat(this, BasicSenseStats.EXISTENCE_PLANES);
		return sensingActor.getWorld().getActors().stream()
				.filter((a) -> a.distance(sensingActor) < distance
						&& IInteractability.interactibleWith(a.getVisage().sensabilityMode(this), planes))
				.sorted((a, b) -> (int) (a.distance(sensingActor) - b.distance(sensingActor))); // actors;
	}

	@Override
	public Stream<IPhenomenon> getPhysicalPhenomena(Actor sensingActor, IComponentPart usingPart) {
		int distance = usingPart.getAbilityStat(this, BasicSenseStats.DISTANCE);
		Collection<IInteractability> planes = usingPart.getAbilityStat(this, BasicSenseStats.EXISTENCE_PLANES);
		return sensingActor.getWorld().getPhenomena().stream()
				.filter((a) -> a.isPhysical()
						&& IInteractability.interactibleWith(a.getVisage().sensabilityMode(this), planes))
				.map((a) -> (IPhysicalEntity) a).filter((a) -> a.distance(sensingActor) < distance)
				.sorted((a, b) -> (int) (a.distance(sensingActor) - b.distance(sensingActor)))
				.map((a) -> (IPhenomenon) a); // physical phenomena
	}

	@Override
	public Stream<IPhenomenon> getWorldPhenomena(Actor sensingActor, IComponentPart using) {
		Collection<IInteractability> planes = using.getAbilityStat(this, BasicSenseStats.EXISTENCE_PLANES);
		return sensingActor.getWorld().getPhenomena().stream()
				.filter((a) -> !a.isPhysical()
						&& IInteractability.interactibleWith(a.getVisage().sensabilityMode(this), planes))
				.sorted((a, b) -> {
					int v1 = (int) (100 * (b.uniqueness() - a.uniqueness()));
					if (v1 == 0)
						return a.getUUID().compareTo(b.getUUID());
					return v1;
				}).map((a) -> (IPhenomenon) a); // world phenomena
	}

	@Override
	public Stream<IUniqueEntity> getSensedVisages(Actor sensingActor, IComponentPart usingPart) {
		int distance = usingPart.getAbilityStat(this, BasicSenseStats.DISTANCE);
		Collection<IInteractability> planes = usingPart.getAbilityStat(this, BasicSenseStats.EXISTENCE_PLANES);
		// TODO darkvision, cloud penetration
		Stream<Actor> actorstream = sensingActor.getWorld().getActors().stream()
				.filter((a) -> a.distance(sensingActor) < distance
						&& IInteractability.interactibleWith(a.getVisage().sensabilityMode(this), planes))
				.sorted((a, b) -> (int) (a.distance(sensingActor) - b.distance(sensingActor))); // actors
		Stream<IPhenomenon> phenstream = sensingActor.getWorld().getPhenomena().stream()
				.filter((a) -> a.isPhysical()
						&& IInteractability.interactibleWith(a.getVisage().sensabilityMode(this), planes))
				.map((a) -> (IPhysicalEntity) a).filter((a) -> a.distance(sensingActor) < distance)
				.sorted((a, b) -> (int) (a.distance(sensingActor) - b.distance(sensingActor)))
				.map((a) -> (IPhenomenon) a); // physical phenomena

		Stream<IPhenomenon> wophenstream = sensingActor.getWorld().getPhenomena().stream()
				.filter((a) -> !a.isPhysical()
						&& IInteractability.interactibleWith(a.getVisage().sensabilityMode(this), planes))
				.sorted((a, b) -> {
					int v1 = (int) (100 * (b.uniqueness() - a.uniqueness()));
					if (v1 == 0)
						return a.getUUID().compareTo(b.getUUID());
					return v1;
				}).map((a) -> (IPhenomenon) a); // world phenomena

		return Stream.concat(Stream.concat(actorstream, phenstream), wophenstream);
	}

	@Override
	public Map<? extends IComponentPart, ? extends Collection<? extends IMaterialLayer>> getSensableParts(
			Actor sensingActor, IComponentPart usingPart, IVisage forEntity) {
		Multimap<IComponentPart, IMaterialLayer> map = MultimapBuilder.hashKeys().arrayListValues().build();
		forEntity.getOutermostParts().forEach((part) -> {
			float additiveTransparency = 0.0f;
			for (IMaterialLayer materi : part.getMaterials().values()) {
				if (materi.getState().gone())
					continue;
				map.put(part, materi);
				additiveTransparency += materi.getProperty(SenseProperty.OPACITY, true).getValue();
				map.put(part, materi);
				if (additiveTransparency >= 1f) {
					break;
				}

			}
		});
		return map.asMap();
	}

}
