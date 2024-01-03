package sim;

import java.awt.Color;
import java.awt.Point;
import java.awt.Rectangle;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.PrimitiveIterator.OfInt;
import java.util.stream.IntStream;

import main.Pair;
import mind.thought_exp.IThought;
import mind.thought_exp.IUpgradedMind;
import sim.interfaces.IRenderable;

public class UpgradedMindDisplay implements IRenderable {

	private IUpgradedMind containedMind;
	/** boolean -> true if paused */
	private Map<IThought, Pair<Rectangle, Boolean>> thoughtBoxes = new HashMap<>();
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

	public IThought getFocusedThought() {
		return focusedThought;
	}

	public void setFocusedThought(IThought focusedThought) {
		this.focusedThought = focusedThought;
	}

	@Override
	public boolean canRender() {
		return containedMind != null;
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
				System.err.println("Failed to show thought box for " + thought);
			}

		}
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
			String name = (containedMind.hasActor() ? containedMind.getAsHasActor().getActor().getName() : "");
			g.text(name, g.width() / 2 - g.textWidth(name) / 2, g.textAscent() + 10);
			// render all thoughts
			this.renderAllThoughts(g);
			// TODO render focused thought
			if (this.focusedThought != null) {
				if (!this.thoughtBoxes.containsKey(focusedThought)) {
					focusedThought = null;
				} else {
					int leftX = g.width() / 5;
					int leftY = g.height() / 4;
					int width = g.width() * 3 / 5;
					int height = g.height() / 2;
					g.pushStyle();
					g.fill(this.thoughtBoxes.get(focusedThought).getSecond() ? g.color(255, 100, 100)
							: g.color(100, 100, 255));
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
