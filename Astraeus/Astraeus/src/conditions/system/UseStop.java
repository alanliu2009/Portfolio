package conditions.system;

import components.MovementPenalty;
import conditions.Use;

public class UseStop extends Use
{	
	public UseStop()
	{
		super();
		mobilityScalar = 1-MovementPenalty.STOP_USE_MOVE_PENALTY;
		locksPosition = true;
	}

}
