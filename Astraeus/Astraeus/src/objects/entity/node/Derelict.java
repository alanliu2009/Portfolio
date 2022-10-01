package objects.entity.node;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SpriteSheet;

import animations.AnimationManager;
import animations.effects.Boom;
import objects.entity.unit.Frame;
import objects.resource.ResourceManager;
import ui.display.Images;
import ui.sound.Sounds;

public class Derelict extends Node
{
	public static final int SIZE = Frame.ASSAULT_IMAGE_SIZE;

	public static final int AVERAGE_SHIELD = 0;
	
	protected Image imageSecondary;
	protected Image imageAccent;
	protected Image imageMove;
	
	Color colorSecondary;
	Color colorAccent;
	
	public Derelict(float x, float y, float xSpeed, float ySpeed, int size, Color c) 
	{
		super(x, y, xSpeed, ySpeed);

		setupImage();

		setStructure((int) (AVERAGE_HULL));
		//setShield((int) (AVERAGE_SHIELD * 1.5f));
		updateWidthAndHeightToScale();
				
		resourcesOnDeath = (int) (Node.DROP_ON_DEATH_AVERAGE);
		resourcesStart = (int) (Node.RESOURCES_AVERAGE);
		resourcesLeft = resourcesStart;
		recentDamage = 0;
		
		// Adjusts spawn symmetry
		this.x = x - w/2;
		this.y = y - h/2;

		color = c;
		nodeScale = 1;
	}
	

//	protected void setupImage()
//	{
//		image = Images.asteroids.getSprite(0, 0).getScaledCopy(nodeScale);
//		imageHoles = Images.asteroids.getSprite(0, 1).getScaledCopy(nodeScale);
//		imageMinerals = Images.asteroids.getSprite(0, 2).getScaledCopy(nodeScale);
//
//		int r = Utility.random(120, 150);
//		int g = Utility.random(80, 110);
//		int b = Utility.random(50, 80);
//		
//		color = new Color(r, g, b);
//		
//		updateWidthAndHeightToScale();
//	}
	
	protected void setupImage()
	{
		SpriteSheet sheet = Images.getRandomAssaultShip();
		image = sheet.getSprite(0, 0).getScaledCopy(2);
		imageSecondary = sheet.getSprite(1, 0).getScaledCopy(2);
		imageAccent = sheet.getSprite(2, 0).getScaledCopy(2);

			
		
		colorSecondary = new Color(100, 100, 100);
		colorAccent = new Color(100, 100, 100);
		colorAccent = colorAccent.brighter();
	}
	

	
	public void update()
	{
		super.update();

		float totalLostHealth = getMaxStructure() - getCurStructure();
		float spawnedTech = resourcesStart - resourcesLeft;
		float damagePerSpawn =  getMaxStructure() / resourcesStart;
		
		while(totalLostHealth / damagePerSpawn > spawnedTech)
		{
			spawnMinedSalvage(RESOURCE_SPREAD_AVERAGE);
			spawnedTech = resourcesStart - resourcesLeft;

		//	System.out.println(totalLostHealth + " / " + damagePerSpawn + " > " + spawnedTech);
		}
		
//		while(getMaxStructure() - getCurStructure() > (1+resourcesSpawned) * DAMAGE_PER_SPAWN)
//		{
//			spawnSalvagedEnergyCores(RESOURCE_SPREAD_AVERAGE);
//		}
	}
	
	public void render(Graphics g)
	{
		
		if(image != null)
		{
			image.setCenterOfRotation(image.getWidth() / 2 * getScale(), image.getHeight() / 2 * getScale());
			image.setRotation(theta);
			image.draw(x, y, getScale(), color);
		}
		
		if(imageSecondary != null)
		{
			imageSecondary.setCenterOfRotation(imageSecondary.getWidth() / 2 * getScale(), imageSecondary.getHeight() / 2 * getScale());
			imageSecondary.setRotation(theta);
			imageSecondary.draw(x, y, getScale(), colorSecondary);
		}
		
		if(imageAccent != null)
		{
			imageAccent.setCenterOfRotation(imageAccent.getWidth() / 2 * getScale(), imageAccent.getHeight() / 2 * getScale());
			imageAccent.setRotation(theta);
			imageAccent.draw(x, y, getScale(), colorAccent);
		}
		
		if (getCurShield() > 0) 
		{
			// Shield Position
			float width = (float) (w * 1.85);
			float height = (float) (h * 1.85);
			float x = getCenterX() - width / 2;
			float y = getCenterY() - height / 2;

			// Shield Colors and Transparency
			int alpha = (int) (40 * (getCurShield() / getMaxShield()));
							
			// Set normal colors
			Color fill = new Color(50, 50, 150, alpha);
			g.setColor(fill);
			g.fillOval(x+1, y+1, width-2, height-2);	

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
				spawnMinedSalvage(RESOURCE_SPREAD_AVERAGE);
			}
		}
		
		AnimationManager.add(new Boom(getCenterX(), getCenterY(), getSize()));
		Sounds.boom.play(getPosition(), .2f);

		super.die();	
	}
				
	public void spawnMinedSalvage(float radius)
	{
		ResourceManager.spawnScrapNear(getCenterX(), getCenterY(), xSpeed, ySpeed, radius);
		resourcesLeft--;
	}
	
}
