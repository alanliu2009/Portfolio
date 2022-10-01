package components.upgrade;

import components.Component;
import objects.entity.unit.Unit;

abstract public class Upgrade extends Component 
{
	public Upgrade(Unit owner, int size, int mineralCost) 
	{
		super(owner, size, mineralCost);
	}
	
}
