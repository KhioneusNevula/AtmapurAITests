package sim;

import java.awt.Color;
import java.awt.Rectangle;
import java.awt.event.KeyEvent;
import java.util.Map;

import actor.Actor;
import actor.SentientActor;
import actor.UpgradedSentientActor;
import biology.anatomy.Species;
import biology.systems.ESystem;
import humans.Food;
import humans.Person;
import main.Pair;
import mind.Culture;
import mind.Group;
import mind.need.INeed.Degree;
import mind.need.KnowledgeNeed;
import mind.relationships.Relationship;
import mind.thought_exp.IThought;
import processing.core.PApplet;
import processing.event.MouseEvent;
import sim.interfaces.IRenderable;

public class WorldGraphics extends PApplet {

	private World world;
	private Display currentDisplay = Display.WORLD;
	private IRenderable currentScreen;
	private final float fps;
	public static final int BORDER = 30;

	public WorldGraphics(World world, float fps) {
		this.fps = fps;
		this.world = world;
		this.randomSeed(world.rand().nextLong());

	}

	public World getWorld() {
		return world;
	}

	public void setWorld(World world) {
		this.world = world;
		// this.size(world.getWidth(), world.getHeight());
		world.load();
	}

	public int getMaxWidth() {
		return this.displayWidth - BORDER;
	}

	public int getMaxHeight() {
		return this.displayHeight - BORDER;
	}

	public int width() {
		return this.currentScreen == null ? width : width / 2;
	}

	public int height() {
		return height;
		// return this.currentScreen == null ? height : height / 2;
	}

	@Override
	public void settings() {
		super.settings();
		size(world.getWidth() + 2 * BORDER, world.getHeight() + 2 * BORDER);

	}

	@Override
	public void setup() {
		super.setup();
		frameRate(fps);
		world.worldSetup();
		this.windowResizable(true);
	}

	public void changeDisplay(IRenderable newScreen, Display newDisplay) {
		this.currentDisplay = newDisplay;
		this.currentScreen = newScreen;
		this.windowResize(2 * (world.getWidth() + 2 * BORDER), (world.getHeight() + 2 * BORDER));
	}

	public void returnToWorldDisplay() {
		this.currentDisplay = Display.WORLD;
		this.currentScreen = null;
		this.windowResize(world.getWidth() + 2 * BORDER, world.getHeight() + 2 * BORDER);

	}

	public float getFps() {
		return fps;
	}

	@Override
	public void keyPressed() {
		if (key == KeyEvent.VK_ESCAPE) {
			if (this.currentDisplay != Display.WORLD) {
				if (this.currentScreen instanceof UpgradedMindDisplay umd && umd.getFocusedThought() != null) {
					umd.setFocusedThought(null);
				} else {
					this.returnToWorldDisplay();
				}
			}
			key = 0;
		}
		super.keyPressed();
	}

	@Override
	public void keyPressed(processing.event.KeyEvent event) {
		if (event.getKeyCode() == KeyEvent.VK_W) {
			world.getTestActor().getMind().getMindMemory().setFeelingCurious(true);
			world.getTestActor().getMind().getMindMemory().addNeed(new KnowledgeNeed(Degree.MILD, null));
		} else if (event.getKeyCode() == KeyEvent.VK_SPACE) {
			if (this.currentDisplay == Display.WORLD) {
				Group group = world.getTestGroup();
				System.out.println(group.report());
			} else if (this.currentScreen instanceof IMindDisplay md) {
				for (Culture c : md.getContainedMind().getMindMemory().cultures()) {
					System.out.println(c.report());
				}
			}

		} else if (event.getKeyCode() == KeyEvent.VK_I) {
			System.out.print("actor:" + world.getTestActor().getName() + ">>>");
			System.out.print("properties:" + world.getTestActor().getSocialProperties());

			System.out.println((world.getTestActor()).getMind().report());

			for (ESystem sys : world.getTestActor().getSystems()) {
				System.out.print(";" + sys.report());
			}
			System.out.println();
		} else if (event.getKeyCode() == KeyEvent.VK_F) {
			/* if (this.currentDisplay == Display.WORLD) */ if (this.mouseX < world.getWidth() + BORDER) {
				world.spawnActor(new Food(world, "nom" + this.mouseX + "_" + this.mouseY, world.clampX(mouseX - BORDER),
						world.clampY(mouseY - BORDER), 5));
			}
		}
		super.keyPressed(event);
	}

	@Override
	public void mouseClicked(MouseEvent event) {
		super.mouseClicked(event);
		if (event.getButton() == RIGHT) {
			/* if (this.currentDisplay == Display.WORLD) */ if (event.getX() < world.getWidth() + BORDER) {
				Actor a;
				world.spawnActor(a = new Person(world, "baba" + event.getX() + "" + event.getY(), Species.IMP,
						world.clampX(event.getX() - BORDER), world.clampY(event.getY() - BORDER), 5));
				a.setOptionalColor(Color.RED.getRGB());
				((Person) a).getMind().establishRelationship(world.getTestGroup(), Relationship.be());
				// world.getTestGroup().establishRelationship(a.getMind(),
				// Relationship.include());
			}
		} else {
			/* if (this.currentDisplay == Display.WORLD) */
			{

				Actor l = world.getActors().stream()
						.filter((a) -> a.distance(event.getX() - BORDER, event.getY() - BORDER) <= a.getRadius() + 5)
						.findAny().orElse(null);
				if (l != null) {
					System.out.print("actor:" + l.getName() + ">>>");
					System.out.print("properties:" + l.getSocialProperties());
					if (l instanceof SentientActor) {
						System.out.println(((SentientActor) l).getMind().report());
						this.changeDisplay(new MindDisplay(((SentientActor) l).getMind()), Display.MIND);
					} else if (l instanceof UpgradedSentientActor usa) {
						System.out.println(usa.getMind().report());
						this.changeDisplay(new UpgradedMindDisplay(usa.getMind()), Display.MIND);

					}
					for (ESystem sys : l.getSystems()) {
						System.out.print(";" + sys.report());
					}
					System.out.println();
				}
			}
			/* else */ if (this.currentScreen instanceof UpgradedMindDisplay screen) {
				int tX = event.getX() - width();
				for (Map.Entry<IThought, Pair<Rectangle, Boolean>> entry : screen.getThoughtBoxes().entrySet()) {
					if (screen.getFocusedThought() == null
							&& tX <= entry.getValue().getFirst().width + entry.getValue().getFirst().x
							&& tX >= entry.getValue().getFirst().x
							&& event.getY() <= entry.getValue().getFirst().height + entry.getValue().getFirst().y
							&& event.getY() >= entry.getValue().getFirst().y) {
						screen.setFocusedThought(entry.getKey());
						break;
					}
				}
			}
		}
	}

	public static enum Display {
		WORLD, MIND, GROUP
	}

	@Override
	public void draw() {
		background(color(0, 100, 100));
		world.worldTick();
		world.draw(this);
		if (this.currentScreen == null) {
		} else {
			g.pushMatrix();
			g.translate(width(), 0);
			this.currentScreen.draw(this);
			g.popMatrix();
		}

	}

}
