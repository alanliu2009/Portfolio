package ui.display.selector;

import java.util.HashMap;
import java.util.Map;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;

public abstract class UIElement {
	protected float x, y;
	protected float w, h;
	private Map<Object, Color> colors;
	private Map<Object, String> texts;
	
	UIElement(float x, float y, float w, float h) {
		this.x = x;
		this.y = y;
		this.w = w;
		this.h = h;
		
		colors = new HashMap<Object, Color>();
		texts = new HashMap<Object, String>();
		
		UIManager.addUIElement(this);
	}
	
	public float getX() {
		return x;
	}
	
	public float getY() {
		return y;
	}
	
	public void setX(float x) {
		this.x = x;
	}
	
	public void setY(float y) {
		this.y = y;
	}
	
	public float getWidth() {
		return w;
	}
	
	public float getHeight() {
		return h;
	}
	
	public void setWidth(float w) {
		this.w = w;
	}
	
	public void setHeight(float h) {
		this.h = h;
	}
	
	public void addText(Object key, String text) {
		texts.put(key, text);
	}
	
	public String getText(Object key) {
		return texts.get(key);
	}
	
	public String removeText(Object key) {
		return texts.remove(key);
	}
	
	public void addColor(Object key, Color color) {
		colors.put(key, color);
	}
	
	public Color getColor(Object key) {
		return colors.get(key);
	}
	
	public Color removeColor(Object key) {
		return colors.remove(key);
	}
	
	public abstract void render(GameContainer gc, Graphics g);
}
