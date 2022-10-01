package ui.display.selector;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;

import engine.Main;
import ui.display.Fonts;

public class WriteButton extends Button<String> {
	boolean selected = false;
	int timer = 0;
	final int BLINK_DURATION = 75;
	String currentInput = "";
	String defaultInput = "";
	WritingType type = WritingType.ANY;
	
	public WriteButton(float x, float y, float w, float h, WritingType type) {
		super(x, y, w, h, null);
		this.addText(0, "Write Button");
		this.type = type;
	}
	
	public WriteButton(float x, float y, float w, float h) {
		this(x, y, w, h, WritingType.ANY);
	}
		
	public WritingType getType() {
		return type;
	}
	
	public void setDefault(String text, String defaultInput) {
		this.defaultInput = defaultInput;
		setReturnOBJ(defaultInput);
		addText(0, text);
		addText(1, text + defaultInput);
	}
	
	public void keyEvent() {
		if(selected) {
			if(isKeyDown(Input.KEY_BACK) && currentInput.length() > 0)
				currentInput = currentInput.substring(0, currentInput.length()-1);
			
			timer = 0;
			addText(1, getText(0) + currentInput);
			if(isKeyDown(Input.KEY_ENTER)) {
				selected = false;
				if(!currentInput.equals("")) {
					setReturnOBJ(currentInput);
					currentInput = "";
				}
				else
					addText(1, getText(0) + getReturnOBJ());
				timer = 0;
				UIManager.unlockInput();
			}
		}
	}
	
	public void mouseEvent() {
		if(!selected) {
			if(isLeftClick()) {
				selected = true;
				addText(1, getText(0));
				UIManager.lockInput(this);
			}
			else if(isRightClick()) {
				setReturnOBJ(defaultInput);
				addText(1, getText(0) + defaultInput);
				currentInput = "";
			}
		}
		else {
			if(isMouseButtonDown(0) && !isMouseButtonDown(1)) {
				selected = false;
				if(!currentInput.equals("")) {
					setReturnOBJ(currentInput);
					currentInput = "";
				}
				else
					addText(1, getText(0) + getReturnOBJ());
				timer = 0;
				UIManager.unlockInput();
			} else if(!isMouseButtonDown(0) && isMouseButtonDown(1)) {
				selected = false;
				addText(1, getText(0) + getReturnOBJ());
				currentInput = "";
				timer = 0;
				UIManager.unlockInput();
			}
		}
	}
		
	@Override
	public void render(GameContainer gc, Graphics g) 
	{
		String s = this.getText(0);
		
		if(getText(1) != null)
			this.addText(0, this.getText(1));
				
		String userHelper = "";
		if(selected) {
			if(++timer % (BLINK_DURATION*2) < BLINK_DURATION)
				userHelper = "|";
		}
		
		if(h > Main.getScreenHeight() * .05f)
		{
			borderSize = 2;
			g.setFont(Fonts.ocr32);
		}
		else
		{
			borderSize = 1;
			g.setFont(Fonts.ocr14);
		}
		if(selected) {
			// Box Border
			g.setColor(getColor(1).darker(.2f));
			g.fillRect(x, y, w, h);
			
			// Box Shadow
			g.setColor(Color.black);
			g.fillRect(x + borderSize+4, y + borderSize+4, w - borderSize * 2, h - borderSize * 2);
			
			// Box
			g.setColor(getColor(0).darker(.2f));
			g.fillRect(x + borderSize, y + borderSize, w - borderSize * 2, h - borderSize * 2);
			
			// Shadow
			g.setColor(getColor(3));
			g.drawString(getText(0)+userHelper, x + w / 2 - g.getFont().getWidth(getText(0))/2 + 1, y + h / 2-g.getFont().getHeight(getText(0))/2 + 1);
			
			// Text
			g.setColor(getColor(2).darker(.2f));
			g.drawString(getText(0)+userHelper, x + w / 2 - g.getFont().getWidth(getText(0))/2, y + h / 2-g.getFont().getHeight(getText(0))/2);
		}
		else
			super.render(gc, g);
		
		this.addText(0, s);
	}
	
	public void keyPressed(int key, char c) {
		if(validKey(c) || Character.getType(c) == Character.CONTROL)
			super.keyPressed(key, c);
	}
	
	public void keyReleased(int key, char c) {
		if(selected && validKey(c))
			currentInput += c;
		super.keyReleased(key, c);
	}
	
	private boolean validKey(char c) {
		return (type == WritingType.ANY && Character.getType(c) != Character.CONTROL) || (type == WritingType.INTEGER && Character.isDigit(c)) || (type == WritingType.LETTER && Character.isLetter(c));
	}
	
	public enum WritingType {
		ANY, INTEGER, LETTER
	}
}
