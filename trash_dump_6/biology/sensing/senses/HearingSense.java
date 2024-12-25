package biology.sensing.senses;

import java.util.Collection;
import java.util.Map;
import java.util.stream.Stream;

import com.google.common.collect.Multimap;
import com.google.common.collect.MultimapBuilder;
import com.google.common.collect.Streams;

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

public class HearingSense extends AbstractSense {

	public HearingSense(String name) {
		super(name, BasicSenseStats.DISTANCE, BasicSenseStats.EXISTENCE_PLANES, BasicSenseStats.MATERIAL_PENETRATION);
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
				.map((a) -> (IPhenomenon) a); // world phenomena
	}

	@Override
	public Stream<IUniqueEntity> getSensedVisages(Actor sensingActor, IComponentPart usingPart) {
		int distance = usingPart.getAbilityStat(this, BasicSenseStats.DISTANCE);
		Collection<IInteractability> planes = usingPart.getAbilityStat(this, BasicSenseStats.EXISTENCE_PLANES);
		// TODO material penetration
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
				.map((a) -> (IPhenomenon) a); // world phenomena

		return Streams.concat(actorstream, phenstream, wophenstream);
	}

	@Override
	public Map<? extends IComponentPart, ? extends Collection<? extends IMaterialLayer>> getSensableParts(
			Actor sensingActor, IComponentPart usingPart, IVisage forEntity) {
		Multimap<IComponentPart, IMaterialLayer> map = MultimapBuilder.hashKeys().arrayListValues().build();
		forEntity.getParts().forEach((part) -> {
			// TODO material penetration
			for (IMaterialLayer materi : part.getMaterials().values()) {
				if (materi.getState().gone())
					continue;
				if (materi.getProperty(SenseProperty.SOUND, false).canBeSensed())
					map.put(part, materi);

			}
		});
		return map.asMap();
	}

}
