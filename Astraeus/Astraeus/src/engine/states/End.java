
package engine.states;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;

import engine.Main;
import engine.Utility;
import ui.display.Fonts;
import ui.display.Images;

public class End extends BasicGameState 
{
	private StateBasedGame sbg;
	private GameContainer gc;
	private int id;

	
	public End(int id)
	{
		this.id = id;
	}
	public void init(GameContainer gc, StateBasedGame sbg) throws SlickException 
	{
		this.sbg = sbg;
		this.gc = gc;
		Images.loadEndImages();
	}

	public void render(GameContainer gc, StateBasedGame sbg, Graphics g) throws SlickException 
	{	
		g.setColor(Color.white);
		Utility.drawStringCenterCenter(g, Fonts.ocr32, "Game Over!", Main.getScreenWidth()/2, Main.getScreenHeight()/2);
	}	
	
//	public void drawLeftBar(String label, Color c, int y, int value, float percent, int id, Graphics g)
//	{
//		drawBar(label, c, y, value, percent, id, g, 20);
//	}
//	public void drawRightBar(String label, Color c, int y, int value, float percent, int id, Graphics g)
//	{
//		drawBar(label, c, y, value, percent, id, g, Engine.getScreenHeight()/2);
//	}
	
//	private void drawBar(String label, Color c, int y, int value, float percent, int id, Graphics g, int x)
//	{
//		
//	
//	}

	public void update(GameContainer gc, StateBasedGame sbg, int delta) throws SlickException 
	{
		sbg.enterState(Main.SELECTION_BATTLE_ID);

	}
	
	public void enter(GameContainer gc, StateBasedGame sbg)
	{

	}
	
	public void leave(GameContainer gc, StateBasedGame sbg)
	{
		
	}
		
	public void keyReleased(int key, char c) 
	{

	}

	public int getID() 
	{
		return id;
	}

}
