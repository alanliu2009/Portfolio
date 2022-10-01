package ui.display.selector;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;

import ui.display.Fonts;

public class DropDownElement<T> extends Button<T> {
	DropDown<T> parent;
	int index;
	
	public DropDownElement(DropDown<T> parent, T returnOBJ, String text, int index) {
		super(parent.x, parent.y, 0, 0, returnOBJ);
		this.parent = parent;
		this.index = index;

		this.addText(0, text);
	}
	
	public void mouseEvent() {
		if(parent.expanded && isLeftClick()) {
			if(this.index == 1) {
				parent.removeText(1);
			}
			else
				parent.addText(1, this.getText(0));
			parent.setReturnOBJ(this.getReturnOBJ());
			parent.expanded = false;
			UIManager.unlockInput();
		}
	}
	
	public void update() {
		if(index > parent.scroll && index <= parent.scroll + parent.elementsShow) {
			y = parent.y + parent.h + parent.h/2 * (index - 1 - parent.scroll);
			w = parent.w;
			h = parent.h/2;
		}
		else {
			y = parent.y + parent.h;
			w = 0;
			h = 0;
		}
	}
	
	public void render(GameContainer gc, Graphics g)
	{
		
		g.setFont(Fonts.ocr14);
		if(parent.expanded) {
			update();
			if(index > parent.scroll && index <= parent.scroll + parent.elementsShow)
				super.render(gc, g);
		}
	}
}
