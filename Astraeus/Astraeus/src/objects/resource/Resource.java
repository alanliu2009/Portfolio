package objects.resource;

import java.util.ArrayList;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;

import engine.Utility;
import engine.Values;
import engine.states.Game;
import objects.GameObject;
import objects.entity.Entity;
import objects.entity.node.Node;
import objects.entity.node.NodeManager;
import objects.entity.unit.Frame;

public class Resource extends GameObject
{
	public static final float ROTATION_SPEED = .2f;

	protected Color color;
	private int spinDirection;
	private boolean pickedUp;
	public final static float NODE_PULL_FREQUENCY = 5;		
	public final static float STARTING_MOVE = 15 * Values.ACC;
	
	public final static float RESOURCE_SPEED = Frame.ASSAULT_SPEED;		
//	public final static float NODE_PULL_ACCELERATION = .01f * Values.ACC * NODE_PULL_FREQUENCY;		
//	public final static float NODE_GRAVITY_DISTANCE_MAX = 2000;
//	public final static float NODE_GRAVITY_DISTANCE_MIN = 0;
	public final static float RESOURCE_SPEED_NOISE = .001f * NODE_PULL_FREQUENCY;
	public final static float BEYOND_EDGE_TOLERANCE = 1000;
	
	public Resource(float x, float y) 
	{
		super(x, y);

		theta = Utility.random(0,  360);
		spinDirection = Utility.random(0, 1);
		if(spinDirection == 0)
		{
			spinDirection = -1;
		}
		setSpeed(RESOURCE_SPEED);
		changeSpeed(STARTING_MOVE, getTheta());	
		}
	
	public Resource(float x, float y, float xSpeed, float ySpeed) 
	{
		super(x, y);

		theta = Utility.random(0,  360);
		spinDirection = Utility.random(0, 1);
		if(spinDirection == 0)
		{
			spinDirection = -1;
		}
		
		this.xSpeed = xSpeed;
		this.ySpeed = ySpeed;
		
		setSpeed(RESOURCE_SPEED);
		changeSpeed(STARTING_MOVE, getTheta());	
	}

	public void render(Graphics g) 
	{
		super.render(g);
		
		if(image != null)
		{
			image.setCenterOfRotation(image.getWidth() / 2 * getScale(), image.getHeight() / 2 * getScale());
			image.setRotation(theta);
			image.draw(x, y, getScale(), color);
		}
	}

	public void update()
	{
		
		super.update();

		theta = theta + ROTATION_SPEED * spinDirection;		

		if(getX() < Game.getMapLeftEdge() - BEYOND_EDGE_TOLERANCE|| getX() > Game.getMapRightEdge() + BEYOND_EDGE_TOLERANCE || 
		   getY() < Game.getMapTopEdge() - BEYOND_EDGE_TOLERANCE || getY() > Game.getMapBottomEdge() + BEYOND_EDGE_TOLERANCE)
		{
			setForRemovalFromMap();
		}

	}

	public void setForRemovalFromMap()
	{
		pickedUp = true;
	}
	
	public void pull(float x, float y, float acceleration)
	{
		float oldTheta = getTheta();
		changeSpeed(acceleration, getAngleToward(x, y));
		rotate(oldTheta);
	}

	public void pull(Entity e, float acceleration)
	{
		pull(e.getCenterX(), e.getCenterY(), acceleration);
	}


	public boolean wasPickedUp()
	{
		return pickedUp;
	}

	public Node getNearestNode()
	{
		float nearestDistance = Float.MAX_VALUE;
		Node nearestResource = null;
		ArrayList<Node> nodes =  NodeManager.getNodes();

		for(Node n : nodes)
		{
			if(Utility.distance(getPosition(), n.getPosition()) < nearestDistance)
			{
				nearestResource = n;
				nearestDistance = Utility.distance(getPosition(), n.getPosition());
			}
		}

		return nearestResource;
	}






}
