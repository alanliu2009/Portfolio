package ui.display.hud;

import org.newdawn.slick.Font;
import org.newdawn.slick.Graphics;

import engine.Main;
import engine.states.Game;
import objects.entity.Entity;
import objects.entity.node.Node;
import objects.entity.unit.Unit;
import ui.display.Fonts;
import ui.input.InputManager;

public class Hud 
{
	public static float PANEL_WIDTH_PERCENT = .16f;
	public static float PANEL_HEIGHT_PERCENT = .16f;
	public static float BAR_HEIGHT_PERCENT = .10f;
	
	public static Font bigFont;
	public static Font mediumFont;
	public static Font headingFont;
	public static Font smallFont;


	private static EntityPanel unitPanel;
	private static Minimap minimap;
	private static Mainbar mainbar;
	private static UnitList unitListLeft;
	private static UnitList unitListRight;
	private static PlayerMessages playerMessagesLeft;
	private static PlayerMessages playerMessagesRight;
	
	private static Graphics g;

	
	public static void setup(Graphics g)
	{
		Hud.g = g;
		
		float PANEL_PIXEL_WIDTH =  Main.getScreenWidth() * PANEL_WIDTH_PERCENT;
		float PANEL_PIXEL_HEIGHT =  Main.getScreenHeight() * PANEL_HEIGHT_PERCENT;
		float PANEL_LOCATION_Y = Main.getScreenHeight() * (1-PANEL_HEIGHT_PERCENT);
		float PANEL_LOCATION_X = Main.getScreenWidth() * (1-PANEL_WIDTH_PERCENT);
		float BAR_LOCATION_X = PANEL_PIXEL_WIDTH - 2;
		float BAR_LOCATION_Y = Main.getScreenHeight() * (1-BAR_HEIGHT_PERCENT);
		float BAR_WIDTH = Main.getScreenWidth() - PANEL_PIXEL_WIDTH * 2 + 4;
		float BAR_HEIGHT = Main.getScreenHeight() * BAR_HEIGHT_PERCENT;
		float BUFFER = Main.getScreenHeight() * .07f;
				
		minimap = new Minimap(0, PANEL_LOCATION_Y, PANEL_PIXEL_WIDTH , PANEL_PIXEL_HEIGHT);
		unitPanel = new EntityPanel(PANEL_LOCATION_X, PANEL_LOCATION_Y, PANEL_PIXEL_WIDTH,  PANEL_PIXEL_HEIGHT);
	//	unitPanel = new EntityPanel(PANEL_LOCATION_X, PANEL_LOCATION_Y-500, PANEL_PIXEL_WIDTH,  PANEL_PIXEL_HEIGHT);
		mainbar = new Mainbar(BAR_LOCATION_X, BAR_LOCATION_Y, BAR_WIDTH, BAR_HEIGHT);
		unitListLeft = new UnitList(Game.getPlayerOne(), 0, PANEL_LOCATION_Y - BUFFER);
		unitListRight = new UnitList(Game.getPlayerTwo(), PANEL_LOCATION_X, PANEL_LOCATION_Y - BUFFER);
		
		playerMessagesLeft = new PlayerMessages(Game.getPlayerOne(), Main.getScreenWidth() * .03f , Main.getScreenHeight() * .03f);
		playerMessagesRight = new PlayerMessages(Game.getPlayerTwo(), Main.getScreenWidth() * .85f, Main.getScreenHeight() * .03f);
		
		
		if(Main.getScreenWidth() > 1920)
		{
			headingFont = Fonts.ocr26i;
			bigFont = Fonts.ocr28;
			mediumFont = Fonts.ocr20;
			smallFont = Fonts.ocr16;
		}
		else if(Main.getScreenWidth() == 1920)
		{
			headingFont = Fonts.ocr20i;
			bigFont = Fonts.ocr22;
			mediumFont = Fonts.ocr16;
			smallFont = Fonts.ocr14;
		}
		else
		{
			headingFont = Fonts.ocr18i;
			bigFont = Fonts.ocr20;
			mediumFont = Fonts.ocr14;
			smallFont = Fonts.ocr12;
		}
	}
	
	public static void render()
	{
		unitPanel.render(g);
		minimap.render(g);
		mainbar.render(g);
		unitListLeft.render(g);
		unitListRight.render(g);
		playerMessagesLeft.render(g);
		playerMessagesRight.render(g);
	}
	
	public static void update()
	{
		if(InputManager.hasSelectedEntity())
		{
			Entity e = InputManager.getSelectedEntity();
			if(e instanceof Unit)
			{
				unitPanel.setUnit((Unit) e);
			}
			else if(e instanceof Node)
			{
				unitPanel.setNode((Node) e);
			}
		}
		
		mainbar.update();
	}
	
}
