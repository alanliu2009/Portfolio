package ui.display.selector;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;

import engine.Main;
import ui.display.Fonts;

public class Button<T> extends InteractiveUIElement{
	private T returnOBJ;
	protected float borderSize = 2;
	
	public Button(float x, float y, float w, float h, T returnOBJ) {
		super(x, y, w, h);
		this.returnOBJ = returnOBJ;

		//System.out.println(this);
		//myFont = Fonts.ocr14;
		//this.addColor(0, new Color(168, 84, 104));

		this.addColor(0, new Color(40, 25, 80)); //dark purple
		this.addColor(1, new Color(32, 32, 32));
		this.addColor(2, new Color(255, 255, 255));
		
		this.addText(0, "Button");
	}
		
	public T getReturnOBJ() {
		return this.returnOBJ;
	}
	
	public void setReturnOBJ(T returnOBJ) {
		this.returnOBJ = returnOBJ;
	}

	@Override
	public void render(GameContainer gc, Graphics g) 
	{

		if(h > Main.getScreenHeight() * .05f)
		{
			g.setFont(Fonts.ocr32);
			borderSize = 2;
		}
		else
		{
			g.setFont(Fonts.ocr14);
			borderSize = 1;
		}
		
		if(isPressed()) {
			
			// Box Border
			g.setColor(getColor(1).darker(.5f));
			g.fillRect(x, y, w, h);
			
			// Box Shadow
			g.setColor(Color.black);
			g.fillRect(x + borderSize+4, y + borderSize+4, w - borderSize * 2, h - borderSize * 2);
			
			// Box
			g.setColor(getColor(0).darker(.5f));
			g.fillRect(x + borderSize, y + borderSize, w - borderSize * 2, h - borderSize * 2);
			
			// Shadow
			g.setColor(getColor(3));
			g.drawString(getText(0), x + w / 2 - g.getFont().getWidth(getText(0))/2 + 1, y + h / 2-g.getFont().getHeight(getText(0))/2 + 1);
			
			// Text
			g.setColor(getColor(2).darker(.5f));
			g.drawString(getText(0), x + w / 2 - g.getFont().getWidth(getText(0))/2, y + h / 2-g.getFont().getHeight(getText(0))/2);

		}
		else if(this.isMouseOver()) 
		{
			// Box Border
			g.setColor(getColor(1).brighter(.5f));
			g.fillRect(x, y, w, h);
			
			// Box Shadow
			g.setColor(Color.black);
			g.fillRect(x + borderSize+4, y + borderSize+4, w - borderSize * 2, h - borderSize * 2);
			
			// Box 
			g.setColor(getColor(0).brighter(.5f));
			g.fillRect(x + borderSize, y + borderSize, w - borderSize * 2, h - borderSize * 2);
			
			// Shadow
			g.setColor(getColor(3));
			g.drawString(getText(0), x + w / 2 - g.getFont().getWidth(getText(0))/2 + 1, y + h / 2-g.getFont().getHeight(getText(0))/2 + 1);
			
			// Main Text
			g.setColor(getColor(2).brighter(.5f));
			g.drawString(getText(0), x + w / 2 - g.getFont().getWidth(getText(0))/2, y + h / 2-g.getFont().getHeight(getText(0))/2);
		}
		else 
		{
			// Box Border
			g.setColor(getColor(1));
			g.fillRect(x, y, w, h);
			
			// Box Shadow
			g.setColor(Color.black);
			g.fillRect(x + borderSize+4, y + borderSize+4, w - borderSize * 2, h - borderSize * 2);
			
			// Box
			g.setColor(getColor(0));
			g.fillRect(x + borderSize, y + borderSize, w - borderSize * 2, h - borderSize * 2);
			
			// Shadow
			g.setColor(getColor(3));
			g.drawString(getText(0), x + w / 2 - g.getFont().getWidth(getText(0))/2 + 1, y + h / 2-g.getFont().getHeight(getText(0))/2 + 1);
		//	g.drawString(getText(0), x + w / 2 - g.getFont().getWidth(getText(0))/2 + 2, y + h / 2-g.getFont().getHeight(getText(0))/2 + 1);

			// Text
			g.setColor(getColor(2));
			g.drawString(getText(0), x + w / 2 - g.getFont().getWidth(getText(0))/2, y + h / 2-g.getFont().getHeight(getText(0))/2);
		}
	}
}
