package objects;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SpriteSheet;
import org.newdawn.slick.geom.Point;

import engine.Utility;
import engine.states.Game;
import objects.entity.unit.BaseShip;
import ui.display.message.MessageList;
import ui.display.message.ObjectMessage;

public abstract class GameObject 
{

	protected SpriteSheet sheet;
	protected Image image;	
	protected GameContainer gc;
	private int gameNumber;
	
	protected int scale = 1;

	protected int timer = 0;
	protected float x = 0;
	protected float y = 0;
	protected int w = 0;
	protected int h = 0;
	protected float theta = 0;
	protected float thetaOld = 0;
	protected float xSpeed = 0;
	protected float ySpeed = 0;
	protected int spinDirection = 1;

	private boolean atMaxSpeed = false;
	private boolean isMoving = false;
	private float maxSpeed = 0;
	private float maxSpeedBase = 0;
	private float acceleration = 0;
	private float accelerationBase = 0;

	private boolean hasNotMoved = true;
	private MessageList messages;

	public GameObject(float x, float y) 
	{
		image = null;
		this.x = x;
		this.y = y;
		if (Math.random() < .5)
		{
			spinDirection = -1;
		}
		messages = new MessageList();
		gameNumber = Game.getGameNumber();

	}

	/**************** Accessors ******************/

	public boolean canMove() 					{	return hasNotMoved	|| this instanceof BaseShip;	}
	public final Image getImage()				{	return image;										}
	public final float getScale()				{	return scale;										}

	public final int getGameNumber()			{	return gameNumber;									}
	public final float getX()					{	return x;											}
	public final float getCenterX() 			{	return x + w / 2;									}
	public final float getY() 					{	return y;											}
	public final float getCenterY() 			{	return y + h / 2;									}
	public final Point getPosition() 			{	return new Point(getCenterX(), getCenterY());		}
	
	public final float getWidth() 				{	return w;											}	
	public final float getHeight() 				{	return h;											}
	public final float getSize()				{	return (w + h) / 2; 								}

	public final float getTheta() 				{	return theta;										}		
	public final float getSpeedX()				{	return xSpeed;										}
	public final float getSpeedY()				{	return ySpeed;										}
	public String getName()						{	return getClass().getSimpleName();					}

	public final boolean atMaxSpeed()			{	return atMaxSpeed;									}
	public final float getAcceleration()		{	return acceleration;								}
	public final float getAccelerationBase()	{	return accelerationBase;							}

	public final float getMaxSpeed() 			{	return maxSpeed;									}
	public final float getMaxSpeedBase() 		{	return maxSpeedBase;								}
	public final boolean isMoving()				{	return isMoving;									}

	/**************** Mutators ******************/

	public final void setImage(Image i)			{		image = i;			}

	//	public void setTheta(int degrees)		{		theta = degrees;	}


	public final void increaseSpeed(float amount)
	{
		maxSpeed += amount;
		maxSpeedBase += amount;
	}
	
	public final void increaseSpeedByPercent(float amount)
	{
		maxSpeed += maxSpeed * amount;
		maxSpeedBase += maxSpeedBase * amount;
	}
	
	public final void decreaseSpeedByPercent(float amount)
	{
		maxSpeed -= maxSpeed * amount;
		maxSpeedBase -= maxSpeedBase * amount;
	}
	
	public final void setSpeed(float amount)
	{
		maxSpeed = amount;
		maxSpeedBase = amount;
	}

	public final void setSpeedCurrent(float amount)
	{
		maxSpeed = amount;
	}

	public final void setAcceleration(float amount)
	{
		acceleration = amount;
		accelerationBase = amount;
	}
	
	public final void increaseAcceleration(float amount)
	{
		acceleration += amount;
		accelerationBase += amount;
	}
	
	public final void decreaseAcceleration(float amount)
	{
		acceleration -= amount;
		accelerationBase -= amount;
	}
	
	public final void decreaseAccelerationByPercent(float amount)
	{
		acceleration -= acceleration * amount;
		accelerationBase -= acceleration * amount;
	}

	public final void setAccelerationCurrent(float amount)
	{
		acceleration = amount;
	}

	

	
	public final void moveComplete() 				
	{	
		hasNotMoved = false;			
	}

	public final float getDistance(float x, float y) 
	{
		return Utility.distance(getCenterX(), getCenterY(), x, y);
	}

	public final float getDistance(GameObject o) 
	{
		return Utility.distance(this, o);
	}

	public final float getDistance(Point p) 
	{
		return Utility.distance(getPosition(), p);
	}

