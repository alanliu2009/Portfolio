package ui.display.opener;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;

import engine.Main;
import engine.Utility;
import engine.states.Game;
import territory.TerritoryManager;
import ui.display.Fonts;

public class Opener 
{
	public static float PANEL_WIDTH_PERCENT = .16f;
	public static float PANEL_HEIGHT_PERCENT = .16f;
	public static float BAR_HEIGHT_PERCENT = .10f;

//
//
//	private static EntityPanel unitPanel;
//	private static Minimap minimap;
//	private static Mainbar mainbar;
//	private static UnitList unitListLeft;
//	private static UnitList unitListRight;

	private static PlayerProfile playerProfileLeft;
	private static PlayerProfile playerProfileRight;

	
	private static Graphics g;

	
	private static int startingFrames;
	private static int framesLeft;
	private static int alpha;
	
	public static void setup(Graphics g)
	{
		startingFrames = 600;
		framesLeft = startingFrames;
		
		Opener.g = g;
		
//		float PANEL_PIXEL_WIDTH =  Main.getScreenWidth() * PANEL_WIDTH_PERCENT;
//		float PANEL_PIXEL_HEIGHT =  Main.getScreenHeight() * PANEL_HEIGHT_PERCENT;
//		float PANEL_LOCATION_Y = Main.getScreenHeight() * (1-PANEL_HEIGHT_PERCENT);
//		float PANEL_LOCATION_X = Main.getScreenWidth() * (1-PANEL_WIDTH_PERCENT);
//		float BAR_LOCATION_X = PANEL_PIXEL_WIDTH - 2;
//		float BAR_LOCATION_Y = Main.getScreenHeight() * (1-BAR_HEIGHT_PERCENT);
//		float BAR_WIDTH = Main.getScreenWidth() - PANEL_PIXEL_WIDTH * 2 + 4;
//		float BAR_HEIGHT = Main.getScreenHeight() * BAR_HEIGHT_PERCENT;

//		minimap = new Minimap(0, PANEL_LOCATION_Y, PANEL_PIXEL_WIDTH , PANEL_PIXEL_HEIGHT);
//		unitPanel = new EntityPanel(PANEL_LOCATION_X, PANEL_LOCATION_Y, PANEL_PIXEL_WIDTH,  PANEL_PIXEL_HEIGHT);
////		unitPanel = new EntityPanel(PANEL_LOCATION_X, PANEL_LOCATION_Y-200, PANEL_PIXEL_WIDTH,  PANEL_PIXEL_HEIGHT);
//		mainbar = new Mainbar(BAR_LOCATION_X, BAR_LOCATION_Y, BAR_WIDTH, BAR_HEIGHT);
		playerProfileLeft = new PlayerProfile(Game.getPlayerOne(), Main.getScreenWidth() * .20f, Main.getScreenHeight() * .25f);
		playerProfileRight = new PlayerProfile(Game.getPlayerTwo(), Main.getScreenWidth() * .60f,  Main.getScreenHeight() * .25f);
		
		

	}
	
	public static void render()
	{
		float percent = (float) framesLeft / (float) startingFrames;
		alpha = (int) (Math.min((Math.pow(percent, 4) * 5000), 255));
		
//		g.setColor(new Color(30, 30, 30,  (int) (Opener.getAlpha() *  .3f)));
//		g.fillRect(0, Main.getScreenHeight() * .25f,  Main.getScreenWidth(), Main.getScreenHeight() * .5f);
//		
		
		
		renderBackground(g);
		
		playerProfileLeft.render(g);
		playerProfileRight.render(g);
		
		g.setColor(new Color(40, 40, 40, Opener.getAlpha()));
		Utility.drawStringCenterCenter(g,  Fonts.ocr48, "VS", Main.getScreenWidth() * .5f+2, Main.getScreenHeight() * .37f+2);		
		g.setColor(new Color(255, 255, 255, Opener.getAlpha()));
		Utility.drawStringCenterCenter(g,  Fonts.ocr48, "VS", Main.getScreenWidth() * .5f, Main.getScreenHeight() * .37f);		
		
		
		g.setColor(new Color(40, 40, 40, Opener.getAlpha()));
		Utility.drawStringCenterCenter(g,  Fonts.ocr32, "Map", Main.getScreenWidth() * .5f+2, Main.getScreenHeight() * .50f+2);		
		g.setColor(new Color(200, 200, 120,  Opener.getAlpha()));
		Utility.drawStringCenterCenter(g,  Fonts.ocr32, "Map", Main.getScreenWidth() * .5f, Main.getScreenHeight() * .50f);		
		
		g.setColor(new Color(40, 40, 40, Opener.getAlpha()));
		Utility.drawStringCenterCenter(g,  Fonts.ocr26i, TerritoryManager.getTerritory().getName(), Main.getScreenWidth() * .5f+2, Main.getScreenHeight() * .54f+2);		
		g.setColor(new Color(255, 255, 255, Opener.getAlpha()));
		Utility.drawStringCenterCenter(g,  Fonts.ocr26i, TerritoryManager.getTerritory().getName(), Main.getScreenWidth() * .5f, Main.getScreenHeight() * .54f);		
		
		
		//Utility.drawStringCenterCenter(g,  Fonts.ocr48, "VS", 500f, 500);		

//		unitPanel.render(g);
//		minimap.render(g);
//		mainbar.render(g);
//		unitListLeft.render(g);
//		unitListRight.render(g);

	}
	
	public static void renderBackground(Graphics g)
	{
		float a = .2f;

		Color c1 = new Color(Game.getPlayerOne().getColorPrimary().getRed(), 	// left red
							 Game.getPlayerOne().getColorPrimary().getGreen(), 	// left green
							 Game.getPlayerOne().getColorPrimary().getBlue(), 	// left blue					
							 (int) (Opener.getAlpha() * a));
		
		Color c2 = new Color(Game.getPlayerTwo().getColorPrimary().getRed(), 	// left red
				 Game.getPlayerTwo().getColorPrimary().getGreen(), 	// left green
				 Game.getPlayerTwo().getColorPrimary().getBlue(), 	// left blue					
				  (int) (Opener.getAlpha() * a));
		
	
		g.setLineWidth(10);
		for(int i = 0; i < Main.getScreenHeight() / 2; i += 10)
		{
			int yPos = (int) (Main.getScreenHeight() * .25f + i);
			g.drawGradientLine (0, yPos, c1, Main.getScreenWidth(), yPos, c2); 
		}
		
	

	}
	
	public static int getAlpha()
	{
		return alpha;
	}
	
	public static void update()
	{
		if(framesLeft > 0)
		{
			framesLeft--;	
		}

		
		
		
		playerProfileLeft.update();
		playerProfileRight.update();
		
//		if(InputManager.hasSelectedEntity())
//		{
//			Entity e = InputManager.getSelectedEntity();
//			if(e instanceof Unit)
//			{
//				unitPanel.setUnit((Unit) e);
//			}
//			else if(e instanceof Node)
//			{
//				unitPanel.setNode((Node) e);
//			}
//		}
//		
//		mainbar.update();
	}
	
}
