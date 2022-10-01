package territory;

import java.util.ArrayList;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;

import engine.Settings;
import engine.states.Game;
import objects.entity.node.NodeManager;
import objects.resource.ResourceManager;
import territory.basic.AsteroidBelt;
import territory.basic.Battlefield;
import territory.basic.Scrapyard;
import territory.basic.ShatteredSpace;
import territory.basic.ThePassage;
import territory.trials.ArenaOfGlory;
import territory.trials.DelphiTemple;
import territory.trials.GlitteringPath;
import territory.trials.HiddenReserve;
import territory.trials.HuntingGrounds;
import territory.trials.RiverStyx;
import territory.trials.RockyShoals;

public class TerritoryManager 
{
	private static ArrayList<Territory> types;
	protected static Territory territory;
	
	public static void setup()
	{
		NodeManager.setup();
		ResourceManager.setup();
		
		types = new ArrayList<Territory>();
		addBasicMaps();
		addTrialMaps();
		addTournamentMaps();
		
		territory = getRandomTerritory();
		
		if(Settings.forceTrialMaps)
		{
			loadTrialMap();
		}
//			
		if(Settings.lockedMap != null)
		{
			Territory temp = findMap(Settings.lockedMap);
			if(temp != null)
			{
				territory = temp;
			}
			else
			{
				System.out.println("Territory Manager - Map Not Found");
				System.out.println("Loading Random Map Instead");
			}
		}
		
		territory.spawn();
	
	}
	
	public static void loadTrialMap()
	{
		if(Game.getPlayerTwo().getName().equals("Ares"))
		{
			territory = new ArenaOfGlory();
		}
		else if(Game.getPlayerTwo().getName().equals("Poseidon"))
		{
			territory = new RockyShoals();
		}
		else if(Game.getPlayerTwo().getName().equals("Artemis"))
		{
			territory = new HuntingGrounds();
		}		
		else if(Game.getPlayerTwo().getName().equals("Hermes"))
		{
			territory = new GlitteringPath();
		}
		else if(Game.getPlayerTwo().getName().equals("Dionysus"))
		{
			territory = new HiddenReserve();
		}
		else if(Game.getPlayerTwo().getName().equals("Apollo"))
		{
			territory = new DelphiTemple();
		}
		else if(Game.getPlayerTwo().getName().equals("Hades"))
		{
			territory = new RiverStyx();
		}
	}
	
	public static Territory getTerritory()
	{
		return territory;
	}
	
	public static Image getBackground()
	{
		return territory.getBackground();
	}
	
	public static void leave()
	{
		types.clear();
		territory = null;
	}
	
	
	public static Color getAsteroidColor()
	{
		return territory.getAsteroidColor();
	}
	
	public static Color getDerelictColor()
	{
		return territory.getDerelictColor();
	}
	
	public static Color getMineralColor()
	{
		return territory.getMineralColor();
	}
	
	public static Color getSalvageColor()
	{
		return territory.getScrapColor();
	}
	
	public static void update()
	{
//		if(Game.getTime() == 5)
//		{
//			DisplayManager.addMessage(territory.getName());
//		}
		
		NodeManager.update();
		ResourceManager.update();
	}
	
	public static void cleanUp()
	{
		NodeManager.cleanUp();
		ResourceManager.cleanUp();
	}
	
	public static void render(Graphics g)
	{
		NodeManager.render(g);
		ResourceManager.render(g);
	}
	
	private static void addBasicMaps()
	{
		types.add(new ShatteredSpace());
		types.add(new AsteroidBelt());
		types.add(new ThePassage());
		types.add(new Scrapyard());
		types.add(new Battlefield());
	}
	
	private static void addTrialMaps()
	{
		types.add(new ArenaOfGlory());
		types.add(new RockyShoals());
		types.add(new HuntingGrounds());
		types.add(new GlitteringPath());
		types.add(new HiddenReserve());
		types.add(new RiverStyx());

	}
	
	private static void addTournamentMaps()
	{
		
	}
	
	public static Territory findMap(Class <? extends Territory> clazz)
	{
		for(Territory t : types)
		{
			if(clazz.isInstance(t))
			{
				return t;
			}
		}
		
		return null;
	}
	
	public static Territory getRandomTerritory()
	{
		return types.get((int) (Math.random() * types.size()));
	}
	
}
