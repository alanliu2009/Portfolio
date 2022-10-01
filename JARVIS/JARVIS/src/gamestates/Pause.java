package gamestates;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;

import core.Engine;
import core.Values;
import managers.ImageManager;

public class Pause extends BasicGameState
{
	private StateBasedGame sbg;
	int id;
	
	//ready booleans
	private boolean readyRespawn;
	private boolean readyReturn;

	//image background variables
	private Image respawnButton;
	private int respawnButtonX, respawnButtonY, respawnButtonW, respawnButtonH;
	private Image returnButton;
	private int returnButtonX, returnButtonY, returnButtonW, returnButtonH;
	
	public Pause(int id) 
	{
		this.id = id;
	}
	
	// Returns the ID code for this game state
	public int getID() { return id; }


	// Initializer, first time
	public void init(GameContainer gc, StateBasedGame sbg) throws SlickException 
	{
		this.sbg = sbg;
		respawnButtonX = gc.getWidth()/2;
		respawnButtonY = gc.getHeight()/3;
		returnButtonX = gc.getWidth()/2;
		returnButtonY = 2 * gc.getHeight()/3;
		
		//image settings
		
		// 100 width: (int) (0.05208333333*gc.getWidth());
		// 100 height: (int) (0.09259259259*gc.getHeight());
		respawnButtonW = 3* (int) (0.05208333333*gc.getWidth());
		respawnButtonH = (int) (0.09259259259*gc.getHeight());
		
		returnButtonW = 3* (int) (0.05208333333*gc.getWidth());
		returnButtonH = (int) (0.09259259259*gc.getHeight());
		
		this.respawnButton = ImageManager.getImage("respawnButton");
		this.returnButton = ImageManager.getImage("backButton");
	}
	
	//render, all visuals
	public void render(GameContainer gc, StateBasedGame sbg, Graphics g) throws SlickException 
	{
		// Render the game without updating it
		Engine.game.render(gc, sbg, g);
		drawImages(g);
	}

	public void update(GameContainer gc, StateBasedGame sbg, int delta) throws SlickException {
		if (readyRespawn) {
			Values.LastState = Engine.Pause_ID;
			readyRespawn = !readyRespawn;
			Engine.game.respawn();
			sbg.enterState(Engine.Game_ID);
			
			
		} else if (readyReturn) {
			Values.LastState = Engine.Pause_ID;
			readyReturn = !readyReturn;
			sbg.enterState(Engine.WorldSelect_ID);
		}
	}

	public void enter(GameContainer gc, StateBasedGame sbg) throws SlickException {}

	public void leave(GameContainer gc, StateBasedGame sbg) {}


	public void keyPressed(int key, char c)
	{
		switch(key) {
			case Input.KEY_P: // Unpause Key Binding
				Values.LastState = Engine.Pause_ID;
				sbg.enterState(Engine.Game_ID);
				break;
		}
	}
	
	public void mousePressed(int button, int x, int y)
	{
		//check respawnButton
		if ((x > respawnButtonX - (respawnButtonW / 2))
				&& (x < respawnButtonX + (respawnButtonW / 2))
				&& (y > respawnButtonY - (respawnButtonH / 2))
				&& (y < respawnButtonY + (respawnButtonH / 2))
				) {
			readyRespawn = true;
			return;
		}
		//check returnButton
		if ((x > returnButtonX - (returnButtonW / 2))
				&& (x < returnButtonX + (returnButtonW / 2))
				&& (y > returnButtonY - (returnButtonH / 2))
				&& (y < returnButtonY + (returnButtonH / 2))
				) {
			readyReturn = true;
			return;
		}

		// sbg.enterState(2);
	}
	
	// Image Drawing
	public void drawImages(Graphics g) {
		// Draw respawn button
		respawnButton.setFilter(Image.FILTER_NEAREST);
		respawnButton.draw(respawnButtonX - (respawnButtonW / 2), respawnButtonY - (respawnButtonH / 2), respawnButtonW, respawnButtonH);
		
		// Draw back button
		returnButton.setFilter(Image.FILTER_NEAREST);
		returnButton.draw(returnButtonX - (returnButtonW / 2), returnButtonY - (returnButtonH / 2), returnButtonW, returnButtonH);
	}

}
