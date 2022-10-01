package background.sky;

import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;

import core.Engine;
import core.Values;
import managers.ImageManager;

public class Sky
{
	private int resX, resY;
	private Image blue;
	private Image night;
	private Image stars;
	private Image sunset;
	
	private float nightAlpha;
	private float sunsetAlpha;
	private int tLength;
	private int dayLength;
	private int nightLength;
	private int total;
	
	private Sun sun;
	private Moon moon;
	
	public Sky() throws SlickException
	{
		tLength = Values.transitionLength;
		dayLength = Values.dayLength;
		nightLength = Values.nightLength;
		total = Values.totalLength;
		nightAlpha = 0;
		sunsetAlpha = 0;
		
		resX = Engine.RESOLUTION_X;
		resY = Engine.RESOLUTION_Y;
		
		blue = ImageManager.getImage("daySky");
		sunset = ImageManager.getImage("sunset");
		night = ImageManager.getImage("night");
		stars = ImageManager.getImage("stars");
		
		sun = new Sun(0, resY, dayLength + tLength);
		moon = new Moon(0, resY, nightLength);
	}
	
	public void render(Graphics g)
	{
		blue.draw(0, 0, resX, resY);
		
		night.setAlpha(nightAlpha); //still don't know how to do overlapping stuff w/out using 2 different images
		sunset.setAlpha(sunsetAlpha);
		stars.setAlpha(nightAlpha);
		
		night.draw(0, 0, resX, resY);
		sunset.draw(0, 0, resX, resY);
		stars.draw(0, 0, resX, resY);
		
		sun.render(g);		
		moon.render(g);
	}
	
	public void update(int time)
	{
		int temp = time % total;
		
		if(temp > dayLength &&
				temp < dayLength + tLength)
		{
			sunsetAlpha = (float)Math.sin(3.14159 * (temp - dayLength) / tLength); //https://www.desmos.com/calculator/wxnsmit4pw
			nightAlpha += 0.92 / tLength;
		} 
		else if(temp > total - tLength)
		{
			sunsetAlpha = (float)Math.sin(3.14159 * (temp - (total - tLength)) / tLength);
			nightAlpha -= 0.92 / tLength;
		} 
		else
		{
			sunsetAlpha = 0;
		}
		
		if(temp < dayLength + tLength * 0.5) //sun
		{
			sun.update(temp + tLength * 0.5f);
		} 
		else if (temp > total - tLength * 0.5)
		{
			sun.update(temp - (total - tLength * 0.5f));
		}
		
		if(temp > dayLength + tLength) //moon
		{
			moon.update(temp - dayLength - tLength);

		}
	}
	
	public float getNightAlpha() { return nightAlpha; }
}
