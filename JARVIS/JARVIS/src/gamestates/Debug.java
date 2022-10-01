package gamestates;

import java.util.ArrayList;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Line;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;

import core.Engine;
import core.Values;
import entities.core.Coordinate;
import entities.core.Entity;

public class Debug extends BasicGameState
{
	private StateBasedGame sbg;
	private GameContainer gc;
	private int id;
	
	public Debug(int id) 
	{
		this.id = id;
	}

	public int getID(){ return id; }
	
	// Initializer, first time
	public void init(GameContainer gc, StateBasedGame sbg) throws SlickException 
	{
		this.gc = gc;
		this.sbg = sbg;
	}
	
	//render, all visuals
	public void render(GameContainer gc, StateBasedGame sbg, Graphics g) throws SlickException 
	{
		Engine.game.render(gc, sbg, g);
		
		// Render all entities
		for(ArrayList<Entity> list: Engine.game.getAllEntities().values()) {
			for(Entity e: list) {
	    		e.debug(g);
			}	
		}
	}

	//update, runs consistently
	public void update(GameContainer gc, StateBasedGame sbg, int delta) throws SlickException
	{	Engine.game.update(gc, sbg, delta);
		
	}

	public void enter(GameContainer gc, StateBasedGame sbg) throws SlickException 
	{
		gc.setShowFPS(true); // Shows the FPS of the game
	}

	public void leave(GameContainer gc, StateBasedGame sbg) 
	{
		gc.setShowFPS(false); // Shows the FPS of the game
	}


	public void keyPressed(int key, char c)
	{
		Engine.game.keyPressed(key, c);
		switch(key) {
			case Input.KEY_BACKSLASH: // Exit Debug Mode Key Binding
				Values.LastState = Engine.Debug_ID;
				sbg.enterState(Engine.Game_ID);
				break;
		}

	}
	
	public void mousePressed(int button, int x, int y){}

}
