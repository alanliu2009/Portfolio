package conditions.system;

import components.MovementPenalty;
import conditions.Use;

public class UseLight extends Use
{	
	public UseLight()
	{
		super();
		mobilityScalar = 1-MovementPenalty.LIGHT_USE_MOVE_PENALTY;
	}

}
