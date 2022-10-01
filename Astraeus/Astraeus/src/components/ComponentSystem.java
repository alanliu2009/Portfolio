package components;

import java.util.ArrayList;

import components.weapon.Weapon;
import components.weapon.WeaponType;
import objects.entity.unit.Unit;

public class ComponentSystem 
{
	Unit owner;
	ArrayList<Component> components;
	int componentSlotsUsed;
	
	public ComponentSystem(Unit owner)
	{
		this.owner = owner;
		components = new ArrayList<Component>();
	}
	
	public int getComponentSlotsUsed()
	{
		return componentSlotsUsed;
	}
	
	public int getComponentSlotsOpen()
	{
		return owner.getFrame().getComponentSlots() - componentSlotsUsed;
	}
	
	public Component get(int i)
	{
		return components.get(i);
	}
	
	public ArrayList<Component> getAll()
	{
		return components;
	}
	

	
	public boolean canAdd(Component c)
	{
//		System.out.println(c);
//		System.out.println(owner);
//		System.out.println(owner.getFrame());
//		System.out.println("what");
		
		return componentSlotsUsed + c.getSize() <= owner.getFrame().getComponentSlots();
	}
	
	public boolean add(Component c)
	{
		if(!canAdd(c))
		{
			System.out.println("Error: " + owner + " has insufficient component slots remaining");
			return false;
		}

		componentSlotsUsed += c.getSize();
		components.add(c);
		c.onAddition();
		
		return true;
	
	}
	
	public boolean has(Component comp)
	{
		for(Component c : components)
		{
			if(comp.getClass().equals(c.getClass()))
			{
				return true;
			}
		}
		return false;
	}
	
	public boolean has(WeaponType type)
	{
		for(Component c : components)
		{
			if(c instanceof Weapon && ((Weapon) c).getWeaponType() == type)
			{
				return true;
			}
		}
		return false;
	}
	
	public boolean has(Class<? extends Component> clazz)
	{
		
		for(Component c : components)
		{
			if(clazz.isInstance(c))
			{
				//System.out.println(clazz + " is a " + c);

				return true;
			}
		}

		return false;
	}

	public Component get(Class<? extends Component> clazz)
	{
		for(Component c : components)
		{
			if(clazz.isInstance(c))
			{
				return c;
			}
		}

		return null;
	}
	
	public void update()
	{
		for(Component c : components)
		{
			c.update();
		}
	}
	
	
}
