package ui.display.hud;

import java.util.ArrayList;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;

import engine.Settings;
import engine.Values;
import engine.states.Game;
import objects.entity.node.Node;
import objects.entity.node.NodeManager;
import objects.entity.unit.BaseShip;
import objects.entity.unit.Unit;
import objects.resource.Minerals;
import objects.resource.Resource;
import objects.resource.ResourceManager;
import territory.TerritoryManager;
import ui.display.Camera;

public class Minimap extends HudElement
{
	public Minimap(float x, float y, float w, float h)
	{
		super(x, y, w, h);
	}

	public void render(Graphics g)
	{		
		super.render(g);

		if(Settings.showBackground)
		{
			Image i = TerritoryManager.getBackground();
			int bright = (int) (255 * Settings.backgroundBrightness);
			Color c = new Color(bright, bright, bright);
			i.draw(x+2,y+1, w-4, h-4, c);
		}
		
		renderNodes(g);
		renderResources(g);
		renderUnits(g);
		renderCamera(g);
	}

	public void renderUnits(Graphics g)
	{
		ArrayList<Unit> units = Game.getUnits();

		for(Unit u : units)
		{
			if(u.isInBounds())
			{
				float xPos = u.getX() / (float) Values.PLAYFIELD_WIDTH * w + w/2;
				float yPos = u.getY() / (float) Values.PLAYFIELD_HEIGHT * h + y + h/2;
		

				if(u instanceof BaseShip)
				{
					int width = 20;
					int height = 10;
					g.setColor(u.getColorPrimary().darker().darker());
					g.fillRect(xPos-width/2, yPos-height/2, width, height);
					g.setLineWidth(1);
					g.setColor(u.getColorPrimary().darker());
					g.drawRect(xPos-width/2, yPos-height/2, width, height);

				}
				else
				{
					int size = u.getFrame().getImageSize() / 12;
					g.setColor(u.getColorPrimary().brighter());
					g.fillOval(xPos-size/2, yPos-size/2, size, size);
				}
			}
		}

	}

	public void renderResources(Graphics g)
	{
		ArrayList<Resource> resources = ResourceManager.getResources();

		for(Resource r : resources)
		{
			if(r.isInBounds())
			{
				float xPos = r.getX() / (float) Values.PLAYFIELD_WIDTH * w + w/2;
				float yPos = r.getY() / (float) Values.PLAYFIELD_HEIGHT * h + y + h/2;
				int size = 2;


				if(r instanceof Minerals)
				{
					g.setColor(TerritoryManager.getMineralColor());	
				}
				else
				{
					g.setColor(TerritoryManager.getSalvageColor());
				}			


				g.fillOval(xPos-size/2, yPos-size/2, size, size);
			}

		}
	}

	public void renderNodes(Graphics g)
	{
		ArrayList<Node> nodes = NodeManager.getNodes();
		for(Node n : nodes)
		{
			if(n.isInBounds())
			{
				int size = Math.round(6 * n.getNodeScale());
				
				
				float xPos = n.getX() / (float) Values.PLAYFIELD_WIDTH * w + w/2;
				float yPos = n.getY() / (float) Values.PLAYFIELD_HEIGHT * h + y + h/2;
	

//				if(n instanceof Asteroid)
//				{
//					
//					g.setColor(TerritoryManager.getAsteroidColor());
//				}
//				else
//				{
//					g.setColor(TerritoryManager.getDerelictColor());
//				}
				g.setColor(n.getColor());
				g.fillOval(xPos-size/2, yPos-size/2, size, size);
			}
		}
	}

	public void renderCamera(Graphics g)
	{
		// Maths

		float camX = Camera.getX() / ((float) Game.getMapWidth()) * w + x + w / 2;
		float camY = Camera.getY() / ((float) Game.getMapHeight()) * h + y + h / 2;
		float camW = ((Camera.getViewWidth() / ((float) Game.getMapWidth())) * w);
		float camH = ((Camera.getViewHeight() / ((float) Game.getMapHeight())) * h);

		// Snap to right 
		if(camX + camW / 2 > w)
		{
			camX = w - camW/2;
		}

		// Snap to left 
		if(camX - camW / 2 < 0)
		{
			camX = camW / 2;
		}

		// Snap to bottom
		if(camY + camH / 2 > y + h)
		{
			camY = y + h - camH/2;
		}

		// Snap to top
		if(camY - camH/ 2 < y)
		{
			camY = y + camH / 2;
		}

		// Display box
		g.setLineWidth(2);
		g.setColor(new Color(200, 200, 200, 10));
		g.fillRect(camX - camW / 2, camY - camH / 2, camW, camH);
		g.setColor(new Color(200, 200, 200, 45));
		g.drawRect(camX - camW / 2, camY - camH / 2, camW, camH);
		g.resetLineWidth();
	}

}
