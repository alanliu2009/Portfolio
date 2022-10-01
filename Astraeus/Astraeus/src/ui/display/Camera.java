package ui.display;


import java.nio.IntBuffer;

import org.lwjgl.BufferUtils;
import org.lwjgl.LWJGLException;
import org.lwjgl.input.Cursor;
import org.lwjgl.input.Mouse;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Input;

import engine.Main;
import engine.Settings;
import engine.Settings.CameraMode;
import engine.Values;
import objects.entity.unit.BaseShip;

public class Camera 
{	
	// Basic Zooming and Scrolling
	public static final float ZOOM_RATE = .02f; //.01


	public static final float ZOOM_DISTANT = .25f;
	public static final float ZOOM_MEDIUM = .5f;
	public static final float ZOOM_CLOSE = 1;
	
	public static final float HEALTHBAR_ZOOM_THRESHOLD = .30f;

	public static final float SCROLL_RATE_BASE_AUTO = 2;
	public static final float SCROLL_RATE_BASE_MOUSE = 2;
	public static final float SCROLL_RATE_KEY_MAX = 75;
	public static final float SCROLL_RATE_KEY_ACCELLERATION = 5f;

	private static float x;
	private static float y;

	private static float currentZoom;
	private static float goalZoom;

	private static int viewSizeX;
	private static int viewSizeY;

	private static float xSpeed = 0;
	private static float ySpeed = 0;

	public static boolean mousePanActivated;


	public static float getX() 				{	return x;			}
	public static float getY() 				{	return y;			}
	public static float getViewWidth() 		{	return viewSizeX;	}
	public static float getViewHeight() 	{	return viewSizeY;	}
	public static float getZoom()			{	return currentZoom;	}
	public static float getScreenCenterX()	{	return (float) (Main.getScreenWidth() / Camera.getZoom() / 2 - Camera.getX()); }
	public static float getScreenCenterY()	{	return (float) (Main.getScreenHeight() / Camera.getZoom() / 2 - Camera.getY()); }
	
	public static boolean isOnScreen(float x, float y)
	{
		return  x > getX() - getViewWidth() && 
				x < getX() + getViewWidth() && 
				y > getY() - getViewHeight() && 
				y < getY() + getViewHeight();
	}
	
	public static boolean isNearScreen(float x, float y)
	{
		return  x > getX() - getViewWidth() * 1.1 && 
				x < getX() + getViewWidth() * 1.1 && 
				y > getY() - getViewHeight() * 1.1 && 
				y < getY() + getViewHeight() * 1.1;
	}
	
	public static void setup()
	{
		double r = Math.random();
		
		if(Settings.cameraStart == CameraMode.LEFT || Settings.cameraStart == CameraMode.RANDOM && r > .5)
		{
			x = Main.getScreenWidth() / 2 - BaseShip.BASE_SHIP_X_POSITION;
			y = Main.getScreenHeight() / 2 - BaseShip.BASE_SHIP_Y_POSITION;
		}
		else
		{
			x = Main.getScreenWidth() / 2 + BaseShip.BASE_SHIP_X_POSITION;
			y = Main.getScreenHeight() / 2 + BaseShip.BASE_SHIP_Y_POSITION;
		}


		currentZoom = 	ZOOM_MEDIUM;// .1f;
		goalZoom = 		ZOOM_MEDIUM;//.1f;
		

	}
	
	public static void update(GameContainer gc) 
	{
		controls(gc);
		
		if(currentZoom < goalZoom)
		{
			currentZoom = (float) Math.min(currentZoom + ZOOM_RATE, goalZoom);
		}
		
		if(currentZoom > goalZoom)
		{
			currentZoom = (float) Math.max(currentZoom - ZOOM_RATE, goalZoom);
		}
		
		viewSizeX = (int) (Main.getScreenWidth() * 1 / currentZoom);
		viewSizeY = (int) (Main.getScreenHeight() * 1 / currentZoom);
	}
	
	public static void controls(GameContainer gc)
	{
		keyboard(gc);
		mouse(gc);
	}
	
	public static float getZoomScrollModifier()
	{
		return Math.min(getZoom() * 2, 1);
	}
	
