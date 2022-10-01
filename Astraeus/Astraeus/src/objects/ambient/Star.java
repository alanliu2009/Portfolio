package objects.ambient;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;

import engine.Utility;
import engine.states.Game;

public class Star extends Ambient
{
	public final static float STAR_SPEED = 0.002f;

	private Color color;
	private int size;
	private int scale;
	private float xSpeed;

	public Star()
	{
		super(Utility.random(Game.getMapLeftEdge(), Game.getMapRightEdge()), 
				Utility.random(Game.getMapTopEdge(), Game.getMapBottomEdge()));

		setSize();
		color = new Color(getBrightness(), getBrightness(), getBrightness());
		xSpeed = STAR_SPEED * size;
	}

	public int getBrightness()
	{
		return 25 + Utility.random(20, 45) * scale;
	}
	public void setSize()
	{
		scale = Utility.random(1, 3);

		// Reroll Medium Stars 33% of the time
		if(scale == 2 && Math.random() < .33f)
		{
			scale = Utility.random(1, 3);
		}

		// Reroll Large Stars 67% of the time
		if(scale == 3 && Math.random() < 67)
		{
			scale = Utility.random(1, 3);
		}

		size = (int) Math.pow(2, scale);		
	}
	
	public void render(Graphics g)
	{
		drawStar(g);


//			if(scale == 1 && Camera.getZoom() >= Camera.ZOOM_CLOSE)
//			{
//				drawStar(g);
//			}
//			if(scale == 2 && Camera.getZoom() >= Camera.ZOOM_MEDIUM)
//			{
//				drawStar(g);
//			}
//			if(scale == 3)
//			{
//				drawStar(g);
//			}
//		

	}

	private void drawStar(Graphics g)
	{	

		
//		g.setColor(new Color(70, 20, 70, 100));
//		g.fillOval(x+Utility.random(-5, 5), y+Utility.random(-5, 5), size+Utility.random(-5, 5), size+Utility.random(-5, 5));
//		
		g.setColor(color);
		g.fillOval(x, y, size, size);
	}

//	public void update()
//	{
//		x += xSpeed * scale;
//
//		if(x > Game.getMapRightEdge())
//		{
//			x = Game.getMapLeftEdge();
//		}
//	}
}
