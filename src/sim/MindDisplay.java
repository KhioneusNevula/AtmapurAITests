package sim;

import mind.IIndividualMind;
import mind.Will.ActionQueue;
import mind.goals.IGoal;
import mind.goals.ITaskGoal;

public class MindDisplay implements IMindDisplay {

	private IIndividualMind containedMind;

	public MindDisplay(IIndividualMind mind) {
		this.containedMind = mind;
	}

	public void setContainedMind(IIndividualMind containedMind) {
		this.containedMind = containedMind;
	}

	public IIndividualMind getContainedMind() {
		return containedMind;
	}

	@Override
	public boolean canRender() {
		return containedMind != null;
	}

	@Override
	public void draw(WorldGraphics g) {
		g.pushStyle();
		;
		if (!containedMind.isNotViable() && containedMind.isConscious()) {
			g.textSize(10);
			float x = 60;
			final int border = 10;
			float height = g.textAscent() + g.textDescent();
			for (IGoal goal : containedMind.getKnowledgeBase().getGoals()) {
				float y = 30;
				String goaltext = goal.toString();
				g.fill(g.color(0, 255, 50));
				float awidth = g.textWidth(goaltext);
				float maxWidth = awidth;
				float centerX = x + awidth / 2;

				ActionQueue queue = containedMind.getWill().getQueueForGoal(goal);
				if (queue != null && !queue.empty()) {
					int length = queue.queueSize();
					g.pushStyle();
					g.stroke(g.color(255, 100, 0));
					g.strokeWeight(10);
					g.line(centerX, y + height + border, centerX, length * (2 * height + border * 3 + border / 2));
					g.popStyle();
				}
				g.rectMode(WorldGraphics.CORNER);
				g.rect(x, y, awidth + border * 2, height + border);
				g.fill(0);
				g.text(goal + "", x + border, y + height + border / 2);
				if (queue != null && !queue.empty()) {
					y += height + border;
					for (int i = 0; i < queue.queueSize(); i++) {
						String text = queue.getActionQueue().get(i).toString();
						float width = g.textWidth(text);
						if (width > maxWidth)
							maxWidth = width;
						g.fill(0);
						g.rect(centerX - width / 2 - border / 2, y, width + border * 2, height + border);
						g.fill(g.color(255, 255, 255));
						g.text(text, centerX - width / 2, y + height + border / 2);
						ITaskGoal itg = queue.getGoalQueue().get(i);
						if (!itg.isDone()) {
							text = itg.toString();
							width = g.textWidth(text);
							if (width > maxWidth)
								maxWidth = width;
							g.fill(g.color(0, 255, 255));
							g.rect(centerX - width / 2 - border / 2, y + height + border * 2, width + border * 2,
									height + border);
							g.fill(0);
							g.text(text, centerX - width / 2, y + 2 * height + border * 2);
						} else {
							g.fill(g.color(255, 50, 50));
							g.rect(centerX - awidth / 2 - border / 2, y + height + border, awidth + border * 2, border);
						}
						y += 2 * height + border * 3 + border / 2;
					}

				}
				x = centerX + maxWidth + border;
			}
		} else {
			g.fill(g.color(255, 200, 200));
			g.textSize(100);
			String msg = containedMind.isNotViable() ? "NO MIND" : "ASLEEP";
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
