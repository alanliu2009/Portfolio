package objects.entity.unit;

public enum Style 
{	
	ARROW, BUBBLE, DAGGER, ROCKET, BOXY, ORB, WEDGE;

	public int getID()
	{
		switch(this)
		{
			case ARROW: 	return 0;
			case BUBBLE: 	return 1;
			case DAGGER:	return 2;
			case ROCKET:	return 3;
			case BOXY:		return 4;
			case ORB:		return 5;		
			case WEDGE:		return 6;
			default:		return 0;
		}
	}
}