	public static void keyboard(GameContainer gc)
	{
		if (gc.getInput().isKeyDown(Input.KEY_W))
		{
			ySpeed = Math.min(0, ySpeed - SCROLL_RATE_KEY_ACCELLERATION / getZoomScrollModifier()); 
			if(ySpeed <= -SCROLL_RATE_KEY_MAX / getZoomScrollModifier())
			{
				ySpeed = -SCROLL_RATE_KEY_MAX / getZoomScrollModifier();
			}
			y += ySpeed;
		}

		if (gc.getInput().isKeyDown(Input.KEY_S))
		{
			ySpeed = Math.max(0, ySpeed + SCROLL_RATE_KEY_ACCELLERATION / getZoomScrollModifier()); 
			if(ySpeed >= SCROLL_RATE_KEY_MAX / getZoomScrollModifier())
			{
				ySpeed = SCROLL_RATE_KEY_MAX / getZoomScrollModifier();
			}
			y += ySpeed;
		}

		if (gc.getInput().isKeyDown(Input.KEY_A))
		{
			xSpeed = Math.min(0, xSpeed - SCROLL_RATE_KEY_ACCELLERATION / getZoomScrollModifier()); 
			xSpeed -= SCROLL_RATE_KEY_ACCELLERATION; 
			if(xSpeed <= -SCROLL_RATE_KEY_MAX / getZoomScrollModifier())
			{
				xSpeed = -SCROLL_RATE_KEY_MAX / getZoomScrollModifier();
			}
			x += xSpeed;
		}

		if (gc.getInput().isKeyDown(Input.KEY_D))
		{
			xSpeed = Math.max(0, xSpeed + SCROLL_RATE_KEY_ACCELLERATION / getZoomScrollModifier()); 

			xSpeed += SCROLL_RATE_KEY_ACCELLERATION; 
			if(xSpeed >= SCROLL_RATE_KEY_MAX / getZoomScrollModifier())
			{
				xSpeed = SCROLL_RATE_KEY_MAX / getZoomScrollModifier();
			}
			x += xSpeed;
		}

		if (gc.getInput().isKeyDown(Input.KEY_HOME)) {
			x = 0;
			y = 0;
		}
	}
	
	public static void mouse(GameContainer gc)
	{
		if (gc.getInput().isMouseButtonDown(1))
		{
			mousePanActivated = true;
			
			Cursor emptyCursor;
			int min = org.lwjgl.input.Cursor.getMinCursorSize();
			IntBuffer tmp = BufferUtils.createIntBuffer(min * min);
			try 
			{
				emptyCursor = new org.lwjgl.input.Cursor(min, min, min / 2, min / 2, 1, tmp, null);
					Mouse.setNativeCursor(emptyCursor);
			} 
			catch (LWJGLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
		else
		{
			mousePanActivated = false;
			gc.setDefaultMouseCursor();
		}

		if (gc.getInput().isMouseButtonDown(2)) 
		{
			x = 0;
			y = 0;
			
			
//			try 
//			{
//				System.out.println("mosue");
//				gc.setMouseCursor(Images.booms.getSprite(0, 0),  0, 0);
//
//				
//				//gc.setMouseCursor(Images.cursors.getSprite(0, 0),  0, 0);
//			} catch (SlickException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
		}

		
	}

	


	public static void panCamera(float deltaX, float deltaY) 
	{		
		if(mousePanActivated)
		{

			float DELTA_X = deltaX * 1 / currentZoom;
			float DELTA_Y = deltaY * 1 / currentZoom;

			if(Settings.invertCameraDrag)
			{
				DELTA_X *= -1;
				DELTA_Y *= -1;
			}
			
			// West
			if (DELTA_X < 0 && x - viewSizeX / 2 + DELTA_X > -Values.PLAYFIELD_WIDTH * 3/5) {
				x += DELTA_X * SCROLL_RATE_BASE_MOUSE;
			} 

			// East
			if (DELTA_X > 0 && x + viewSizeX / 2 + DELTA_X < Values.PLAYFIELD_WIDTH * 3/5) {
				x += DELTA_X * SCROLL_RATE_BASE_MOUSE;
			} 

			// North
			if (DELTA_Y < 0 && y - viewSizeY / 2 + DELTA_Y > -Values.PLAYFIELD_HEIGHT * 3/5) {
				y += DELTA_Y * SCROLL_RATE_BASE_MOUSE;
			} 

			// South
			if (DELTA_Y > 0 && y + viewSizeY / 2 + DELTA_Y < Values.PLAYFIELD_HEIGHT * 3/5) {
				y += DELTA_Y * SCROLL_RATE_BASE_MOUSE;
			} 
			
		
		

		}

	}

	public static void centerView(float xPos, float yPos) {
		x = xPos;
		y = yPos;
	}


	public static void zoom(float amount) 
	{
		// Increase by a zoom level
		if(amount > 0)
		{
			if(goalZoom == ZOOM_MEDIUM)
			{
				goalZoom = ZOOM_CLOSE;
			}
			else if(goalZoom == ZOOM_DISTANT)
			{
				goalZoom = ZOOM_MEDIUM;
			}
		}
		
		// Decrease by a zoom level
		else if(amount < 0)
		{
			if(goalZoom == ZOOM_CLOSE)
			{
				goalZoom = ZOOM_MEDIUM;
			}
			else if(goalZoom == ZOOM_MEDIUM)
			{
				goalZoom = ZOOM_DISTANT;
			}
		}

		viewSizeX = (int) (Main.getScreenWidth() * 1 / currentZoom);
		viewSizeY = (int) (Main.getScreenHeight() * 1 / currentZoom);
	}
}