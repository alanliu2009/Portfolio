package conditions.system;

import components.MovementPenalty;
import conditions.Use;

public class UseMedium extends Use
{		
	public UseMedium()
	{
		super();
		mobilityScalar = 1-MovementPenalty.MEDIUM_USE_MOVE_PENALTY;
	}

}
