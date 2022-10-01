package background;

import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;

import background.biome.Desert;
import background.biome.Hills;
import background.biome.Tundra;
import background.biome.Underground;
import background.sky.Sky;
import core.Engine;
import core.Values;
import entities.core.Coordinate;

public class Background
{
	private int time;
	private float nightAlpha;
	private int gameState;
	private int[] desertPosition;
	private int[] tundraPosition;
	
	private Sky sky;
	private Hills hills;
	private Desert desert;
	private Tundra tundra;
	private Underground underground;
	
	private Cloud[] clouds;
	
	public Sky getSky() { return sky; }
	
	public Background(int gameState) throws SlickException
	{
		this.gameState = gameState;
		nightAlpha = 0;
		time = 0;
		desertPosition = new int[2];
		tundraPosition = new int[2];
		
		sky = new Sky();
		hills = new Hills();
		desert = new Desert();
		tundra = new Tundra();
		
		underground = new Underground();
		
		clouds = new Cloud[7];
		
		for(int i = 0; i < clouds.length; i++)
		{
			clouds[i] = new Cloud();
		}
		
		loadVariables();
	}
	
	private void loadVariables() //gets variables accessed from other places and makes it a local variable for less typing
	{
		if(gameState == Engine.Game_ID)
		{
			desertPosition[0] = Engine.game.getWorld().getDesertStart();
			desertPosition[1] = Engine.game.getWorld().getDesertEnd();
			tundraPosition[0] = Engine.game.getWorld().getTundraStart();
			tundraPosition[1] = Engine.game.getWorld().getTundraEnd();
		}
		else
		{
			desertPosition[0] = 0; 
			desertPosition[1] = 0;
			tundraPosition[0] = 0;
			tundraPosition[1] = 0;
		}
	}
	
	public void render(Graphics g, float x, float y)
	{	
		float currentChunk = (x - Values.CenterX) / Values.Pixels_Per_Block / Values.Chunk_Size_X * -1;
		
		if(y > -500)
		{
			sky.render(g);
			
			for(int i = 0; i < clouds.length; i++) //the clouds
			{
				clouds[i].render(g, x, y);
			}
			
			if(currentChunk > desertPosition[0]
					&& currentChunk < desertPosition[1])
			{
				desert.render(g, x, y);
				desert.setSceneAlpha(1);
			} 
			else if(currentChunk > tundraPosition[0]
					&& currentChunk < tundraPosition[1])
			{
				tundra.render(g, x, y);
				tundra.setSceneAlpha(1);
			}
			else 
			{
				hills.render(g, x, y);
			}
			
			
			if(currentChunk > desertPosition[0] - 1
					&& currentChunk < desertPosition[0])
			{
				desert.setSceneAlpha(transition(currentChunk, desertPosition[0] - 1, desertPosition[0]));
				desert.render(g, x, y);
			} 
			else if(currentChunk > tundraPosition[0] - 1
					&& currentChunk < tundraPosition[0])
			{
				tundra.setSceneAlpha(transition(currentChunk, tundraPosition[0] - 1, tundraPosition[0]));
				tundra.render(g, x, y);
			}
			
			if(currentChunk < desertPosition[1] + 1
					&& currentChunk > desertPosition[1])
			{
				desert.setSceneAlpha(transition(currentChunk, desertPosition[1] + 1, desertPosition[1]));
				desert.render(g, x, y);
			} 
			else if(currentChunk < tundraPosition[1] + 1
					&& currentChunk > tundraPosition[1])
			{
				tundra.setSceneAlpha(transition(currentChunk, tundraPosition[1] + 1, tundraPosition[1]));
				tundra.render(g, x, y);
			}	
		}
		
		
		if(y < 0)
		{
			underground.render(g, x, y);
		}
	}
	
	public void update()
	{
		nightAlpha = sky.getNightAlpha();
		if(gameState == Engine.Game_ID)
		{
			time = Engine.game.getWorld().getTime();
		}
		else
		{
			time++;
		}
		
		for(int i = 0; i < clouds.length; i++) //the clouds
		{
			clouds[i].update(nightAlpha);
		}
		
		desert.update(nightAlpha);
		tundra.update(nightAlpha);
		hills.update(nightAlpha);
		sky.update(time);
	}
	
	public float transition(float pos, float start, float end) //takes two points
	{
		float alpha = Math.abs((pos - start) / (end - start));
		
		return alpha;
	}
}
