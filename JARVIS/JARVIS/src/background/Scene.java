package background;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;

import core.Engine;

public class Scene
{
	protected Layer foreground;
	protected Layer middleground;
	protected Layer background;
	
	protected int resX; //placeholders so this code is actually readable
	protected int resY;
	
	protected Color tempFrontFiller;
	protected Color tempBackFiller;
	
	protected Color frontFiller;
	protected Color backFiller;
	
	protected float lighting;
	protected float alpha;
	
	public Scene() throws SlickException
	{
		resX = Engine.RESOLUTION_X;
		resY = Engine.RESOLUTION_Y;
		lighting = 1;
		
		foreground = new Layer("placeholder", 0.4f, 0, 0.4f, 1.8f, 0.35f);
		middleground = new Layer("placeholder", 0.3f, 0, 0.4f, 1f, 0.25f);
		background  = new Layer("placeholder", 0.2f, 0, 0.35f, 1f, 0.4f);

		frontFiller = foreground.getColor();
		backFiller = middleground.getColor();
		
		tempFrontFiller = frontFiller;
		tempBackFiller = backFiller;
	}
	
	public void setSceneAlpha(float f)
	{
		alpha = f;
		foreground.setAlpha(f);
		middleground.setAlpha(f);
		background.setAlpha(f);
		
		frontFiller.a = f;
		backFiller.a = f;
	}
	
	public void render(Graphics g, float x, float y)
	{
		y  *= 0.5f; //parallax
		
		background.render(g, x, y);
		
		g.setColor(tempBackFiller);
		g.fillRect(0, resY * 0.6f + (y * middleground.getParallax()), resX, resY * 0.25f);	
		
		middleground.render(g, x - 600, y);
		
		g.setColor(tempFrontFiller);
		g.fillRect(0, resY * 0.7f + (y * foreground.getParallax()), resX, resY * 0.5f);	
		
		foreground.render(g, x, y);
	}
	
	public void update(float light) 
	{
		lighting = 1 - (light * 0.6f);
		
		tempBackFiller.r = backFiller.r * lighting;
		tempBackFiller.g = backFiller.g * lighting;
		tempBackFiller.b = backFiller.b * lighting;
		tempBackFiller.a = frontFiller.a;
		
		tempFrontFiller.r = frontFiller.r * lighting;
		tempFrontFiller.g = frontFiller.g * lighting;
		tempFrontFiller.b = frontFiller.b * lighting;
		tempFrontFiller.a = frontFiller.a; 
		//this is retarded
		
		background.update(lighting);
		middleground.update(lighting);
		foreground.update(lighting);
	}
}
