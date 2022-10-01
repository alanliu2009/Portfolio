package ui.display.hud;

import org.newdawn.slick.Font;
import org.newdawn.slick.Graphics;

import engine.Main;
import engine.Settings;
import engine.states.Game;
import player.Player;
import ui.display.Fonts;

public class PlayerMessages
{	
	Player owner;
	protected float x;
	protected float y;
	
	Font myFont;

	private int spacing = 28;

	public PlayerMessages(Player owner, float x, float y)
	{
		this.x = x;
		this.y = y;
		this.owner = owner;
	}

	public void render(Graphics g)
	{
		if (Settings.showPlayerOneInfo && owner == Game.getPlayerOne() ||
				Settings.showPlayerTwoInfo && owner == Game.getPlayerTwo()) 
		{
			setFont();
			g.setFont(myFont);
			
			for(int i = 0; i < owner.getMessages().size(); i++)
			{
				g.setColor(owner.getMessage(i).getColor());
				
				
//				if(owner == Game.getPlayerOne())
//				{
					g.drawString(owner.getMessage(i).getMessage(), x, y + spacing * i);
//
//				}
//				else
//				{
//					Utility.drawStringRightTop(g, myFont, owner.getMessages(i).getMessage(), x, y + spacing * i);
//				}
			}
		}
	
	}

	public void setFont()
	{
		if(Main.getScreenWidth() > 1920)
		{
			myFont = Fonts.ocr20;
			spacing = 28;
		}
		else if(Main.getScreenWidth() == 1920)
		{
			myFont = Fonts.ocr20;
			spacing = 23;
		}
		else
		{
			myFont = Fonts.ocr16;
			spacing = 19;
		}
	}



}
