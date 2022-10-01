package ui.display.hud;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;

import engine.Main;
import engine.Utility;
import engine.states.Game;

public class Mainbar extends HudElement
{
	public Relativebar relativebar;
	private PlayerInfo playerOne;
	private PlayerInfo playerTwo;
	
	public final float CENTER_START = .45f;
	public final float CENTER_WIDTH = .1f;
	
	public Mainbar(float x, float y, float w, float h)
	{
		super(x, y, w, h);
		
		relativebar = new Relativebar(x + 2, y, w - 4, 10);
		playerOne = new PlayerInfo(Game.getPlayerOne(), x, y + 20, w * CENTER_START, h);
		playerTwo = new PlayerInfo(Game.getPlayerTwo(), x + w * (CENTER_START + CENTER_WIDTH/2), y + 20,  w * CENTER_START, h);
	}
	
	public void render(Graphics g)
	{		
		super.render(g);
		relativebar.render(g);

		
		g.setColor(new Color(10, 10, 10));
		g.fillRect(x + w * CENTER_START, y + 12, w * CENTER_WIDTH, h-14);
		


		g.setColor(Color.white);
		
		//Utility.drawStringCenterCenter(g, Hud.smallFont, "Time", x + w / 2, y + h *.2f );
		Utility.drawStringCenterCenter(g, Hud.bigFont, ""+(int) (Game.getTime()/60), x + w / 2, y + h *.4f );
		Utility.drawStringCenterCenter(g, Hud.smallFont, Math.min(60, Main.getFPS()) + " FPS", x + w / 2, y + h * .75f);

//		g.drawRect(x + w / 2, 0, 1, Main.getScreenHeight());
//		g.drawRect(x, 0, 1, Main.getScreenHeight());
//		g.drawRect(x + w , 0, 1, Main.getScreenHeight());

		playerOne.render(g);
		playerTwo.render(g);

	}
	
	public void update()
	{
		relativebar.update();
	}
	
}
