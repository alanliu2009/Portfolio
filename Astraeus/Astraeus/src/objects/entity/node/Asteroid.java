package objects.entity.node;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;

import animations.AnimationManager;
import animations.effects.Boom;
import engine.Settings;
import objects.resource.ResourceManager;
import ui.display.Images;
import ui.sound.Sounds;

public class Asteroid extends Node
{
	
	public static final int AVERAGE_PLATING = 0;
	
	private Image imageHoles;
	private Image imageMinerals;
		
	public Asteroid(float x, float y, float xSpeed, float ySpeed, int size, Color nodeColor, Color resourceColor) 
	{
		super(x, y, xSpeed, ySpeed);
		setupScale(size);
		setupImage();
		
		setStructure((int) (AVERAGE_HULL * nodeScale));
		setPlating((int) (AVERAGE_PLATING * nodeScale));
		
		resourcesOnDeath = (int) (Node.DROP_ON_DEATH_AVERAGE* nodeScale);
		resourcesStart = (int) (Node.RESOURCES_AVERAGE * nodeScale);
		resourcesLeft = resourcesStart;
		recentDamage = 0;
		
		// Adjusts spawn symmetry
		this.x = x - w/2;
		this.y = y - h/2;
		
		this.color = nodeColor;
		this.resourceColor = resourceColor;
	
	}
	
	protected void setupScale(int size)
	{
		
		if(size == 1)
		{
			nodeScale = .75f;
		}
		else if(size == 2)
		{
			nodeScale = 1;
		}
		else if(size == 3)
		{
			nodeScale = 1.5f;
		}
		else
		{
			nodeScale = 0;
		}
		
	}
	
	protected void setupImage()
	{
		image = Images.asteroids.getSprite(0, 0).getScaledCopy(nodeScale);
		imageHoles = Images.asteroids.getSprite(0, 1).getScaledCopy(nodeScale);
		imageMinerals = Images.asteroids.getSprite(0, 2).getScaledCopy(nodeScale);


		
		updateWidthAndHeightToScale();
	}
	
	public void update()
	{
		super.update();
		
//		dbgMessage((int)x);
//		dbgMessage(""+(int)getCenterX(), 0, Color.green);

		float totalLostHealth = getMaxStructure() - getCurStructure();
		float spawnedMinerals = resourcesStart - resourcesLeft;
		float damagePerSpawn =  getMaxStructure() / resourcesStart;
		
		while(totalLostHealth / damagePerSpawn > spawnedMinerals)
		{
			spawnMinedMinerals(RESOURCE_SPREAD_AVERAGE * nodeScale);
			spawnedMinerals = resourcesStart - resourcesLeft;
			//System.out.println(totalLostHealth + " / " + damagePerSpawn + " > " + spawnedMinerals);

		}
	}
	
	public void render(Graphics g)
	{
		
		if(image != null)
		{
			image.setCenterOfRotation(image.getWidth() / 2 * getScale(), image.getHeight() / 2 * getScale());
			image.setRotation(theta);
			image.draw(x, y, getScale(), color);
		}
		
		if(imageHoles != null)
		{
			imageHoles.setCenterOfRotation(imageHoles.getWidth() / 2 * getScale(), imageHoles.getHeight() / 2 * getScale());
			imageHoles.setRotation(theta);

			if(Settings.showAnimations)
			{
				float progressTowardHalf = Math.min(1, (1-getPercentStructure()) * 2);
				float alpha = (float) Math.sqrt(progressTowardHalf);
				imageHoles.setAlpha(alpha);
				imageHoles.draw(x, y, getScale(), color);
			}

		}
		
		if(imageMinerals != null)
		{
			imageMinerals.setCenterOfRotation(imageMinerals.getWidth() / 2 * getScale(), imageMinerals.getHeight() / 2 * getScale());
			imageMinerals.setRotation(theta);
			
			if(Settings.showAnimations)
			{
				float progressTowardHalf = Math.min(1, (1-getPercentStructure()) * 2);
				float alpha = (float) Math.pow(progressTowardHalf, 2);
				imageMinerals.setAlpha(alpha);
				imageMinerals.draw(x, y, getScale(), resourceColor);
			}
		}
		
		super.render(g);
		
//		original spawn debug code
//		g.setColor(Color.red);
//		g.fillOval(xOriginal-2, yOriginal-2, 5, 5);
		
	}
	

	
	final public void die()
	{
		if(isAlive())
		{
			for(int i = 0; i < resourcesOnDeath; i++)
			{
				spawnMinedMinerals(RESOURCE_SPREAD_AVERAGE * nodeScale);
			}
		}
		
		AnimationManager.add(new Boom(getCenterX(), getCenterY(), getSize()));
		Sounds.boom.play(getPosition(), .2f);

		super.die();	
	}
				
	public void spawnMinedMinerals(float radius)
	{
		ResourceManager.spawnMineralNear(getCenterX(), getCenterY(), xSpeed, ySpeed, radius);
		resourcesLeft--;
	}
	
}
