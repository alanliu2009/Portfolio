package background;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;

import background.sky.Sky;
import core.Engine;
import core.Values;
import managers.ImageManager;

public class Layer 
{

	private Image sprite;
	private float color;
	private float parallax;
	private float alpha;
	
	private float x, y, w, h, resX, resY;
	
	public Layer(String image, float parallax, float x, float y, float w, float h)
	{	
		alpha = 1;
		color = 1;
		
		resX = Engine.RESOLUTION_X;
		resY = Engine.RESOLUTION_Y;
		x = 0;
		this.y = resY * y;
		this.h = resY * h;
		this.w = resX * w;
		
		this.parallax = parallax;
		
		try { sprite = ImageManager.getImage(image); } 
		catch (Exception e) { }
	}
	
	public void render(Graphics g, float x, float y)
	{
		sprite.setFilter(Image.FILTER_NEAREST);
		sprite.setAlpha(alpha);
		sprite.setImageColor(color, color, color, alpha);
		
		sprite.draw(x * parallax % w, y * parallax + this.y, w, h);
		sprite.draw(x * parallax % w + w, y * parallax + this.y, w, h);
	}
	
	public void update(float lighting)
	{
		color =  lighting;
	}
	
	public float getParallax() 
	{
		return(parallax);
	}
	
	public void setAlpha(float alpha)
	{
		this.alpha = alpha;
	}
	
	public void setImage(String name)
	{
		try
		{
			sprite = ImageManager.getImage(name);
		}
		catch (Exception e) { }
	}
	
	public Color getColor()
	{
		return sprite.getColor(0, (int)(h * 0.1f));
	}
}
