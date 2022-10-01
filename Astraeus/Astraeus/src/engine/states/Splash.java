package engine.states;

import java.awt.Font;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.TrueTypeFont;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;
import org.newdawn.slick.state.transition.FadeInTransition;
import org.newdawn.slick.state.transition.FadeOutTransition;

import engine.Main;
import engine.Values;
import ui.display.Fonts;
import ui.display.Images;
import ui.sound.AudioManager;

public class Splash extends BasicGameState 
{
	private Image splash;
	private Image splashScaled;
	private int id;
	private int step;
	private String message = "";
	public static boolean rock = false;
	
	public Splash(int id)
	{
		this.id = id;
	}
	
	public void init(GameContainer gc, StateBasedGame sbg) throws SlickException 
	{
		gc.setMouseGrabbed(true);
		Images.loadSelectImages();

		splash = new Image("res/menus/metal.png");
		splashScaled = 	splash.getScaledCopy(Main.getScreenWidth(), Main.getScreenHeight());
		Fonts.ocr32 = new TrueTypeFont(new Font("Calibri", Font.BOLD, 32), false);
	}

	public void render(GameContainer gc, StateBasedGame sbg, Graphics g) throws SlickException 
	{
		if(splash != null)
		{  
			splashScaled.draw();
		}
		
		String m = message;
		
		TrueTypeFont f = Fonts.ocr32;
		g.setFont(f);	
		g.setColor(new Color(220, 220, 220, 255));
		g.drawString(m, Main.getScreenWidth()/2 - f.getWidth(m)/2, Main.getScreenHeight() - f.getHeight(m)/2 - 50);
		if(step == 0)
		{
			step = 1;
		}
	}
	

	public void keyPressed(int key, char c) 
	{
		if(key == Input.KEY_R)
		{
			System.out.println("Rock mode activated");
			rock = true;
		}
	}
	

	public void update(GameContainer gc, StateBasedGame sbg, int delta) throws SlickException 
	{		
		message = "Loading Fonts";
		
		if(step == 1)
		{	
			Fonts.loadFonts();
			step = 2;
			message = "Loading Images";
		}
		else if(step == 2)
		{
			Images.loadImages();
			step = 3;
			message = "Loading SFX";
		}
		else if(step == 3)
		{
			AudioManager.loadSFX();
			step = 4;
			message = "Loading Music";
		}
		else if(step == 4)
		{
			AudioManager.loadMusic();
			step = 5;
			message = "Ready!";
		}
		
		if(step == 5)
		{
			
		sbg.enterState(Main.SELECTION_BATTLE_ID, 
				new FadeOutTransition(Color.black, Values.TRANSITION_FADE_TIME), 
				new FadeInTransition(Color.black, Values.TRANSITION_FADE_TIME));
		}

	}
	

		
	public int getID() 
	{
		return id;
	}

}
