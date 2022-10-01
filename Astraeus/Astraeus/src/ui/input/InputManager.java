package ui.input;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Point;
import org.newdawn.slick.state.StateBasedGame;

import engine.Main;
import engine.Settings;
import engine.Settings.GraphicsMode;
import engine.Settings.InfoMode;
import engine.states.Game;
import objects.entity.Entity;
import ui.display.Camera;
import ui.display.DisplayManager;
import ui.sound.AudioManager;

public class InputManager 
{
	private static GameContainer gc;
	private static StateBasedGame sbg;
	private static float soundVolumeOld;
	private static float musicVolumeOld;
	private static Entity selectedEntity;

	public static void setup(GameContainer gc, StateBasedGame sbg)
	{
		InputManager.gc = gc;
		InputManager.sbg = sbg;
	}
	
	public static void leave()
	{
		selectedEntity = null;
	}

	public static void keyReleased(int key, char c)
	{
		try
		{
			// Music
			if (key == Input.KEY_M) 
			{
				if(Settings.soundVolume == 0 && Settings.musicVolume == 0)
				{
					Settings.soundVolume = soundVolumeOld;
					Settings.musicVolume = musicVolumeOld;
					AudioManager.setMusicVolume(Settings.musicVolume);
					DisplayManager.addMessage("Audio Restored");
				}
				else
				{
					soundVolumeOld = Settings.soundVolume;
					musicVolumeOld = Settings.musicVolume;
					Settings.soundVolume = 0;
					Settings.musicVolume = 0;
					AudioManager.setMusicVolume(Settings.musicVolume);
					DisplayManager.addMessage("Audio Muted");
				}
			}

			if (key == Input.KEY_N) 
			{
				AudioManager.setRandomGameMusic();
				AudioManager.playGameMusic();
			}
			
			// Restart Game
			if (key == Input.KEY_R && gc.getInput().isKeyPressed(Input.KEY_LSHIFT)) {
				sbg.enterState(Main.GAME_ID);
			}
			
			// Restart Game
			if (key == Input.KEY_R && gc.getInput().isKeyPressed(Input.KEY_LCONTROL)) {
				sbg.enterState(Main.SELECTION_BATTLE_ID);
			}

			// End Game Early
			if (key == Input.KEY_T && gc.getInput().isKeyPressed(Input.KEY_LSHIFT)) {
				sbg.enterState(Main.END_ID);
			}

			if (key == Input.KEY_SPACE && Game.isGameOver()) {
				sbg.enterState(Main.END_ID);
			}




		}
		catch (SlickException e) 
		{
			e.printStackTrace();
		}
	}

	public static void keyPressed(int key, char c) 
	{
		// Speeds
		if(!Game.isGameOver())
		{
			switch(key)
			{
			// Game Speeds
			
			case Input.KEY_SPACE:
				Game.togglePause();
				break;
			case Input.KEY_1:
				Settings.gameSpeed = 1;
				DisplayManager.addMessage("1x Speed");
				break;
			case Input.KEY_2:
				Settings.gameSpeed = 2;
				DisplayManager.addMessage("2x Speed");
				break;
			case Input.KEY_3:
				Settings.gameSpeed = 5;
				DisplayManager.addMessage("5x Speed");
				break;
			case Input.KEY_4:
				Settings.gameSpeed = 10;
				DisplayManager.addMessage("10x Speed");
				break;
			case Input.KEY_5:
				Settings.gameSpeed = 20;
				DisplayManager.addMessage("20x Speed");
				break;
			case Input.KEY_0:
				Settings.gameSpeed = 100;
				DisplayManager.addMessage("100x Speed (Unsupported)");
				break;

			// Graphics and Information Modes
				
			case Input.KEY_I:
				nextInfoMode();
				break;
			case Input.KEY_O:
				nextGraphicsMode();
				break;
			case Input.KEY_Q:
				toggleLeftDrawings();
				break;
			case Input.KEY_E:
				toggleRightDrawings();
				break;
			case Input.KEY_G:
				toggleGrid();
				break;
			case Input.KEY_B:
				toggleBorders();
				break;

			}

		}

	}
	
