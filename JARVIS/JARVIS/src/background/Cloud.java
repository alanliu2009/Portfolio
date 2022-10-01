package background;

import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;

import core.Engine;
import core.Values;
import managers.ImageManager;

public class Cloud 
{
	private float x;
	private float y;
	private float w;
	private float h;
	
	private float xSpeed;
	private float parallax;
	private float color;
	
	private Integer cloudType;
	
	private Image sprite;
	
	public Cloud() throws SlickException
	{
		this.x = (float) Math.random() * Engine.RESOLUTION_X;
		this.y = (float) Math.random() * Engine.RESOLUTION_Y * 0.4f;
		
		this.w = 256 + 32 * (int)(Math.random() * 3);
		this.h = w * 0.4375f + 28 * (int)(Math.random() * 2);
		
		color = 1;
		
		cloudType = (int)(Math.random() * 4) + 1;
		sprite = ImageManager.getImage("cloud-" + cloudType.toString());
		
		xSpeed = (w * w) * 0.000005f;
		parallax  = w * 0.0003f;
	}
	
	public void render(Graphics g, float x, float y)
	{
		sprite.setFilter(Image.FILTER_NEAREST);
		sprite.setImageColor(color, color, color);
		
		sprite.draw((x * parallax + this.x) % (Engine.RESOLUTION_X + w) - w, this.y + y * parallax, w, h);
	}
	
	public void update(float nightAlpha)
	{
		color = 1 - nightAlpha * 0.6f;
		this.x += this.xSpeed;
	}
}