	public void render(Graphics g)
	{
		messages.render(g);
	}

	public void update() 
	{
		//limitSpeed();
		timer++;
		if (theta > 360)
		{
			theta -= 360;
		}
		if (theta < 0)
		{
			theta += 360;
		}

		x += xSpeed;
		y += ySpeed;

		updateWidthAndHeightToScale();

		isMoving = false;
		atMaxSpeed = false;
		maxSpeed = maxSpeedBase;
		acceleration = accelerationBase;
		hasNotMoved = true;
		messages.update();


	}

	protected final void updateWidthAndHeightToScale()
	{
		if (image != null) 
		{
			w = (int) (image.getWidth() * getScale());
			h = (int) (image.getHeight() * getScale());
		}
	}
	
	public final boolean isInBounds(Point p) {
		return p.getX() > Game.getMapLeftEdge() && p.getX() < Game.getMapRightEdge()
				&& p.getY() > Game.getMapTopEdge() &&  p.getY() <  Game.getMapBottomEdge();
	}

	public final boolean isInBounds() {
		return x > Game.getMapLeftEdge() && x + w <  Game.getMapRightEdge()
				&& y > Game.getMapTopEdge() && y + h <  Game.getMapBottomEdge();
	}

	// Movement

	public final void move() 
	{
		if(canMove())
		{
			changeSpeed(acceleration, getTheta());
			hasNotMoved = false;
		}
	}

	public final float getCurSpeed()
	{
		return (float) Math.sqrt(Math.pow(xSpeed, 2) +Math.pow(ySpeed, 2));
	}

	public final float getPercentSpeed()
	{
		return getCurSpeed() / getMaxSpeed();
	}
//	this doesn't work - causing stuttering
//	public void limitSpeed()
//	{
//		float xSpeedMax = (float) (maxSpeed * Math.cos(Math.toRadians(theta)));
//		float ySpeedMax = (float) (maxSpeed * Math.sin(Math.toRadians(theta)));
//		
//		if(Math.abs(xSpeed) > Math.abs(xSpeedMax))
//		{	
//			if(xSpeed > 0)
//			{
//				xSpeed = xSpeedMax;
//			}
//			else
//			{
//				xSpeed = -xSpeedMax;
//			}
//		}
//		
//		if(Math.abs(ySpeed) > Math.abs(ySpeedMax))
//		{	
//			if(ySpeed > 0)
//			{
//				ySpeed = ySpeedMax;
//			}
//			else
//			{
//				ySpeed = -ySpeedMax;
//			}
//		}
//	}

	// Josh Lin's Bug Fix / New Version
	public final void changeSpeed(float amount, float theta) 
	{ 
		// Find the change in my speed from moving forward for one frame in the direction I'm facing
		float xDelta = (float) (amount * Math.cos(Math.toRadians(theta))); // Calculate change in x speed
		float yDelta = (float) (amount * Math.sin(Math.toRadians(theta))); // Calculate change in y speed

		xSpeed += xDelta; // Change xSpeed 
		ySpeed += yDelta; // Change ySpeed
			
		// If the magnitude of the velocity vector is > maxSpeed, lower it.
		final float VelocityMagnitude = (float) Math.sqrt(xSpeed * xSpeed + ySpeed * ySpeed);
		
		if(VelocityMagnitude > maxSpeed) 
		{
			atMaxSpeed = true; // Begin max speed animation
				
			// Normalize the velocity vector, then multiply by maxSpeed so magnitude remains maxSpeed
			xSpeed = xSpeed / VelocityMagnitude * maxSpeed; 
			ySpeed = ySpeed / VelocityMagnitude * maxSpeed;
		}	
	}	
	
//	public final void changeSpeed(float amount, float theta) 
//	{
//		// Find the change in my speed from moving forward for one frame in the
//		// direction I'm facing
//		
//		float xDelta = (float) (amount * Math.cos(Math.toRadians(theta)));
//		float yDelta = (float) (amount * Math.sin(Math.toRadians(theta)));
//		float xSpeedMax = (float) (maxSpeed * Math.cos(Math.toRadians(theta)));
//		float ySpeedMax = (float) (maxSpeed * Math.sin(Math.toRadians(theta)));
//
//		isMoving = true;
//
//		// If I am moving left
//		if (xDelta < 0)
//		{
//			// If I won't go beyond max speed, speed up
//			if(xSpeed + xDelta >= -Math.abs(xSpeedMax))
//			{
//				xSpeed += xDelta;
//			}
//			// Else, set max speed animation
//			else
//			{
//				xSpeed = -Math.abs(xSpeedMax);
//				atMaxSpeed = true;
//			}
//		}
//
//		// If I am moving right
//		else if (xDelta > 0)
//		{
//			// If I won't go beyond max speed, speed up
//			if(xSpeed + xDelta <= Math.abs(xSpeedMax))
//			{
//				xSpeed += xDelta;
//			}
//			// Else, I am going max speed in one direction
//			else
//			{
//				xSpeed = Math.abs(xSpeedMax);
//				atMaxSpeed = true;
//			}
//		}
//
//
//		// If I am moving up
//		if (yDelta < 0)
//		{
//			// If I won't go beyond max speed, speed up
//			if(ySpeed + yDelta >= -Math.abs(ySpeedMax))
//			{
//				ySpeed += yDelta;
//			}
//			// Else, I am going max speed in one direction
//			else
//			{
//				ySpeed = -Math.abs(ySpeedMax);
//				atMaxSpeed = true;
//			}
//
//		}
//		else if (yDelta > 0 )
//		{
//			// If I won't go beyond max speed, speed up
//			if(ySpeed + yDelta <= Math.abs(ySpeedMax))
//			{
//				ySpeed += yDelta;
//			}
//			// Else, I am going max speed in one direction
//			else
//			{
//				ySpeed = Math.abs(ySpeedMax);
//				atMaxSpeed = true;
//			}	
//		}
//
//
//	}

