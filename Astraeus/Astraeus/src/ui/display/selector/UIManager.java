package ui.display.selector;

import java.util.ArrayList;
import java.util.List;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;

public class UIManager {
	
	public static GameContainer gc;
	public static List<UIElement> elements = new ArrayList<UIElement>();
	
	public static void addUIElement(UIElement e) {
		if(e instanceof InteractiveUIElement) {
			gc.getInput().addKeyListener((InteractiveUIElement)e);
			gc.getInput().addMouseListener((InteractiveUIElement)e);
		}
		elements.add(e);
	}
	
	public static void removeUIElement(UIElement e) {
		if(e instanceof InteractiveUIElement) {
			gc.getInput().removeKeyListener((InteractiveUIElement)e);
			gc.getInput().removeMouseListener((InteractiveUIElement)e);
		}
		elements.remove(e);
	}
	
	public static void reset() {
		for(UIElement e : elements) {
			if(e instanceof InteractiveUIElement)
				((InteractiveUIElement)e).updateInput();
		}
	}
	
	public static void render(GameContainer gc, Graphics g) {
		for(UIElement e : elements)
			e.render(gc, g);
	}
	
	public static void makeTopLevel(UIElement e) {
		elements.remove(e);
		elements.add(e);
	}
	
	public static void lockInput(List<InteractiveUIElement> e) {
		for(UIElement a: elements) {
			if(a instanceof InteractiveUIElement && !e.contains(a)) {
				((InteractiveUIElement)a).setInput(false);
			}
		}
	}
	
	public static void lockInput(InteractiveUIElement e) {
		for(UIElement a: elements) {
			if(a instanceof InteractiveUIElement && e != a) {
				((InteractiveUIElement)a).setInput(false);
			}
		}
	}
	
	public static void unlockInput() {
		for(UIElement a: elements) {
			if(a instanceof InteractiveUIElement) {
				((InteractiveUIElement)a).setInput(true);
			}
		}
	}
}