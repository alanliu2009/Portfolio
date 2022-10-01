package ui.display.hud;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;

import engine.Main;
import engine.Settings;
import engine.Utility;
import objects.entity.unit.BaseShip;
import objects.entity.unit.Unit;
import player.Player;
import ui.display.Fonts;

public class UnitList
{	
	Player owner;
	protected float x;
	protected float y;

	private int spacing = 28;

	public UnitList(Player owner, float x, float y)
	{
		this.x = x;
		this.y = y;
		this.owner = owner;
	}

	public void render(Graphics g)
	{
		if(Settings.showUnitList)
		{
			ArrayList<Unit> units = owner.getMyUnits();
			Map<String, Integer> unitCounts = new HashMap<String, Integer>();
			Map<String, Float> unitPercents = new HashMap<String, Float>();

			for(Unit u : units)
			{	
				// Skip baseship
				if(u instanceof BaseShip)
				{
					continue;
				}

				// Fill up names + counts;
				if(unitCounts.containsKey(u.getName()))
				{
					unitCounts.replace(u.getName(), unitCounts.get(u.getName()) + 1);
				}

				// Add a new entry to counts and percents
				else
				{
					unitCounts.put(u.getName(),  1);
					unitPercents.put(u.getName(), u.getPlayer().getFleetValuePercentage(u.getClass()));
				}
			}	

			// Move the compacted list into an easy to sort ArrayList of custom data objects

			List<Data> unitData = new ArrayList<Data>();

			for(Map.Entry<String, Float> mapElement : unitPercents.entrySet())
			{
				String name = mapElement.getKey();
				int percentage = (int) (mapElement.getValue() * 100);
				int count = unitCounts.get(name);

				unitData.add(new Data(name, percentage, count));
			}

			Collections.sort(unitData);

			// Output the sorted elements

			for(int i = 0; i < unitData.size(); i++)
			{
				Data d = unitData.get(i);
				
//				int r = (int) (owner.getColorPrimary().getRed() * (d.percentage/100.0)) + 127;
//				int gr = (int) (owner.getColorPrimary().getGreen() * (d.percentage/100.0)) + 127;
//				int b = (int) (owner.getColorPrimary().getBlue() * (d.percentage/100.0)) + 127;
//
//				g.setColor(new Color(r, gr, b));
				setFont(g);
				//g.drawString(d.percentage + "% " + d.name + " (x" + d.count + ")", x + 20, y - i * spacing + 40);
				
				g.setColor(new Color(90, 90, 90));
				g.drawString(d.percentage + "%", x + 20, y - i * spacing + 40);
				g.setColor(Utility.blend(owner.getColorPrimary(), Color.gray));
				g.drawString("x" + d.count + " " + d.name, x + 75, y - i * spacing + 40);

			}
		}
	}

	public void setFont(Graphics g)
	{
		if(Main.getScreenWidth() > 1920)
		{
			g.setFont(Fonts.ocr20);
			spacing = 28;
		}
		else if(Main.getScreenWidth() == 1920)
		{
			g.setFont(Fonts.ocr20);
			spacing = 23;
		}
		else
		{
			g.setFont(Fonts.ocr14);
			spacing = 19;
		}
	}

	class Data implements Comparable<Data>
	{
		public String name;
		public int percentage;
		public int count;

		Data(String name, int percentage, int count)
		{
			this.name = name;
			this.percentage = percentage;
			this.count = count;
		}

		public int compareTo(Data other)
		{
			if(percentage > other.percentage)
			{
				return -1;
			}
			else if(percentage < other.percentage)
			{
				return 1;
			}
			else
			{
				return 0;
			}
		}

	}


}
