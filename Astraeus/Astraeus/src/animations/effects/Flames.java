package animations.effects;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;

import animations.Animation;
import engine.Utility;

public class Flames extends Animation {
	Image image;

	float w;
	float h;
	int sides;
	Color outer;
	Color inner;
	float displaySize;
	float displayX;
	float displayY;

	public Flames(float x, float y, float size) 
	{
		super(x, y, 25);

		this.w = size;
		this.h = size;
		this.x = x;
		this.y = y;
	}
	
	public Flames(float x, float y, float size, int duration) 
	{
		super(x, y, duration);

		this.w = size;
		this.h = size;
		this.x = x;
		this.y = y;
	}
	
	public void update()
	{
		super.update();
		
		int r1 = 255;
		int gr1= Utility.random(0, 255);
		int b1 = Utility.random(0, gr1);
		float percentComplete = ((float) ticks) / ((float) duration);
		int alpha = (int) (percentComplete * 100) + 155;
		
		
		outer = new Color(r1, gr1, b1, alpha);
		
		displaySize = (int) (w * (1-percentComplete) * Utility.random(.5, 1.5));
		displayX = x + Utility.random(-displaySize/2, displaySize/2) - displaySize / 2;
		displayY = y + Utility.random(-displaySize/2, displaySize/2) - displaySize / 2;
	
	}

	public void render(Graphics g) 
	{	
		g.setColor(outer);
		g.fillOval(displayX, displayY, displaySize, displaySize);
	}
}