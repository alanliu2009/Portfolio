package animations.effects;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;

import animations.Animation;
import engine.Utility;

public class Sparks extends Animation {
	Image image;

	float w;
	float h;
	int sides;
	Color color;
	float displaySize;
	float displayX;
	float displayY;

	
	int corn;
	int segs;

	public Sparks(float x, float y, float size, int duration) 
	{
		super(x, y, duration);

		this.w = size;
		this.h = size;
		this.x = x;
		this.y = y;
		 setup();
	}
	
	public Sparks(float x, float y, float size, int duration, Color c) 
	{
		super(x, y, duration);

		this.w = size;
		this.h = size;
		this.x = x;
		this.y = y;
		color = c;
		 setup();
	}
	
//	public int getAlpha()
//	{
//		return (int) (255 * percentLeft());
//	}
	
	
	public void setup()
	{
		if(color == null)
		{
			int r1 = 100;
			int gr1= 120;
			int b1 = Utility.random(240, 255);
			int lighten = Utility.random(75);
			color = new Color(r1+lighten, gr1+lighten, b1);
		}
		
		corn = (int) Utility.random(w/2, w);
		segs = Utility.random(1, 7);
		
	}
	
	public void update()
	{
		super.update();
		
		//System.out.println(this.ticks);

		//float percentComplete = ((float) ticks) / ((float) duration);
		displaySize = w;//(int) (w * percentComplete * Utility.random(.5, 1.5));
		displayX = x + Utility.random(-displaySize/2, displaySize/2) - displaySize / 2;
		displayY = y + Utility.random(-displaySize/2, displaySize/2) - displaySize / 2;
	
	}

	public void render(Graphics g) 
	{
	//	Utility.modifyAlpha(color, getAlpha());
		g.setColor(color);
		g.fillRoundRect(displayX, displayY, displaySize, displaySize, corn, segs);
	}
}