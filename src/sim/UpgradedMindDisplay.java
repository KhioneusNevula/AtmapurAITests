package sim;

import java.awt.Color;
import java.awt.Point;
import java.awt.Rectangle;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.PrimitiveIterator.OfInt;
import java.util.stream.IntStream;

import main.Pair;
import mind.concepts.relations.IConceptRelationType;
import mind.concepts.relations.RelationsGraph;
import mind.concepts.type.IMeme;
import mind.thought_exp.IThought;
import mind.thought_exp.IUpgradedMind;
import mind.thought_exp.culture.UpgradedCulture;
import mind.thought_exp.memory.IUpgradedKnowledgeBase;
import sim.interfaces.IRenderable;
import sim.relationalclasses.AbstractRelationalGraph.IEdge;

public class UpgradedMindDisplay implements IRenderable {

	public static enum Screen {
		THOUGHTS, RELATION_KNOWLEDGE
	}

	private Screen currentScreen = Screen.THOUGHTS;
	private IUpgradedMind containedMind;
	private boolean cultureMode;
	/** boolean -> true if paused */
	private Map<IThought, Pair<Rectangle, Boolean>> thoughtBoxes = new HashMap<>();
	private Map<RelationsGraph.Node, Rectangle> relationBoxes = new HashMap<>();
	private IThought focusedThought = null;
	private static final int vanishTime = 20;
	private Map<IThought, Pair<Rectangle, Integer>> vanishingThoughts = new HashMap<>();

	public UpgradedMindDisplay(IUpgradedMind mind) {
		this.containedMind = mind;
	}

	public void setContainedMind(IUpgradedMind containedMind) {
		this.containedMind = containedMind;
	}

	public IUpgradedMind getContainedMind() {
		return containedMind;
	}

	public Screen getCurrentScreen() {
		return currentScreen;
	}

	public void setCurrentScreen(Screen currentScreen) {
		this.currentScreen = currentScreen;
	}

	public boolean isCultureMode() {
		return cultureMode;
	}

	public void setCultureMode(boolean cultureMode) {
		this.cultureMode = cultureMode;
	}

	@Override
	public boolean canRender() {
		return containedMind != null;
	}

	public IThought getFocusedThought() {
		return focusedThought;
	}

	public void setFocusedThought(IThought focusedThought) {
		this.focusedThought = focusedThought;
	}

	public Map<IThought, Pair<Rectangle, Boolean>> getThoughtBoxes() {
		return thoughtBoxes;
	}

	private void cleanUpThoughtBoxes() {
		Iterator<Map.Entry<IThought, Pair<Rectangle, Boolean>>> iterator = this.thoughtBoxes.entrySet().iterator();
		while (iterator.hasNext()) {
			Map.Entry<IThought, Pair<Rectangle, Boolean>> entry = iterator.next();
			if (!this.containedMind.hasThought(entry.getKey())) {
				iterator.remove();
				System.out.print("");
				this.vanishingThoughts.put(entry.getKey(), Pair.of(entry.getValue().getFirst(), vanishTime));
			} else {
				entry.setValue(Pair.of(entry.getValue().getFirst(), containedMind.isPaused(entry.getKey())));

			}
		}
	}

	private Rectangle createBoxForThought(IThought thought, String boxText, WorldGraphics g, int border, int startX,
			int startY, int textHeight) {
		double textWidth = g.textWidth(boxText);
		int attempts = 0;
		int width = (int) (textWidth + border + 0.5);
		int height = (int) (textHeight + border + 0.5);
		int ubX = g.width() - width - border;
		int ubY = g.height() - height - border;
		if (startX >= ubX || startY >= ubY)
			return null;
		IntStream xis = this.containedMind.rand().ints(startX, ubX).distinct();
		IntStream yis = this.containedMind.rand().ints(startY, ubY).distinct();
		OfInt xIter = xis.iterator();
		OfInt yIter = yis.iterator();

		for (; attempts < Math.min(200, Math.min(ubX - startX, ubY - startY)); attempts++) {
			int x = xIter.nextInt();
			int y = yIter.nextInt();
			Rectangle rect = new Rectangle(x, y, width, height);
			boolean canMake = true;
			for (Pair<Rectangle, Boolean> ra : this.thoughtBoxes.values()) {
				if (ra.getFirst().intersects(rect)) {
					canMake = false;
					break;
				}
			}
			if (canMake) {
				return rect;
			}
		}
		return null;
	}

