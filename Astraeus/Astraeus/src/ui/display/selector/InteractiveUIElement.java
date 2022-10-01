package ui.display.selector;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.newdawn.slick.Input;
import org.newdawn.slick.KeyListener;
import org.newdawn.slick.MouseListener;

public abstract class InteractiveUIElement extends UIElement implements MouseListener, KeyListener{
	private Map<Integer, Boolean> isMouseButtonDown;
	private int mouseX, mouseY, oldMouseX, oldMouseY;
	private int scrollAmount;
	private List<Integer> keysDown;
	private boolean input = true, inputLock = false;
	
	
	InteractiveUIElement(float x, float y, float w, float h) {
		super(x, y, w, h);
		
		isMouseButtonDown = new HashMap<Integer, Boolean>();
		keysDown = new ArrayList<Integer>();
	}
	
	public void updateInput() {
		scrollAmount = 0;
	}

	public boolean isMouseOver() {
		return mouseX > this.x && mouseX < this.x + this.w && mouseY > this.y && mouseY < this.y + this.h;
	}
	
	public final boolean isPressed() {
		return isMouseOver() && (isMouseButtonDown(0) || isMouseButtonDown(1));
	}
	
	public final boolean isLeftClick() {
		return isMouseOver() && isMouseButtonDown(0) && !isMouseButtonDown(1);
	}
	
	public final boolean isRightClick() {
		return isMouseOver() && !isMouseButtonDown(0) && isMouseButtonDown(1);
	}
	
	protected int getOldMouseX() {
		return oldMouseX;
	}
	
	protected int getOldMouseY() {
		return oldMouseY;
	}
	
	protected int getMouseX() {
		return mouseX;
	}
	
	protected int getMouseY() {
		return mouseY;
	}
	
	protected int getScrollAmount() {
		return scrollAmount;
	}
	
	protected boolean isMouseButtonDown(int button) {
		Boolean b = isMouseButtonDown.get(button);
		if(b != null)
			return b;
		return false;
	}
	
	protected boolean isKeyDown(int key) {
		return keysDown.contains(key);
	}
	

	public final void mouseClicked(int arg0, int arg1, int arg2, int arg3) {}

	public final void mouseDragged(int oldX, int oldY, int newX, int newY) {
		this.oldMouseX = oldX;
		this.oldMouseY = oldY;
		this.mouseX = newX;
		this.mouseY = newY;
	}

	public void mouseMoved(int oldX, int oldY, int newX, int newY) {
		this.oldMouseX = oldX;
		this.oldMouseY = oldY;
		this.mouseX = newX;
		this.mouseY = newY;
	}

	public void mousePressed(int button, int x, int y) {
		isMouseButtonDown.put(button, true);
	}

	public void mouseReleased(int button, int x, int y) {
		if(input && !inputLock)
			mouseEvent();
		isMouseButtonDown.put(button, false);
	}

	public void mouseWheelMoved(int arg0) {
		scrollAmount += arg0;
		if(input && !inputLock)
			scrollEvent();
	}
	
	public final void setInput(boolean b) {
		if(!input && b)
			inputLock = true;
		this.input = b;
	}

	public final void inputEnded() {inputLock = false;}

	public final void inputStarted() {}

	public final boolean isAcceptingInput() {return true;}
	
	public final boolean isUsingInput() {
		return input && !inputLock;
	}
	
	public final void setInput(Input arg0) {}

	public void keyPressed(int key, char c) {
		keysDown.add(key);
	}

	public void keyReleased(int key, char c) {
		if(input && !inputLock)
			keyEvent();
		keysDown.remove(new Integer(key));
	}
	
	public void mouseEvent() {}
	public void keyEvent() {}
	public void scrollEvent() {}
}
