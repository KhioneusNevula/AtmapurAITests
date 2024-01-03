package mind.need;

import mind.need.INeed.INeedType;
import mind.need.INeed.Individuality;

public enum NeedType implements INeedType {
	/**
	 * a need for a role that manages social/power structures, decision makers
	 * between groups.<br>
	 * > Mild: typically nothing happens.<br>
	 * > Moderate: typically, a new management role is produced<br>
	 * > Severe: typically, an existing role is given an additional management
	 * function<br>
	 * > Beyond: mind control people to make decisions as a collective
	 */
	STRUCTURE(Individuality.SOCIETAL),
	/**
	 * a need to protect oneself from immediate dangers, such as being on fire or
	 * swarmed by insects<br>
	 * > Mild: typically some mild effort to rid oneself<br>
	 * > Moderate: typically overlaps with severe<br>
	 * > Severe: typically frantic efforts to rid oneself of the danger<br>
	 * > Beyond: typically a need to protect oneself extremely, such as seeking
	 * invulnerability
	 */
	SELF_PRESERVATION(Individuality.INDIVIDUAL),
	/**
	 * A need for protection from hostile things<br>
	 * > Mild: typically just safer actions like avoiding dangers<br>
	 * > Moderate: typically a new protective role is produced or a structure is
	 * built; protective things may be acquired<br>
	 * > Severe: new protective roles, consolidation of protective and Structural
	 * roles, protective obstructive structures<br>
	 * > Beyond: a need to eradicate a form of danger from the universe entirely
	 */
	SAFETY(Individuality.INDIVIDUAL_OR_SOCIETAL),
	/**
	 * A need for a harmful or transgressive individual to get consequences; <br>
	 * > Mild: typically manifests as spreading rumors. <br>
	 * > Moderate: typically manifests as the presentation of the individual to a
	 * system of justice if existing, or generation of a justice-giving role<br>
	 * > Severe: can overlap with Moderate or Beyond <br>
	 * > Beyond: can result in the taking of vengeance violently and swiftly
	 */
	JUSTICE(Individuality.INDIVIDUAL_OR_SOCIETAL),
	/**
	 * A need for sleep/rest<br>
	 * > Mild: typically manifests as a bit of inaction<br>
	 * > Moderate: going to sleep; socially, may result in architecture changing to
	 * accommodate sleeping<br>
	 * > Severe: typically overlaps with Moderate; can involve using energy
	 * stimulants<br>
	 * > Beyond: can result in the creation of eternal sleep or the removal of the
	 * need for sleep
	 */
	ENERGY(Individuality.INDIVIDUAL_OR_SOCIETAL),
	/**
	 * a need for things to make roles take shorter time.<br>
	 * > Mild: nothing<br>
	 * > Moderate: creation of transportation methods and/or stockpiles<br>
	 * > Severe: consolidation of stockpiling, intense development of transport, a
	 * lot of pre-planning and hoarding<br>
	 * > Beyond: things like researching teleportation or idk (tbd?)
	 */
	ACCESSIBILITY(Individuality.INDIVIDUAL_OR_SOCIETAL),
	/**
	 * A need for necessary resources to remain preserved and not decay<br>
	 * > Mild: removing items from areas of high activity<br>
	 * > Moderate: creation and use of preservation units (Fridge type things),
	 * application of preservatives (salt)<br>
	 * > Severe: overlaps with moderate<br>
	 * > Beyond: eradicate all decay
	 */
	PRESERVATION(Individuality.INDIVIDUAL_OR_SOCIETAL),
	/**
	 * a need for living longer, i.e. being healthy, as well as the desire to
	 * produce kids for a form of immortality of memory, and (taken to the logical
	 * extreme) being immortal<br>
	 * > Mild: cleaner practices<br>
	 * > Moderate: create positions to do medical science, or visit medical
	 * scientists; as individual, maybe consider having kids <br>
	 * > Severe: similar to moderate but with more focus on extending life; possibly
	 * more kids needed<br>
	 * > Beyond: look into immortality and extending lifespan; clone self; etc
	 */
	LONGEVITY(Individuality.INDIVIDUAL_OR_SOCIETAL),
	/**
	 * The need for entertainment and well-being<br>
	 * > Mild: do hobby things<br>
	 * > Moderate: stop doing negative things; form roles for entertainment <br>
	 * > Severe: take a break and just do hobby things; more entertainment roles;
	 * maybe artificially increase happiness<br>
	 * > Beyond: eradicate all lack of happiness
	 */
	HAPPINESS(Individuality.INDIVIDUAL_OR_SOCIETAL),
	/**
	 * The need for dealing with loss<br>
	 * > Mild: think about the lost<br>
	 * > Moderate: visit site of death or do some sort of ritual; create a ritual or
	 * role for mourning<br>
	 * > Severe: overlaps with Moderate<br>
	 * > Beyond: attempt to look for a way to raise the dead
	 */
	MOURNING(Individuality.INDIVIDUAL_OR_SOCIETAL),
	/**
	 * the need for relief from discomfort or pain <br>
	 * > Mild: nothing<br>
	 * > Moderate: seeking some more comfort<br>
	 * > Severe: some form of artificial painkiller<br>
	 * > Beyond: usually either seeking some kind of transformation that removes
	 * pain or suicide.
	 */
	RELIEF(Individuality.INDIVIDUAL),
	/**
	 * The need for something someone else has<br>
	 * > Mild: making something akin to that other's/better<br>
	 * > Moderate: overlapping with Mild, or Trying to take from the other<br>
	 * > Severe: overlapping with Moderate<br>
	 * > Beyond: try to lay claim to everything in existence
	 */
	COVETOUSNESS(Individuality.INDIVIDUAL_OR_SOCIETAL),
	/**
	 * the need to absolve one's guilt, or a society's collective debt<br>
	 * Mild: an apology and a small favor possibly<br>
	 * Moderate: bigger favors<br>
	 * Severe: self punishment<br>
	 * Beyond: suicide
	 */
	ABSOLUTION(Individuality.INDIVIDUAL_OR_SOCIETAL),
	/**
	 * A need to create art and the like<br>
	 * Mild: small hobbies<br>
	 * Moderate: an increase in hobbies; creation of artistic roles and the like<br>
	 * Severe: more hobbyism<br>
	 * Beyond: insanity into art (perhaps like the "strange moods" type thing)
	 */
	CREATIVITY(Individuality.INDIVIDUAL_OR_SOCIETAL),
	/**
	 * A need to create stories and myths and beliefs<br>
	 * Mild: small stories to explain minor things<br>
	 * Moderate: storytelling roles, more stories<br>
	 * Severe: creations of entire belief systems around stories, religions<br>
	 * Beyond: delusion of how the world works, try to change the world to be more
	 * mystic artificially
	 */
	MYSTIQUE(Individuality.INDIVIDUAL_OR_SOCIETAL),
	/**
	 * a need for friendship, companionship, etc<br>
	 * Mild: interact with a few companions<br>
	 * Moderate: seek out larger events/create social events like parties<br>
	 * Severe: overlaps with Moderate<br>
	 * Beyond: stalk people or serial killing delusions
	 */
	COMMUNITY(Individuality.INDIVIDUAL_OR_SOCIETAL),
	/**
	 * a need to conform to social duties<br>
	 * Mild: do things that are expected<br>
	 * Moderate: stick highly to traditional behaviors<br>
	 * Severe: overlaps with moderate<br>
	 * Beyond: cause harm for non-traditionalism
	 */
	DUTY(Individuality.INDIVIDUAL),
	/**
	 * a need for adherence to a cultural ideology<br>
	 * Mild: preach values a bit; <br>
	 * Moderate: write about values; create roles/institutions to promote
	 * culture<br>
	 * Severe: consolidate roles and culture into laws and ensure they are followed;
	 * declare war against ideologically different entities<br>
	 * Beyond: enforce conformity
	 */
	IDEOLOGY(Individuality.INDIVIDUAL_OR_SOCIETAL),
	/**
	 * a need for distinction of one's own identity <br>
	 * Mild: add unicity to one's own style; declare shibboleths or unique identity
	 * markers<br>
	 * Moderate: overlaps with mild<br>
	 * Severe: possibly attempt to separate from surrounding society if oppressed
	 * and form an identity group; revolt<br>
	 * Beyond: isolate from all of society;
	 */
	IDENTITY(Individuality.INDIVIDUAL_OR_SOCIETAL),
	/**
	 * a need for food/drink/etc<br>
	 * Mild: try to increase food production a bit; go get some food<br>
	 * Moderate: create food roles; go eat a meal<br>
	 * Severe: consolidate food roles; put more weight on food production; eat a
	 * lot<br>
	 * Beyond: eat others of the same race; try to change the body to not require
	 * food;
	 */
	SUSTENANCE(Individuality.INDIVIDUAL_OR_SOCIETAL),
	/**
	 * A need for structures to protect from the environment<br>
	 * > Mild: try to find a place indoors; build small structures<br>
	 * > Moderate: create construct-building roles; remain indoors<br>
	 * > Severe: go underground; combine constructs into big constructs and build
	 * underground<br>
	 * > Beyond: prevent any gaps for the environment to enter; prevent anyone from
	 * going in or out; stay inside and never come out
	 */
	SHELTER(Individuality.INDIVIDUAL_OR_SOCIETAL),
	/**
	 * a need for sociopolitical power<br>
	 * Mild: be more charming, manipulate others; exert some cultural power over
	 * others<br>
	 * Moderate: accrue wealth<br>
	 * Severe: use violence to accrue power, fighting to be strongest, harm, war<br>
	 * Beyond: seek methods to rule the world or become omnipotent or magically
	 * superpowered
	 */
	POWER(Individuality.INDIVIDUAL_OR_SOCIETAL),
	/**
	 * a need for knowledge<br>
	 * > Mild: go seek out and study knowledge; practice<br>
	 * > Moderate: research or develop knowledge; make knowledge finding roles <br>
	 * > Severe: overlaps with moderate<br>
	 * > Beyond: seek omniscience, look for knowledge beyond what is known (maybe
	 * try to break the fourth wall For Funzies?)
	 */
	KNOWLEDGE(Individuality.INDIVIDUAL_OR_SOCIETAL),
	/**
	 * the societal need for children, to continue existing (not generated by
	 * individuals; although individuals may desire children, this manifests as a
	 * desire for Longevity) <br>
	 * Mild: encourage childbirth<br>
	 * Moderate: create institutions to assist childbirth<br>
	 * Severe: enforce childbirth and creation of children<br>
	 * Beyond: create a system of automated childbirth or idk
	 */
	PROCREATION(Individuality.SOCIETAL);

	private final Individuality individuality;

	private NeedType(Individuality in) {
		this.individuality = in;
	}

	public Individuality individuality() {
		return individuality;
	}

	@Override
	public String uniqueName() {
		return "basic_" + toString() + "_need";
	}
}