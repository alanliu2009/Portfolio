package ui.display.selector;

import java.util.ArrayList;
import java.util.List;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;

public class DropDown<T> extends Button<T> {

	boolean expanded;
	List<DropDownElement<T>> elements;
	int elementsShow = 16;
	int scroll = 0;
	DropDownElement<T> defaultElement;
	
	public DropDown(float x, float y, float w, float h, String defaultText) {
		super(x, y, w, h, null);
		
		expanded = false;
		
		this.addText(0, "Drop Down");
		
		elements = new ArrayList<DropDownElement<T>>();
		
		defaultElement = new DropDownElement<T>(this, null, defaultText, 1);
		elements.add(defaultElement);
		
	}
	
	public DropDown(float x, float y, float w, float h) {
		this(x, y, w, h, "None Selected");
	}
	
	public void addElement(T returnOBJ, String text) {
		elements.add(new DropDownElement<T>(this, returnOBJ, text, elements.size() + 1));
	}
	
	public void clearElements() {
		for(UIElement e : elements) {
			if(e != defaultElement)
				UIManager.removeUIElement(e);
		}
		elements.clear();
		elements.add(defaultElement);
	}
	
	public void mouseEvent() {
		if(isLeftClick())
			expanded = !expanded;
		else if(isMouseInBounds()) expanded = false;
		
		if(expanded) {
			for(DropDownElement<T> e : elements)
				UIManager.makeTopLevel(e);
			
			ArrayList<InteractiveUIElement> a = new ArrayList<InteractiveUIElement>();
			a.addAll(this.elements);
			a.add(this);
			UIManager.lockInput(a);
		}
		else {
			UIManager.unlockInput();
		}
		
		if(isRightClick()) {
			removeText(1);
			setReturnOBJ(defaultElement.getReturnOBJ());
			expanded = false;
			UIManager.unlockInput();
		}
	}
	
	public boolean isMouseInBounds() {
		return !(this.getMouseX() > x && this.getMouseX() < x + w && this.getMouseY() > y && this.getMouseY() < y + h + (Math.min(elementsShow, elements.size())) * h/2);
	}
	
	public void scrollEvent() {
		if(expanded) {
			int s = this.getScrollAmount() / 120;
			
			if(elements.size() > elementsShow) {
				if(scroll - s < 0)
					scroll = 0;
				else if(scroll - s > elements.size() - elementsShow)
					scroll = elements.size() - elementsShow;
				else
					scroll -= s;
			}
			
			for(DropDownElement<T> e : elements)
				e.update();
		}
	}

	public void render(GameContainer gc, Graphics g) {
		if(this.getText(1) == null)
			super.render(gc, g);
		else {
			String s = this.getText(0);
			this.addText(0, this.getText(1));
			super.render(gc, g);
			this.addText(0, s);
		}
	}
}
