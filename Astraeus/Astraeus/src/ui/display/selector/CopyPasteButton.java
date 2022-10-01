package ui.display.selector;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;

import engine.Main;

public class CopyPasteButton<T> extends Button<T> {

	public CopyPasteButton() {
		super(Main.getScreenWidth()-512-50, 50, 512, 40, null);
		this.addText(0, "Press C over a team to copy");
	}

	public void keyEvent() {
		DropDown<T> dropDown = getDropDownMouseOver();
		
		if(isKeyDown(Input.KEY_C)) {
			if(dropDown == null) {
				setReturnOBJ(null);
				removeColor(3);
				removeColor(4);
				removeText(1);
			}
			else {
				setReturnOBJ(dropDown.getReturnOBJ());
				addColor(3, dropDown.getColor(0));
				addColor(4, dropDown.getColor(2));
				if(dropDown.getReturnOBJ() != null)
					addText(1, dropDown.getText(1));
				else
					removeText(1);
			}
		} 
		else if(getReturnOBJ() != null && dropDown != null && isKeyDown(Input.KEY_V)) {
			dropDown.setReturnOBJ(getReturnOBJ());
			dropDown.addColor(0, getColor(3));
			dropDown.addColor(2, getColor(4));
			dropDown.addText(1, getText(1));
		}
	}
	
	@SuppressWarnings("unchecked")
	public DropDown<T> getDropDownMouseOver() {
		for(UIElement e : UIManager.elements) {
			if(e instanceof DropDown && ((DropDown<T>) e).isMouseOver()) {
				return (DropDown<T>) e;
			}
		}
		return null;
	}
	
	public void render(GameContainer gc, Graphics g) {

	}
}
