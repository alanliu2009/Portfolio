package animations.effects;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;

import animations.Animation;
import engine.Utility;

public class Smoke extends Animation {
	Image image;

	int greyscale;
	
	float w;
	float h;

	public Smoke(float x, float y, float size) {
		super(x, y, 50);

		this.w = size;
		this.h = size;
		this.x = x - w / 2 + Utility.random(-w / 3, w / 3);
		this.y = y - h / 2 + Utility.random(-h / 3, h / 3);
		greyscale = 175;
	}
	
	public Smoke(float x, float y, float size, int duration) {
		super(x, y, duration);

		this.w = size;
		this.h = size;
		this.x = x - w / 2 + Utility.random(-w / 3, w / 3);
		this.y = y - h / 2 + Utility.random(-h / 3, h / 3);
		greyscale = 175;
	}

	public void render(Graphics g) {

		float percent = 1 - ((float) ticks) / ((float) duration);
		float width = w - (w * percent * .75f);
		float height = h - (h * percent * .75f);

		g.setColor(new Color(greyscale, greyscale, greyscale, getFadeAlphaValue()));
		g.fillOval(x, y, width * percentLeft(), height * percentLeft());

	}
}