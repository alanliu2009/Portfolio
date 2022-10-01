package ui.display.opener;

import org.newdawn.slick.Color;
import org.newdawn.slick.Font;
import org.newdawn.slick.Graphics;

import engine.Main;
import engine.Utility;
import player.Player;
import ui.display.Fonts;

public class PlayerProfile
{	
	
	private float x;
	private float y;
	private float w;
	private float h;
	
	Player owner;
	Font bigFont;
	Font smallFont;
	Color primary ;
	Color secondary;
	
	public PlayerProfile(Player owner, float x, float y)
	{
		this.x = x;
		this.y = y;
		
		w = Main.getScreenWidth() / 5;
		h = Main.getScreenHeight() / 2;
		
		this.owner = owner;
		
	
		
//		if(Main.getScreenWidth() >= 1920)
//		{
			bigFont = Fonts.ocr48;
			smallFont = Fonts.ocr26;
//		}

	}
	
	public void render(Graphics g)
	{	
//		g.setColor(new Color(50, 50, 50, 120));
//		g.fillRect(x,  y + h * .13f,  w,  160);
		

		
		primary = Utility.modifyAlpha(owner.getColorPrimary(),  Opener.getAlpha());
		secondary = Utility.modifyAlpha(owner.getColorSecondary(),  Opener.getAlpha());

		g.setColor(new Color(40, 40, 40,  Opener.getAlpha()));
		Utility.drawStringCenterCenter(g,  bigFont, owner.getName(), x+w/2+2, y+h *.2f+2);		
		
		g.setColor(primary);
		Utility.drawStringCenterCenter(g,  bigFont, owner.getName(), x+w/2, y+h *.2f);		
		
		g.setColor(new Color(60, 60, 60,  Opener.getAlpha()));
		Utility.drawStringCenterCenter(g,  smallFont, owner.getBoonOne() + " | " + owner.getBoonTwo(), x+w/2+2, y+h*.28f+2);		
		
		g.setColor(secondary);
		Utility.drawStringCenterCenter(g,  smallFont, owner.getBoonOne() + " | " + owner.getBoonTwo(), x+w/2, y+h*.28f);				
	
		renderTeamImage(g);
	}

	public void renderTeamImage(Graphics g)
	{
		float iW = h * 4 / 9;
		float iH = h * 3 / 9;
		float iX = x + w/2 - iW/2;
		float iY = y + h * .40f;

		float border = 2;
		g.setLineWidth(border);
		g.setColor(primary);
		g.drawRect(iX - border+1, iY - border+1, iW + border * 2-2, iH + border * 2-2);
		
		Color imgColor = new Color(255, 255, 255,  Opener.getAlpha());
		owner.getTeamImage().draw(iX, iY, iW, iH, imgColor);

		g.setColor(new Color(0, 0, 0, (int) ( Opener.getAlpha()*.20f)));
		g.fillRect(iX - border, iY - border, iW + border * 2, iH + border * 2);
		
	}
	
	public void update()
	{
		
	}
	
	

	

	
	
}