	// Allows turning even if move is locked.
	public final void rotate(float degrees) 
	{
		while (degrees > 360) 
		{
			degrees -= 360;
		}
		while (degrees < 0)
		{
			degrees += 360;
		}

		thetaOld = theta;
		theta = degrees;
	}


	// Turning

	public final float getAngleToward(float targetX, float targetY) 
	{
		float yDiff = targetY - getCenterY();
		float xDiff = targetX - getCenterX();

		float angle = (float) Math.toDegrees(Math.atan2(yDiff, xDiff));

		if (angle < 0) 
		{
			angle = 360 + angle;
		}

		return angle;
	}

	public final float getAngleAway(float targetX, float targetY) 
	{
		float yDiff = targetY - getCenterY();
		float xDiff = targetX - getCenterX();

		float angle = (float) Math.toDegrees(Math.atan2(yDiff, xDiff));
		angle -= 180;
		
		if (angle < 0) 
		{
			angle = 360 + angle;
		}

		return angle;
	}

	
	/********************* TURNING *********************/
	
	public final void turnTo(float degrees) 
	{
		if (canMove())
		{
			while (degrees > 360)
			{
				degrees -= 360;
			}
			while (degrees < 0)
			{
				degrees += 360;
			}

			theta = degrees;
			thetaOld = theta;
		}
	}
	
	public final void turnTo(float x, float y) 
	{
		turnTo((int) getAngleToward(x, y));
	}

	public final void turnTo(Point p)
	{
		turnTo(p.getX(), p.getY());
	}

	public final void turnTo(GameObject o) 
	{
		if (o != null)
		{
			turnTo(o.getCenterX(), o.getCenterY());
		}
	}

	public final void turn(float degrees)
	{
		turnTo(getTheta() + degrees);
	}
	
	public final void turnAround() 
	{
		turn(180);
	}


	
	/********************* MOVING *********************/

	public final void move(float degrees)
	{
		if (canMove())
		{
			turnTo(degrees);
			move();
		}
	}

	public final void moveTo(Point p) 
	{
		moveTo(p.getX(), p.getY());
	}

	public final void moveTo(float x, float y) 
	{
		if (canMove())
		{
			turnTo(x, y);
			move();
		}
	}

	public final void moveTo(GameObject o) 
	{
		if (canMove() && o != null)
		{
			turnTo(o);
			move();
		}
	}

	public void dbgMessage(int message)
	{
		messages.addMessage(new ObjectMessage(this, ""+message));
	}

	public void dbgMessage(float message)
	{
		messages.addMessage(new ObjectMessage(this, ""+message));
	}

	public void dbgMessage(String message)
	{

		messages.addMessage(new ObjectMessage(this, message));
	}

	public void dbgMessage(String message, int duration)
	{
		messages.addMessage(new ObjectMessage(this, message, duration));
	}

	public void dbgMessage(String message, int duration, Color color)
	{
		messages.addMessage(new ObjectMessage(this, message, duration, color));
	}

	public void dbgMessage(ObjectMessage message)
	{
		messages.addMessage(message);
	}

	public void clearMessages()
	{
		messages.clear();
	}

}
