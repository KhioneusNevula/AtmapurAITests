package biology.systems.senses;

import java.util.Collection;

import actor.Actor;
import actor.IComponentPart;
import actor.IComponentType;
import actor.IMultipart;
import actor.IVisage;
import actor.MultipartActor;
import biology.systems.types.ISensor;
import mind.concepts.type.SenseProperty;
import mind.thought_exp.ICanThink;
import sim.World;

public class SightSense implements ISensor {

	public SightSense() {
	}

	@Override
	public <T> Object getGeneralTrait(SenseProperty<T> property, Actor target, World inWorld, ICanThink bySystem,
			Actor byEntity) {
		IVisage visage = target.getVisage();
		// TODO check sight line, etc

		return visage.getTrait(property);
	}

	@Override
	public <T> Collection<Object> getSensed(World inWorld, SenseProperty<T> property, ICanThink bySystem,
			Actor byEntity) {
		return null;
	}

	@Override
	public <T> Object getSensedTrait(MultipartActor target, SenseProperty<T> property, IComponentType component,
			World inWorld, ICanThink bySystem, Actor byEntity) {
		IMultipart body = target.getBody();
		if (!body.getOutermostParts().values().stream()
				.anyMatch((a) -> a.getType().getName().equals(component.getName())))
			return null;
		return component.getTrait(property);
	}

	@Override
	public <T> Object getSpecificSensedTrait(MultipartActor target, SenseProperty<T> property,
			IComponentPart componentPart, World inWorld, ICanThink bySystem, Actor byEntity) {
		IMultipart body = target.getBody();
		if (!body.getOutermostParts().values().stream().anyMatch((a) -> a.getId().equals(componentPart.getId())))
			return null;

		return componentPart.getProperty(property, true);
	}

	@Override
	public String toString() {
		return "sense_sight";
	}

}
