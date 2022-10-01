package components;

import objects.entity.unit.Unit;


public abstract class Component 
{
	private Unit owner;
	private int size;
	private int mineralCost;
	
	public Component(Unit owner, int size, int mineralCost) 
	{
		this.owner = owner;
		this.size = size;
		this.mineralCost = mineralCost;
	}
	
	public Unit getOwner()
	{
		return owner;
	}
	
	// Called when the component is created
	public void onAddition()
	{
		owner.addMineralCost(getMineralCost());
	}
	
	// Called every frame
	public void update() 
	{
		
	}
	
	public int getSize()
	{
		return size;
	}
	
	public int getMineralCost()
	{
		return mineralCost;
	}
	
	
	public String getName()
	{
		return getClass().getSimpleName();
	}
	

}
