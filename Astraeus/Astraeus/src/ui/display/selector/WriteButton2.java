package ui.display.selector;

import org.newdawn.slick.Input;

// a version of WriteButton that does not start new inputs from scratch, but instead continues off of what it currently holds.
public class WriteButton2 extends WriteButton {
	
	public WriteButton2(float x, float y, float w, float h, WritingType type) {
		super(x, y, w, h, null);
		this.addText(0, "Write Button");
		this.type = type;
	}
	
	public WriteButton2(float x, float y, float w, float h) {
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
		currentInput = defaultInput;
	}
	
	public void keyEvent() {
		if(selected) {
			if(isKeyDown(Input.KEY_BACK) && currentInput.length() > 0)
				currentInput = currentInput.substring(0, currentInput.length()-1);
			
			timer = 0;
			addText(1, getText(0) + currentInput);
			if(isKeyDown(Input.KEY_ENTER)) {
				selected = false;
				if(!currentInput.equals(""))
					setReturnOBJ(currentInput);
				else {
					currentInput = getReturnOBJ();
					addText(1, getText(0) + currentInput);
				}
				timer = 0;
				UIManager.unlockInput();
			}
		}
	}
	
	public void mouseEvent() {
		if(!selected) {
			if(isLeftClick()) {
				selected = true;
				currentInput = getReturnOBJ();
				UIManager.lockInput(this);
			}
			else if(isRightClick()) {
				setReturnOBJ(defaultInput);
				addText(1, getText(0) + defaultInput);
				currentInput = defaultInput;
			}
		}
		else {
			if(isMouseButtonDown(0) && !isMouseButtonDown(1)) {
				selected = false;
				if(!currentInput.equals(""))
					setReturnOBJ(currentInput);
				else {
					currentInput = getReturnOBJ();
					addText(1, getText(0) + currentInput);
				}
				timer = 0;
				UIManager.unlockInput();
			} else if(!isMouseButtonDown(0) && isMouseButtonDown(1)) {
				selected = false;
				currentInput = getReturnOBJ();
				addText(1, getText(0) + currentInput);
				timer = 0;
				UIManager.unlockInput();
			}
		}
	}
}