	public static void toggleBorders()
	{
		if(Settings.showMapBorders)
		{
			Settings.showMapBorders = false;
			DisplayManager.addMessage("Hiding Borders");
		}
		else
		{
			Settings.showMapBorders = true;
			DisplayManager.addMessage("Showing Borders");
		}
	}

	public static void toggleGrid()
	{
		if(Settings.showGridlines)
		{
			Settings.showGridlines = false;
			DisplayManager.addMessage("Hiding Gridlines");

		}
		else
		{
			Settings.showGridlines = true;
			DisplayManager.addMessage("Showing Gridlines");
		}
	}
	
	public static void toggleLeftDrawings()
	{
		if(Settings.showPlayerOneInfo)
		{
			Settings.showPlayerOneInfo = false;
			DisplayManager.addMessage("Hiding " + Game.getPlayerOne().getName() + " Info");

		}
		else
		{
			Settings.showPlayerOneInfo = true;
			DisplayManager.addMessage("Showing " + Game.getPlayerOne().getName() + " Info");

		}
	}

	public static void toggleRightDrawings()
	{
		if(Settings.showPlayerTwoInfo)
		{
			Settings.showPlayerTwoInfo = false;
			DisplayManager.addMessage("Hiding " + Game.getPlayerTwo().getName() + " Info");

		}
		else
		{
			Settings.showPlayerTwoInfo = true;
			DisplayManager.addMessage("Showing " + Game.getPlayerTwo().getName() + " Info");

		}
	}

	public static void nextInfoMode()
	{	
		if(Settings.infoMode == InfoMode.HIGH || Settings.infoMode == InfoMode.CUSTOM)
		{
			Settings.infoMode = InfoMode.LOW;
			DisplayManager.addMessage("Low Info");

		}
		else if(Settings.infoMode == InfoMode.LOW)
		{
			Settings.infoMode = InfoMode.MEDIUM;
			DisplayManager.addMessage("Medium Info");

		}
		else if(Settings.infoMode == InfoMode.MEDIUM)
		{
			Settings.infoMode = InfoMode.HIGH;
			DisplayManager.addMessage("High Info");
		}

		Settings.loadPresets();
	}

	public static void nextGraphicsMode()
	{
		if(Settings.graphicsMode == GraphicsMode.FANCY || Settings.graphicsMode == GraphicsMode.CUSTOM)
		{
			Settings.graphicsMode = GraphicsMode.FAST;
			DisplayManager.addMessage("Fast Graphics");
		}
		else if(Settings.graphicsMode == GraphicsMode.FAST)
		{
			Settings.graphicsMode = GraphicsMode.FANCY;
			DisplayManager.addMessage("Fancy Graphics");
		}
		Settings.loadPresets();
	}


	public static boolean hasSelectedEntity()
	{
		return selectedEntity != null;
	}
	public static Entity getSelectedEntity()
	{
		return selectedEntity;
	}
	public static void mousePressed(int button, int x, int y)
	{
		if(button == Input.MOUSE_LEFT_BUTTON)
		{				
			float unitScreenX = x / Camera.getZoom() - Camera.getScreenCenterX();
			float unitScreenY = y / Camera.getZoom() - Camera.getScreenCenterY();

			Entity e = Entity.getNearestEntity(new Point(unitScreenX, unitScreenY), Entity.class);

			if(e.getDistance(unitScreenX, unitScreenY) < 100)
			{
				e.select();
				selectedEntity = e;
			}
			else
			{
				e.unselect();
				selectedEntity = null;
			}

			for(Entity other : Game.getEntities())
			{
				if(other != e)
				{
					other.unselect();
				}
			}
		}
	}
}