	private Rectangle createBoxForNode(RelationsGraph.Node node, String boxText, WorldGraphics g, int border,
			int startX, int startY, int textHeight) {
		double textWidth = g.textWidth(boxText);
		int attempts = 0;
		int width = (int) (textWidth + border + 0.5);
		int height = (int) (textHeight + border + 0.5);
		int ubX = g.width() - width - border;
		int ubY = g.height() - height - border;
		if (startX >= ubX || startY >= ubY)
			return null;
		IntStream xis = this.containedMind.rand().ints(startX, ubX).distinct();
		IntStream yis = this.containedMind.rand().ints(startY, ubY).distinct();
		OfInt xIter = xis.iterator();
		OfInt yIter = yis.iterator();

		for (; attempts < Math.min(200, Math.min(ubX - startX, ubY - startY)); attempts++) {
			int x = xIter.nextInt();
			int y = yIter.nextInt();
			Rectangle rect = new Rectangle(x, y, width, height);
			boolean canMake = true;
			for (Rectangle ra : this.relationBoxes.values()) {
				if (ra.intersects(rect)) {
					canMake = false;
					break;
				}
			}
			if (canMake) {
				return rect;
			}
		}
		return null;
	}

	private Point[] getConnectionPoints(Rectangle r, boolean includeCenter) {
		int[] xs = { (int) r.getCenterX(), r.x, (int) r.getMaxX() };
		int[] ys = { (int) r.getCenterY(), r.y, (int) r.getMaxY() };
		Point[] list = new Point[8 + (includeCenter ? 1 : 0)];
		int i = 0;
		for (int xi = 0; xi < xs.length; xi++) {
			for (int yi = 0; yi < ys.length; yi++) {
				if (!includeCenter && (xi == 0 && 0 == yi))
					continue;
				list[i] = new Point(xs[xi], ys[yi]);
				i++;
			}
		}
		return list;
	}

	private void renderThoughtBox(IThought thought, Rectangle box, boolean paused, WorldGraphics g, String boxText,
			int border, int vanishmentTicks, float textSize) {
		float ratio = vanishmentTicks / ((float) vanishTime);
		int alpha = vanishmentTicks == vanishTime ? 255 : (int) (255 * vanishmentTicks / (vanishTime * 1.3f));
		float alpharatio = alpha / 255.0f;
		g.textSize(textSize * ratio);
		Rectangle ogBox = (Rectangle) box.clone();
		box = new Rectangle(ogBox.x, ogBox.y + (int) ((1 - ratio) * (g.height() - ogBox.y)),
				(int) (ogBox.width * ratio), (int) (ogBox.height * ratio));
		Color color = thought.getBoxColor();
		g.fill(paused ? g.color(50, alpha, 255, alpha)
				: g.color((int) (color.getRed() * alpharatio), (int) (color.getGreen() * alpharatio),
						(int) (color.getBlue() * alpharatio), alpha));
		g.rectMode(WorldGraphics.CORNER);
		g.rect((int) box.getMinX(), (int) box.getMinY(), (int) box.getWidth(), (int) box.getHeight());
		g.fill(paused ? g.color(100, 100, 100, alpha) : g.color(0, alpha));
		g.text(boxText, (int) box.getMinX() + border / 2, (int) box.getMaxY() - border / 2);
		if (thought.parentThought() != null) {
			Pair<Rectangle, Boolean> pPair = thoughtBoxes.get(thought.parentThought());
			if (pPair != null) {
				Rectangle pRect = pPair.getFirst();
				Point[] points = null;
				double distance = -1;
				Point[] childPoints = getConnectionPoints(box, false);
				Point[] parentPoints = getConnectionPoints(pRect, false);
				for (Point point : childPoints) {
					for (Point pPoint : parentPoints) {
						double d = point.distance(pPoint);
						if (points == null || d < distance) {
							points = new Point[] { point, pPoint };
							distance = d;
						}
					}
				}
				g.stroke(g.color(0, 255, 0, alpha));
				g.line(points[0].x, points[0].y, points[1].x, points[1].y);
				g.fill(g.color(255, 0, 0, alpha));
				g.ellipseMode(WorldGraphics.CENTER);
				g.circle(points[1].x, points[1].y, 10);
				System.out.print("");
			}
		}
	}

