package ui.display.hud;

import org.newdawn.slick.Graphics;

import engine.states.Game;

public class Relativebar extends HudElement
{
	BarMode mode;
	float leftValue;
	float rightValue;
	float leftPercentRecent = .5f;
	float rightPercentRecent = .5f;

	public Relativebar(float x, float y, float w, float h)
	{
		super(x, y, w, h);
		mode = BarMode.VALUE;
	}
	
	public void update()
	{
		
		leftValue = 0;
		rightValue = 0;
		
		if(mode.equals(BarMode.VALUE))
		{			
			leftValue = Game.getPlayerOne().getFleetValue();
			rightValue = Game.getPlayerTwo().getFleetValue();
		}
	}
	
	public void render(Graphics g)
	{		
		if(mode.equals(BarMode.VALUE))
		{
			float totalValue = leftValue + rightValue;
			float leftPercent = leftValue / totalValue;
			float rightPercent = rightValue / totalValue;
			
			if(leftPercent < 1 && leftPercent > 0)
			{
				leftPercentRecent = leftPercent * .3f + leftPercentRecent * .7f;
			}
			else if(leftPercent == 0)
			{
				leftPercentRecent = 0;
			}
			else if(leftPercent >= 1)
			{
				leftPercentRecent = 1;
			}
//			
			
			if(rightPercent < 1 && rightPercent > 0)
			{
				rightPercentRecent = rightPercent * .3f + rightPercentRecent * .7f;
			}
			else if(rightPercent == 0)
			{
				rightPercentRecent = 0;
			}
			else if(rightPercent >= 1)
			{
				rightPercentRecent = 1;
			}

//			
//			g.drawString(""+totalValue, 50, 50);
//			g.drawString(""+leftPercent, 50, 75);
//			g.drawString(""+rightPercent, 150, 75);
//			g.drawString(""+leftPercentRecent, 50, 100);
//			g.drawString(""+rightPercentRecent, 150, 100);

			
			// Left
			g.setLineWidth(1);
			g.setColor(Game.getPlayerOne().getColorPrimary().darker());
			g.fillRect(x,  y, w * leftPercentRecent, h);
			g.setColor(Game.getPlayerOne().getColorSecondary().darker());
			g.drawRect(x,  y, w * leftPercentRecent, h);

			// Right
			g.setColor(Game.getPlayerTwo().getColorPrimary().darker());
			g.fillRect(x + w * leftPercentRecent,  y, w * rightPercentRecent, h);
			g.setColor(Game.getPlayerTwo().getColorSecondary().darker());
			g.drawRect(x + w * leftPercentRecent,  y, w * rightPercentRecent, h);
					
			g.resetLineWidth();

		}
	}
	
	
	
	enum BarMode
	{
		VALUE;
	}
}
