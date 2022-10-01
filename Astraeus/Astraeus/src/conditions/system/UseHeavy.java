package conditions.system;

import components.MovementPenalty;
import conditions.Use;

public class UseHeavy extends Use
{	
	public UseHeavy()
	{
		super();
		mobilityScalar = 1-MovementPenalty.HEAVY_USE_MOVE_PENALTY;
	}

}
