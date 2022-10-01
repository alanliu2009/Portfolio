package objects.entity.node;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;

import engine.Utility;
import engine.states.Game;
import objects.entity.Entity;
import ui.display.healthbar.NodeHealthbar;

public class Node extends Entity
{

	
	public static final float MIN_TURN_SPEED = .005f;
	public static final float MAX_TURN_SPEED = .02f;
	public static final int RESOURCES_AVERAGE = 20;			//100 damage per spawn		
	public static final int DROP_ON_DEATH_AVERAGE = 0;
	public static final int RESOURCE_SPREAD_AVERAGE = 75;
	public static final int AVERAGE_HULL = 3000;

	protected int resourcesStart;
	protected int resourcesLeft;

	protected float turnSpeed;
	protected float myXSpeed;
	protected float myYSpeed;
	protected Color color;
	protected Color resourceColor;

	private NodeHealthbar healthbar;
	protected float nodeScale;
	protected float recentDamage;
	protected int resourcesOnDeath;
	protected boolean remnant;
	
	public Node(float x, float y, float xSpeed, float ySpeed) 
	{
		super(x, y);
		color = Color.white;
		healthbar = new NodeHealthbar(this);	
		setupSpeed(xSpeed, ySpeed);
	}
	
	public Color getColor()
	{
		return color;
	}
	
	
	
	public void render(Graphics g)
	{
		super.render(g);
		
		if(isSelected())
		{
			g.setColor(Color.white);
			g.drawRect(x, y, w, h);
		}
		
		healthbar.render(g);


	}
	
	public int getMaxResources()
	{
		return resourcesStart + resourcesOnDeath;
	}
	
	public int getCurResources()
	{	
		return resourcesLeft + resourcesOnDeath;
	}
	
	public float getNodeScale()
	{
		return nodeScale;
	}
	
	public void setColor(Color color)
	{
		this.color = color;
	}

	protected void setupSpeed(float xSpeed, float ySpeed)
	{
		myXSpeed = xSpeed;
		myYSpeed = ySpeed;
		turnSpeed = Utility.random(MIN_TURN_SPEED, MAX_TURN_SPEED);
		if(Math.random() > .5f)
		{
			turnSpeed *= -1;
		}
	}	
	
	public void update()
	{
		super.update();		

		if(remnant)
		{
			System.out.println("Error:  Player referencing node from previous game");
		}
		
		updateWidthAndHeightToScale();
		
		move(getTheta() + turnSpeed * spinDirection);
		
		xSpeed = myXSpeed;
		ySpeed = myYSpeed;
		
		if(isAlive())
		{

			if(x < Game.getMapLeftEdge() - image.getWidth() * getScale())
			{
				x = Game.getMapRightEdge();
			}
			if(x > Game.getMapRightEdge())
			{
				x = Game.getMapLeftEdge() - image.getWidth() * getScale();
			}
			if(y < Game.getMapTopEdge() - image.getHeight() * getScale())
			{
				y = Game.getMapBottomEdge();
			}
			if(y > Game.getMapBottomEdge())
			{
				y = Game.getMapTopEdge() - image.getHeight() * getScale();
			}
		}
		
		
	

	}
	
	public void flagAsRemnant()
	{
		resourcesLeft = 0;
		remnant = true;
	}
	
}
