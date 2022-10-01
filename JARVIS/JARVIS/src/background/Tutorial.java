package background;

import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;

import core.Engine;
import core.Values;
import managers.ImageManager;

public class Tutorial {
	
	private Image wasdImage;
	private float wasdX, wasdY, wasdW, wasdH;
	private float time;
	private boolean canRender;
	
	public Tutorial() {
		//50
		this.wasdX = Engine.RESOLUTION_X * 0.02604166666f;
		//890
		this.wasdY = Engine.RESOLUTION_Y * 0.82407407407f;
		//288
		this.wasdW = Engine.RESOLUTION_X * 0.1125f;
		//140
		this.wasdH = Engine.RESOLUTION_Y * 0.12962962963f;
		time = 0;
		canRender = true;
		setImage("wasd");
	}
	
	public void render (Graphics g) {
		wasdImage.draw(wasdX, wasdY, wasdW, wasdH);
	}
	
	public void update () {
		if (time < 500) {
			time++;
		} else if(time < 1000) {
			time++;
			setImage("mouseLeftRight");
		} else if(time < 1500) {
			time++;
			//140
			this.wasdW = Engine.RESOLUTION_X * 0.07291666666f;
			setImage("p");
		} else if(time < 2000) {
			time++;
			setImage("esc");
		} else {
			canRender = false;
		}
	}
	
	public boolean canRender() {
		return canRender;
	}
	
	//acccessors
	public float getY() {
		return wasdY;
	}
	public float getX() {
		return wasdX;
	}
	public float getW() {
		return wasdW;
	}
	public float getH() {
		return wasdH;
	}
	
	//setting images
	public void setImage(String name)
	{
		try
		{
			wasdImage = ImageManager.getImage(name);
		}
		catch(Exception e)		
		{
			System.out.println("Image not found!");
		}
	}
}
