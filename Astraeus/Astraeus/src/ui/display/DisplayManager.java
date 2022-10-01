package ui.display;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;

import animations.AnimationManager;
import engine.Settings;
import engine.Values;
import engine.states.Game;
import objects.ambient.AmbientManager;
import objects.entity.missile.Missile;
import objects.entity.unit.Unit;
import territory.TerritoryManager;
import ui.display.hud.Hud;
import ui.display.message.GlobalMessage;
import ui.display.message.MessageList;
import ui.display.opener.Opener;

public class DisplayManager 
{
	public static final int MESSAGE_TIME = 120;
	private static Graphics g;

	private static MessageList globalMessages;


	public static void setup(Graphics g)
	{
		DisplayManager.g = g;
		Hud.setup(g);
		Opener.setup(g);
		
		globalMessages = new MessageList();

	}

	public static void leave()
	{
		globalMessages.clear();
	}

	public static void updateCanPause()
	{
		Opener.update();
	}
	
	public static void update()
	{
		Hud.update();
		globalMessages.update();
	}

	public static void render()
	{
		// Skybox first
		//g.scale(Camera.getZoom()/2, Camera.getZoom()/2);
		g.translate(Camera.getScreenCenterX()/16, Camera.getScreenCenterY()/16);
		renderSkybox();

		// Translate and Zoom for In-World Graphics
		g.resetTransform();	
		g.scale(Camera.getZoom(), Camera.getZoom());
		g.translate(Camera.getScreenCenterX(), Camera.getScreenCenterY());
		renderWorld();


		// Reset for Overlay
		g.resetTransform();	
		renderOverlay();
	}

	private static void renderSkybox()
	{
		if(Settings.showBackground)
		{
			Image i = TerritoryManager.getBackground();		//		 3840 x 2160

			float w = 3840 * 4/5;
			float h = 2160 * 4/5;

			int bright = (int) (255 * Settings.backgroundBrightness);
			Color c = new Color(bright, bright, bright);
			i.draw(-w/4, -h/4, w, h, c);
		}


	}

	private static void renderWorld()
	{
		AmbientManager.render();	

		// Draw edge of map and ambient objects
		drawBorders();
		drawGridlines();


		// Draw resource nodes and pickups
		TerritoryManager.render(g);

		// Render each unit
		for (Unit a : Game.getUnits()) 
		{			
			if(Camera.isNearScreen(a.getX(), a.getY()))
			{
				a.render(g);
			}
		}

		// Render each missile
		for (Missile m: Game.getMissiles()) 
		{			
			if(Camera.isNearScreen(m.getX(), m.getY()))
			{
				m.render(g);
			}
		}

		// Draw animations
		AnimationManager.render();

		// Render graphics drawn by the player's draw() method
		Game.getPlayerOne().render(g);
		Game.getPlayerTwo().render(g);


	}

	private static void renderOverlay()
	{
		// Display bottom bar, minimap, and unit info panel
		Hud.render();
		Opener.render();
		
		// Renders messages
		globalMessages.render(g);


	}

	private static void drawGridlines()
	{
		if(Settings.showGridlines)
		{
			g.setLineWidth(2);	

			g.setFont(Fonts.ocr32);
			// Horizontal Lines (small)
			for(float i = 1; i <= 4; i++)
			{
				g.setColor(new Color(150, 20, 50, 50));

				float x = i * Values.PLAYFIELD_WIDTH / 8;
				g.drawLine(-x, Game.getMapTopEdge(), -x, Game.getMapBottomEdge());
				g.drawLine(x, Game.getMapTopEdge(), x, Game.getMapBottomEdge());

				g.setColor(Color.white);
				g.drawString("+"+(int) x, x, 0);
				g.drawString("-"+(int) x, -x, 0);

			}

			// Vertical Lines (small)
			for(float i = 1; i <= 4; i++)
			{
				g.setColor(new Color(150, 20, 50, 50));

				float y = i * Values.PLAYFIELD_HEIGHT / 8;
				g.drawLine(Game.getMapLeftEdge(), -y, Game.getMapRightEdge(), -y);
				g.drawLine(Game.getMapLeftEdge(), y, Game.getMapRightEdge(), y);
				g.setColor(Color.white);
				g.drawString("+"+(int) y, 0, y);
				g.drawString("-"+(int) y, 0, -y);

			}

			g.drawString("0", 0, 0);

			// Large lines
			g.setColor(new Color(150, 20, 50, 100));
			g.setLineWidth(3);		

			g.drawLine(Game.getMapLeftEdge(), 0, Game.getMapRightEdge(),0);
			g.drawLine(0, Game.getMapTopEdge(), 0, Game.getMapBottomEdge());
		}
	}

	private static void drawBorders() 
	{
		if(Settings.showMapBorders)
		{
			g.setColor(new Color(150, 20, 50));

			g.setLineWidth(3);		

			// LEFT
			g.drawLine(Game.getMapLeftEdge(), Game.getMapTopEdge(),
					Game.getMapLeftEdge(), Game.getMapBottomEdge());

			// TOP
			g.drawLine(Game.getMapLeftEdge(), Game.getMapTopEdge(),
					Game.getMapRightEdge(), Game.getMapTopEdge());

			// RIGHT
			g.drawLine(Game.getMapRightEdge(), Game.getMapTopEdge(),
					Game.getMapRightEdge(), Game.getMapBottomEdge());
			// BOTTOM
			g.drawLine(Game.getMapLeftEdge(), Game.getMapBottomEdge(),
					Game.getMapRightEdge(), Game.getMapBottomEdge());

			g.resetLineWidth();
		}


	}

	public static void addMessage(String message)
	{
		globalMessages.addMessage(new GlobalMessage(message, MESSAGE_TIME));
	}

	public static void addMessage(String message, int time)
	{
		globalMessages.addMessage(new GlobalMessage(message, time));
	}

}