	private void renderRelationMapBox(Rectangle box, WorldGraphics g, String boxText, int border, float textSize,
			Color color) {
		g.fill(g.color((int) (color.getRed()), (int) (color.getGreen()), (int) (color.getBlue())));
		g.rectMode(WorldGraphics.CORNER);
		g.rect((int) box.getMinX(), (int) box.getMinY(), (int) box.getWidth(), (int) box.getHeight());
		g.fill(g.color(0));
		g.text(boxText, (int) box.getMinX() + border / 2, (int) box.getMaxY() - border / 2);
	}

	private void renderRelationMapConnection(IEdge<IConceptRelationType, Collection<IMeme>, IMeme> edge,
			WorldGraphics g, float textSize, Color color) {
		Rectangle left = relationBoxes.get(edge.getLeft());
		Rectangle right = relationBoxes.get(edge.getRight());
		if (left == null || right == null)
			return;

		Point[] points = null;
		double distance = -1;
		Point[] childPoints = getConnectionPoints(left, false);
		Point[] parentPoints = getConnectionPoints(right, false);
		for (Point point : childPoints) {
			for (Point pPoint : parentPoints) {
				double d = point.distance(pPoint);
				if (points == null || d < distance) {
					points = new Point[] { point, pPoint };
					distance = d;
				}
			}
		}
		g.stroke(g.color(color.getRGB()));
		g.line(points[0].x, points[0].y, points[1].x, points[1].y);
		g.textSize(textSize);
		g.fill(g.color(color.getRGB()));
		g.ellipseMode(WorldGraphics.CENTER);
		g.circle(points[1].x, points[1].y, 10);
		g.fill(g.color(Color.white.getRGB()));
		String edgeString = edge.getType().toString() + edge.getArgs();
		float w = g.textWidth(edgeString);
		g.text(edgeString, (points[0].x + points[1].x) / 2 - w / 2, (points[0].y + points[1].y) / 2);

	}

	private void renderAllThoughts(WorldGraphics g) {
		final int border = 10, startX = 30, startY = 30;
		float textSize = 0;

		Iterator<Map.Entry<IThought, Pair<Rectangle, Integer>>> iterator1 = this.vanishingThoughts.entrySet()
				.iterator();
		while (iterator1.hasNext()) {
			Map.Entry<IThought, Pair<Rectangle, Integer>> entry = iterator1.next();
			g.textSize(textSize = entry.getKey().isLightweight() ? 10 : 20);
			entry.getValue().setSecond(entry.getValue().getSecond() - 1);
			if (entry.getValue().getSecond() <= 0) {
				iterator1.remove();
			} else {

				String boxText = entry.getKey().displayText();
				this.renderThoughtBox(entry.getKey(), entry.getValue().getFirst(),
						this.containedMind.isPaused(entry.getKey()), g, boxText, border, entry.getValue().getSecond(),
						textSize);
			}
		}
		for (IThought thought : containedMind.thoughts()) {
			g.textSize(textSize = thought.isLightweight() ? 10 : 20);
			final int textHeight = (int) (g.textAscent() + g.textDescent() + 0.5);
			Pair<Rectangle, Boolean> pair = thoughtBoxes.get(thought);
			Rectangle box = pair != null ? thoughtBoxes.get(thought).getFirst() : null;
			String boxText = thought.displayText();
			if (box == null) {
				pair = Pair.of(this.createBoxForThought(thought, boxText, g, border, startX, startY, textHeight),
						containedMind.isPaused(thought));
				box = pair.getFirst();
				if (box != null) {
					thoughtBoxes.put(thought, pair);
				}
			}
			if (box != null) {
				this.renderThoughtBox(thought, box, pair.getSecond(), g, boxText, border, vanishTime, textSize);
			} else {
				// System.err.println("Failed to show thought box for " + thought);
				break;
			}

		}
	}

