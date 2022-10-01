package objects.ambient;

import java.util.ArrayList;

import org.newdawn.slick.Graphics;

import engine.Settings;
import ui.display.Camera;

public class AmbientManager 
{
	private static Graphics g;
	private static ArrayList<Ambient> ambients;
	
	public static final int NUM_STARS = 8000;
	
	public static void setup(Graphics g)
	{
		AmbientManager.g = g;
		ambients = new ArrayList<Ambient>();
		
//		for(int i = 0; i < NUM_STARS; i++)
//		{
//			ambients.add(new Star());
//		}
		
		//ambients.add(0, new Nebula());
	}
	
	public static void leave()
	{
		ambients.clear();
	}
	
	public static void update()
	{
		if(Settings.showAmbient && Settings.showAmbientMovement)
		{
			for(Ambient a : ambients)
			{
					a.update();
			}
		}
	}
	
	public static void render()
	{
		if(Settings.showAmbient)
		{
			for(Ambient a : ambients)
			{
				if(Camera.isOnScreen(a.getX(), a.getY()))
				{
					a.render(g);
				}
			}
		}
	}

}