	private void renderEdges(IUpgradedKnowledgeBase knowledge, WorldGraphics g, float textSize, Color color) {
		for (RelationsGraph.Edge edge : knowledge.getRelationsGraph().getAllEdges()) {
			this.renderRelationMapConnection(edge, g, textSize, color);
		}
	}

	private void renderNodes(IUpgradedKnowledgeBase knowledge, WorldGraphics g, int border, int startX, int startY,
			float textSize, Color color) {

		for (RelationsGraph.Node node : knowledge.getRelationsGraph().getAllNodes()) {
			g.textSize(20);
			final int textHeight = (int) (g.textAscent() + g.textDescent() + 0.5);
			Rectangle box = this.relationBoxes.get(node);
			String boxText = node.getData().toString();
			if (box == null) {
				box = this.createBoxForNode(node, boxText, g, border, startX, startY, textHeight);
				if (box != null) {
					relationBoxes.put(node, box);
				}
			}
			if (box != null) {
				this.renderRelationMapBox(box, g, boxText, border, textSize, color);
			} else {
				// System.err.println("Failed to show thought box for " + thought);
				break;
			}

		}
	}

	private void renderRelationsGraph(WorldGraphics g) {
		final int border = 10, startX = 30, startY = 30;
		float textSize = 10;
		if (this.cultureMode) {
			for (UpgradedCulture culture : this.containedMind.getMemory().cultures()) {
				this.renderEdges(culture, g, textSize, Color.pink);
			}
		}
		if (this.cultureMode) {
			for (UpgradedCulture culture : this.containedMind.getMemory().cultures()) {
				this.renderNodes(culture, g, border, startX, startY, textSize, Color.cyan);
			}
		}
		this.renderEdges(this.containedMind.getMemory(), g, textSize, Color.red);
		this.renderNodes(this.containedMind.getMemory(), g, border, startX, startY, textSize, Color.green);

	}

	private void renderFocusedThought(WorldGraphics g) {
		int leftX = g.width() / 5;
		int leftY = g.height() / 4;
		int width = g.width() * 3 / 5;
		int height = g.height() / 2;
		g.pushStyle();
		g.fill(this.thoughtBoxes.get(focusedThought).getSecond() ? g.color(255, 100, 100) : g.color(100, 100, 255));
		g.rectMode(WorldGraphics.CORNER);
		g.rect(leftX, leftY, width, height);
		g.popStyle();
		g.fill(0);
		g.stroke(0);
		g.pushStyle();
		g.pushMatrix();
		g.translate(leftX, leftY);
		this.focusedThought.renderThoughtView(g, width, height);
		g.popMatrix();
		g.popStyle();
	}

	@Override
	public void draw(WorldGraphics g) {
		g.pushStyle();
		if (containedMind.isActive()) {
			if (g.random(5) < g.getWorld().ticks % 5) {
				this.cleanUpThoughtBoxes();
			}
			g.fill(0);
			g.textSize(20);
			String name = (containedMind.hasActor() ? containedMind.getAsHasActor().getActor().getName() : "")
					+ (this.cultureMode && this.currentScreen == Screen.RELATION_KNOWLEDGE ? " (with cultures)" : "");
			g.text(name, g.width() / 2 - g.textWidth(name) / 2, g.textAscent() + 10);
			if (this.currentScreen == Screen.THOUGHTS) {
				// render all thoughts
				this.renderAllThoughts(g);
				// TODO render focused thought
				if (this.focusedThought != null) {
					if (!this.thoughtBoxes.containsKey(focusedThought)) {
						focusedThought = null;
					} else {
						this.renderFocusedThought(g);
					}
				}
			} else if (currentScreen == Screen.RELATION_KNOWLEDGE) {
				this.renderRelationsGraph(g);
			}
		} else {
			g.fill(g.color(255, 200, 200));
			g.textSize(100);
			String msg = "NO MIND";
			float w = g.textWidth(msg);
			g.text(msg, g.width() / 2 - w / 2, g.height() / 2);

		}

		/*
		 * String displaystr = containedMind.report() + "\n" +
		 * containedMind.getWill().report() + "\n" + (containedMind.hasActor() ? "held:"
		 * + containedMind.getAsHasActor().getActor().getHeld() : "");
		 * 
		 * g.text(displaystr, 10, 10);
		 */

		g.popStyle();
		;
	}
}
